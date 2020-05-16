package com.spring.group.services.bases;

import com.spring.group.models.property.Property;

import java.util.Collection;

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
     * @param PropertyID the target property id we are searching by/
     * @return the property linked to that id
     */
    Property getPropertyByID(Integer PropertyID);

    /**
     * Persists a collection of properties to our database, leveraging JPA saveALL method to create one transaction,
     * making it lighter for our database.
     *
     * @param updatedProperties a collection of properties to be saved
     * @return the collection of the saved properties.
     */
    Collection<Property> updateProperties(Collection<Property> updatedProperties);

    /**
     * Utilized JPA findbyID method to fetch a property object EAGERLY,
     * only to be used when getPropertyByID would return an exception, as it stresses the database.
     *
     * @param PropertyID the target property id we are searchinb by
     * @return a full property object from our database.
     * @see getPropertyByID
     */
    Property getFullProperty(Integer PropertyID);
}
