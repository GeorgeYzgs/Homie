package com.spring.group.services;

import com.spring.group.models.rental.PaymentLog;
import com.spring.group.repos.PaymentLogRepository;
import com.spring.group.services.bases.PaymentLogServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class PaymentLogServiceImpl implements PaymentLogServiceInterface {

    @Autowired
    private PaymentLogRepository paymentLogRepository;

    @Override
    public PaymentLog insertPaymentLog(PaymentLog paymentLog) {
        return paymentLogRepository.save(paymentLog);
    }
}
