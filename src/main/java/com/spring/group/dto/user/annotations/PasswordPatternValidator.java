package com.spring.group.dto.user.annotations;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PasswordPatternValidator implements ConstraintValidator<ValidPassword, String> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Properties props = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            props.load(new FileInputStream(classLoader.getResource("messages.properties").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageResolver resolver = new PropertiesMessageResolver(props);

        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(
                // at least 8 characters
                new LengthRule(8, 20),
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // at least one digit
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // at least one symbol
                new CharacterRule(EnglishCharacterData.Special, 1),
                // no whitespace
                new WhitespaceRule()

        ));
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);
//        List<String> filteredMessages = messages.stream()
//                .filter(msg -> !msg.contains(EnglishCharacterData.Digit.getErrorCode())
//                        && !msg.contains(EnglishCharacterData.Special.getErrorCode()))
//                .collect(Collectors.toList());
        String messageTemplate = String.join(",", messages);

        //disables default error message of the @interface and passes error messages created as a violation.
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
