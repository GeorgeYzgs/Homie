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
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                     Locale userLocale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        String attempt = userServiceImpl.registerUser(dto, userLocale);
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("register", "messageDanger", attempt);
        }
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Register.success.email.sent", null, userLocale));
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/confirm-account/{token}")
    public ModelAndView confirmUserAccount(@PathVariable String token, Locale userLocale) {
        String attempt = tokenService.validateConfirmationToken(token);
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("login", "messageDanger", attempt);
        }
        return new ModelAndView("login", "messageSuccess", messageSource.getMessage("Account.activated", null, userLocale));
    }

    @GetMapping("/change-pass")
    public ModelAndView changePass() {
        return new ModelAndView("change-pass", "changeUserPass", new RegisterUserDto());
    }

    @PostMapping("/change-pass")
    public ModelAndView changeUserPass(@Validated({ChangePassValidator.class})
                                       @ModelAttribute("changeUserPass") RegisterUserDto dto,
                                       BindingResult bindingResult, Authentication auth, RedirectAttributes redirectAttributes,
                                       Locale userLocale) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("change-pass");
        }
        MyUserDetails loggedUser = (MyUserDetails) auth.getPrincipal();
        String attempt = userServiceImpl.changePass(dto, loggedUser.getId(), userLocale);
        if (!attempt.equals("SUCCESS")) {
            return new ModelAndView("change-pass", "messageDanger", attempt);
        }
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Password.change.success", null, userLocale));
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/forgot-pass")
    public ModelAndView forgotPass() {
        return new ModelAndView("forgot-pass", "forgotUserPass", new RegisterUserDto());
    }

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
            return new ModelAndView("forgot-pass", "messageDanger", attempt);
        }
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Reset.email.sent", null, userLocale));
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/reset-password/{token}")
    public ModelAndView resetUserPassword(@PathVariable String token) {
        String attempt = tokenService.validateResetToken(token);
        if (!attempt.contains("@")) {
            return new ModelAndView("login", "messageDanger", attempt);
        }
        RegisterUserDto dto = new RegisterUserDto();
        dto.setToken(token);
        return new ModelAndView("reset-pass", "resetUserPass", dto);
    }

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
            redirectAttributes.addFlashAttribute("messageDanger", attempt);
            return new ModelAndView("redirect:/login");
        }
        tokenService.setNewPass(dto, attempt);
        redirectAttributes.addFlashAttribute("messageSuccess",
                messageSource.getMessage("Reset.success", null, userLocale));
        return new ModelAndView("redirect:/login");
    }
}
