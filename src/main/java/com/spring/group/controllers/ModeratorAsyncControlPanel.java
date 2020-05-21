package com.spring.group.controllers;

import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.services.bases.PropertyServiceInterface;
import com.spring.group.services.bases.RentalServiceInterface;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/async/mod")
public class ModeratorAsyncControlPanel {

    @Autowired
    UserServiceInterface userService;

    @Autowired
    PropertyServiceInterface propertyService;

    @Autowired
    RentalServiceInterface rentalService;

    /**
     * A controller to access a user's profile
     *
     * @param id the user's id provided as a path variable
     * @return the user page of that user
     */
    @GetMapping("/user/{id}/personal-details")
    public String displayUser(@PathVariable Integer id, ModelMap modelMap) {
        modelMap.addAttribute("userId", id);
        modelMap.addAttribute("user", userService.getUserByID(id));
        return "control-panel/personal-details";
    }

    @GetMapping("/user/{id}/properties")
    public String displayProperties(@PathVariable Integer id, ModelMap modelMap) {
        List<Property> propertiesOwned = propertyService.getPropertiesByOwnerUser(userService.getUserByID(id));
        List<Property> propertiesRenting = propertyService.getPropertiesByTenantUser(userService.getUserByID(id));
        modelMap.addAttribute("userId", id);
        modelMap.addAttribute("userPropertiesOwned", propertiesOwned);
        modelMap.addAttribute("userPropertiesRenting", propertiesRenting);
        return "control-panel/properties";
    }

    @GetMapping("/user/{id}/offers")
    public String displayOffers(@PathVariable Integer id, ModelMap modelMap) {
        List<Rental> offersSent = rentalService.getRentalsByTenant(userService.getUserByID(id));
        List<Rental> offersReceived = rentalService.getRentalsByOwner(userService.getUserByID(id));
        modelMap.addAttribute("userId", id);
        modelMap.addAttribute("userOffersSent", offersSent);
        modelMap.addAttribute("userOffersReceived", offersReceived);
        return "control-panel/offers";
    }

}
