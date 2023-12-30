package com.vladima.gamingrental.helpers.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateValidatorImpl implements ConstraintValidator<DateValidator, LocalDateTime> {

    @Override
    public void initialize(DateValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) {
            return false;
        }
        var currentTime = LocalDateTime.now();
        return currentTime.getYear() >= 1990 && (currentTime.isBefore(currentTime) || currentTime.isEqual(currentTime));
    }
}
