package com.blackswandata.controller;

import com.blackswandata.domain.User;
import com.blackswandata.dto.UserDto;
import com.blackswandata.dto.UserUpdateDto;
import com.blackswandata.mapper.UserMapperService;
import com.blackswandata.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json")
public class UserController {

  private final UserService userService;
  private final UserMapperService userMapperService;

  @Inject
  public UserController(UserService userService, UserMapperService userMapperService) {
    this.userMapperService = userMapperService;
    this.userService = userService;
  }

  @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
  public @Valid UserDto add(final @RequestBody @Valid UserDto userDto) {
    User user = convertToEntity(userDto);
    User savedUser = userService.save(user);
    return convertToDto(savedUser);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
  public UserDto update(final @PathVariable Long id, final @RequestBody UserUpdateDto userDto) {
    User user = convertToEntity(userDto);
    User updatedUser = userService.update(id, user);
    return convertToDto(updatedUser);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Iterable<UserDto> listAll() {
    return convertToDto(userService.listUsers());
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public UserDto get(final @PathVariable Long id) {
    return convertToDto(userService.get(id));
  }

  private UserDto convertToDto(User user) {
    return userMapperService.convertToDto(user);
  }

  private User convertToEntity(UserDto userDto) {
    return userMapperService.convertToEntity(userDto);
  }

  private User convertToEntity(UserUpdateDto userDto) {
    return userMapperService.convertToEntity(userDto);
  }

  private Iterable<UserDto> convertToDto(Iterable<User> users) {
    return userMapperService.convertToDto(users);
  }

}