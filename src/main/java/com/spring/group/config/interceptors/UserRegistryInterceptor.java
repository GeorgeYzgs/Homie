package com.spring.group.config.interceptors;

import com.spring.group.models.user.UserRole;
import com.spring.group.pojo.websockets.chat.ConnectedUser;
import com.spring.group.pojo.websockets.chat.ConnectedUsersRegistry;
import com.spring.group.pojo.websockets.chat.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Class to intercept the stomp messages.
 * Here we intercept the subscribe messages and infrorm others when a MODERATOR has connected. Next they can start
 * chatting
 */
@Component
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class UserRegistryInterceptor implements ChannelInterceptor, WebSocketMessageBrokerConfigurer {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ConnectedUsersRegistry connectedModeratorsRegistry;

    @Autowired
    private MessageSource messageSource;

    /**
     * Add interceptor to the registry to manipulate incoming messages or inform other users when someone gets online
     *
     * @param registration the ChannelRegistration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            /**
             * Overriding this method lets us detect when a moderator sends a Subscribe message we inform
             * others already subscribed that the moderator is online. Here we have two channels that the user subscribes
             * one for online status of other users and one for messages.
             * @param message the message intercepted
             * @param channel the MessageChannel functional interface
             * @return
             */
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                Map<String, Object> sessionHeaders = accessor.getSessionAttributes();
                Locale userLocale = (Locale) sessionHeaders.get("HEADER_HTTP_LOCALE");
                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())
                        && accessor.getDestination().equals("/user/queue/specific-user")) {
                    if (isModerator(accessor)) {
                        ConnectedUser connectedUser =
                                new ConnectedUser(accessor.getUser().getName(), accessor.getSessionId(), UserRole.MODERATOR.toString());
                        connectedModeratorsRegistry.addUser(connectedUser.getUsername(), connectedUser);
                        sendMessageUserIdentifier(accessor);
                        sendMessageModeratorConnected(accessor, userLocale);
                    } else {
                        // When someone other than a moderator connects
                        sendMessageUserIdentifier(accessor);
                        sendMessageChatConnectionStatus(accessor, userLocale);
                    }
                }

                // When a MODERATOR disconnects
                if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    if (isModerator(accessor)) {
                        connectedModeratorsRegistry.removeUser(accessor.getUser().getName());
                        sendMessageModeratorDisconnected(accessor, userLocale);
                    }
                }
                return message;
            }

            /**
             * Check if the sender of the message is a moderator
             *
             * @param accessor the StompHeaderAccessor of the messsage sent
             * @return boolean whether the sender is a moderator or not
             */
            private boolean isModerator(StompHeaderAccessor accessor) {
                if (accessor.getHeader("simpUser") != null
                        && accessor.getHeader("simpUser") instanceof AbstractAuthenticationToken) {
                    AbstractAuthenticationToken userToken = (AbstractAuthenticationToken) accessor.getHeader("simpUser");
                    return (userToken != null
                            && userToken.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(UserRole.MODERATOR.toString())));
                }
                return false;
            }

            /**
             * Method to inform others connected to the websocket that a moderator has CONNECTED.
             *
             * @param accessor   the StompHeaderAccessor of the Moderator, who sends the Subscribe message
             * @param userLocale the user Locale used for choosing the language (the moderator's one is used for all
             *                   the users)
             */
            private void sendMessageModeratorConnected(StompHeaderAccessor accessor, Locale userLocale) {
                AbstractAuthenticationToken userToken = (AbstractAuthenticationToken) accessor.getHeader("simpUser");
                OutputMessage outputMessage = new OutputMessage();
                outputMessage
                        .setMessage(messageSource.getMessage("User.connected", new Object[]{userToken.getName()}, userLocale))
                        .setStatus(OutputMessage.MODERATOR_CONNECTED)
                        .setTime(new SimpleDateFormat("HH:mm").format(new Date()))
                        .setAssignedModerator(userToken.getName(), accessor.getSessionId());
                simpMessagingTemplate.convertAndSend("/user/queue/online", outputMessage);
            }

            /**
             * Method to inform others connected to the websocket that a moderator has DISCONNECTED.
             *
             * @param accessor   the StompHeaderAccessor of the Moderator who is disconnecting
             * @param userLocale the user Locale used for choosing the language (the moderator's one is used for all
             *                   the users)
             */
            private void sendMessageModeratorDisconnected(StompHeaderAccessor accessor, Locale userLocale) {
                AbstractAuthenticationToken userToken = (AbstractAuthenticationToken) accessor.getHeader("simpUser");
                OutputMessage outputMessage = new OutputMessage();
                outputMessage
                        .setMessage(messageSource.getMessage("User.disconnected", new Object[]{userToken.getName()}, userLocale))
                        .setStatus(OutputMessage.MODERATOR_DISCONNECTED)
                        .setTime(new SimpleDateFormat("HH:mm").format(new Date()));

                simpMessagingTemplate.convertAndSend("/user/queue/online", outputMessage);
            }

            /**
             * Method to send to the user currently subscribing their identification - username(if logged-in) and
             * sessionId
             *
             * @param accessor the StompHeaderAccessor of the user who is subscribing
             */
            private void sendMessageUserIdentifier(StompHeaderAccessor accessor) {
                String sessionId = accessor.getSessionId();
                // SessionId has to go to the HEADERS as well
                SimpMessageHeaderAccessor headerAccessor = getMessageHeaders(sessionId);
                OutputMessage userIdentifierMsg = new OutputMessage();
                userIdentifierMsg
                        .setStatus(OutputMessage.USER_IDENTIFIER)
                        .setUserIdentifier(
                                (accessor.getUser() != null) ? accessor.getUser().getName() : "",
                                sessionId);
                simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/online",
                        userIdentifierMsg,
                        headerAccessor.getMessageHeaders());

            }

            /**
             * Method to inform users connected to the websocket endpoint if the chat is available. If there are no
             * moderators connected then it sends a messages that the chat is unavailable. If there is at least one then
             * then it sends a messages that the chat is available.
             *
             * @param accessor   the StompHeaderAccessor of the user who is subscribing
             * @param userLocale the Locale of the user subscribing in order to choose the language of sending messages
             */
            private void sendMessageChatConnectionStatus(StompHeaderAccessor accessor, Locale userLocale) {
                String sessionId = accessor.getSessionId();
                SimpMessageHeaderAccessor headerAccessor = getMessageHeaders(sessionId);
                OutputMessage chatStatusMsg;
                if (connectedModeratorsRegistry.getConnectedUsers().isEmpty()) {
                    chatStatusMsg = getMessageChatDisconnected(accessor, userLocale);
                } else {
                    chatStatusMsg = getMessageChatConnected(accessor, userLocale);
                }
                simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/online",
                        chatStatusMsg,
                        headerAccessor.getMessageHeaders());
            }

            /**
             * Method that constructs the message when the chat in available
             *
             * @param accessor   the StompHeaderAccessor of the user who is subscribing
             * @param userLocale the Locale of the user subscribing in order to choose the language of sending message
             * @return the constructed OutputMessage message to be sent
             */
            private OutputMessage getMessageChatConnected(StompHeaderAccessor accessor, Locale userLocale) {
                OutputMessage chatStatusMsg = new OutputMessage();
                ConnectedUser randomMod = connectedModeratorsRegistry.getRandomUser();
                chatStatusMsg
                        .setTime(new SimpleDateFormat("HH:mm").format(new Date()))
                        .setTo(accessor.getSessionId())
                        .setStatus(OutputMessage.MESSAGE_CONNECTED)
                        .setMessage(randomMod + " " + messageSource.getMessage("User.connected", null, userLocale).toLowerCase())
                        .setAssignedModerator(randomMod.getUsername(), randomMod.getUserSession());
                return chatStatusMsg;
            }

            /**
             * Method that constructs the message when the chat in unavailable
             *
             * @param accessor   the StompHeaderAccessor of the user who is subscribing
             * @param userLocale the Locale of the user subscribing in order to choose the language of sending message
             * @return the constructed OutputMessage message to be sent
             */
            private OutputMessage getMessageChatDisconnected(StompHeaderAccessor accessor, Locale userLocale) {
                OutputMessage chatStatusMsg = new OutputMessage();
                chatStatusMsg
                        .setTime(new SimpleDateFormat("HH:mm").format(new Date()))
                        .setTo(accessor.getSessionId())
                        .setStatus(OutputMessage.MESSAGE_DISCONNECTED)
                        .setMessage(messageSource.getMessage("Chat.currently.not.available", null, userLocale));
                return chatStatusMsg;
            }

            /**
             * Create message Header with specific sessionId as a target. Needed for the websocket underline mechanism
             * to send the message to the intended recipient
             *
             * @param sessionId of the recipient of the message
             * @return SimpMessageHeaderAccessor object of the headers needed to send the message
             */
            private SimpMessageHeaderAccessor getMessageHeaders(String sessionId) {
                SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                        .create(SimpMessageType.MESSAGE);
                headerAccessor.setSessionId(sessionId);
                headerAccessor.setLeaveMutable(true);
                return headerAccessor;
            }
        });
    }
}