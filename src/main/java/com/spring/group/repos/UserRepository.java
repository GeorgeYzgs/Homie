package com.spring.group.repos;

import com.spring.group.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George.Giazitzis
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
