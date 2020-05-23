package com.spring.group.pojo.websockets.chat;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component("ConnectedUsersRegistry")
public class ConnectedUsersRegistry {

    private static final Map<String, ConnectedUser> usersRegistry = new ConcurrentHashMap<>();

    public Map<String, ConnectedUser> getUsersRegistry() {
        return usersRegistry;
    }

    public Map<String, ConnectedUser> getConnectedUsers() {
        return usersRegistry;
    }

    public void addUsers(Map<String, ConnectedUser> connectedUserMap) {
        usersRegistry.putAll(connectedUserMap);
    }

    public void addUser(String username, ConnectedUser connectedUser) {
        if (usersRegistry.containsKey(username)) usersRegistry.replace(username, connectedUser);
        usersRegistry.put(username, connectedUser);
    }

    public ConnectedUser getRandomUser() {
        List<String> keysAL = new ArrayList<>(usersRegistry.keySet());
        String randomUsernameKey = keysAL.get(new Random().nextInt(keysAL.size()));
        return usersRegistry.get(randomUsernameKey);
    }

    public void removeUser(String username) {
        usersRegistry.remove(username);
    }

}
