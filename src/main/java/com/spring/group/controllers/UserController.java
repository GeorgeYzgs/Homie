package com.spring.group.controllers;

import com.spring.group.dto.user.RegisterUserDto;
import com.spring.group.dto.user.validationgroups.ChangePassValidator;
import com.spring.group.dto.user.validationgroups.RegistrationEmailValidator;
import com.spring.group.dto.user.validationgroups.RegistrationValidator;
import com.spring.group.dto.user.validationgroups.ResetPassValidator;
import com.spring.group.models.user.MyUserDetails;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    //TODO Merge index with search html
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    public String getSearchPage() {
        return "full-search";
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register", "registerUser", new RegisterUserDto());
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Validated({RegistrationValidator.class})
                                     @ModelAttribute("registerUser") RegisterUserDto dto,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        String attempt = userServiceImpl.registerUser(dto);
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("register", "messageDanger", attempt);
        }
        redirectAttributes.addFlashAttribute("messageSuccess",
                "Registered successfully, a confirmation email has been sent to your email address!");
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/confirm-account/{token}")
    public ModelAndView confirmUserAccount(@PathVariable String token) {
        String attempt = tokenService.validateConfirmationToken(token);
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("login", "messageDanger", attempt);
        }
        return new ModelAndView("login", "messageSuccess", "Your account has been activated!");
    }

    @GetMapping("/changePass")
    public ModelAndView changePass() {
        return new ModelAndView("changePass", "changeUserPass", new RegisterUserDto());
    }

    @PostMapping("/changePass")
    public ModelAndView changeUserPass(@Validated({ChangePassValidator.class})
                                       @ModelAttribute("changeUserPass") RegisterUserDto dto,
                                       BindingResult bindingResult, Authentication auth, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("changePass");
        }
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        String attempt = userServiceImpl.changePass(dto, loggedUser.getId());
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("changePass", "messageDanger", attempt);
        }
        redirectAttributes.addFlashAttribute("messageSuccess", "Password changed successfully!");
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/forgotPass")
    public ModelAndView forgotPass() {
        return new ModelAndView("forgotPass", "forgotUserPass", new RegisterUserDto());
    }

    @PostMapping("/forgotPass")
    public ModelAndView resetPass(@Validated({RegistrationEmailValidator.class})
                                  @ModelAttribute("forgotUserPass") RegisterUserDto dto,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("forgotPass");
        }
        Optional<User> user = userServiceImpl.checkEmail(dto.getEmail());
        if (!user.isPresent()) {
            return new ModelAndView("forgotPass",
                    "messageDanger", "There is no account linked to that email");
        }
        tokenService.createResetEmail(user.get());
        redirectAttributes.addFlashAttribute("messageSuccess",
                "An email to reset your password has been sent to your account!");
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/reset-password/{token}")
    public ModelAndView resetUserPassword(@PathVariable String token) {
        String attempt = tokenService.validateResetToken(token);
        if (!attempt.contains("@")) {
            return new ModelAndView("login", "messageDanger", attempt);
        }
        RegisterUserDto dto = new RegisterUserDto();
        dto.setEmail(attempt);
        return new ModelAndView("resetPass", "resetUserPass", dto);
    }

    @PostMapping("/setNewPass")
    public ModelAndView setNewPass(@Validated({ResetPassValidator.class})
                                   @ModelAttribute("resetUserPass") RegisterUserDto dto,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("resetPass");
        }
        User user = userServiceImpl.checkEmail(dto.getEmail()).get();
        user.setPassword(dto.getPassword());
        userServiceImpl.insertUser(user);
        redirectAttributes.addFlashAttribute("messageSuccess", "Your new password has been set!");
        return new ModelAndView("redirect:/login");
    }
}
