package com.spring.group.controllers;

import com.spring.group.dto.user.RegisterUserDto;
import com.spring.group.dto.user.validationgroups.ChangePassValidator;
import com.spring.group.dto.user.validationgroups.RegistrationEmailValidator;
import com.spring.group.dto.user.validationgroups.RegistrationValidator;
import com.spring.group.dto.user.validationgroups.ResetPassValidator;
import com.spring.group.models.user.MyUserDetails;
import com.spring.group.services.TokenService;
import com.spring.group.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import java.util.Locale;

/**
 * @author George.Giazitzis
 */
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MessageSource messageSource;

    /**
     * The login controller, will return the login page or redirect to home if the user is already logged in.
     *
     * @param auth the logged user
     * @return
     */
    @GetMapping("/login")
    public String login(Authentication auth) {
        return auth == null ? "login" : "redirect:/";
    }

    /**
     * @return the main page.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * A controller that returns the registration page, or the main page if the user is already logged in
     *
     * @param auth the logged user
     * @return the registration page, or redirects to the main page if the user is logged in.
     */
    @GetMapping("/register")
    public ModelAndView register(Authentication auth) {
        if (auth != null) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("register", "registerUser", new RegisterUserDto());
    }

    /**
     * A controller that handles form submission for registration,
     * if successful will email the user with an activation link.
     *
     * @param dto                the registration object to be validated
     * @param bindingResult      validates the registration object with the given validation group
     * @param redirectAttributes informs the user that registration was successful upon completion
     * @return redirects to the login page if successful, otherwise returns to the registration page.
     */
    @PostMapping("/register")
    public ModelAndView registerUser(@Validated({RegistrationValidator.class})
                                     @ModelAttribute("registerUser") RegisterUserDto dto,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                     Locale userLocale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        String attempt = userServiceImpl.registerUser(dto);
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("register", "messageDanger",
                    messageSource.getMessage(attempt, null, userLocale));
        }
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Register.success.email.sent", null, userLocale));
        return new ModelAndView("redirect:/login");
    }

    /**
     * A controller for handling the links provided in the registration email url.
     *
     * @param token takes the token provided as a path variable
     * @return the login page, informs the user if the confirmation was successful.
     */
    @GetMapping("/confirm-account/{token}")
    public ModelAndView confirmUserAccount(@PathVariable String token, Locale userLocale) {
        String attempt = tokenService.validateConfirmationToken(token);
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("login", "messageDanger",
                    messageSource.getMessage(attempt, null, userLocale));
        }
        return new ModelAndView("login", "messageSuccess", messageSource.getMessage("Account.activated", null, userLocale));
    }

    /**
     * A controller that returns the change pass page, for logged users to change their accounts
     *
     * @return change-pass page
     */
    @GetMapping("/change-pass")
    public ModelAndView changePass() {
        return new ModelAndView("change-pass", "changeUserPass", new RegisterUserDto());
    }

    /**
     * A controller that handles form submission for changing logged users' passwords.
     *
     * @param dto                the change pass object to be validated
     * @param bindingResult      validates the change pass object with the given validation group
     * @param auth               the logged user authentication object
     * @param redirectAttributes informs the user if the change pass process was successful upon completion
     * @return redirects to the main page if successful, otherwise returns to the change pass page.
     */
    @PostMapping("/change-pass")
    public ModelAndView changeUserPass(@Validated({ChangePassValidator.class})
                                       @ModelAttribute("changeUserPass") RegisterUserDto dto,
                                       BindingResult bindingResult, Authentication auth, RedirectAttributes redirectAttributes,
                                       Locale userLocale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("change-pass");
        }
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        String attempt = userServiceImpl.changePass(dto, loggedUser.getId());
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("change-pass", "messageDanger",
                    messageSource.getMessage(attempt, null, userLocale));
        }
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Password.change.success", null, userLocale));
        return new ModelAndView("redirect:/");
    }

    /**
     * A controller for users to reset their forgotten passwords,
     * redirects to home page if the user is already logged in.
     *
     * @return the forgot pass page
     */
    @GetMapping("/forgot-pass")
    public ModelAndView forgotPass(Authentication auth) {
        if (auth != null) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("forgot-pass", "forgotUserPass", new RegisterUserDto());
    }

    /**
     * A controller to validate form submission for forgotten user passwords
     *
     * @param dto                the forgot password object to be validated
     * @param bindingResult      validates the forgot pass object with the given validation group.
     * @param redirectAttributes informs the user if the forgot pass process was successful upon completion
     * @return redirects to the login page if successful, otherwise returns to the forgot pass page.
     */
    @PostMapping("/forgot-pass")
    public ModelAndView resetPass(@Validated({RegistrationEmailValidator.class})
                                  @ModelAttribute("forgotUserPass") RegisterUserDto dto,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                  Locale userLocale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("forgot-pass");
        }
        String attempt = tokenService.forgotPass(dto.getEmail());
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("forgot-pass", "messageDanger",
                    messageSource.getMessage(attempt, null, userLocale));
        }
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Reset.email.sent", null, userLocale));
        return new ModelAndView("redirect:/login");
    }

    /**
     * A controller for handling the links provided in the reset pass email url
     *
     * @param token takes the provided token as a path variable
     * @return the reset pass page if successful, otherwise redirects to the login page
     */
    @GetMapping("/reset-password/{token}")
    public ModelAndView resetUserPassword(@PathVariable String token, RedirectAttributes redirectAttributes, Locale userLocale) {
        String attempt = tokenService.validateResetToken(token);
        if (!attempt.contains("@")) {
            redirectAttributes.addFlashAttribute("messageDanger", messageSource.getMessage(attempt, null, userLocale));
            return new ModelAndView("redirect:/login");
        }
        RegisterUserDto dto = new RegisterUserDto();
        dto.setToken(token);
        return new ModelAndView("reset-pass", "resetUserPass", dto);
    }

    /**
     * A controller handles form submission for setting a new pass.
     *
     * @param dto                the object to be validated
     * @param bindingResult      validates the object with the given validation group
     * @param redirectAttributes informs the user if the process was successful
     * @return redirects to the login page if the token is not valid or if the process was successful,
     * returns back to the reset pass page if there are errors
     */
    @PostMapping("/set-new-pass")
    public ModelAndView setNewPass(@Validated({ResetPassValidator.class})
                                   @ModelAttribute("resetUserPass") RegisterUserDto dto,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                   Locale userLocale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("reset-pass");
        }
        String attempt = tokenService.validateResetToken(dto.getToken());
        if (!attempt.contains("@")) {
            redirectAttributes.addFlashAttribute("messageDanger", messageSource.getMessage(attempt, null, userLocale));
            return new ModelAndView("redirect:/login");
        }
        tokenService.setNewPass(dto, attempt);
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Reset.success", null, userLocale));
        return new ModelAndView("redirect:/login");
    }
}
