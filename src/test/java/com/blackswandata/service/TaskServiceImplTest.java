package com.blackswandata.service;

import com.blackswandata.domain.Task;
import com.blackswandata.domain.User;
import com.blackswandata.exception.TaskNotFoundException;
import com.blackswandata.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

  @Mock
  private TaskRepository taskRepository;

  private TaskService taskService;

  private Task task, taskOther;
  private List<Task> tasks;
  private User user;
  private Long taskId = 3245l;
  private Long userId = 252l;
  private LocalDateTime dueOn = LocalDateTime.now();

  @Before
  public void setUp() {
    taskService = new TaskServiceImpl(taskRepository);
    user = new User("jdoe", "first name", "last name");
    task = new Task(user, "name", "description", dueOn);
    taskOther = new Task(user, "name other", "description other", dueOn);
    tasks = new ArrayList<>();
    tasks.add(task);
    tasks.add(taskOther);
  }

  @Test
  public void itShouldSaveTaskIfNotExisting() {
    when(taskRepository.save(task)).thenReturn(task);

    Task returnedTask = taskService.save(task);
    assertEquals(task, returnedTask);
    verify(taskRepository, times(1)).save(task);
  }

  @Test
  public void itShouldNotUpdateTaskWithUnknownId() {
    Long taskId = 3245l;

    when(taskRepository.findByUserIdAndId(userId, taskId)).thenReturn(Optional.empty());

    assertNotEquals(taskOther.getName(), task.getName());
    assertNotEquals(taskOther.getDescription(), task.getDescription());

    Boolean expectedErrorThrown = false;
    try {
      taskService.update(userId, taskId, taskOther);
      fail();
    } catch (TaskNotFoundException e) {
      expectedErrorThrown = true;
    }
    assertEquals(true, expectedErrorThrown);

    verify(taskRepository, times(1)).findByUserIdAndId(userId, taskId);
    verify(taskRepository, times(0)).save(any(Task.class));
  }

  @Test
  public void itShouldUpdateTaskWhenGivenItsId() {
    Long taskId = 3245l;
    String nameOther = taskOther.getName();
    String descriptionOther = taskOther.getDescription();

    when(taskRepository.findByUserIdAndId(userId, taskId)).thenReturn(Optional.of(task));
    when(taskRepository.save(task)).thenReturn(task);

    assertNotEquals(nameOther, task.getName());
    assertNotEquals(descriptionOther, task.getDescription());

    Task updatedTask = taskService.update(userId, taskId, taskOther);

    assertEquals(task, updatedTask);
    verify(taskRepository, times(1)).findByUserIdAndId(userId, taskId);
    verify(taskRepository, times(1)).save(updatedTask);

    assertEquals(nameOther, task.getName());
    assertEquals(descriptionOther, task.getDescription());
  }

  @Test
  public void itShouldIgnoreNullNameWhenUpdatingTask() {
    Long taskId = 3245l;
    String name = task.getName();
    String descriptionOther = taskOther.getDescription();
    taskOther.setName(null);

    when(taskRepository.findByUserIdAndId(userId, taskId)).thenReturn(Optional.of(task));
    when(taskRepository.save(task)).thenReturn(task);

    assertNotEquals(taskOther.getName(), task.getName());
    assertNotEquals(taskOther.getDescription(), task.getDescription());

    Task updatedTask = taskService.update(userId, taskId, taskOther);

    assertEquals(task, updatedTask);
    verify(taskRepository, times(1)).findByUserIdAndId(userId, taskId);
    verify(taskRepository, times(1)).save(updatedTask);

    assertEquals(name, task.getName());
    assertEquals(descriptionOther, task.getDescription());
  }

  @Test
  public void itShouldSilentlyIgnoreNewDueOnTimeDuringUpdate() {
    LocalDateTime dueOnOther = LocalDateTime.now().plusDays(4);
    taskOther.setDateTime(dueOnOther);
    dueOn = task.getDateTime();

    when(taskRepository.findByUserIdAndId(userId, taskId)).thenReturn(Optional.of(task));
    when(taskRepository.save(task)).thenReturn(task);

    assertNotEquals(taskOther.getDateTime(), task.getDateTime());

    Task updatedTask = taskService.update(userId, taskId, taskOther);

    assertEquals(task, updatedTask);
    verify(taskRepository, times(1)).findByUserIdAndId(userId, taskId);
    verify(taskRepository, times(1)).save(updatedTask);

    assertEquals(dueOn, task.getDateTime());
  }

  @Test
  public void itShouldDeleteATaskWhenGivenItsId() {
    when(taskRepository.findByUserIdAndId(userId, taskId)).thenReturn(Optional.of(task));
    when(taskRepository.save(task)).thenReturn(task);

    Task returnedTask  = taskService.delete(userId, taskId);

    assertEquals(task, returnedTask);
    assertFalse(task.isActive());
    verify(taskRepository, times(1)).findByUserIdAndId(userId, taskId);
    verify(taskRepository, times(1)).save(task);
  }

  @Test
  public void itShouldHandleDeletingNonExistingTask() {
    when(taskRepository.findByUserIdAndId(userId, taskId)).thenReturn(Optional.empty());

    Boolean expectedErrorThrown = false;
    try {
      taskService.delete(userId, taskId);
      fail();
    } catch (TaskNotFoundException e) {
      expectedErrorThrown = true;
    }
    assertEquals(true, expectedErrorThrown);

    verify(taskRepository, times(1)).findByUserIdAndId(userId, taskId);
    verify(taskRepository, times(0)).save(any(Task.class));
  }

  @Test
  public void itShouldGetTaskFromRepository() {
    Long taskId = 325l;
    when(taskRepository.findByUserIdAndId(userId, taskId)).thenReturn(Optional.of(task));

    Task returnedTask = taskService.get(userId, taskId);

    assertEquals(task, returnedTask);
    verify(taskRepository, times(1)).findByUserIdAndId(userId, taskId);
  }

  @Test
  public void itShouldHandleGettingNonExistingTask() {
    Long taskId = 325l;
    when(taskRepository.findByUserIdAndId(userId, taskId)).thenReturn(Optional.empty());

    Boolean expectedErrorThrown = false;
    try {
      taskService.get(userId, taskId);
      fail();
    } catch (TaskNotFoundException e) {
      expectedErrorThrown = true;
    }
    assertEquals(true, expectedErrorThrown);

    verify(taskRepository, times(1)).findByUserIdAndId(userId, taskId);
  }

  @Test
  public void itShouldGetAllUserTasksFromRepository() {
    when(taskRepository.findByUserId(userId, true)).thenReturn(tasks);

    Iterable<Task> returnedTasks = taskService.listTasks(userId);

    assertEquals(tasks, returnedTasks);
    verify(taskRepository, times(1)).findByUserId(userId, true);
  }
}
