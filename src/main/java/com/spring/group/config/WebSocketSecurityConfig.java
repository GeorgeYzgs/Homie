package com.spring.group.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig
        extends AbstractSecurityWebSocketMessageBrokerConfigurer {

//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .nullDestMatcher().authenticated()
//                .simpDestMatchers("/chatroom/**").hasAuthority("USER")
//                .anyMessage().authenticated();
//    }

    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
//                .nullDestMatcher().authenticated()
//                .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
//                .simpDestMatchers("/app/**","/topic/greetings").hasAuthority("USER")
//                .simpSubscribeDestMatchers("/user/**", "chatroom/topic/**", "/chatroom/**").permitAll()
                .anyMessage().permitAll();
    }

}
