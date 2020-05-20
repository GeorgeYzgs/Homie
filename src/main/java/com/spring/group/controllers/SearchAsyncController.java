package com.spring.group.controllers;

import com.spring.group.dto.property.specifications.SearchCriteria;
import com.spring.group.pojo.PropertyCollectionResponse;
import com.spring.group.pojo.SearchParamsPojo;
import com.spring.group.services.bases.PropertyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PropertyCollectionResponse getSearchResults(SearchParamsPojo searchParamsPojo,
                                                       @RequestParam("city") String city,
                                                       @RequestParam(name = "page", defaultValue = "1") int pageNumber,
                                                       Locale userLocale) {
        List<SearchCriteria> searchCriteria = searchParamsPojo.toSearchCriteria();
        List<Specification> specifications = new ArrayList<>();
        if (city != null && !city.isEmpty()) {
            specifications.add(getPropertiesByCity(city));
        }
        Pageable reqCount = PageRequest.of(pageNumber - 1, 10);
        PropertyCollectionResponse propertiesRes = propertyService.searchPropertiesJsonResponse(searchCriteria, specifications, userLocale, reqCount);
        return propertiesRes;
    }
}
