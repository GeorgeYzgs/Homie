package com.spring.group.repos;

import com.spring.group.models.rental.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author George.Giazitzis
 */
@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLog, Integer> {
}
