package com.blackswandata.validator;

import com.blackswandata.enums.TaskStatusEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

public class TaskStatusValidatorImpl implements ConstraintValidator<TaskStatusValidator, TaskStatusEnum> {

  @Override
  public void initialize(TaskStatusValidator constraintAnnotation) {
  }

  @Override
  public boolean isValid(TaskStatusEnum value, ConstraintValidatorContext context) {
    if (isNull(value)) {
      return true;
    }
    for (TaskStatusEnum taskStatusEnum : TaskStatusEnum.values()) {
      if (taskStatusEnum.name().equalsIgnoreCase(value.name())) {
        return true;
      }
    }
    return false;
  }
}
