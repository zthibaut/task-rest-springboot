package com.blackswandata.dto;

import com.blackswandata.enums.TaskStatusEnum;
import com.blackswandata.serializer.JsonDateTimeDeserializer;
import com.blackswandata.serializer.JsonDateTimeSerializer;
import com.blackswandata.validator.DateInFutureValidator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto {
  private Long id;

  @JsonProperty("user_id")
  private Long userId;

  @Getter(AccessLevel.NONE)
  private Boolean active;

  @NotNull(message = "Name can't be null")
  @NotBlank(message = "Name must not be blank!")
  private String name;
  private String description;

  private TaskStatusEnum status;

  @JsonSerialize(using=JsonDateTimeSerializer.class)
  @JsonDeserialize(using=JsonDateTimeDeserializer.class)
  @DateInFutureValidator
  @JsonProperty("date_time")
  @NotNull(message = "Date time can't be null")
  private LocalDateTime dateTime;

  TaskDto() {
    this.active = true;
    this.status = TaskStatusEnum.pending;
  }

  public TaskDto(Long userId, String name, String description, LocalDateTime dateTime) {
    this();
    this.userId = userId;
    this.name = name;
    this.description = description;
    this.dateTime = dateTime;
  }

  public Boolean isActive() {
    return active;
  }
}
