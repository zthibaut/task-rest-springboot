package com.blackswandata.service;

import com.blackswandata.domain.User;
import com.blackswandata.exception.UserNotFoundException;
import com.blackswandata.exception.UsernameDuplicateException;
import com.blackswandata.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class UserServiceImpl extends CommonService implements UserService {
  private final UserRepository userRepository;

  @Inject
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User save(User user) {
    String username = user.getUsername();
    Optional<User> userByUsernameOptional = userRepository.findByUsername(username);
    if(userByUsernameOptional.isPresent()) {
      throw new UsernameDuplicateException(username);
    }
    return userRepository.save(user);
  }

  @Override
  public User update(Long userId, User user) {
    Optional<User> existingUserOptional = userRepository.findById(userId);
    if(!existingUserOptional.isPresent()) {
      throw new UserNotFoundException(userId);
    }
    User existingUser = existingUserOptional.get();
    return updateExistingUser(user, existingUser);
  }

  @Override
  public Iterable<User> listUsers() {
    return userRepository.findAll();
  }

  @Override
  public User get(Long userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    if(!userOptional.isPresent()) {
      throw new UserNotFoundException(userId);
    }
    return userOptional.get();
  }

  private User updateExistingUser(User user, User existingUser) {
    existingUser.setFirstName(ifNotNull(user.getFirstName(), existingUser.getFirstName()));
    existingUser.setLastName(ifNotNull(user.getLastName(), existingUser.getLastName()));

    return userRepository.save(existingUser);
  }

}
