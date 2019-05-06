package com.blackswandata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
  private Long id;

  @NotNull(message = "Username can't be null")
  @NotBlank(message = "Username must not be blank!")
  private String username;

  @NotNull(message = "First name can't be null")
  @NotBlank(message = "First name must not be blank!")
  @JsonProperty("first_name")
  private String firstName;

  @NotNull(message = "Last name can't be null")
  @NotBlank(message = "Last name must not be blank!")
  @JsonProperty("last_name")
  private String lastName;

  public UserDto() {
  }

  public UserDto(String username, String firstName, String lastName) {
    this();
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
