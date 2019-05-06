package com.blackswandata.mapper;

import com.blackswandata.domain.Task;
import com.blackswandata.domain.User;
import com.blackswandata.dto.TaskDto;
import com.blackswandata.dto.TaskUpdateDto;
import com.blackswandata.exception.UserNotFoundException;
import com.blackswandata.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class TaskMapperServiceImpl implements TaskMapperService {
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;

  @Inject
  public TaskMapperServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
    this.modelMapper = modelMapper;
    this.userRepository = userRepository;
  }

  @Override
  public Task convertToEntity(Long userId, TaskDto taskDto) {
    User existingUser = getUser(userId);
    Task task = modelMapper.map(taskDto, Task.class);
    task.setUser(existingUser);
    return task;
  }

  @Override
  public Task convertToEntity(Long userId, TaskUpdateDto taskDto) {
    User existingUser = getUser(userId);
    Task task = modelMapper.map(taskDto, Task.class);
    task.setUser(existingUser);
    return task;
  }

  @Override
  public TaskDto convertToDto(Task task) {
    return modelMapper.map(task, TaskDto.class);
  }

  @Override
  public Iterable<TaskDto> convertToDto(Iterable<Task> tasks) {

    if(isNull(tasks)) {
      return new ArrayList<>();
    }
    List<TaskDto> taskDtos = new ArrayList<>();
    for (Task task: tasks) {
      taskDtos.add(convertToDto(task));
    }
    return taskDtos;
  }

  private User getUser(Long userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    if(!userOptional.isPresent()) {
      throw new UserNotFoundException(userId);
    }
    return userOptional.get();
  }
}
