package com.blackswandata.service;

import com.blackswandata.domain.Task;
import com.blackswandata.exception.TaskNotFoundException;
import com.blackswandata.repository.TaskRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class TaskServiceImpl extends CommonService implements TaskService {
  private TaskRepository taskRepository;

  @Inject
  public TaskServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public Task save(Task task) {
    return taskRepository.save(task);
  }

  @Override
  public Task update(Long userId, Long taskId, Task task) {
    Optional<Task> existingTaskOptional = taskRepository.findByUserIdAndId(userId, taskId);
    if(!existingTaskOptional.isPresent()) {
      throw new TaskNotFoundException(userId, taskId);
    }
    Task existingTask = existingTaskOptional.get();
    return updateExistingTask(task, existingTask);
  }

  @Override
  public Task delete(Long userId, Long taskId) {
    Optional<Task> taskOptional = taskRepository.findByUserIdAndId(userId, taskId);
    if(!taskOptional.isPresent()) {
      throw new TaskNotFoundException(userId, taskId);
    }
    Task existingTask = taskOptional.get();
    existingTask.setActive(false);
    return taskRepository.save(existingTask);
  }

  @Override
  public Task get(Long userId, Long taskId) {
    Optional<Task> taskOptional = taskRepository.findByUserIdAndId(userId, taskId);
    if(!taskOptional.isPresent()) {
      throw new TaskNotFoundException(userId, taskId);
    }
    return taskOptional.get();
  }

  private Task updateExistingTask(Task task, Task existingTask) {
    existingTask.setName(ifNotNull(task.getName(), existingTask.getName()));
    existingTask.setDescription(ifNotNull(task.getDescription(), existingTask.getDescription()));

    return taskRepository.save(existingTask);
  }

  @Override
  public Iterable<Task> listTasks(Long userId) {
    return taskRepository.findByUserId(userId, true);
  }

}
