package com.spring.group.services;

import com.spring.group.models.Address;
import com.spring.group.repos.AddressRepository;
import com.spring.group.services.bases.AddressServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressServiceInterface {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public Address insertAddress(Address address) {
        return addressRepository.save(address);
    }
}
