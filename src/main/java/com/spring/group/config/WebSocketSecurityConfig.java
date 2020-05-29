package com.spring.group.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

/**
 * This class is used for setting up websocket security and manage access to endpoints for authenticated users.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig
        extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    /**
     * Method to manage access to inbound messages and connection attempts
     *
     * @param messages the MessageSecurityMetadataSourceRegistry
     */
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
//                .nullDestMatcher().authenticated()
//                .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
//                .simpDestMatchers("/app/**","/topic/greetings").hasAuthority("USER")
//                .simpSubscribeDestMatchers("/user/**", "chatroom/topic/**", "/chatroom/**").permitAll()
                .anyMessage().permitAll();
    }

}
