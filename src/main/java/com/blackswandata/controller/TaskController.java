package com.blackswandata.controller;

import com.blackswandata.domain.Task;
import com.blackswandata.dto.TaskDto;
import com.blackswandata.dto.TaskUpdateDto;
import com.blackswandata.mapper.TaskMapperService;
import com.blackswandata.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user/{user_id}/task", produces = "application/json")
public class TaskController {

  private final TaskService taskService;
  private final TaskMapperService taskMapperService;

  public TaskController(TaskService taskService, TaskMapperService taskMapperService) {
    this.taskMapperService = taskMapperService;
    this.taskService = taskService;
  }

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public TaskDto add(final @PathVariable("user_id") Long userId, final @RequestBody @Valid TaskDto taskDto) {
    Task task = convertToEntity(userId, taskDto);
    Task savedTask = taskService.save(task);
    return convertToDto(savedTask);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
  public TaskDto update(final @PathVariable("user_id") Long userId, final @PathVariable Long id,
                        final @RequestBody @Valid TaskUpdateDto taskDto) {
    Task task = convertToEntity(userId, taskDto);
    Task updatedTask = taskService.update(userId, id, task);
    return convertToDto(updatedTask);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public TaskDto delete(final @PathVariable("user_id") Long userId, final @PathVariable Long id) {
    Task deletedUser = taskService.delete(userId, id);
    return convertToDto(deletedUser);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public TaskDto get(final @PathVariable("user_id") Long userId, final @PathVariable Long id) {
    return convertToDto(taskService.get(userId, id));
  }

  @RequestMapping(method = RequestMethod.GET)
  public Iterable<TaskDto> listTasks(final @PathVariable("user_id") Long userId) {
    return convertToDto(taskService.listTasks(userId));
  }

  private TaskDto convertToDto(Task task) {
    return taskMapperService.convertToDto(task);
  }

  private Task convertToEntity(Long userId, TaskDto taskDto) {
    return taskMapperService.convertToEntity(userId, taskDto);
  }

  private Task convertToEntity(Long userId, TaskUpdateDto taskDto) {
    return taskMapperService.convertToEntity(userId, taskDto);
  }

  private Iterable<TaskDto> convertToDto(Iterable<Task> tasks) {
    return taskMapperService.convertToDto(tasks);
  }

}
