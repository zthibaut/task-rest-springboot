package com.blackswandata.mapper;

import com.blackswandata.domain.User;
import com.blackswandata.dto.UserDto;
import com.blackswandata.dto.UserUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class UserMapperServiceImpl implements UserMapperService {
  private final ModelMapper modelMapper;

  @Inject
  UserMapperServiceImpl(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public User convertToEntity(UserDto userDto) {
    return modelMapper.map(userDto, User.class);
  }

  @Override
  public UserDto convertToDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public User convertToEntity(UserUpdateDto userUpdateDto) {
    return modelMapper.map(userUpdateDto, User.class);
  }

  @Override
  public Iterable<UserDto> convertToDto(Iterable<User> users) {
    if(isNull(users)) {
      return new ArrayList<>();
    }
    List<UserDto> userDtos = new ArrayList<>();
    for (User user: users) {
      userDtos.add(convertToDto(user));
    }
    return userDtos;
  }
}
