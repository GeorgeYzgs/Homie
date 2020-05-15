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

    @GetMapping("/users")
    public ModelAndView displayUsersList() {
        return new ModelAndView("display-users", "userList", userService.getUserList());
    }

    @GetMapping("/user/{id}")
    public ModelAndView displayUser(@PathVariable Integer id) {
        return new ModelAndView("user-page", "user", userService.getUserByID(id));
    }

    @PostMapping("/lock-user")
    public ModelAndView lockUser(@RequestParam Integer id, Authentication auth, RedirectAttributes redirectAttributes) {
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        User user = userService.getUserByID(id);
        if (loggedUser.getId() == user.getId()) {
            redirectAttributes.addFlashAttribute("messageDanger", "Did you just try to ban yourself?");
            return new ModelAndView("redirect:/mod/user/" + id);
        }
        if (!user.getUserRole().equals(UserRole.USER)) {
            redirectAttributes.addFlashAttribute("messageDanger", "You can only ban / un-ban users.");
            return new ModelAndView("redirect:/mod/user/" + id);
        }
        user.setNonLocked(!user.isNonLocked());
        userService.updateUser(user);
        deleteActiveSession(id);
        redirectAttributes.addFlashAttribute("messageSuccess", "User has been banned / unbanned");
        return new ModelAndView("redirect:/mod/user/" + id);
    }

    private void deleteActiveSession(Integer id) {
        sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof MyUserDetails)
                .map(MyUserDetails.class::cast)
                .filter(user -> user.getId() == id).findFirst()
                .ifPresent(myUserDetails -> sessionRegistry.getAllSessions(myUserDetails, false)
                        .forEach(SessionInformation::expireNow));
    }

    @PostMapping("/lock-property")
    public ModelAndView lockProperty(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        Property property = propertyService.getPropertyByID(id);
        property.setNonLocked(!property.isNonLocked());
        propertyService.insertProperty(property);
        redirectAttributes.addFlashAttribute("messageSuccess", "Property has been locked / unlocked");
        return new ModelAndView("redirect:/mod/user/" + id);
    }
}
