package com.spring.group.services.bases;

import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;

import java.util.List;

/**
 * @author George.Giazitzis
 */
public interface RentalServiceInterface {

    Rental insertRental(Rental rental);

    Rental getRentalByID(Integer rentalID);

    List<Rental> getRentalsByTenant(User tenant);

    List<Rental> getRentalsByOwner(User owner);

    boolean handleOffer(Rental rental, boolean isAccepted);
}
