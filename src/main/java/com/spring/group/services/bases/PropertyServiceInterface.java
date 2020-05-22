package com.spring.group.services.bases;

import com.spring.group.dto.property.PropertyDTO;
import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author George.Giazitzis
 */
public interface PropertyServiceInterface {

    /**
     * Persists a given property to our database and returns the persisted object
     *
     * @param property the target property attempting to save.
     * @return a property object that was saved.
     */
    Property insertProperty(Property property);

    /**
     * Leverages JPA getOne method to return a proxy reference of a property in an attempt to not constantly
     * query our database.
     *
     * @param propertyID the target property id we are searching by/
     * @return the property linked to that id
     */
    Property getPropertyByID(Integer propertyID);

    /**
     * Persists a collection of properties to our database, leveraging JPA saveALL method to create one transaction,
     * making it lighter for our database.
     *
     * @param updatedProperties a collection of properties to be saved
     * @return the collection of the saved properties.
     */
    Collection<Property> updateProperties(Collection<Property> updatedProperties);

    /**
     * Attempts to submit an offer for a property, by validating that the provided userID is not the property owner's ID,
     * the property is not locked or unavailable, and the user has not already submitted an offer for the provided property
     *
     * @param property the provided property to submit an offer for
     * @param userID   the id of the user interested in submitting an offer.
     * @return "SUCCESS" if successful or an error message.
     */
    String submitOffer(Property property, int userID);


    /**
     * Queries the database to return an optional property by searching with ID
     * A method to avoid exceptions when manually inputting urls, ensures the user will be redirected
     * to a 404 page if the property does not exist
     *
     * @param propertyID the property id we are searching by
     * @return an optional property
     */
    Optional<Property> findPropertyByID(Integer propertyID);

    List<Property> getPropertiesByOwnerUser(User ownerUser);

    List<Property> getPropertiesByTenantUser(User tenantUser);

    Property unWrapUpdatableProperty(PropertyDTO propertyDTO);
}
