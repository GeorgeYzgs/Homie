package com.spring.group.controllers;

import com.spring.group.dto.property.specifications.SearchCriteria;
import com.spring.group.pojo.PropertyCollectionResponse;
import com.spring.group.pojo.SearchParamsPojo;
import com.spring.group.services.bases.PropertyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.spring.group.dto.property.specifications.PropertySpecificationsCustom.getPropertiesByCity;

@Controller
public class SearchController {

    @Autowired
    PropertyServiceInterface propertyService;

    @GetMapping("/async/search")
    @ResponseBody
    public PropertyCollectionResponse getAsyncSearchResults(SearchParamsPojo searchParamsPojo,
                                                            @RequestParam("city") String city,
                                                            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
                                                            @RequestParam(name = "sort", defaultValue = "AUTOMATIC") String sortType,
                                                            Locale userLocale) {
        List<SearchCriteria> searchCriteria = searchParamsPojo.toSearchCriteria();
        List<Specification> specifications = new ArrayList<>();
        if (city != null && !city.isEmpty()) {
            specifications.add(getPropertiesByCity(city));
        }
        int pageLimit = 10;
        PropertyCollectionResponse propertiesRes =
                propertyService.getAllPropertiesByUserCriteria(searchCriteria, specifications, pageNumber, pageLimit, sortType, userLocale);
        return propertiesRes;
    }

    @GetMapping("/custom-search")
    public String getSearchPage(ModelMap modelMap) {
        SearchParamsPojo searchParamsPojo = new SearchParamsPojo();
        modelMap.addAttribute("searchParams", searchParamsPojo);
        modelMap.addAttribute("sortType", "AUTOMATIC");
        return "search-results";
    }

    @GetMapping("/search")
    public String getSearchResults(SearchParamsPojo searchParamsPojo,
                                   @RequestParam("city") String city,
                                   @RequestParam(name = "page", defaultValue = "1") int pageNumber,
                                   @RequestParam(name = "sort", defaultValue = "AUTOMATIC") String sortType,
                                   Locale userLocale,
                                   ModelMap modelMap) {
        List<SearchCriteria> searchCriteria = searchParamsPojo.toSearchCriteria();
        List<Specification> specifications = new ArrayList<>();
        if (city != null && !city.isEmpty()) {
            specifications.add(getPropertiesByCity(city));
        }
        int pageLimit = 10;
        PropertyCollectionResponse propertiesRes =
                propertyService.getAllPropertiesByUserCriteria(searchCriteria, specifications, pageNumber, pageLimit, sortType, userLocale);
        modelMap.addAttribute("propertiesResults", propertiesRes);
        modelMap.addAttribute("searchParams", searchParamsPojo);
        modelMap.addAttribute("searchCity", city);
        modelMap.addAttribute("sortType", sortType);
        return "search-results";
    }
}
