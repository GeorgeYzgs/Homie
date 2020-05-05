package com.spring.group.services;

import com.spring.group.models.postalcode.City;
import com.spring.group.repos.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityServiceInterface {

    @Autowired
    CityRepository cityRepository;

    @Override
    public List<City> getCityNamesAutocomplete(String query) {
        return cityRepository.findCitiesByCityNameContainingIgnoreCase(query);
    }
}
