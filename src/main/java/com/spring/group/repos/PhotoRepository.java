package com.spring.group.repos;

import com.spring.group.models.property.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George.Giazitzis
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
