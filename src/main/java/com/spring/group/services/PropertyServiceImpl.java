package com.spring.group.services;


import com.spring.group.dto.property.specifications.PropertySpecificationBuilder;
import com.spring.group.dto.property.specifications.SearchCriteria;
import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;
import com.spring.group.pojo.PropertyCollectionResponse;
import com.spring.group.pojo.PropertyResponse;
import com.spring.group.repos.PropertyRepository;
import com.spring.group.services.bases.PropertyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class PropertyServiceImpl implements PropertyServiceInterface {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    MessageSource messageSource;

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

    @Override
    public List<Property> searchProperties(List<SearchCriteria> searchCriteria) {
        PropertySpecificationBuilder psb = new PropertySpecificationBuilder();
        searchCriteria.forEach(psb::with);
        final Specification<Property> spec = psb.build();
        return propertyRepository.findAll(spec);
    }

    @Override
    public List<Property> searchProperties(List<SearchCriteria> searchCriteria, List<Specification> specifications) {
        PropertySpecificationBuilder psb = new PropertySpecificationBuilder();
        searchCriteria.forEach(psb::with);
        specifications.forEach(psb::with);
        final Specification<Property> spec = psb.build();
        if (spec == null) return new ArrayList<>();
        return propertyRepository.findAll(spec);
    }

    @Override
    public PropertyCollectionResponse searchPropertiesJsonResponse(List<SearchCriteria> searchCriteria,
                                                                   List<Specification> specifications,
                                                                   Locale userLocale,
                                                                   Pageable pageable) {
        PropertySpecificationBuilder psb = new PropertySpecificationBuilder();
        searchCriteria.forEach(psb::with);
        specifications.forEach(psb::with);
        final Specification<Property> spec = psb.build();
        if (spec == null) return new PropertyCollectionResponse();
        Page<Property> propertiesSlice = propertyRepository.findAll(spec, pageable);
        PropertyCollectionResponse response = new PropertyCollectionResponse();
        response.setProperties(
                propertiesSlice.getContent().stream()
                        .map(p -> {
                            PropertyResponse res = new PropertyResponse(p);
                            res.setCategory(messageSource.getMessage(res.getCategory(), null, userLocale));
                            return res;
                        }).collect(Collectors.toList())
        );
        response.setTotalPages(propertiesSlice.getTotalPages());
        // Zoro based pagination
        response.setCurrentPage(pageable.getPageNumber() + 1);
        return response;
    }
}
