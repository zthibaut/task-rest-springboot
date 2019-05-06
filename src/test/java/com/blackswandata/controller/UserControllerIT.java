package com.blackswandata.controller;

import com.blackswandata.domain.User;
import com.blackswandata.dto.UserDto;
import com.blackswandata.dto.UserUpdateDto;
import com.blackswandata.repository.UserRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class UserControllerIT extends CommonControllerIT {

  @Autowired
  private UserRepository userRepository;

  private UserDto userDto;

  String username = "jsmith";
  String firstName = "John";
  String lastName = "Smith";

  String usernameOther = "jdoe";
  String firstNameOther = "Jane";
  String lastNameOther = "Doe";

  private static final String USERS_ENDPOINT = "/api/user";
  private static final String USER_ENDPOINT = "/api/user/{id}";

  @Before
  public void setUp() {
    RestAssured.port = serverPort;

    userRepository.deleteAll();

    userDto = new UserDto(username, firstName, lastName);
  }

  @Test
  public void canAddUserSuccessfully() {
    Optional<User> userOnRecordOptional = userRepository.findByUsername(username);
    assertFalse(userOnRecordOptional.isPresent());

    given()
        .body(userDto)
        .contentType(ContentType.JSON)
        .when()
        .post(USERS_ENDPOINT)
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", not(nullValue()))
        .body("first_name", is(firstName))
        .body("last_name", is(lastName))
        .body("username", is(username));

    userOnRecordOptional = userRepository.findByUsername(username);
    assertTrue(userOnRecordOptional.isPresent());
  }

  @Test
  public void refuseToAddUserWithoutUserName() {
    Optional<User> userOnRecordOptional = userRepository.findByUsername(username);
    assertFalse(userOnRecordOptional.isPresent());

    String jsonString = asJsonString(userDto);
    jsonString = jsonString.replace(username, "");

    given()
        .body(jsonString)
        .contentType(ContentType.JSON)
        .when()
        .post(USERS_ENDPOINT)
        .then()
        .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
        .body("errors.defaultMessage", hasItem(containsString("Username")));

    userOnRecordOptional = userRepository.findByUsername(username);
    assertFalse(userOnRecordOptional.isPresent());
  }

  @Test
  public void refuseToAddUserWithExistingUsername() {
    User user = new User(username, firstName, lastName);
    userRepository.save(user);

    userDto = new UserDto(username, firstName + " other", lastName + " other");

    Optional<User> userOnRecord = userRepository.findByUsername(username);
    assertTrue(userOnRecord.isPresent());

    given()
        .body(userDto)
        .contentType(ContentType.JSON)
        .when()
        .post(USERS_ENDPOINT)
        .then()
        .statusCode(equalTo(HttpStatus.CONFLICT.value()))
        .body("message", containsString(username));
  }

  @Test
  public void addUserShouldReturnBadRequestWithoutBody() {
    given().contentType(ContentType.JSON)
        .when()
        .post(USERS_ENDPOINT)
        .then()
        .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void addUserShouldReturnNotSupportedMediaTypeIfNonJSON() {
    given()
        .body("some payload")
        .when()
        .post(USERS_ENDPOINT)
        .then()
        .statusCode(equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
  }

  @Test
  public void shouldUpdateUserProperly() {
    String lastNameOther = "Johnny";
    String firstNameOther = "Doe";

    assertNotEquals(firstNameOther, firstName);
    assertNotEquals(lastNameOther, lastName);

    UserUpdateDto userUpdateDto = new UserUpdateDto(firstNameOther, lastNameOther);

    User user = new User(username, firstName, lastName);
    user = userRepository.save(user);

    given()
        .body(userUpdateDto)
        .contentType(ContentType.JSON).when()
        .put(USER_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", is(user.getId().intValue()))
        .body("first_name", is(firstNameOther))
        .body("last_name", is(lastNameOther))
        .body("username", is(username));

    Optional<User> userOnRecordOptional = userRepository.findByUsername(username);
    assertTrue(userOnRecordOptional.isPresent());

    User userOnRecord = userOnRecordOptional.get();
    assertEquals(user.getId(), userOnRecord.getId());
    assertEquals(username, userOnRecord.getUsername());
    assertEquals(firstNameOther, userOnRecord.getFirstName());
    assertEquals(lastNameOther, userOnRecord.getLastName());
  }

  @Test
  public void updatingUserWithNewUserNameShouldIgnoreNewUsername() {
    String lastNameOther = "Johnny";
    String firstNameOther = "Doe";
    String usernameOther = "jDoe";

    assertNotEquals(firstNameOther, firstName);
    assertNotEquals(lastNameOther, lastName);
    assertNotEquals(usernameOther, username);

    userDto = new UserDto(usernameOther, firstNameOther, lastNameOther);

    User user = new User(username, firstName, lastName);
    user = userRepository.save(user);

    Optional<User> userOnRecordOptional = userRepository.findByUsername(username);
    assertTrue(userOnRecordOptional.isPresent());

    given()
        .body(userDto)
        .contentType(ContentType.JSON).when()
        .put(USER_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", is(user.getId().intValue()))
        .body("first_name", is(firstNameOther))
        .body("last_name", is(lastNameOther))
        .body("username", is(username));

    userOnRecordOptional = userRepository.findByUsername(username);
    assertTrue(userOnRecordOptional.isPresent());

    User userOnRecord = userOnRecordOptional.get();
    assertEquals(user.getId(), userOnRecord.getId());
    assertEquals(username, userOnRecord.getUsername());
    assertEquals(firstNameOther, userOnRecord.getFirstName());
    assertEquals(lastNameOther, userOnRecord.getLastName());
  }

  @Test
  public void updateUserShouldReturnNotSupportedMediaTypeIfNonJSON() {
    given()
        .body(userDto)
        .when()
        .put(USER_ENDPOINT, 123l)
        .then()
        .statusCode(equalTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
  }

  @Test
  public void canListAllUsersSuccessfully() {
    User user = new User(username, firstName, lastName);
    user = userRepository.save(user);
    User userOther = new User(usernameOther, firstNameOther, lastNameOther);
    userOther = userRepository.save(userOther);

    given().contentType(ContentType.JSON)
        .when()
        .get(USERS_ENDPOINT)
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", hasItems(user.getId().intValue(), userOther.getId().intValue()))
        .body("first_name", hasItems(firstName, firstNameOther))
        .body("last_name", hasItems(lastName, lastNameOther))
        .body("username", hasItems(username, usernameOther));
  }

  @Test
  public void canPullExistingUsersSuccessfully() {
    User user = new User(username, firstName, lastName);
    user = userRepository.save(user);

    given().contentType(ContentType.JSON)
        .when()
        .get(USER_ENDPOINT, user.getId())
        .then()
        .statusCode(equalTo(HttpStatus.OK.value()))
        .body("id", is(user.getId().intValue()))
        .body("first_name", is(firstName))
        .body("last_name", is(lastName))
        .body("username", is(username));
  }
}
