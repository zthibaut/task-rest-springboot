package com.blackswandata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends BaseException {

  public TaskNotFoundException(Long userId, Long taskId) {
    super("10003", String.format("Please provide a valid user + task id combination: user with id %s don't have a task with id %s ",
          userId.toString(), taskId.toString()),
        new String[]{userId.toString(), taskId.toString()});
  }
}
