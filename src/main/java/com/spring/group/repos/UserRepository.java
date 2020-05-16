package com.spring.group.repos;

import com.spring.group.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * A method query to check if a user exists with a provided unique user name
     *
     * @param username the username we are searching by
     * @return an optional user if the given username is used.
     */
    Optional<User> findByUsername(String username);

    /**
     * A method query to check if a user exists with a provided unique email address
     *
     * @param email the email address we are searching by
     * @return an optional user if the given email address is used.
     */
    Optional<User> findByEmail(String email);
}
