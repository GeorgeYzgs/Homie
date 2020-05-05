package com.spring.group.repos;

import com.spring.group.models.user.ResetPassToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Repository
public interface ResetPassTokenRepository extends JpaRepository<ResetPassToken, Integer> {

    Optional<ResetPassToken> findByResetPassToken(String confirmationToken);

    Optional<ResetPassToken> findByUserEmail(String email);
}
