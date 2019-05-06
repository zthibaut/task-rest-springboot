package com.blackswandata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BaseException {
  public UserNotFoundException(Long userId) {
    super("10002", String.format("Please provide a valid user id: %s is invalid", userId.toString()),
        new String[]{userId.toString()});
  }
}
