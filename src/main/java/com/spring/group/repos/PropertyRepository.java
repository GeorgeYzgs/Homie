package com.spring.group.repos;

import com.spring.group.models.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author George.Giazitzis
 */
public interface PropertyRepository extends JpaRepository<Property, Integer> {
}
