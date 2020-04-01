package com.spring.group.services;

import com.spring.group.models.user.User;

import java.util.Optional;

/**
 * @author George.Giazitzis
 */
public interface UserServiceInterface {

    User insertUser(User user);

    public Optional<User> checkUserName(String username);

    public User getUserByID(Integer userID);
}
