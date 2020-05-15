package com.spring.group.services.bases;

import com.spring.group.models.rental.Rental;

/**
 * @author George.Giazitzis
 */
public interface RentalServiceInterface {

    Rental insertRental(Rental rental);

    Rental getRentalByID(Integer rentalID);

    boolean handleOffer(Rental rental, boolean isAccepted);
}
