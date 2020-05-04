package com.spring.group.controllers;

import com.spring.group.dto.ChangePassValidator;
import com.spring.group.dto.RegisterUserDto;
import com.spring.group.dto.RegistrationValidator;
import com.spring.group.models.user.ConfirmationToken;
import com.spring.group.models.user.User;
import com.spring.group.services.ConfirmationTokenService;
import com.spring.group.services.MyUserDetails;
import com.spring.group.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @GetMapping("/")
    public String getWelcomePage() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register(ModelMap mm) {
        mm.addAttribute("registerUser", new RegisterUserDto());
        return "register";
    }

    //TODO rewrite / move to service.
    @PostMapping("/register")
    public String registerUser(@Validated({RegistrationValidator.class}) @ModelAttribute("registerUser") RegisterUserDto dto,
                               BindingResult bindingResult, ModelMap mm) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (userServiceImpl.checkUserName(dto.getUsername()).isPresent()) {
            mm.addAttribute("message", "This username is unavailable");
            return "register";
        }
        if (userServiceImpl.checkEmail(dto.getEmail()).isPresent()) {
            mm.addAttribute("message", "This email is unavailable");
            return "register";
        }
        User user = new User(dto);
        userServiceImpl.insertUser(user);
        confirmationTokenService.emailUser(user);
        return "redirect:/login?register";
    }

    //TODO rewrite / move to service?
    @GetMapping("/confirm-account")
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) {
        Optional<ConfirmationToken> token = confirmationTokenService.checkToken(confirmationToken);

        if (token.isPresent()) {
            User user = userServiceImpl.checkEmail(token.get().getUser().getEmail()).get();
            user.setEnabled(true);
            userServiceImpl.insertUser(user);
        }
        return "redirect:/login";
    }

    @GetMapping("/changePass")
    public String changePass(ModelMap mm) {
        mm.addAttribute("changeUserPass", new RegisterUserDto());
        return "changePass";
    }

    @PostMapping("/changePass")
    public String changeUserPass(@Validated({ChangePassValidator.class}) @ModelAttribute("changeUserPass") RegisterUserDto dto,
                                 BindingResult bindingResult, ModelMap mm, Authentication loggedUser) {
        if (bindingResult.hasErrors()) {
            return "changePass";
        }
        if (!dto.getOldPassword().equals(loggedUser.getCredentials())) {
            mm.addAttribute("message", "Given password does not match your old password");
            return "changePass";
        }
        if (dto.getPassword().equals(loggedUser.getCredentials())) {
            mm.addAttribute("message", "Your new password cannot be your old password");
            return "changePass";
        }
        User user = userServiceImpl.checkUserName(loggedUser.getName()).get();
        user.setPassword(dto.getPassword());
        userServiceImpl.insertUser(user);
        mm.addAttribute("message", "Password changed successfully!");
        return "index";
    }

    @GetMapping("/forgotPass")
    public String forgotPass(ModelMap mm) {
        mm.addAttribute("resetUserPass", new RegisterUserDto());
        return "forgotPass";
    }

    //TODO Finish Reset Pass.
    @PostMapping("/forgotPass")
    public String resetPass() {

        return null;
    }
}
