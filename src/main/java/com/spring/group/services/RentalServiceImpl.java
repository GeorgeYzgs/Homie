package com.spring.group.services;

import com.spring.group.repos.RentalRepository;
import com.spring.group.services.bases.RentalServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class RentalServiceImpl implements RentalServiceInterface {

    @Autowired
    private RentalRepository rentalRepository;
}
