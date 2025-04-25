package org.training.meetingroombooking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import org.training.meetingroombooking.validation.impl.StartBeforeEndValidator;

@Documented
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEnd {
  String message() default "Start time must be before end time.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
