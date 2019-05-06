package com.blackswandata.domain;

import com.blackswandata.enums.TaskStatusEnum;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaskTest {

  private User user;
  private Task task;

  @Before
  public void setUp() {
    user = new User("jsmith", "John", "Smith");
    task = new Task(user, "my task", "to do", LocalDateTime.now());
  }

  @Test
  public void isActiveByDefault() {
    assertTrue(task.isActive());
  }

  @Test
  public void isPendingByDefault() {
    assertEquals(TaskStatusEnum.pending, task.getStatus());
  }

}
