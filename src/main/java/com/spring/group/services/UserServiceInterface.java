package com.spring.group.services;

import com.spring.group.models.user.User;

import java.util.Optional;

/**
 * @author George.Giazitzis
 */
public interface UserServiceInterface {

    User insertUser(User user);

    Optional<User> checkUserName(String username);

    Optional<User> checkEmail(String email);

    User getUserByID(Integer userID);
}
