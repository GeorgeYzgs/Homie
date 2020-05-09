package com.spring.group.services.bases;

import com.spring.group.models.postalcode.City;

import java.util.List;

public interface CityServiceInterface {

    List<City> getCityNamesAutocomplete(String query);
}
