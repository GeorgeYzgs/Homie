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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private ConnectedUsersRegistry connectedUsersRegistry;


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {


            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())
                        && accessor.getDestination().equals("/user/queue/specific-user")) {
                    // When a MODERATOR connects
                    if (accessor.getHeader("simpUser") != null
                            && (accessor.getHeader("simpUser") instanceof UsernamePasswordAuthenticationToken
                            || accessor.getHeader("simpUser") instanceof OAuth2AuthenticationToken)) {
                        UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) accessor.getHeader("simpUser");
                        if (userToken.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(UserRole.MODERATOR.toString()))) {
                            ConnectedUser connectedUser = new ConnectedUser(userToken.getName(), accessor.getSessionId(), UserRole.MODERATOR.toString());
                            connectedUsersRegistry.addUser(connectedUser.getUsername(), connectedUser);
                            OutputMessage outputMessage = new OutputMessage();
                            outputMessage
                                    .setMessage("The chat is connected")
                                    .setStatus(OutputMessage.MESSAGE_CONNECTED)
                                    .setFrom(userToken.getName())
                                    .setTime(new SimpleDateFormat("HH:mm").format(new Date()))
                                    .setAssignedModerator(userToken.getName(), accessor.getSessionId());
                            simpMessagingTemplate.convertAndSend("/user/queue/online", outputMessage);
//                            System.out.println(connectedUsersRegistry.getConnectedUsers());
                        }
                    } else {
                        // When someone connects
                        String sessionId = accessor.getSessionId();
                        // SessionId has to go to the HEADERS as well
                        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                                .create(SimpMessageType.MESSAGE);
                        headerAccessor.setSessionId(sessionId);
                        headerAccessor.setLeaveMutable(true);
                        OutputMessage userIdentifierMsg = new OutputMessage();
                        userIdentifierMsg
                                .setStatus("UserIdentifier")
                                .setUserIdentifier("", sessionId);
                        simpMessagingTemplate.convertAndSendToUser(accessor.getSessionId(), "/queue/online",
                                userIdentifierMsg,
                                headerAccessor.getMessageHeaders());
                        OutputMessage chatStatusMsg = new OutputMessage();
                        chatStatusMsg
                                .setTime(new SimpleDateFormat("HH:mm").format(new Date()))
                                .setTo(accessor.getSessionId());
                        if (connectedUsersRegistry.getConnectedUsers().isEmpty()) {
                            chatStatusMsg
                                    .setStatus(OutputMessage.MESSAGE_DISCONNECTED)
                                    .setMessage("The chat is currently unavailable");
                        } else {
                            chatStatusMsg
                                    .setStatus(OutputMessage.MESSAGE_CONNECTED)
                                    .setMessage("The chat is available");

                        }
                        simpMessagingTemplate.convertAndSendToUser(accessor.getSessionId(), "/queue/online",
                                chatStatusMsg,
                                headerAccessor.getMessageHeaders());
                    }
                }

                // When a MODERATOR disconnects
                if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    if (accessor.getHeader("simpUser") != null
                            && (accessor.getHeader("simpUser") instanceof UsernamePasswordAuthenticationToken
                            || accessor.getHeader("simpUser") instanceof OAuth2AuthenticationToken)) {
                        UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) accessor.getHeader("simpUser");
//            System.out.println(userToken.getPrincipal());
                        if (userToken.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(UserRole.MODERATOR.toString()))) {
                            connectedUsersRegistry.removeUser(userToken.getName());
                            simpMessagingTemplate.convertAndSend("/user/queue/online",
                                    new OutputMessage(OutputMessage.MESSAGE_DISCONNECTED,
                                            "The chat is currently unavailable",
                                            new SimpleDateFormat("HH:mm").format(new Date())));
//                            System.out.println(connectedUsersRegistry.getConnectedUsers());
                        }
                    }
                }
                return message;
            }
        });
    }
}

