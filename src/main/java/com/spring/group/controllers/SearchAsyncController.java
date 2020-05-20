package com.spring.group.controllers;

import com.spring.group.dto.property.specifications.SearchCriteria;
import com.spring.group.pojo.PropertyJsonResponse;
import com.spring.group.pojo.SearchParamsPojo;
import com.spring.group.services.bases.PropertyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.spring.group.dto.property.specifications.PropertySpecificationsCustom.getPropertiesByCity;

@RestController
public class SearchAsyncController {

    @Autowired
    PropertyServiceInterface propertyService;

    @GetMapping("/async/search")
    public List<PropertyJsonResponse> getSearchResults(SearchParamsPojo searchParamsPojo,
                                                       @RequestParam("city") String city, Locale userLocale) {
        List<SearchCriteria> searchCriteria = searchParamsPojo.toSearchCriteria();
        List<Specification> specifications = new ArrayList<>();
        if (city != null && !city.isEmpty()) {
            specifications.add(getPropertiesByCity(city));
        }
        List<PropertyJsonResponse> propertiesRes = propertyService.searchPropertiesJsonResponse(searchCriteria, specifications, userLocale);
//        properties.forEach(p -> System.out.println("Id: " + p.getId() + " Area: " + p.getArea() + " Category: " + p.getCategory()));
        return propertiesRes;
    }
}
