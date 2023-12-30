package com.vladima.gamingrental.helpers.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidatorImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateValidator {
    String message() default "The date must be between 01.01.1990 and present";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

