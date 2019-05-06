package com.blackswandata.service;

import com.blackswandata.domain.User;

public interface UserService {
  User save(User user);

  User update(Long userId, User user);

  Iterable<User> listUsers();

  User get(Long userId);
}
