package com.spring.group.controllers;

import com.spring.group.models.property.Property;
import com.spring.group.models.user.MyUserDetails;
import com.spring.group.models.user.User;
import com.spring.group.models.user.UserRole;
import com.spring.group.services.bases.PropertyServiceInterface;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author George.Giazitzis
 */
@Controller
@RequestMapping("/mod")
public class ModeratorController {

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private PropertyServiceInterface propertyService;

    @Autowired
    private SessionRegistry sessionRegistry;

    /**
     * A controller to access registered users data
     *
     * @return the display users page
     */
    @GetMapping("/users")
    public ModelAndView displayUsersList() {
        return new ModelAndView("display-users", "userList", userService.getUserList());
    }

    /**
     * A controller to access a user's profile
     *
     * @param id the user's id provided as a path variable
     * @return the user page of that user
     */
    @GetMapping("/user/{id}")
    public ModelAndView displayUser(@PathVariable Integer id) {
        return new ModelAndView("user-page", "user", userService.getUserByID(id));
    }

    /**
     * A controller for banning / unbanning users
     *
     * @param userID             the target user's id provided as a path variable
     * @param auth               the logged user
     * @param redirectAttributes informs the user of the result of his attempt
     * @return the target user's profile page.
     */
    @PostMapping("/lock-user")
    public ModelAndView lockUser(@RequestParam Integer userID, Authentication auth, RedirectAttributes redirectAttributes) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        User user = userService.getUserByID(userID);
        if (loggedUser.getId() == user.getId()) {
            redirectAttributes.addFlashAttribute("messageDanger", "Did you just try to ban yourself?");
            return new ModelAndView("redirect:/mod/user/" + userID);
        }
        if (!user.getUserRole().equals(UserRole.USER)) {
            redirectAttributes.addFlashAttribute("messageDanger", "You can only ban / un-ban users.");
            return new ModelAndView("redirect:/mod/user/" + userID);
        }
        user.setNonLocked(!user.isNonLocked());
        userService.updateUser(user);
        deleteActiveSession(userID);
        redirectAttributes.addFlashAttribute("messageSuccess", "User has been banned / unbanned");
        return new ModelAndView("redirect:/mod/user/" + userID);
    }

    /**
     * A method that is called when banning a user, to epxire any active sessions they may have
     *
     * @param id the target user's id
     */
    private void deleteActiveSession(Integer id) {
        sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof MyUserDetails)
                .map(MyUserDetails.class::cast)
                .filter(user -> user.getId() == id).findFirst()
                .ifPresent(myUserDetails -> sessionRegistry.getAllSessions(myUserDetails, false)
                        .forEach(SessionInformation::expireNow));
    }

    /**
     * A controller for locking / unlocking properties
     *
     * @param propertyID         the property id
     * @param redirectAttributes informs the user of the result of his attempt
     * @return redirects to the property page
     */
    @PostMapping("/lock-property")
    public ModelAndView lockProperty(@RequestParam Integer propertyID, RedirectAttributes redirectAttributes) {
        Property property = propertyService.getPropertyByID(propertyID);
        property.setNonLocked(!property.isNonLocked());
        propertyService.insertProperty(property);
        String message = property.isNonLocked() ? "Property has been unlocked" : "Property has been locked";
        redirectAttributes.addFlashAttribute("messageSuccess", message);
        return new ModelAndView("redirect:/view/" + propertyID);
    }
}
