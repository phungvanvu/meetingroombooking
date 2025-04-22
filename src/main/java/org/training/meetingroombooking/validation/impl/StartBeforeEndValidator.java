package org.training.meetingroombooking.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.training.meetingroombooking.entity.dto.RoomBookingDTO;
import org.training.meetingroombooking.validation.StartBeforeEnd;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, RoomBookingDTO> {
    @Override
    public boolean isValid(RoomBookingDTO dto, ConstraintValidatorContext context) {
        if (dto.getStartTime() == null || dto.getEndTime() == null) return true;

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start time must be before end time.")
                    .addPropertyNode("startTime")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
