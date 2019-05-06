package com.blackswandata.job;

import com.blackswandata.domain.Task;
import com.blackswandata.enums.TaskStatusEnum;
import com.blackswandata.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
@EnableScheduling
public class TaskExpiry {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TaskRepository taskRepository;

  @Inject
  public TaskExpiry(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Scheduled(fixedDelay = 60000)
  void cronTask() {
    LocalDateTime now = LocalDateTime.now();
    try {
      Iterable<Task> pendingTasksInPast = this.taskRepository.findByStatusDueInPast(TaskStatusEnum.pending, now);
      int taskToBeProcessedCount = ((Collection<?>) pendingTasksInPast).size();
      logger.info(String.format("RUN_START - will expire # %d tasks", taskToBeProcessedCount));
      for (Task task : pendingTasksInPast) {
        process(task);
      }
    } catch (Exception e) {
      logger.error(String.format("ERROR %s", e.getMessage()));
    }
    logger.info("RUN_END");
  }

  private void process(Task task) {
    try {
      String msg = String.format("Expiring task '%s' ", task.getName());
      logger.info(msg);
      task.setStatus(TaskStatusEnum.done);
      taskRepository.save(task);
      msg = String.format("Task '%s' expired", task.getName());
      logger.info(msg);
    } catch (Exception e) {
      logger.error(String.format("Error during expiration of task '%s' ", task.getName()));
    }
  }
}
