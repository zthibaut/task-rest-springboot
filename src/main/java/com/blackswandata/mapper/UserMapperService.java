package com.blackswandata.mapper;

import com.blackswandata.domain.User;
import com.blackswandata.dto.UserDto;
import com.blackswandata.dto.UserUpdateDto;

public interface UserMapperService {
  User convertToEntity(UserDto userDto);

  UserDto convertToDto(User user);

  User convertToEntity(UserUpdateDto userUpdateDto);

  Iterable<UserDto> convertToDto(Iterable<User> users);
}
