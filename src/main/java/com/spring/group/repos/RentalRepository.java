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

    /**
     * Fetch all offers or rentals by the user submitted the offer or the tenant respectively
     *
     * @param tenantUser the user submitted the offer or tenant
     * @return the List<Rental> with all the rentals found
     */
    List<Rental> findAllByTenant(User tenantUser);

    /**
     * Fetch all rentals pending (essentially the offers) by the user submitted the offer
     *
     * @param ownerUser the user submitted the offer
     * @return the List<Rental> with all the rentals found
     */
    List<Rental> findAllByIsPendingTrueAndResidenceOwner(User ownerUser);

    /**
     * Fetch all rentals that have started but not ended by the tenant
     *
     * @param tenant the tenant of the rental
     * @return the List<Rental> with all the rentals found
     */
    List<Rental> findAllByStartDateNotNullAndEndDateNullAndTenant(User tenant);

}
