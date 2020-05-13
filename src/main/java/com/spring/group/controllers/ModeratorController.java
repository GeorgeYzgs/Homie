package com.spring.group.controllers;

import com.spring.group.models.user.MyUserDetails;
import com.spring.group.models.user.User;
import com.spring.group.models.user.UserRole;
import com.spring.group.services.bases.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author George.Giazitzis
 */
@Controller
@RequestMapping("/mod")
public class ModeratorController {

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/users")
    public ModelAndView displayUsersList() {
        return new ModelAndView("displayUsers", "userList", userService.getUserList());
    }

    @GetMapping("/user/{id}")
    public ModelAndView displayUser(@PathVariable Integer id) {
        return new ModelAndView("user-page", "user", userService.getUserByID(id));
    }

    @PostMapping("/lockUser")
    public ModelAndView lockUser(@RequestParam Integer id) {
        User user = userService.getUserByID(id);
        if (user.getUserRole().equals(UserRole.USER)) {
            user.setNonLocked(!user.isNonLocked());
            userService.updateUser(user);
            deleteActiveSession(id);
        }
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
}
