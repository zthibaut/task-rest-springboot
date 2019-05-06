package com.blackswandata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserUpdateDto {

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  UserUpdateDto() {}

  public UserUpdateDto(String firstName, String lastName) {
    this();
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
