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

    /**
     * A method query created to find reset pass tokens by their reset pass token generated String
     *
     * @param resetPassToken the UUID string linked to the token
     * @return an optional token if the given string was linked to a token
     */
    Optional<ResetPassToken> findByResetPassToken(String resetPassToken);
}
