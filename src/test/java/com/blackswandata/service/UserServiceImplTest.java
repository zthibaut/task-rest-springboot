package com.blackswandata.service;

import com.blackswandata.domain.User;
import com.blackswandata.exception.UserNotFoundException;
import com.blackswandata.exception.UsernameDuplicateException;
import com.blackswandata.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  private UserService userService;

  private String username = "jsmith";
  private String firstName = "John";
  private String lastName = "Smith";
  private String usernameOther = "other";
  private User user, userOther;
  private List<User> users;

  @Before
  public void setUp() {
    user = new User(username, firstName, lastName);
    userOther = new User(usernameOther, "other", "other");
    users = new ArrayList<>();
    users.add(user);
    userService = new UserServiceImpl(userRepository);
  }

  @Test
  public void itShouldSaveUserIfNotExisting() {
    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
    when(userRepository.save(user)).thenReturn(user);

    User returnedUser = userService.save(user);

    assertEquals(user, returnedUser);
    verify(userRepository, times(1)).findByUsername(user.getUsername());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void itShouldNotSaveUserWithExistingUsername() {
    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

    Boolean expectedErrorThrown = false;
    try {
      userService.save(user);
      fail();
    } catch (UsernameDuplicateException e) {
      expectedErrorThrown = true;
    }
    assertEquals(true, expectedErrorThrown);

    verify(userRepository, times(1)).findByUsername(user.getUsername());
    verify(userRepository, times(0)).save(user);
  }

  @Test
  public void itShouldUpdateUserWhenGivenItsId() {
    Long userId = 3245l;
    String firstNameOther = userOther.getFirstName();
    String lastNameOther = userOther.getLastName();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    assertNotEquals(userOther.getFirstName(), user.getFirstName());
    assertNotEquals(userOther.getLastName(), user.getLastName());

    User updatedUser = userService.update(userId, userOther);

    assertEquals(user, updatedUser);
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(updatedUser);

    assertEquals(firstNameOther, user.getFirstName());
    assertEquals(lastNameOther, user.getLastName());
  }

  @Test
  public void itShouldNotUpdateUserWithUnknownId() {
    Long userId = 3245l;

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertNotEquals(userOther.getFirstName(), user.getFirstName());
    assertNotEquals(userOther.getLastName(), user.getLastName());

    Boolean expectedErrorThrown = false;
    try {
      userService.update(userId, userOther);
      fail();
    } catch (UserNotFoundException e) {
      expectedErrorThrown = true;
    }
    assertEquals(true, expectedErrorThrown);

    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(0)).save(any(User.class));
  }

  @Test
  public void itShouldIgnoreNullNameWhenUpdatingUser() {
    Long userId = 3245l;
    String firstName = user.getFirstName();
    String lastNameOther = userOther.getLastName();
    userOther.setFirstName(null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    assertNotEquals(userOther.getFirstName(), user.getFirstName());
    assertNotEquals(userOther.getLastName(), user.getLastName());

    User updatedUser = userService.update(userId, userOther);

    assertEquals(user, updatedUser);
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(updatedUser);

    assertEquals(firstName, user.getFirstName());
    assertEquals(lastNameOther, user.getLastName());
  }

  @Test
  public void itShouldSilentlyIgnoreNewUsernameDuringUpdate() {
    Long userId = 3245l;
    username = user.getUsername();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    assertNotEquals(userOther.getUsername(), user.getUsername());

    User updatedUser = userService.update(userId, userOther);

    assertEquals(user, updatedUser);
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(updatedUser);

    assertEquals(username, user.getUsername());
  }

  @Test
  public void itShouldGetAllUsersFromRepository() {
    when(userRepository.findAll()).thenReturn(users);

    Iterable<User> returnedUsers = userService.listUsers();

    assertEquals(users, returnedUsers);
    verify(userRepository, times(1)).findAll();
  }

  @Test
  public void itShouldGetUserFromRepository() {
    Long userId = 325l;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    User returnedUser = userService.get(userId);

    assertEquals(user, returnedUser);
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  public void itShouldHandleGettingNonExistingUser() {
    Long userId = 325l;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    Boolean expectedErrorThrown = false;
    try {
      userService.get(userId);
      fail();
    } catch (UserNotFoundException e) {
      expectedErrorThrown = true;
    }
    assertEquals(true, expectedErrorThrown);

    verify(userRepository, times(1)).findById(userId);
  }
}