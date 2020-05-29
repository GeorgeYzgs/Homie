package com.spring.group.services.bases;

import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;

import java.util.List;

/**
 * @author George.Giazitzis
 */
public interface RentalServiceInterface {

    /**
     * Persists a rental object to our database
     *
     * @param rental the rental object to be persisted
     * @return a rental object that was saved.
     */
    Rental insertRental(Rental rental);

    /**
     * Utilized JPA getOne method to LAZILY fetch a rental object from a proxy, not stressing our database.
     *
     * @param rentalID the rental id we are searching by
     * @return the rental object affiliated with that id
     */
    Rental getRentalByID(Integer rentalID);

    /**
     * Processes the offer an owner has received for his property based on his decision,
     * Updates the affiliated entities based on said decision
     *
     * @param rental     the offer the owner has reviewed
     * @param isAccepted the owner's decision of accepting or declining said offer
     * @return the result of handling the offer
     */
    String manageOffers(Rental rental, boolean isAccepted, int userID);

    /**
     * Queries the database to get the rentals by tenant that are ongoing
     *
     * @param tenant the tenant of the rentals
     * @return the List of the rentals found
     */
    List<Rental> getRentalsByTenantStartedAndNotEnded(User tenant);

    /**
     * Queries the database to get all rentals by tenant
     *
     * @param tenant the tenant of the rentals
     * @return the List of the rentals found
     */
    List<Rental> getRentalsByTenant(User tenant);

    /**
     * Queries the database to get the rentals by the owner
     *
     * @param owner the owner of the rentals
     * @return the List of the rentals found
     */
    List<Rental> getRentalsByOwner(User owner);

    boolean hasPaidRent(Rental rental);

    void closeRental(Rental rental);
}
