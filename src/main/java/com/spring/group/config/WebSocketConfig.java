package com.spring.group.config;

import com.spring.group.config.interceptors.UserRegistryInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private UserRegistryInterceptor userRegistryInterceptor;

//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.enableSimpleBroker("/topic/");
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
//                .addInterceptors(new HttpSessionHandshakeInterceptor());

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic/", "/user", "/queue");
        registry.setUserDestinationPrefix("/user");
    }

//    @Override
//    public void configureStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat").withSockJS();
////                .addInterceptors(new HttpSessionHandshakeInterceptor());
//    }
//

}

