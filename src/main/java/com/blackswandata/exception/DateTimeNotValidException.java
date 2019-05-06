package com.blackswandata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.blackswandata.helper.DateHelper.DATE_TIME_FORMAT_PATTERN;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DateTimeNotValidException extends BaseException {

  public DateTimeNotValidException(String dateString) {
    super("10004", String.format("Invalid Date time provided: %s please provide a date time in the format %s",
        dateString, DATE_TIME_FORMAT_PATTERN),
        new String[]{dateString, DATE_TIME_FORMAT_PATTERN});
  }
}
