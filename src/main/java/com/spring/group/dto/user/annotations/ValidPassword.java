package com.spring.group.dto.user.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An interface to create our own annotation for validating password patterns.
 */
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordPatternValidator.class)
@Documented
public @interface ValidPassword {

    String message() default "{Invalid.password}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
