package com.spring.group.services;

import com.spring.group.models.postalcode.City;

import java.util.List;

public interface CityServiceInterface {

    List<City> getCityNamesAutocomplete(String query);
}
