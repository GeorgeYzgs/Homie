package com.spring.group.services.bases;

import com.spring.group.models.user.User;

import java.util.Collection;
import java.util.Optional;

/**
 * @author George.Giazitzis
 */
public interface UserServiceInterface {

    /**
     * Persists a NEW user to our database, after encoding his password and returns an object of that user
     *
     * @param user the user to be persisted
     * @return a user object that was saved
     */
    User insertUser(User user);

    /**
     * Updates an already persisted user to our database
     *
     * @param user the user object to be updated
     * @return the updated user object
     */
    User updateUser(User user);

    /**
     * Checks the database for the availability of a username, returns an optional user that was linked to it
     *
     * @param username the username to search by
     * @return an optional user, depending on the availability of the given username
     */
    Optional<User> checkUserName(String username);

    /**
     * Checks the database for the availability of an email address, returns an optional user that was linked to it
     *
     * @param email the email address to search by
     * @return an optional user, depending on the availability of the given email address
     */
    Optional<User> checkEmail(String email);

    /**
     * Leverages JPA getOne method to LAZILY return a reference of a user object by proxy,
     * to avoid straining our database
     *
     * @param userID the user id to search by
     * @return a user object affiliated with the given id
     */
    User getUserByID(Integer userID);

    /**
     * A method to return all our registered users
     *
     * @return a collection of all users
     */
    Collection<User> getUserList();
}
