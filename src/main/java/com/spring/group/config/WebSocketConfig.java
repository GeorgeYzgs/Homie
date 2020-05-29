package com.spring.group.config;

import com.spring.group.config.interceptors.HttpWebsocketHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Websocket main configuration of endpoints and destination prefixes. Stomp protocol is used for messages.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Method to register stomp protocol endpoints. We also add the interceptor we set up for localization purposes.
     * We also add SockS support for http or pooling fallback on older browsers without websocket functionality.
     *
     * @param registry the StompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .addInterceptors(new HttpWebsocketHandshakeInterceptor())
                .withSockJS();

    }

    /**
     * Method to register simple broker prefixes, application destination prefixes and user specific destination prefixes
     *
     * @param registry the MessageBrokerRegistry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic/", "/user", "/queue");
        registry.setUserDestinationPrefix("/user");
    }


}

