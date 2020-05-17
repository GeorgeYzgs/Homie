package com.spring.group.services;

import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;
import com.spring.group.repos.RentalRepository;
import com.spring.group.services.bases.PropertyServiceInterface;
import com.spring.group.services.bases.RentalServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Collection;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class RentalServiceImpl implements RentalServiceInterface {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PropertyServiceInterface propertyService;

    @Override
    public Rental insertRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Override
    public Rental getRentalByID(Integer rentalID) {
        return rentalRepository.getOne(rentalID);
    }

    @Override
    public List<Rental> getRentalsByTenant(User tenant) {
        return rentalRepository.findAllByTenant(tenant);
    }

    @Override
    public List<Rental> getRentalsByOwner(User owner) {
        return rentalRepository.findAllByResidenceOwner(owner);
    }

    @Override
    public String manageOffers(Rental rental, boolean isAccepted, int userID) {
        if (userID != rental.getResidence().getOwner().getId()) {
            return "You can only manage offers to your properties!";
        }
        if (!rental.isPending()) {
            return "This offer has already been managed.";
        }
        if (handleOffer(rental, isAccepted)) {
            return "Offer accepted!";
        }
        return "Offer declined";
    }

    private boolean handleOffer(Rental rental, boolean isAccepted) {
        rental.setPending(false);
        if (isAccepted) {
            rental.setStartDate(Instant.now());
            Property property = rental.getResidence();
            property.setAvailable(false);
            propertyService.insertProperty(property);
            Collection<Rental> offers = property.getRentalCollection();
            offers.forEach(offer -> offer.setPending(false));
            rentalRepository.saveAll(offers);
            return true;
        }
        rentalRepository.save(rental);
        return false;
    }
}
