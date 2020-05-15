package com.spring.group.services.bases;

import com.spring.group.models.property.Property;

import java.util.Collection;

/**
 * @author George.Giazitzis
 */
public interface PropertyServiceInterface {

    Property insertProperty(Property property);

    Property getPropertyByID(Integer PropertyID);

    Collection<Property> updateProperties(Collection<Property> updatedProperties);

    void alterAvailability(Property property);
}
