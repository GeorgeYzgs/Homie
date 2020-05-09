package com.spring.group.services.bases;

import com.spring.group.models.user.User;

import java.util.Optional;

/**
 * @author George.Giazitzis
 */
public interface UserServiceInterface {

    User insertUser(User user);

    User updateUser(User user);

    Optional<User> checkUserName(String username);

    Optional<User> checkEmail(String email);

    User getUserByID(Integer userID);
}
