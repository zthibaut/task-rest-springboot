package com.blackswandata.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { DateInFutureValidatorImpl.class })
public @interface DateInFutureValidator {

  String message() default "Date time must be in the future";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
