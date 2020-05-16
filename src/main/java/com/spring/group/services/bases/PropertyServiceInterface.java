package com.spring.group.services.bases;

import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;

import java.util.Collection;
import java.util.List;

/**
 * @author George.Giazitzis
 */
public interface PropertyServiceInterface {

    Property insertProperty(Property property);

    Property getPropertyByID(Integer propertyID);

    List<Property> getPropertiesByOwnerUser(User ownerUser);

    List<Property> getPropertiesByTenantUser(User tenantUser);

    Collection<Property> updateProperties(Collection<Property> updatedProperties);

    void alterAvailability(Property property);
}
