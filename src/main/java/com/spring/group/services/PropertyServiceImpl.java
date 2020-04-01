package com.spring.group.services;


import com.spring.group.repos.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author George.Giazitzis
 */
public class PropertyServiceImpl implements PropertyServiceInterface {

    @Autowired
    PropertyRepository propertyRepository;
}
