package com.vladima.gamingrental.client.validators;

import com.vladima.gamingrental.client.dto.RentalDTO;
import com.vladima.gamingrental.client.dto.RentalRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RentalRequestValidator implements ConstraintValidator<ValidateRentalRequest, RentalRequestDTO> {

    @Override
    public boolean isValid(RentalRequestDTO rentalDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (rentalDTO == null) {
            throw new IllegalArgumentException("Invalid rentalDTO");
        }
        return rentalDTO.getDeviceUnitId() != null || !rentalDTO.getGameCopiesId().isEmpty();
    }
}
