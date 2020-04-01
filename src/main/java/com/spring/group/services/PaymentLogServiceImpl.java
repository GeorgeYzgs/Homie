package com.spring.group.services;

import com.spring.group.repos.PaymentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author George.Giazitzis
 */
public class PaymentLogServiceImpl implements PaymentLogServiceInterface {

    @Autowired
    PaymentLogRepository paymentLogRepository;
}
