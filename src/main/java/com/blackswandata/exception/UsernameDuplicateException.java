package com.blackswandata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameDuplicateException extends BaseException {

  public UsernameDuplicateException(String username) {
    super("10001", String.format("Please pick a different username: %s is already allocated", username),
        new String[]{username});
  }
}

