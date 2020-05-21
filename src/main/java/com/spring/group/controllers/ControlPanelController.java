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

@Controller
@RequestMapping("/my-profile")
public class ControlPanelController {

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private PropertyServiceInterface propertyService;

    @Autowired
    private RentalServiceInterface rentalService;

    @GetMapping("/personal-details")
    public String displayPersonalDetails(Authentication auth, ModelMap modelMap) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        modelMap.addAttribute("currentUserDetails", new UserDetailsPojo(userService.getUserByID(loggedUser.getId())));
        return "user-page";
    }

    @GetMapping("/properties")
    public String displayProperties(Authentication auth, ModelMap modelMap) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        User currentUser = userService.getUserByID(loggedUser.getId());
        List<Property> propertiesOwned = propertyService.getPropertiesByOwnerUser(currentUser);
        List<Property> propertiesRenting = propertyService.getPropertiesByTenantUser(currentUser);
        modelMap.addAttribute("user", currentUser);
        modelMap.addAttribute("userId", currentUser.getId());
        modelMap.addAttribute("userPropertiesOwned", propertiesOwned);
        modelMap.addAttribute("userPropertiesRenting", propertiesRenting);
        return "user-page";
    }

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
