package com.blackswandata.service;

import com.blackswandata.domain.Task;

public interface TaskService {
  Task save(Task task);

  Task update(Long userId, Long taskId, Task task);

  Task delete(Long userId, Long taskId);

  Task get(Long userId, Long taskId);

  Iterable<Task> listTasks(Long userId);
}
