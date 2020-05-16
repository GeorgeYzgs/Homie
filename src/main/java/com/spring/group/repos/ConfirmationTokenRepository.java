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

    /**
     * A method query created to find confirmation tokens by their confirmation token generated String
     *
     * @param confirmationToken the UUID string linked to the token
     * @return an optional token if the given string was linked to a token
     */
    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);

}
