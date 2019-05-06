package com.blackswandata.repository;

import com.blackswandata.domain.User;
import com.blackswandata.TasksApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.junit.Assert.*;

@WebAppConfiguration
@Rollback
@SpringBootTest(classes = { TasksApplication.class })
@RunWith(SpringRunner.class)

public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Before
  public void setUp() {
    userRepository.deleteAll();
  }

  @Test
  public void shouldSaveUser() {
    String username = "jsmith";
    String firstName = "John";
    String lastName = "Smith";
    User user = new User(username, firstName, lastName);

    Optional<User> userOnRecordOptional = userRepository.findByUsername(username);

    assertFalse(userOnRecordOptional.isPresent());
    userRepository.save(user);
    userOnRecordOptional = userRepository.findByUsername(username);
    assertTrue(userOnRecordOptional.isPresent());

    User userOnRecord = userOnRecordOptional.get();
    assertNotNull(userOnRecord.getId());
    assertEquals(firstName, userOnRecord.getFirstName());
    assertEquals(lastName, userOnRecord.getLastName());
    assertEquals(username, userOnRecord.getUsername());
  }
}
