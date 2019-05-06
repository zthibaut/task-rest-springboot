package com.blackswandata.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static com.blackswandata.helper.DateHelper.DATE_TIME_FORMATTER;
import static java.util.Objects.isNull;

public class DateInFutureValidatorImpl implements ConstraintValidator<DateInFutureValidator, LocalDateTime> {

  @Override
  public void initialize(DateInFutureValidator pastDate) {
  }

  @Override
  public boolean isValid(LocalDateTime date, ConstraintValidatorContext context) {
    LocalDateTime currentDateTime = LocalDateTime.now();
    if (isNull(date)) { // NOTE: Not relevant, date parsing will prevent this
      return true;
    }
    if (currentDateTime.isBefore(date)) {
      return true;
    }
    try {
      context.disableDefaultConstraintViolation();
      String dateTimeString = date.format(DATE_TIME_FORMATTER);
      String errorMsg = String.format("Invalid date time, %s. The time must be in the future.", dateTimeString);
      context.buildConstraintViolationWithTemplate(errorMsg)
          .addConstraintViolation();
      return false;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
