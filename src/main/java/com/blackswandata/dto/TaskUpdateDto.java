package com.blackswandata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskUpdateDto {

  private Long userId;

  private String name;
  private String description;

  public TaskUpdateDto() {}

  public TaskUpdateDto(Long userId, String name, String description) {
    this();
    this.userId = userId;
    this.name = name;
    this.description = description;
  }
}
