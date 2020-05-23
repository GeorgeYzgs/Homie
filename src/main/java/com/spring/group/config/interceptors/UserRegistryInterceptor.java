package com.spring.group.config.interceptors;

import com.spring.group.models.user.UserRole;
import com.spring.group.pojo.websockets.chat.ConnectedUser;
import com.spring.group.pojo.websockets.chat.ConnectedUsersRegistry;
import com.spring.group.pojo.websockets.chat.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
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

// This class intercepts the stomp subscribe messages and informs other users which user connected and the same user who
// is already online.
@Component
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class UserRegistryInterceptor implements ChannelInterceptor, WebSocketMessageBrokerConfigurer {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ConnectedUsersRegistry connectedModeratorsRegistry;


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {


            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())
                        && accessor.getDestination().equals("/user/queue/specific-user")) {
                    if (isModerator(accessor)) {
                        ConnectedUser connectedUser =
                                new ConnectedUser(accessor.getUser().getName(), accessor.getSessionId(), UserRole.MODERATOR.toString());
                        connectedModeratorsRegistry.addUser(connectedUser.getUsername(), connectedUser);
                        sendMessageUserIdentifier(accessor);
                        sendMessageModeratorConnected(accessor);
                    } else {
                        // When someone other than a moderator connects
                        sendMessageUserIdentifier(accessor);
                        sendMessageChatConnectionStatus(accessor);
                    }
                }

                // When a MODERATOR disconnects
                if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    if (isModerator(accessor)) {
                        connectedModeratorsRegistry.removeUser(accessor.getUser().getName());
                        sendMessageModeratorDisconnected(accessor);
                    }
                }
                return message;
            }

            private boolean isModerator(StompHeaderAccessor accessor) {
                if (accessor.getHeader("simpUser") != null
                        && accessor.getHeader("simpUser") instanceof AbstractAuthenticationToken) {
                    AbstractAuthenticationToken userToken = (AbstractAuthenticationToken) accessor.getHeader("simpUser");
                    return (userToken != null
                            && userToken.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(UserRole.MODERATOR.toString())));
                }
                return false;
            }

            private void sendMessageModeratorConnected(StompHeaderAccessor accessor) {
                AbstractAuthenticationToken userToken = (AbstractAuthenticationToken) accessor.getHeader("simpUser");
                OutputMessage outputMessage = new OutputMessage();
                outputMessage
                        .setMessage(userToken.getName() + " is connected")
                        .setStatus(OutputMessage.MODERATOR_CONNECTED)
                        .setTime(new SimpleDateFormat("HH:mm").format(new Date()))
                        .setAssignedModerator(userToken.getName(), accessor.getSessionId());
                simpMessagingTemplate.convertAndSend("/user/queue/online", outputMessage);
            }

            private void sendMessageModeratorDisconnected(StompHeaderAccessor accessor) {
                AbstractAuthenticationToken userToken = (AbstractAuthenticationToken) accessor.getHeader("simpUser");
                OutputMessage outputMessage = new OutputMessage();
                outputMessage
                        .setMessage(userToken.getName() + " is disconnected")
                        .setStatus(OutputMessage.MODERATOR_DISCONNECTED)
                        .setTime(new SimpleDateFormat("HH:mm").format(new Date()));

                simpMessagingTemplate.convertAndSend("/user/queue/online", outputMessage);
            }

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

            private void sendMessageChatConnectionStatus(StompHeaderAccessor accessor) {
                String sessionId = accessor.getSessionId();
                SimpMessageHeaderAccessor headerAccessor = getMessageHeaders(sessionId);
                OutputMessage chatStatusMsg;
                if (connectedModeratorsRegistry.getConnectedUsers().isEmpty()) {
                    chatStatusMsg = getMessageChatDisconnected(accessor);
                } else {
                    chatStatusMsg = getMessageChatConnected(accessor);
                }
                simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/online",
                        chatStatusMsg,
                        headerAccessor.getMessageHeaders());
            }

            private OutputMessage getMessageChatConnected(StompHeaderAccessor accessor) {
                OutputMessage chatStatusMsg = new OutputMessage();
                ConnectedUser randomMod = connectedModeratorsRegistry.getRandomUser();
                chatStatusMsg
                        .setTime(new SimpleDateFormat("HH:mm").format(new Date()))
                        .setTo(accessor.getSessionId())
                        .setStatus(OutputMessage.MESSAGE_CONNECTED)
                        .setMessage("The chat is available")
                        .setAssignedModerator(randomMod.getUsername(), randomMod.getUserSession());
                return chatStatusMsg;
            }

            private OutputMessage getMessageChatDisconnected(StompHeaderAccessor accessor) {
                OutputMessage chatStatusMsg = new OutputMessage();
                chatStatusMsg
                        .setTime(new SimpleDateFormat("HH:mm").format(new Date()))
                        .setTo(accessor.getSessionId())
                        .setStatus(OutputMessage.MESSAGE_DISCONNECTED)
                        .setMessage("The chat is currently unavailable");
                return chatStatusMsg;
            }

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