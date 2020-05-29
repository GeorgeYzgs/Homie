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
/**
 * Controller class that handles all ajax calls made for the moderator's control panel pages
 */
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
     * @return the personal details fragment view with the user's details
     */
    @GetMapping("/user/{id}/personal-details")
    public String displayUser(@PathVariable Integer id, ModelMap modelMap) {
        modelMap.addAttribute("userId", id);
        modelMap.addAttribute("user", userService.getUserByID(id));
        return "control-panel/personal-details";
    }

    /**
     * A controller to access a user's properties and rentals
     *
     * @param id the user's id provided as a path variable
     * @return the properties fragment view with the user's properties and rentals
     */
    @GetMapping("/user/{id}/properties")
    public String displayProperties(@PathVariable Integer id, ModelMap modelMap) {
        List<Property> propertiesOwned = propertyService.getPropertiesByOwnerUser(userService.getUserByID(id));
        List<Property> propertiesRenting = propertyService.getPropertiesByTenantUser(userService.getUserByID(id));
        modelMap.addAttribute("userId", id);
        modelMap.addAttribute("userPropertiesOwned", propertiesOwned);
        modelMap.addAttribute("userPropertiesRenting", propertiesRenting);
        return "control-panel/properties";
    }

    /**
     * A controller to access a user's offers sent and received
     *
     * @param id the user's id provided as a path variable
     * @return the offers fragment view with the user's offers sent and received
     */
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
