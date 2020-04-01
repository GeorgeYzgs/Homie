package com.spring.group.services;

import com.spring.group.repos.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author George.Giazitzis
 */
public class RentalServiceImpl implements RentalServiceInterface {

    @Autowired
    RentalRepository rentalRepository;
}
