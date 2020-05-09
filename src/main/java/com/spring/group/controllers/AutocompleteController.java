package com.spring.group.controllers;

import com.spring.group.pojo.AutocompleteResponse;
import com.spring.group.services.bases.CityServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/autocomplete-utility")
public class AutocompleteController {

    @Autowired
    CityServiceInterface cityService;

    @GetMapping("/city/{query}")
    public List<AutocompleteResponse> getCityAutocomplete(@PathVariable String query) {
        ArrayList<AutocompleteResponse> autocompleteAL = new ArrayList<>();
        cityService.getCityNamesAutocomplete(query)
                .forEach(city -> autocompleteAL.add(new AutocompleteResponse(city.getCityName())));
        return autocompleteAL;
    }
}
