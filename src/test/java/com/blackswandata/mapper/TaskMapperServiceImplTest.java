package com.blackswandata.mapper;

import com.blackswandata.dto.TaskUpdateDto;
import com.blackswandata.domain.Task;
import com.blackswandata.domain.User;
import com.blackswandata.dto.TaskDto;
import com.blackswandata.exception.UserNotFoundException;
import com.blackswandata.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskMapperServiceImplTest {

  private ModelMapper modelMapper;
  @Mock
  private UserRepository userRepository;

  private TaskMapperService taskMapperService;
  private TaskDto taskDto;
  private TaskUpdateDto taskUpdateDto;
  private Task task, taskOther;
  private Long userId = 252l;
  private User user;
  private LocalDateTime dueOn = LocalDateTime.now();
  private String name = "name";
  private String description = "description";

  @Before
  public void setUp() {
    modelMapper = new ModelMapper();
    taskMapperService = new TaskMapperServiceImpl(modelMapper, userRepository);
    user = new User("jdoe", "first name", "last name");
    task = new Task(user, "name", "description", dueOn);
    taskDto = new TaskDto(userId, name, description, dueOn);
  }

  @Test
  public void itShouldConvertTaskDtoToTask() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Task returnedTask = taskMapperService.convertToEntity(userId, taskDto);

    assertEquals(name, returnedTask.getName());
    assertEquals(description, returnedTask.getDescription());
    assertEquals(dueOn, returnedTask.getDateTime());
    assertEquals(task.getUser(), user);
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  public void itShouldHandleConvertingTaskDtoToTaskWithUnknownUserId() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    Boolean expectedErrorThrown = false;
    try {
      taskMapperService.convertToEntity(userId, taskDto);
      fail();
    } catch (UserNotFoundException e) {
      expectedErrorThrown = true;
    }
    assertEquals(true, expectedErrorThrown);

    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  public void itShouldHandleConvertingTaskDtoToTaskWithUnknownUserIdDuringUpdate() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    Boolean expectedErrorThrown = false;
    try {
      taskMapperService.convertToEntity(userId, taskUpdateDto);
      fail();
    } catch (UserNotFoundException e) {
      expectedErrorThrown = true;
    }
    assertEquals(true, expectedErrorThrown);

    verify(userRepository, times(1)).findById(userId);
  }
  
}
