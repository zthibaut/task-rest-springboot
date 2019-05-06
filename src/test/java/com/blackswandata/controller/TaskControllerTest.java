package com.blackswandata.controller;

import com.blackswandata.domain.Task;
import com.blackswandata.domain.User;
import com.blackswandata.dto.TaskDto;
import com.blackswandata.dto.TaskUpdateDto;
import com.blackswandata.mapper.TaskMapperService;
import com.blackswandata.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {
  @Mock
  private TaskService taskService;
  @Mock
  private TaskMapperService taskMapperService;

  private MockMvc mockMvc;

  private TaskController taskController;

  private Long userId = 1274l, taskId = 544l;
  private TaskDto taskDto;
  private TaskUpdateDto taskUpdateDto;
  private Task task;
  private List<Task> tasks;
  private List<TaskDto> taskDtos;
  private User user;
  private LocalDateTime dueOn = LocalDateTime.now();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    taskController = new TaskController(taskService, taskMapperService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

    user = new User("username", "first name", "last name");
    task = new Task(user, "name", "description", dueOn);
    taskDto = new TaskDto(userId,"name", "description", dueOn);
    taskUpdateDto = new TaskUpdateDto(userId, "name", "description");
    tasks = new ArrayList<>();
    tasks.add(task);
    taskDtos = new ArrayList<>();
    taskDtos.add(taskDto);
  }

  @Test
  public void itShouldSaveTask() {
    when(taskMapperService.convertToEntity(userId, taskDto)).thenReturn(task);
    when(taskMapperService.convertToDto(task)).thenReturn(taskDto);
    when(taskService.save(task)).thenReturn(task);

    TaskDto returnedTaskDto = taskController.add(userId, taskDto);

    assertEquals(taskDto, returnedTaskDto);
    verify(taskService, times(1)).save(task);
    verify(taskMapperService, times(1)).convertToDto(task);
    verify(taskMapperService, times(1)).convertToEntity(userId, taskDto);
  }

  @Test
  public void itShouldUpdateTask() {
    when(taskMapperService.convertToEntity(userId, taskUpdateDto)).thenReturn(task);
    when(taskMapperService.convertToDto(task)).thenReturn(taskDto);
    when(taskService.update(userId, taskId, task)).thenReturn(task);

    TaskDto returnedTaskDto = taskController.update(userId, taskId, taskUpdateDto);

    assertEquals(taskDto, returnedTaskDto);
    verify(taskService, times(1)).update(userId, taskId, task);
    verify(taskMapperService, times(1)).convertToDto(task);
    verify(taskMapperService, times(1)).convertToEntity(userId, taskUpdateDto);
  }

  @Test
  public void itShouldDeleteATaskGivenItsId() {
    when(taskMapperService.convertToDto(task)).thenReturn(taskDto);
    when(taskService.delete(userId, taskId)).thenReturn(task);

    TaskDto returnedTaskDto = taskController.delete(userId, taskId);

    assertEquals(taskDto, returnedTaskDto);
    verify(taskService, times(1)).delete(userId, taskId);
    verify(taskMapperService, times(1)).convertToDto(task);
  }

  @Test
  public void itShouldGetTasksByIdFromService() {
    when(taskMapperService.convertToDto(task)).thenReturn(taskDto);
    when(taskService.get(userId, taskId)).thenReturn(task);

    TaskDto returnedTaskDto = taskController.get(userId, taskId);

    assertEquals(taskDto, returnedTaskDto);
    verify(taskService, times(1)).get(userId, taskId);
    verify(taskMapperService, times(1)).convertToDto(task);
  }

  @Test
  public void itShouldGetAllTasksFromService() {
    when(taskMapperService.convertToDto(tasks)).thenReturn(taskDtos);
    when(taskService.listTasks(userId)).thenReturn(tasks);

    Iterable<TaskDto> returnedTaskDtos = taskController.listTasks(userId);

    assertEquals(taskDtos, returnedTaskDtos);
    verify(taskService, times(1)).listTasks(userId);
    verify(taskMapperService, times(1)).convertToDto(tasks);
  }
}
