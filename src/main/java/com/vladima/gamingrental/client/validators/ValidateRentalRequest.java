package com.vladima.gamingrental.client.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RentalRequestValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateRentalRequest {
    String message() default "Rental cannot be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

