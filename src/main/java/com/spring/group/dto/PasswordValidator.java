package com.spring.group.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author George.Giazitzis
 */
public class PasswordValidator implements ConstraintValidator<PasswordMatches, RegisterUserDto> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegisterUserDto dto, ConstraintValidatorContext context) {
        return dto.getPassword().equals(dto.getPassword2());
    }
}
