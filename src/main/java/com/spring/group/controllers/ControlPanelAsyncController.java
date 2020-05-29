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

/**
 * Controller class that handles all ajax calls made for the user's control panel pages
 */
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

    /**
     * Controller to display the personal details tab in the the user's control panel
     *
     * @param auth     Authentication object of user
     * @param modelMap the user details added sent to the view as attributes
     * @return the fragment view that corresponds to the user details page
     */
    @GetMapping("/personal-details")
    public String displayPersonalDetails(Authentication auth, ModelMap modelMap) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        modelMap.addAttribute("currentUserDetails", new UserDetailsPojo(userService.getUserByID(loggedUser.getId())));
        return "control-panel/personal-details";
    }

    /**
     * Controller to display the properties and rental of a user
     *
     * @param auth     Authentication object of the user
     * @param modelMap the current user, the properties, the rentals sent to the view as attributes
     * @return the fragment view that corresponds to the properties page
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
        return "control-panel/properties";
    }

    /**
     * Controller to display the offers sent and received of a user
     *
     * @param auth     Authentication object of the user
     * @param modelMap the current user, the offers sent, the offers received to the view as attributes
     * @return the fragment view that corresponds to the offers page
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
        return "control-panel/offers";
    }

    /**
     * Controller that handles user responses in offers received
     *
     * @param rentalID           the id of the offer received (rental)
     * @param isAccepted         boolean that representes if the user accepted or declined the offer (rental)
     * @param auth               Authentication object of the user
     * @param redirectAttributes the message in the current Locale send as flash attribute to the redirect action
     * @param userLocale         the current user locale
     * @return the redirection to the offers page with the message of the result of the managed offer
     */
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
