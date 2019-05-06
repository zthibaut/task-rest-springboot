package com.blackswandata.exception;

import java.util.Arrays;
import java.util.List;

public class BaseException extends RuntimeException {

  private String errorCode;
  private String errorMessage;
  private List<String> paramsList;

  public BaseException(String errorCode, String errorMessage, String... params) {
    super(errorMessage);
    this.errorCode = errorCode;
    this.paramsList = Arrays.asList(params);
  }
}
