package com.spring.group.repos;

import com.spring.group.models.user.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {

    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);

    Optional<ConfirmationToken> findByUserEmail(String email);
}
