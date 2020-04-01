package com.spring.group.repos;

import com.spring.group.models.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George.Giazitzis
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
}
