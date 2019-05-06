package com.blackswandata.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TaskStatusValidatorImpl.class)
@Target(value = { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskStatusValidator {
  String message() default "Task Status value can only be one of : done, pending";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
