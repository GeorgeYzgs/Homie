package com.spring.group.controllers;

import com.spring.group.pojo.websockets.chat.ConnectedUsersRegistry;
import com.spring.group.pojo.websockets.chat.Message;
import com.spring.group.pojo.websockets.chat.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    //    Getting users subscribed
    @Autowired
    private SimpUserRegistry userRegistry;

    @Autowired
    private ConnectedUsersRegistry connectedUsersRegistry;

    @MessageMapping("/chat")
    public void sendSpecific(
            @Payload Message msg,
            MessageHeaders messageHeaders,
            Principal principal,
            @Header("simpSessionId") String sessionId
    ) {
        OutputMessage outMsg = new OutputMessage();
        outMsg
                .setTo(msg.getTo())
                .setFrom(sessionId)
                .setMessage(msg.getMessage())
                .setTime(new SimpleDateFormat("HH:mm").format(new Date()));
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(msg.getTo());
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(msg.getTo(), "/queue/specific-user", outMsg, headerAccessor.getMessageHeaders());
    }
}
