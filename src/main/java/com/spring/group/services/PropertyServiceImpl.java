package com.spring.group.services;


import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;
import com.spring.group.models.rental.Rental;
import com.spring.group.repos.PropertyRepository;
import com.spring.group.services.bases.PropertyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class PropertyServiceImpl implements PropertyServiceInterface {

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Property insertProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Property getPropertyByID(Integer propertyID) {
        return propertyRepository.getOne(propertyID);
    }

    @Override
    public List<Property> getPropertiesByOwnerUser(User ownerUser) {
        return propertyRepository.findAllByOwner(ownerUser);
    }

    @Override
    public List<Property> getPropertiesByTenantUser(User tenantUser) {
        return propertyRepository.findAllByTenant(tenantUser);
    }

    @Override
    public Collection<Property> updateProperties(Collection<Property> updatedProperties) {
        return propertyRepository.saveAll(updatedProperties);
    }

    @Override
    public Property getFullProperty(Integer PropertyID) {
        return propertyRepository.findById(PropertyID).get();
    }

    @Override
    public String submitOffer(Property property, int userID) {
        if (userID == property.getOwner().getId()) {
            return "You cannot submit an offer for your own property";
        }
        if (hasPendingOffer(property, userID)) {
            return "You have already submitted an offer for this property";
        }
        if (!property.isAvailable()) {
            return "This property is no longer available";
        }
        if (!property.isNonLocked()) {
            return "You cannot submit an offer for a locked property";
        }
        return "SUCCESS";
    }

    /**
     * Checks the collection of pending rentals of a property to see if a user has
     * already submitted an offer for the provided property
     *
     * @param property the provided property
     * @param userID   the id of the user interested in submitting an offer
     * @return true if the user already has an offer pending, otherwise false.
     */
    private boolean hasPendingOffer(Property property, int userID) {
        return property.getRentalCollection().stream().
                filter(rental -> rental.getTenant().getId() == userID).anyMatch(Rental::isPending);
    }
}
