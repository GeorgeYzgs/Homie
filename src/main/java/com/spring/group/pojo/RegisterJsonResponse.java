package com.spring.group.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterJsonResponse {

    public final String SUCCESS = "success";
    public final String ERROR = "error";

    private Map<Object, List<String>> messages;
    private Map<Object, List<String>> errors;
    private String status;

    public RegisterJsonResponse() {
        messages = new HashMap<>();
        errors = new HashMap<>();
    }

    public Map<Object, List<String>> getMessages() {
        return messages;
    }

    public void addFieldMessages(String key, List<String> messagesAL) {
        this.messages.put(key, messagesAL);
    }

    public void setMessagesMap(String key, List<String> messagesAL) {
        this.messages.put(key, messagesAL);
    }

    public Map<Object, List<String>> getErrors() {
        return errors;
    }

    public void addFieldErrors(String key, List<String> errorsAL) {
        this.errors.put(key, errorsAL);
    }

    public void setErrorsMap(Map<Object, List<String>> errors) {
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
