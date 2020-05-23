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
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/async/my-profile")
public class ControlPanelAsyncController {

    @Autowired
    private UserServiceInterface userService;
    @Autowired
    private PropertyServiceInterface propertyService;
    @Autowired
    private RentalServiceInterface rentalService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    MessageSource messageSource;

    @GetMapping("/personal-details")
    public String displayPersonalDetails(Authentication auth, ModelMap modelMap) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        modelMap.addAttribute("currentUserDetails", new UserDetailsPojo(userService.getUserByID(loggedUser.getId())));
        return "control-panel/personal-details";
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
        return "control-panel/properties";
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
        return "control-panel/offers";
    }

    @PostMapping("/manage-offers")
    public ModelAndView manageOffers(@RequestParam("id") Integer rentalID, @RequestParam("isAccepted") boolean isAccepted,
                                     Authentication auth, RedirectAttributes redirectAttributes, Locale userLocale) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        Rental rental = rentalService.getRentalByID(rentalID);
        String attempt = rentalService.manageOffers(rental, isAccepted, loggedUser.getId());
        redirectAttributes.addFlashAttribute(
                (attempt.contains("accepted")) ? "messageSuccess" : "messageDanger",
                messageSource.getMessage(attempt, null, userLocale));
        return new ModelAndView("redirect:/my-profile/offers");
    }

}
