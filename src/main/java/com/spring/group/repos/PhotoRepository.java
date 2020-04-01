package com.spring.group.repos;

import com.spring.group.models.property.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author George.Giazitzis
 */
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
