package com.spring.group.repos;

import com.spring.group.models.rental.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George.Giazitzis
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
