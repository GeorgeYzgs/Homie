package com.spring.group.controllers;

import com.spring.group.dto.PropertyDTO;
import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.MyUserDetails;
import com.spring.group.models.user.User;
import com.spring.group.services.PhotoServiceImpl;
import com.spring.group.services.bases.PropertyServiceInterface;
import com.spring.group.services.bases.RentalServiceInterface;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class MainController {

    /**
     * A thread safe map to store new views for every property
     */
    private static final Map<Integer, Integer> pageViewCount = new ConcurrentHashMap<>();

    @Autowired
    private PropertyServiceInterface propertyService;

    @Autowired
    private PhotoServiceImpl photoServiceImpl;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private RentalServiceInterface rentalService;

    @Autowired
    private MessageSource messageSource;

    /**
     * A controller that allows users to list a new property
     *
     * @return the insert property page
     */
    @GetMapping("/list-new-property")
    public ModelAndView list() {
        return new ModelAndView("insert-property", "propertyDTO", new PropertyDTO());
    }

    /**
     * A controller that handles form submission for listing new properties
     *
     * @param propertyDTO        the object to be validated
     * @param bindingResult      validates the object for errors
     * @param auth               the logged user
     * @param redirectAttributes informs the user of the result of his attempt
     * @return redirects to the home page if successful, otherwise returns the insert property
     * @throws IOException if the parsing of the provided photos of the property fails.
     */
    @PostMapping("/list-new-property")
    public ModelAndView listProperty(@Valid @ModelAttribute("propertyDTO") PropertyDTO propertyDTO,
                                     BindingResult bindingResult, Authentication auth,
                                     RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("insert-property");
        }
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        User loggedUser = userService.getUserByID(user.getId());
        Property property = propertyDTO.unWrapProperty(loggedUser);
        propertyService.insertProperty(property);
        photoServiceImpl.uploadPhotos(propertyDTO.getPhotoCollection(), property);
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Property.listed.success", null, Locale.UK));
        return new ModelAndView("redirect:/");
    }

    /**
     * A controller to handle user offers for a property
     *
     * @param propertyID         the id of the target property
     * @param price              the price the user offers
     * @param auth               the logged user
     * @param redirectAttributes informs the user of the result of his attempt
     * @return redirects to the target property page
     */
    @PostMapping("/submit-offer")
    public ModelAndView submitOffer(@RequestParam Integer propertyID, @RequestParam Integer price,
                                    Authentication auth, RedirectAttributes redirectAttributes) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        Property property = propertyService.getPropertyByID(propertyID);
        String attempt = propertyService.submitOffer(property, loggedUser.getId());
        if (!attempt.equals("SUCCESS")) {
            redirectAttributes.addFlashAttribute("messageDanger", attempt);
            return new ModelAndView("redirect:/view/" + propertyID);
        }
        User tenant = userService.getUserByID(loggedUser.getId());
        rentalService.insertRental(new Rental(price, property, tenant));
        redirectAttributes.addFlashAttribute("messageSuccess", "Offer submitted successfully!");
        return new ModelAndView("redirect:/view/" + propertyID);
    }

    /**
     * A controller that manages offers a user has on a property
     *
     * @param rentalID           the target property id
     * @param isAccepted         the owners decision of accepting or declining the given offer
     * @param auth               the logged user / owner
     * @param redirectAttributes informs the owner of the result of his decision
     * @return redirects to the owner's profile page
     */
    @PostMapping("/manage-offers")
    public ModelAndView manageOffers(@RequestParam Integer rentalID, @RequestParam boolean isAccepted,
                                     Authentication auth, RedirectAttributes redirectAttributes) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        Rental rental = rentalService.getRentalByID(rentalID);
        String attempt = rentalService.manageOffers(rental, isAccepted, loggedUser.getId());
        if (!attempt.contains("Offer")) {
            redirectAttributes.addFlashAttribute("messageDanger", attempt);
        }
        redirectAttributes.addFlashAttribute("messageSuccess", attempt);
        return new ModelAndView("redirect:/my-profile");
    }

    /**
     * A controller for users to access their profile page
     *
     * @param auth the logged user
     * @return returns the logged user's profile page
     */
    @GetMapping("/my-profile")
    public ModelAndView displayProfile(Authentication auth) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        return new ModelAndView("user-page", "user", userService.getUserByID(loggedUser.getId()));
    }

    /**
     * A controller that returns the page of a property
     *
     * @param id the target property id
     * @return the target property page
     */
    @GetMapping("/view/{id}")
    public ModelAndView viewProperty(@PathVariable Integer id) {
        pageViewCount.merge(id, 1, Integer::sum);
        return new ModelAndView("view-property", "property", propertyService.getPropertyByID(id));
    }

    /**
     * A method that updates property view count with a cron expression,
     * to not stress the database, currently updating every 5 minutes
     * (seconds minutes hours days months years)
     */
    @Scheduled(cron = "0 */5 * * * ?")
    private void updateViewCount() {
        System.out.println("Updating Property View Counts!");
        List<Property> updatedProperties = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : pageViewCount.entrySet()) {
            Property property = propertyService.getFullProperty(entry.getKey());
            property.setViews(property.getViews() + entry.getValue());
            updatedProperties.add(property);
        }
        pageViewCount.clear();
        propertyService.updateProperties(updatedProperties);
    }
}