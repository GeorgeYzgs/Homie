package com.spring.group.services;


import com.spring.group.dto.property.PropertyDTO;
import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;
import com.spring.group.repos.PropertyRepository;
import com.spring.group.services.bases.PropertyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public Optional<Property> findPropertyByID(Integer propertyID) {
        return propertyRepository.findById(propertyID);
    }

    @Override
    public String submitOffer(Property property, int userID) {
        if (userID == property.getOwner().getId()) {
            return "property.noOffer.myOwn";
        }
        if (hasPendingOffer(property, userID)) {
            return "property.noOffer.submitted";
        }
        if (!property.isAvailable()) {
            return "property.unavailable";
        }
        if (!property.isNonLocked()) {
            return "property.noOffer.locked";
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

    //TODO beatify this fucking SHIT
    @Override
    public Property unWrapUpdatableProperty(PropertyDTO propertyDTO) {
        Property persistent = getPropertyByID(propertyDTO.getPropertyID());
        persistent.setCategory(propertyDTO.getCategory());
        persistent.setArea(propertyDTO.getArea());
        persistent.setDescription(propertyDTO.getDescription());
        persistent.setFloor(propertyDTO.getFloor());
        persistent.setHeatingFuel(propertyDTO.getHeatingFuel());
        persistent.setHeatingType(propertyDTO.getHeatingType());
        persistent.setNumberOfRooms(propertyDTO.getNumberOfRooms());
        persistent.setPrice(propertyDTO.getPrice());
        persistent.getAddress().setCity(propertyDTO.getAddress_city());
        persistent.getAddress().setNumber(propertyDTO.getAddress_number());
        persistent.getAddress().setState(propertyDTO.getAddress_state());
        persistent.getAddress().setStreet(propertyDTO.getAddress_street());
        persistent.getAddress().setZipCode(propertyDTO.getAddress_zipCode());
        return persistent;
    }
}
