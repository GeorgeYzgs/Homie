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

    @GetMapping("/list-new-property")
    public ModelAndView list() {
        return new ModelAndView("insert-property", "propertyDTO", new PropertyDTO());
    }

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

    @PostMapping("/submit-offer")
    public ModelAndView submitOffer(@RequestParam Integer id, @RequestParam Integer price,
                                    Authentication auth, RedirectAttributes redirectAttributes) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        Property property = propertyService.getPropertyByID(id);
        if (loggedUser.getId() == property.getOwner().getId()) {
            redirectAttributes.addFlashAttribute("messageDanger", "You cannot submit an offer on your own property!");
            return new ModelAndView("redirect:/view/" + id);
        }
        User tenant = userService.getUserByID(loggedUser.getId());
        rentalService.insertRental(new Rental(price, property, tenant));
        redirectAttributes.addFlashAttribute("messageSuccess", "Offer submitted!");
        return new ModelAndView("redirect:/view/" + id);
    }

    //TODO redirect back to his profile................................
    @PostMapping("/manage-offers")
    public ModelAndView manageOffers(@RequestParam Integer id, @RequestParam boolean isAccepted,
                                     Authentication auth, RedirectAttributes redirectAttributes) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        Rental rental = rentalService.getRentalByID(id);
        if (loggedUser.getId() != rental.getResidence().getOwner().getId()) {
            redirectAttributes.addFlashAttribute("messageDanger", "You can only manage offers to your properties!");
            return new ModelAndView("redirect:/view/" + id);
        }
        if (rentalService.handleOffer(rental, isAccepted)) {
            redirectAttributes.addFlashAttribute("messageSuccess", "Offer accepted!");
            return new ModelAndView("redirect:/view/" + id);
        }
        redirectAttributes.addFlashAttribute("messageSuccess", "Offer declined!");
        return new ModelAndView("redirect:/view/" + id);
    }

    @GetMapping("/my-profile")
    public ModelAndView displayProfile(Authentication auth) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        return new ModelAndView("user-page", "user", userService.getUserByID(loggedUser.getId()));
    }

    //TODO Create a html page for property views.
    @GetMapping("/view/{id}")
    public ModelAndView viewProperty(@PathVariable Integer id) {
        pageViewCount.merge(id, 1, Integer::sum);
        return new ModelAndView("view-property", "property", propertyService.getPropertyByID(id));
    }

    @Scheduled(cron = "0 */5 * * * ?")
    // seconds minutes hours days months years <-currently updating every 5 minutes
    private void updateViewCount() {
        System.out.println("Updating Property View Counts!");
        List<Property> updatedProperties = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : pageViewCount.entrySet()) {
            Property property = propertyService.getPropertyByID(entry.getKey());
            property.setViews(property.getViews() + entry.getValue());
            updatedProperties.add(property);
        }
        propertyService.updateProperties(updatedProperties);
        pageViewCount.clear();
    }
}