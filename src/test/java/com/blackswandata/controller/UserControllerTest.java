package com.blackswandata.controller;

import com.blackswandata.domain.User;
import com.blackswandata.dto.UserDto;
import com.blackswandata.dto.UserUpdateDto;
import com.blackswandata.mapper.UserMapperService;
import com.blackswandata.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
  @Mock
  private UserService userService;
  @Mock
  private UserMapperService userMapperService;

  private MockMvc mockMvc;

  private UserController userController;

  private UserDto userDto;
  private UserUpdateDto userUpdateDto;
  private User user;
  private List<User> users;
  private List<UserDto> userDtos;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    userController = new UserController(userService, userMapperService);

    this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    user = new User("username", "first name", "last name");
    userDto = new UserDto("username", "first name", "last name");
    userUpdateDto = new UserUpdateDto("first name", "last name");
    users = new ArrayList<>();
    users.add(user);
    userDtos = new ArrayList<>();
    userDtos.add(userDto);
  }

  @Test
  public void itShouldSaveUser() {
    when(userMapperService.convertToEntity(userDto)).thenReturn(user);
    when(userMapperService.convertToDto(user)).thenReturn(userDto);
    when(userService.save(user)).thenReturn(user);

    UserDto returnedUserDto = userController.add(userDto);

    assertEquals(userDto, returnedUserDto);
    verify(userService, times(1)).save(user);
    verify(userMapperService, times(1)).convertToDto(user);
    verify(userMapperService, times(1)).convertToEntity(userDto);
  }

  @Test
  public void itShouldUpdateUser() {
    Long userId = 3232l;

    when(userMapperService.convertToEntity(userUpdateDto)).thenReturn(user);
    when(userMapperService.convertToDto(user)).thenReturn(userDto);
    when(userService.update(userId, user)).thenReturn(user);

    UserDto returnedUserDto = userController.update(userId, userUpdateDto);

    assertEquals(userDto, returnedUserDto);
    verify(userService, times(1)).update(userId, user);
    verify(userMapperService, times(1)).convertToDto(user);
    verify(userMapperService, times(1)).convertToEntity(userUpdateDto);
  }

  @Test
  public void itShouldGetAllUsersFromService() {
    when(userMapperService.convertToDto(users)).thenReturn(userDtos);
    when(userService.listUsers()).thenReturn(users);

    Iterable<UserDto> returnedUserDtos = userController.listAll();

    assertEquals(userDtos, returnedUserDtos);
    verify(userService, times(1)).listUsers();
    verify(userMapperService, times(1)).convertToDto(users);
  }

  @Test
  public void itShouldGetAUserFromService() {
    Long userId = 25446l;

    when(userMapperService.convertToDto(user)).thenReturn(userDto);
    when(userService.get(userId)).thenReturn(user);

    UserDto returnedUserDto = userController.get(userId);

    assertEquals(userDto, returnedUserDto);
    verify(userService, times(1)).get(userId);
    verify(userMapperService, times(1)).convertToDto(user);
  }
}
