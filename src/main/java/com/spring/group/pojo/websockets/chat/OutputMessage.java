package com.spring.group.pojo.websockets.chat;

import java.util.HashMap;
import java.util.Map;

public class OutputMessage {

    public static final String MESSAGE_SUCCESS = "SUCCESS";
    public static final String MESSAGE_FAIL = "FAIL";
    public static final String USER_IDENTIFIER = "USER_IDENTIFIER";
    public static final String MODERATOR_CONNECTED = "MOD_CONNECTED";
    public static final String MODERATOR_DISCONNECTED = "MOD_DISCONNECTED";
    public static final String MESSAGE_CONNECTED = "CHAT_CONNECTED";
    public static final String MESSAGE_DISCONNECTED = "CHAT_DISCONNECTED";

    private String status;
    private String from;
    private String to;
    private String time;
    private String message;
    private Map<String, Map<String, String>> metadata;

    public OutputMessage() {
        this.metadata = new HashMap<>();
    }

    public OutputMessage(String from, String message, String status, String time) {
        this.from = from;
        this.status = status;
        this.setMessage(message);
        this.time = time;
        this.metadata = new HashMap<>();
    }

    public OutputMessage(String status, String message, String time) {
        this.status = status;
        this.setMessage(message);
        this.time = time;
        this.metadata = new HashMap<>();
    }

    public String getStatus() {
        return status;
    }

    public OutputMessage setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public OutputMessage setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTime() {
        return time;
    }

    public OutputMessage setTime(String time) {
        this.time = time;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public OutputMessage setMessage(String text) {
        this.message = text;
        return this;
    }

    public String getTo() {
        return to;
    }

    public OutputMessage setTo(String to) {
        this.to = to;
        return this;
    }

    public Map<String, Map<String, String>> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Map<String, String>> metadata) {
        this.metadata = metadata;
    }

    public void setAssignedModerator(String username, String sessionId) {
        HashMap<String, String> moderatorHashMap = new HashMap<>();
        moderatorHashMap.put("username", username);
        moderatorHashMap.put("sessionId", sessionId);
        this.metadata.put("moderator", moderatorHashMap);
    }

    public void setUserIdentifier(String username, String sessionId) {
        HashMap<String, String> moderatorHashMap = new HashMap<>();
        moderatorHashMap.put("username", username);
        moderatorHashMap.put("sessionId", sessionId);
        this.metadata.put("userDetails", moderatorHashMap);
    }

}
