package com.spring.group.controllers;

import com.spring.group.dto.user.RegisterUserDto;
import com.spring.group.dto.user.validationgroups.RegistrationEmailValidator;
import com.spring.group.dto.user.validationgroups.RegistrationPassMatchValidator;
import com.spring.group.dto.user.validationgroups.RegistrationPasswordValidator;
import com.spring.group.dto.user.validationgroups.RegistrationUsernameValidator;
import com.spring.group.pojo.RegisterJsonResponse;
import com.spring.group.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller class user for registration validation ajax calls
 */
@RestController
@RequestMapping("/validate")
public class RegisterValidatorController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MessageSource messageSource;


    /**
     * Method used to merge two lists into one. It is used in case the Collectors.toMap function encounters multiple
     * occurrences of the same key. It adds all values to the key that they correspond.
     *
     * @param existingResults List<String> with existing results to be merged
     * @param newResults      List<String> of new results to be merged
     * @return the merged results
     */
    //
    private static List<String> mergeEntriesWithDuplicatedKeys(List<String> existingResults, List<String> newResults) {
        List<String> mergedResults = new ArrayList<>();
        mergedResults.addAll(existingResults);
        mergedResults.addAll(newResults);
        return mergedResults;
    }

    /**
     * Controller to validate the email. First we check if it is a valid email and then if it is already registered to
     * a user
     *
     * @param user          the user DTO from the form in the front-end
     * @param bindingResult the results of the validation
     * @param userLocale    the user's locale
     * @return json response that contains all errors
     */
    @PostMapping("/check-email")
    public RegisterJsonResponse validateEmail(@Validated({RegistrationEmailValidator.class})
                                              @ModelAttribute("user") RegisterUserDto user,
                                              BindingResult bindingResult,
                                              Locale userLocale) {
        RegisterJsonResponse response = new RegisterJsonResponse();
        if (!bindingResult.hasFieldErrors()) {
            boolean isEmailPresent = userService.isEmailPresent(user.getEmail());
            if (isEmailPresent) {
                response.setStatus(response.ERROR);
                response.addFieldErrors("email", new ArrayList<String>() {
                    {
                        add(messageSource.getMessage("Email.exists", null, userLocale));
                    }
                });
            }
            return response;
        }
        return getRegisterJsonResponse(bindingResult, response);
    }

    /**
     * Controller to validate the username. First we check if it is a valid username and then if it is already
     * registered to a user
     *
     * @param user          the user DTO from the form in the front-end
     * @param bindingResult the results of the validation
     * @param userLocale    the user's locale
     * @return json response that contains all errors
     */
    @PostMapping("/check-username")
    public RegisterJsonResponse validateUsername(@Validated({RegistrationUsernameValidator.class})
                                                 @ModelAttribute("user") RegisterUserDto user,
                                                 BindingResult bindingResult,
                                                 Locale userLocale) {

        RegisterJsonResponse response = new RegisterJsonResponse();
        if (!bindingResult.hasFieldErrors()) {
            boolean isUserPresent = userService.isUsernamePresent(user.getUsername());
            if (isUserPresent) {
                response.setStatus(response.ERROR);
                response.addFieldErrors("username", new ArrayList<String>() {
                    {
                        add(messageSource.getMessage("Username.exists", null, userLocale));
                    }
                });
            }
            return response;
        }
        return getRegisterJsonResponse(bindingResult, response);
    }

    /**
     * Controller to validate the password.
     *
     * @param user          the user DTO from the form in the front-end
     * @param bindingResult the results of the validation
     * @return json response that contains all errors
     */
    @PostMapping("/check-password")
    public RegisterJsonResponse validatePassword(@Validated({RegistrationPasswordValidator.class})
                                                 @ModelAttribute("user") RegisterUserDto user,
                                                 BindingResult bindingResult) {
        return getRegisterJsonResponse(bindingResult, new RegisterJsonResponse());
    }

    /**
     * Controller to check if the two passwords entered match.
     *
     * @param user          the user DTO from the form in the front-end
     * @param bindingResult the results of the validation
     * @return json response that contains all errors
     */
    @PostMapping("/check-password-match")
    public RegisterJsonResponse validatePasswordMatch(@Validated({RegistrationPassMatchValidator.class})
                                                      @ModelAttribute("user") RegisterUserDto user,
                                                      BindingResult bindingResult) {
        RegisterJsonResponse response = new RegisterJsonResponse();
        if (!bindingResult.hasErrors()) {
            response.setStatus(response.SUCCESS);
        } else {
            response.setStatus(response.ERROR);
            List<String> errors = bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            response.addFieldErrors("password2", errors);
        }
        return response;
    }

    /**
     * Method to construct the RegisterJsonResponse object to be sent as a json response with all the errors and messages
     *
     * @param bindingResult the results of the validation
     * @param response      the RegisterJsonResponse object
     * @return the constructed RegisterJsonResponse object
     */
    private RegisterJsonResponse getRegisterJsonResponse(BindingResult bindingResult, RegisterJsonResponse response) {
        if (!bindingResult.hasErrors()) {
            response.setStatus(response.SUCCESS);
        } else {
            response.setStatus(response.ERROR);
            Map<Object, List<String>> errors = bindingResult.getFieldErrors().stream()
                    .collect(
                            Collectors.toMap(
                                    FieldError::getField,
                                    fieldError -> Collections.singletonList(fieldError.getDefaultMessage()),
                                    RegisterValidatorController::mergeEntriesWithDuplicatedKeys
                            ));
            response.setErrorsMap(errors);
        }
        return response;
    }
}
