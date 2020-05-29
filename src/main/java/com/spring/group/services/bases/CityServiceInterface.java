package com.spring.group.services.bases;

import com.spring.group.models.postalcode.City;

import java.util.List;

public interface CityServiceInterface {

    /**
     * Gets the suggested cities based on the user input and returns them
     *
     * @param query user input
     * @return the LIst<City> of the suggested cities
     */
    List<City> getCityNamesAutocomplete(String query);
}
