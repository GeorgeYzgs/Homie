package com.spring.group.repos;

import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author George.Giazitzis
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    List<Rental> findAllByTenant(User tenantUser);

    List<Rental> findAllByIsPendingTrueAndResidenceOwner(User ownerUser);

    List<Rental> findAllByIsPendingTrueAndResidenceOwner(User ownerUser);
}
