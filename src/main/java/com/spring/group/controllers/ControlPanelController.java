package com.spring.group.controllers;

import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.MyUserDetails;
import com.spring.group.models.user.User;
import com.spring.group.pojo.UserDetailsPojo;
import com.spring.group.services.bases.PropertyServiceInterface;
import com.spring.group.services.bases.RentalServiceInterface;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller class that handles all regular calls made for the user's control panel pages
 */
@Controller
@RequestMapping("/my-profile")
public class ControlPanelController {

    @Autowired
    private UserServiceInterface userService;
    @Autowired
    private PropertyServiceInterface propertyService;
    @Autowired
    private RentalServiceInterface rentalService;

    /**
     * Controller to display the personal details tab in the the user's control panel
     *
     * @param auth     Authentication object of user
     * @param modelMap the user details added sent to the view as attributes
     * @return the user page with the current user details
     */
    @GetMapping("/personal-details")
    public String displayPersonalDetails(Authentication auth, ModelMap modelMap) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        modelMap.addAttribute("currentUserDetails", new UserDetailsPojo(userService.getUserByID(loggedUser.getId())));
        return "user-page";
    }

    /**
     * Controller to display the properties and rental of a user
     *
     * @param auth     Authentication object of the user
     * @param modelMap the current user, the properties, the rentals sent to the view as attributes
     * @return the user page with the properties page
     */
    @GetMapping("/properties")
    public String displayProperties(Authentication auth, ModelMap modelMap) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        User currentUser = userService.getUserByID(loggedUser.getId());
        List<Property> propertiesOwned = propertyService.getPropertiesByOwnerUser(currentUser);
        List<Rental> rentals = rentalService.getRentalsByTenantStartedAndNotEnded(currentUser);
        modelMap.addAttribute("user", currentUser);
        modelMap.addAttribute("userId", currentUser.getId());
        modelMap.addAttribute("userPropertiesOwned", propertiesOwned);
        modelMap.addAttribute("userRentals", rentals);
        return "user-page";
    }

    /**
     * Controller to display the offers sent and received of a user
     *
     * @param auth     Authentication object of the user
     * @param modelMap the current user, the offers sent, the offers received to the view as attributes
     * @return the user page with the offers page
     */
    @GetMapping("/offers")
    public String displayOffers(Authentication auth, ModelMap modelMap) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        User currentUser = userService.getUserByID(loggedUser.getId());
        List<Rental> offersSent = rentalService.getRentalsByTenant(currentUser);
        List<Rental> offersReceived = rentalService.getRentalsByOwner(currentUser);
        modelMap.addAttribute("user", currentUser);
        modelMap.addAttribute("userId", currentUser.getId());
        modelMap.addAttribute("userOffersSent", offersSent);
        modelMap.addAttribute("userOffersReceived", offersReceived);
        return "user-page";
    }

}
