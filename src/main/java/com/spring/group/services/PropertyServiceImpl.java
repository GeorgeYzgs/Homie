package com.spring.group.services;


import com.spring.group.models.property.Property;
import com.spring.group.repos.PropertyRepository;
import com.spring.group.services.bases.PropertyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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
    public Property getPropertyByID(Integer PropertyID) {
        return propertyRepository.getOne(PropertyID);
    }

    @Override
    public Collection<Property> updateProperties(Collection<Property> updatedProperties) {
        return propertyRepository.saveAll(updatedProperties);
    }

    @Override
    public Property getFullProperty(Integer PropertyID) {
        return propertyRepository.findById(PropertyID).get();
    }
}
