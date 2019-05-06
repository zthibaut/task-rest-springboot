package com.blackswandata.controller;

import com.blackswandata.domain.Task;
import com.blackswandata.domain.User;
import com.blackswandata.dto.TaskDto;
import com.blackswandata.dto.TaskUpdateDto;
import com.blackswandata.enums.TaskStatusEnum;
import com.blackswandata.repository.TaskRepository;
import com.blackswandata.repository.UserRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;

import static com.blackswandata.helper.DateHelper.DATE_TIME_FORMATTER;
import static com.blackswandata.helper.DateHelper.DATE_TIME_FORMAT_PATTERN;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TaskControllerIT extends CommonControllerIT {

  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private UserRepository userRepository;

  private static final String TASKS_ENDPOINT = "/api/user/{user_id}/task";
  private static final String TASK_ENDPOINT = "/api/user/{id}/task/{task_id}";

  private TaskDto taskDto;
  private User user;

  private String name = "My task";
  private String description = "Description";
  private LocalDateTime taskTime = LocalDateTime.of(2037, Month.FEBRUARY, 20, 06, 30, 12);
  private String taskTimeString = "2037-02-20 06:30:12";

  @Before
  public void setUp() {
    RestAssured.port = serverPort;

    taskRepository.deleteAll();
    userRepository.deleteAll();

    user = new User("jsmith", "John", "Smith");
    user = userRepository.save(user);

    taskDto = new TaskDto(user.getId(), name, description, taskTime);
  }

  @Test
  public void canAddTaskSuccessfully() {
    Iterable<Task> taskOnRecordList = taskRepository.findByUserId(user.getId(), true);
    assertEquals(0, ((Collection<Task>)taskOnRecordList).size());

    given()
        .body(taskDto)
        .contentType(ContentType.JSON)
        .when()
        .post(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", not(nullValue()))
        .body("user_id", is(user.getId().intValue()))
        .body("active", is(true))
        .body("name", is(name))
        .body("status", is(TaskStatusEnum.pending.name()))
        .body("description", is(description))
        .body("date_time", is(taskTimeString));

    taskOnRecordList = taskRepository.findByUserId(user.getId(), true);
    assertEquals(1, ((Collection<Task>)taskOnRecordList).size());
  }

  @Test
  public void refuseToAddTaskWithDueDateInPast() {
    taskTime = LocalDateTime.of(2017, Month.FEBRUARY, 20, 06, 30, 12);
    taskTimeString = "2017-02-20 06:30:12";
    taskDto.setDateTime(taskTime);

    given()
        .body(taskDto)
        .contentType(ContentType.JSON)
        .when()
        .post(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
        .body("errors.defaultMessage", hasItems(containsString(taskTimeString)));
  }

  @Test
  public void refuseToAddTaskWithoutDueDate() {
    taskDto.setDateTime(null);

    given()
        .body(taskDto)
        .contentType(ContentType.JSON)
        .when()
        .post(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void refuseToAddTaskWithDueDateInIncorrectFormat() {
    String payload = asJsonString(taskDto);
    payload = payload.replace("-", "/");

    given()
        .body(payload)
        .contentType(ContentType.JSON)
        .when()
        .post(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
        .body("message", containsString(DATE_TIME_FORMAT_PATTERN));
  }

  @Test
  public void addTaskWithDueDateInPast() {
    taskTime = LocalDateTime.now();
    taskDto.setDateTime(taskTime);

    given()
        .body(taskDto)
        .contentType(ContentType.JSON)
        .when()
        .post(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
        .body("errors.defaultMessage", hasItems(containsString("future")));
  }

  @Test
  public void refuseToAddTaskWithoutTaskName() {
    String jsonString = asJsonString(taskDto);
    jsonString = jsonString.replace(name, "");

    given()
        .body(jsonString)
        .contentType(ContentType.JSON)
        .when()
        .post(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));

    Iterable<Task> taskOnRecordList = taskRepository.findByUserId(user.getId(), true);
    assertEquals(0, ((Collection<?>) taskOnRecordList).size());
  }

  @Test
  public void addTaskShouldReturnBadRequestWithoutBody() {
    given().contentType(ContentType.JSON)
        .when()
        .post(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void addTaskShouldReturnNotSupportedMediaTypeIfNonJSON() {
    given()
        .body(taskDto)
        .when()
        .post(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
  }

  @Test
  public void updatingTaskShouldWork() {
    String nameOther = "other";
    String descriptionOther = "other description";

    assertNotEquals(nameOther, name);
    assertNotEquals(descriptionOther, description);

    TaskUpdateDto taskUpdateDto = new TaskUpdateDto(user.getId(), nameOther, descriptionOther);

    Task task = new Task(user, name, description, taskTime);
    task = taskRepository.save(task);

    given()
        .body(taskUpdateDto)
        .contentType(ContentType.JSON).when()
        .put(TASK_ENDPOINT, user.getId(), task.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", is(task.getId().intValue()))
        .body("name", is(nameOther))
        .body("description", is(descriptionOther));

    Iterable<Task> taskOnRecordList = taskRepository.findByUserId(user.getId(), true);
    assertEquals(1, ((Collection<?>)taskOnRecordList).size());
    Task taskOnRecord = (Task) ((Collection<?>) taskOnRecordList).toArray()[0];
    assertNotNull(taskOnRecord);

    assertEquals(task.getId(), taskOnRecord.getId());
    assertEquals(nameOther, taskOnRecord.getName());
    assertEquals(descriptionOther, taskOnRecord.getDescription());
  }

  @Test
  public void updatingTaskWithIncorrectUserIdShouldFail() {
    String nameOther = "other";
    String descriptionOther = "other description";

    assertNotEquals(nameOther, name);
    assertNotEquals(descriptionOther, description);

    TaskUpdateDto taskUpdateDto = new TaskUpdateDto(user.getId(), nameOther, descriptionOther);

    Task task = new Task(user, name, description, taskTime);
    task = taskRepository.save(task);

    Iterable<Task> taskOnRecordList = taskRepository.findByUserId(user.getId(), true);
    assertEquals(1, ((Collection<?>)taskOnRecordList).size());

    User userOther = new User("jsmithother", "Johnathan", "Smithy");
    userOther = userRepository.save(userOther);

    given()
        .body(taskUpdateDto)
        .contentType(ContentType.JSON).when()
        .put(TASK_ENDPOINT, userOther.getId(), task.getId())
        .then()
        .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
        .body("message", containsString(userOther.getId().toString()))
        .body("message", containsString(task.getId().toString()));

    taskOnRecordList = taskRepository.findByUserId(user.getId(), true);
    assertEquals(1, ((Collection<?>)taskOnRecordList).size());
    Task taskOnRecord = (Task) ((Collection<?>) taskOnRecordList).toArray()[0];
    assertNotNull(taskOnRecord);

    assertEquals(task.getId(), taskOnRecord.getId());
    assertEquals(name, taskOnRecord.getName());
    assertEquals(description, taskOnRecord.getDescription());
  }

  @Test
  public void updatingTaskWithNewDueTimeShouldIgnoreNewDueTime() {
    String nameOther = "other";
    String descriptionOther = "other description";
    LocalDateTime dueOnOther = LocalDateTime.now().plusDays(10);

    assertNotEquals(nameOther, name);
    assertNotEquals(descriptionOther, description);
    assertFalse(dueOnOther.isEqual(taskTime));

    taskDto = new TaskDto(user.getId(), nameOther, descriptionOther, dueOnOther);

    Task task = new Task(user, name, description, taskTime);
    task = taskRepository.save(task);

    Iterable<Task> taskOnRecordList = taskRepository.findByUserId(user.getId(), true);
    assertEquals(1, ((Collection<?>)taskOnRecordList).size());
    Task taskOnRecord = (Task) ((Collection<?>) taskOnRecordList).toArray()[0];
    assertNotNull(taskOnRecord);

    given()
        .body(taskDto)
        .contentType(ContentType.JSON).when()
        .put(TASK_ENDPOINT, user.getId(), task.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", is(task.getId().intValue()))
        .body("name", is(nameOther))
        .body("description", is(descriptionOther))
        .body("date_time", is(taskTimeString));

    taskOnRecordList = taskRepository.findByUserId(user.getId(), true);
    assertEquals(1, ((Collection<?>)taskOnRecordList).size());
    taskOnRecord = (Task) ((Collection<?>) taskOnRecordList).toArray()[0];
    assertNotNull(taskOnRecord);

    assertEquals(task.getId(), taskOnRecord.getId());
    assertEquals(nameOther, taskOnRecord.getName());
    assertEquals(descriptionOther, taskOnRecord.getDescription());
    assertTrue(taskTime.equals(taskOnRecord.getDateTime()));
  }

  @Test
  public void updateTaskShouldReturnNotSupportedMediaTypeIfNonJSON() {
    given()
        .body(taskDto)
        .when()
        .put(TASK_ENDPOINT, 123l, 23l)
        .then()
        .statusCode(equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
  }

  @Test
  public void deleteTaskShouldUpdateTaskProperly() {
    Task task = new Task(user, name, description, taskTime);
    task = taskRepository.save(task);

    given()
        .when()
        .delete(TASK_ENDPOINT, user.getId(), task.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", is(task.getId().intValue()))
        .body("name", is(name))
        .body("description", is(description))
        .body("date_time", is(taskTimeString));

    Iterable<Task> tasksOnRecord = taskRepository.findByUserId(user.getId(), false);
    assertEquals(1, ((Collection<?>)tasksOnRecord).size());
    Task taskOnRecord = (Task) ((Collection<?>)tasksOnRecord).toArray()[0];
    assertFalse(taskOnRecord.isActive());
  }

  @Test
  public void deleteTaskShouldBeNotFoundIfNonExistingId() {
    Long randomId = 32546l;

    given()
        .when()
        .delete(TASK_ENDPOINT, user.getId(), randomId)
        .then()
        .statusCode(equalTo(HttpStatus.NOT_FOUND.value()))
        .body("message", containsString(randomId.toString()));
  }

  @Test
  public void canPullExistingTasksSuccessfully() {
    Task task = new Task(user, name, description, taskTime);
    task = taskRepository.save(task);

    given().contentType(ContentType.JSON)
        .when()
        .get(TASK_ENDPOINT, user.getId(), task.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", is(task.getId().intValue()))
        .body("name", is(name))
        .body("description", is(description))
        .body("date_time", is(taskTimeString));
  }

  @Test
  public void canListAllTasksOfExistingUser() {
    Task task = new Task(user, name, description, taskTime);
    task = taskRepository.save(task);

    LocalDateTime dueOnOther = LocalDateTime.now().plusDays(9);
    String dueOnOtherString = dueOnOther.format(DATE_TIME_FORMATTER);
    String nameOther = "other task";
    String descriptionOther = "other description";
    Task taskOther = new Task(user, nameOther, descriptionOther, dueOnOther);
    taskOther = taskRepository.save(taskOther);

    Task taskInactive = new Task(user, "inactive task", "inactive task", dueOnOther);
    taskInactive.setActive(false);
    taskInactive = taskRepository.save(taskInactive);

    given().contentType(ContentType.JSON)
        .when()
        .get(TASKS_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", hasItems(task.getId().intValue(), taskOther.getId().intValue()))
        .body("id", not(hasItems(taskInactive.getId().intValue())))
        .body("name", hasItems(name, nameOther))
        .body("description", hasItems(description, descriptionOther))
        .body("date_time", hasItems(taskTimeString, dueOnOtherString));
  }
}
