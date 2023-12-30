package com.vladima.gamingrental.helpers.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class ValidatorYear implements ConstraintValidator<ValidateYear, Integer> {
    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
        return year >= 1990 && year <= LocalDateTime.now().getYear();
    }
}
