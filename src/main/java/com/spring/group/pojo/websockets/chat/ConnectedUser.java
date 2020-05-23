package com.spring.group.pojo.websockets.chat;

public class ConnectedUser {

    private String username;
    private String userSessionId;
    private String authority;

    public ConnectedUser(String username, String userSessions, String authority) {
        this.username = username;
        this.userSessionId = userSessions;
        this.authority = authority;
    }


    public ConnectedUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserSessions() {
        return userSessionId;
    }

    public void setUserSessions(String userSessions) {
        this.userSessionId = userSessions;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "ConnectedUser{" +
                "username='" + username + '\'' +
                ", userSessionId='" + userSessionId + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }
}
