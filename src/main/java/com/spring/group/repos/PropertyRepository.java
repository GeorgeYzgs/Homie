package com.spring.group.repos;

import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author George.Giazitzis
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

    public List<Property> findAllByOwner(User ownerUser);

    @Query("SELECT p FROM properties p, rentals r where r member of p.rentalCollection and r.startDate is not null and r.endDate is null and r.tenant = :tenant")
    public List<Property> findAllByTenant(@Param("tenant") User tenantUser);
}
