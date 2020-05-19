package com.spring.group.controllers;

import com.spring.group.dto.property.specifications.SearchCriteria;
import com.spring.group.models.property.Property;
import com.spring.group.pojo.SearchPojo;
import com.spring.group.services.bases.PropertyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.spring.group.dto.property.specifications.PropertySpecificationsCustom.getPropertiesByCity;

@RestController
public class SearchAsyncController {

    @Autowired
    PropertyServiceInterface propertyService;

    @GetMapping("/async/search")
    public String getSearchResults(SearchPojo searchPojo, @RequestParam("city") String city) {
        List<SearchCriteria> searchCriteria = searchPojo.toSearchCriteria();
        List<Specification> specifications = new ArrayList<>();
        if (city != null && !city.isEmpty()) {
            specifications.add(getPropertiesByCity(city));
        }
        List<Property> properties = propertyService.searchProperties(searchCriteria, specifications);
        properties.forEach(p -> System.out.println("Id: " + p.getId() + " Area: " + p.getArea() + " Category: " + p.getCategory()));
        return "Hi";
    }
}
