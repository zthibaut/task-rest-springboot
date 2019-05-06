package com.blackswandata.service;

public class CommonService {
  protected  <T> T ifNotNull(T data, T existingData) {
    return data == null ? existingData : data;
  }
}
