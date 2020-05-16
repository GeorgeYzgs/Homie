package com.spring.group.services;

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
    public boolean handleOffer(Rental rental, boolean isAccepted) {
        rental.setPending(false);
        if (isAccepted) {
            rental.setStartDate(Instant.now());
            rentalRepository.save(rental);
            propertyService.alterAvailability(rental.getResidence());
            return true;
        }
        rentalRepository.save(rental);
        return false;
    }
}
