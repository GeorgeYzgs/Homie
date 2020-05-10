package com.spring.group.repos;

import com.spring.group.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George.Giazitzis
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
