package com.spring.group.controllers;

import com.spring.group.dto.RegisterUserDto;
import com.spring.group.models.user.ConfirmationToken;
import com.spring.group.models.user.User;
import com.spring.group.services.ConfirmationTokenService;
import com.spring.group.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerUser") RegisterUserDto dto,
                               BindingResult bindingResult, ModelMap mm) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (userServiceImpl.checkUserName(dto.getUsername()).isPresent()) {
            mm.addAttribute("usernameExists", true);
            return "register";
        }
        if (userServiceImpl.checkEmail(dto.getEmail()).isPresent()) {
            mm.addAttribute("emailExists", true);
            return "register";
        }
        User user = new User(dto);
        userServiceImpl.insertUser(user);
        confirmationTokenService.emailUser(user);
        return "redirect:/login?register";
    }

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
}
