package com.spring.group.controllers;

import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;
import com.spring.group.models.user.UserRole;
import com.spring.group.services.bases.PropertyServiceInterface;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author George.Giazitzis
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private PropertyServiceInterface propertyService;

    /**
     * A controller to promote and demote users to and from moderators.
     *
     * @param userID             the target user's id
     * @param redirectAttributes informs the user of the result of his attempt.
     * @return the target user's profile page
     */
    @PostMapping("/change-role")
    public ModelAndView changeUserRole(@RequestParam Integer userID, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByID(userID);
        if (user.getUserRole().equals(UserRole.ADMIN)) {
            redirectAttributes.addFlashAttribute("messageDanger", "You cannot change the role of Admins.");
            return new ModelAndView("redirect:/mod/user/" + userID);
        }
        UserRole role = user.getUserRole().equals(UserRole.USER) ? UserRole.MODERATOR : UserRole.USER;
        user.setUserRole(role);
        userService.updateUser(user);
        String message = user.getUserRole().equals(UserRole.USER) ? "User has been demoted" : "User has been promoted";
        redirectAttributes.addFlashAttribute("messageSuccess", message);
        return new ModelAndView("redirect:/mod/user/" + userID);
    }

    /**
     * A controller to change a property search value
     *
     * @param propertyID    the id of the target property
     * @param value the value to be changed to
     * @return the page of the target property
     */
    @PostMapping("/change-property-value")
    public ModelAndView changePropertyValue(@RequestParam Integer propertyID,
                                            @RequestParam Integer value, RedirectAttributes redirectAttributes) {
        Property property = propertyService.getPropertyByID(propertyID);
        property.setSearchValue(value);
        propertyService.insertProperty(property);
        redirectAttributes.addFlashAttribute("messageSuccess", "The Search Value of this property has been set!");
        return new ModelAndView("redirect:/view/" + propertyID);
    }

    /**
     * A controller to change the search value of all the properties of a user
     *
     * @param userID the target user's id
     * @param value  the value to be changed to
     * @return the profile page of the target user
     */
    @PostMapping("/change-all-properties-value")
    public ModelAndView changeAllPropertyValues(@RequestParam Integer userID,
                                                @RequestParam Integer value, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByID(userID);
        user.getPropertyCollection().forEach(p -> p.setSearchValue(value));
        propertyService.updateProperties(user.getPropertyCollection());
        redirectAttributes.addFlashAttribute("messageSuccess",
                "The Search Value of all this user's properties has been set!");
        return new ModelAndView("redirect:/mod/user/" + userID);
    }
}
