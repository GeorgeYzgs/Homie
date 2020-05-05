package com.spring.group.controllers;

import com.spring.group.dto.*;
import com.spring.group.models.user.ConfirmationToken;
import com.spring.group.models.user.ResetPassToken;
import com.spring.group.models.user.User;
import com.spring.group.services.TokenService;
import com.spring.group.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.Optional;

/**
 * @author George.Giazitzis
 */
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register", "registerUser", new RegisterUserDto());
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Validated({RegistrationValidator.class})
                                     @ModelAttribute("registerUser") RegisterUserDto dto,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        if (userServiceImpl.checkUserName(dto.getUsername()).isPresent()) {
            return new ModelAndView("register",
                    "messageDanger", "This username is unavailable");
        }
        if (userServiceImpl.checkEmail(dto.getEmail()).isPresent()) {
            return new ModelAndView("register",
                    "messageDanger", "This email is unavailable");
        }
        User user = new User(dto);
        userServiceImpl.insertUser(user);
        tokenService.createConfirmEmail(user);
        return new ModelAndView("login", "messageSuccess",
                "Registered successfully, a confirmation email has been sent to your email address!");
    }

    @GetMapping("/confirm-account/{token}")
    public ModelAndView confirmUserAccount(@PathVariable("token") String confirmationToken) {
        Optional<ConfirmationToken> token = tokenService.checkConfirmationToken(confirmationToken);
        if (!token.isPresent()) {
            return new ModelAndView("login",
                    "messageDanger", "This is an invalid token");
        }
        ConfirmationToken validToken = token.get();
        User user = userServiceImpl.checkEmail(validToken.getUser().getEmail()).get();
        if (validToken.getExpirationDate().isBefore(Instant.now())) {
            tokenService.createConfirmEmail(user);
            return new ModelAndView("login",
                    "messageDanger", "This token has expired, a new token has been emailed to you");
        }
        user.setEnabled(true);
        userServiceImpl.updateUser(user);
        return new ModelAndView("login",
                "messageSuccess", "Your account has been activated!");
    }

    @GetMapping("/changePass")
    public ModelAndView changePass() {
        return new ModelAndView("changePass", "changeUserPass", new RegisterUserDto());
    }

    @PostMapping("/changePass")
    public ModelAndView changeUserPass(@Validated({ChangePassValidator.class})
                                       @ModelAttribute("changeUserPass") RegisterUserDto dto,
                                       BindingResult bindingResult, Authentication loggedUser) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("changePass");
        }
        String attempt = userServiceImpl.changePass(dto, loggedUser.getName());
        if (!attempt.equals("Password changed successfully!")) {
            return new ModelAndView("changePass", "messageDanger", attempt);
        }
        return new ModelAndView("index", "messageSuccess", attempt);
    }

    @GetMapping("/forgotPass")
    public ModelAndView forgotPass() {
        return new ModelAndView("forgotPass", "forgotUserPass", new RegisterUserDto());
    }

    @PostMapping("/forgotPass")
    public ModelAndView resetPass(@Validated({ResetPassEmailValidator.class})
                                  @ModelAttribute("forgotUserPass") RegisterUserDto dto,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("forgotPass");
        }
        User user = userServiceImpl.checkEmail(dto.getEmail()).get();
        tokenService.createResetEmail(user);
        return new ModelAndView("login", "messageSuccess",
                "An email to reset your password has been sent to your account!");
    }

    @GetMapping("/reset-password/{token}")
    public ModelAndView resetUserPassword(@PathVariable("token") String confirmationToken) {
        Optional<ResetPassToken> token = tokenService.checkResetPassToken(confirmationToken);
        if (!token.isPresent()) {
            return new ModelAndView("login",
                    "messageDanger", "This is an invalid token");
        }
        ResetPassToken validToken = token.get();
        if (validToken.getExpirationDate().isBefore(Instant.now())) {
            return new ModelAndView("login",
                    "messageDanger", "This token has expired, please request another token");
        }
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail(validToken.getUser().getEmail());
        return new ModelAndView("resetPass", "resetUserPass", dto);
    }

    @PostMapping("/setNewPass")
    public ModelAndView setNewPass(@Validated({ResetPassValidator.class})
                                   @ModelAttribute("resetUserPass") RegisterUserDto dto,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("resetPass");
        }
        User user = userServiceImpl.checkEmail(dto.getEmail()).get();
        user.setPassword(dto.getPassword());
        userServiceImpl.insertUser(user);
        return new ModelAndView("login", "messageSuccess",
                "Your new password has been set!");
    }
}
