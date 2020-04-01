package com.spring.group.repos;

import com.spring.group.models.rental.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author George.Giazitzis
 */
public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
