package com.spring.group.pojo.websockets.chat;

public class Message {


    private String from;
    private String to;
    private String message;


    public String getTo() {
        return to;
    }

    public Message setTo(String to) {
        this.to = to;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String text) {
        this.message = text;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
