package com.spring.group.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George.Giazitzis
 */
@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLogRepository, Integer> {
}
