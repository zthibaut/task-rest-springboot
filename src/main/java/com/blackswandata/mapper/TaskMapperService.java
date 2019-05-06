package com.blackswandata.mapper;

import com.blackswandata.domain.Task;
import com.blackswandata.dto.TaskDto;
import com.blackswandata.dto.TaskUpdateDto;

public interface TaskMapperService {
  Task convertToEntity(Long userId, TaskDto taskDto);

  Task convertToEntity(Long userId, TaskUpdateDto taskDto);

  TaskDto convertToDto(Task task);

  Iterable<TaskDto> convertToDto(Iterable<Task> tasks);
}
