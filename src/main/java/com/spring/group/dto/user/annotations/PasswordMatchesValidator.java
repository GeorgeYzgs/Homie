package com.spring.group.dto.user.annotations;

import com.spring.group.dto.user.RegisterUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The implementation of the @PasswordMatches interface that compares the two passwords
 *
 * @author George.Giazitzis
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterUserDto> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegisterUserDto dto, ConstraintValidatorContext context) {
        return dto.getPassword().equals(dto.getPassword2());
    }
}
