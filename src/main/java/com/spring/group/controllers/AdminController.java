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

    @PostMapping("/change-role")
    public ModelAndView changeUserRole(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByID(id);
        if (user.getUserRole().equals(UserRole.ADMIN)) {
            redirectAttributes.addFlashAttribute("messageDanger", "You cannot change the role of Admins.");
            return new ModelAndView("redirect:/mod/user/" + id);
        }
        UserRole role = user.getUserRole().equals(UserRole.USER) ? UserRole.MODERATOR : UserRole.USER;
        user.setUserRole(role);
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("messageSuccess", "User role has been changed");
        return new ModelAndView("redirect:/mod/user/" + id);
    }

    //TODO implement properly page, change redirect!
    @PostMapping("/change-property-value")
    public ModelAndView changePropertyValue(@RequestParam Integer id,
                                            @RequestParam Integer value) {
        Property property = propertyService.getPropertyByID(id);
        property.setSearchValue(value);
        propertyService.insertProperty(property);
        return new ModelAndView("redirect:/mod/users");
    }

    @PostMapping("/change-all-properties-value")
    public ModelAndView changeAllPropertyValues(@RequestParam Integer userID,
                                                @RequestParam Integer value) {
        User user = userService.getUserByID(userID);
        user.getPropertyCollection().forEach(p -> p.setSearchValue(value));
        propertyService.updateProperties(user.getPropertyCollection());
        return new ModelAndView("redirect:/mod/user/" + userID);
    }
}
