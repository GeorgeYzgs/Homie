package com.spring.group.dto.property;


import com.spring.group.models.property.HeatingFuel;
import com.spring.group.models.property.HeatingType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HeatingValidator implements ConstraintValidator<ValidHeating, PropertyDTO> {
    @Override
    public void initialize(ValidHeating constraintAnnotation) {
    }

    @Override
    public boolean isValid(PropertyDTO dto, ConstraintValidatorContext context) {

        return (dto.getHeatingType().equals(HeatingType.NONE) || dto.getHeatingFuel().equals(HeatingFuel.NONE))
                ? (dto.getHeatingType().equals(HeatingType.NONE) && dto.getHeatingFuel().equals(HeatingFuel.NONE))
                : true;
    }
}
