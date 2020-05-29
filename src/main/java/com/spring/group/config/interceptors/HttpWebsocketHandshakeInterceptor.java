package com.spring.group.config.interceptors;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Locale;
import java.util.Map;

/**
 * This http handshake interceptor is used to add locale to websocket request headers so we can differentiate between
 * different languages in the front end response. The @Order is used to set this interceptor before the spring
 * security interceptors.
 * Here we implement WebSocketMessageBrokerConfigurer as well. This is because when we created a different class
 * for this interceptor, there were some issues with the interceptor being read correctly. We got null instead of
 * the interceptor.
 */
@Component
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class HttpWebsocketHandshakeInterceptor implements HandshakeInterceptor, WebSocketMessageBrokerConfigurer {

    /**
     * Before the http handshake this method is adding a new session header that contains the locale of the user
     *
     * @param request    the http request
     * @param response   the http response
     * @param wsHandler  the websocket handler
     * @param attributes the Map <String, Object> where we put the key-pair value containing the locale
     * @return boolean
     * @throws Exception that is not implemented here
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            Locale locale = LocaleContextHolder.getLocale();
            attributes.put("HEADER_HTTP_LOCALE", locale);
        }
        return true;
    }

    /**
     * After the handshake method is not needed here but we have to override it from the HandshakeInterceptor interface
     *
     * @param request   the http request
     * @param response  the http response
     * @param wsHandler the websocket handler
     * @param exception exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {

    }

}
