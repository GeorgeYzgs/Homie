package com.spring.group.services;

import com.spring.group.models.postalcode.City;
import com.spring.group.repos.CityRepository;
import com.spring.group.services.bases.CityServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityServiceImpl implements CityServiceInterface {

    @Autowired
    CityRepository cityRepository;

    @Override
    public List<City> getCityNamesAutocomplete(String query) {
        return cityRepository.findTop10CitiesByCityNameContainingIgnoreCase(query);
    }
}
