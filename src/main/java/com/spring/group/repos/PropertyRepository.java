package com.spring.group.repos;

import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author George.Giazitzis
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer>,
        JpaSpecificationExecutor<Property> {

    /**
     * Fetch all Properties by owner
     *
     * @param ownerUser the owner of the property
     * @return the List<Property> of properties
     */
    List<Property> findAllByOwner(User ownerUser);

    /**
     * Fetch top 10 properties according to views that do have images attached
     *
     * @return the List<Property> of properties
     */
    List<Property> findTop10ByPhotoCollectionNotNullOrderByViews();

    /**
     * Custom jpql query to get properties that have offers
     *
     * @param tenantUser the user that submitted the offer
     * @return the List<Property> of properties
     */
    @Query("SELECT p FROM properties p, rentals r where r member of p.rentalCollection and r.startDate is not null and r.endDate is null and r.tenant = :tenant")
    List<Property> findAllByTenantStartDateNotNullAndEndDateNull(@Param("tenant") User tenantUser);
}
