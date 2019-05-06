package com.blackswandata.job;

import com.jayway.restassured.RestAssured;
import com.blackswandata.domain.Task;
import com.blackswandata.domain.User;
import com.blackswandata.enums.TaskStatusEnum;
import com.blackswandata.repository.TaskRepository;
import com.blackswandata.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class TaskExpiryIT {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private UserRepository userRepository;

  private Task taskInPastPending, taskInFuturePending;
  private User user;

  @Autowired
  private TaskExpiry taskExpiry;

  @Value("${local.server.port}")
  private int serverPort;

  @Before
  public void setUp() {
    RestAssured.port = serverPort;

    taskRepository.deleteAll();
    userRepository.deleteAll();

    user = new User("jasmith", "Jane", "Smith");
    user = userRepository.save(user);
    }

  @Test
  public void itExpiresPendingTaskInPast() {
    LocalDateTime dueOn = LocalDateTime.now().minusDays(6);
    taskInPastPending = new Task(user, "taskInPastPending", "do this 1st ;)", dueOn);
    taskInPastPending = taskRepository.save(taskInPastPending);

    dueOn = LocalDateTime.now().plusDays(6);
    taskInFuturePending = new Task(user, "taskInPastDone", "do this 1st ;)", dueOn);
    taskInFuturePending = taskRepository.save(taskInFuturePending);

    taskExpiry.cronTask();

    Optional<Task> taskOptional = taskRepository.findByUserIdAndId(user.getId(), taskInPastPending.getId());
    assertTrue(taskOptional.isPresent());
    Task taskOnRecord = taskOptional.get();
    assertEquals(TaskStatusEnum.done, taskOnRecord.getStatus());

    taskOptional = taskRepository.findByUserIdAndId(user.getId(), taskInFuturePending.getId());
    assertTrue(taskOptional.isPresent());
    taskOnRecord = taskOptional.get();
    assertEquals(TaskStatusEnum.pending, taskOnRecord.getStatus());
  }
}
