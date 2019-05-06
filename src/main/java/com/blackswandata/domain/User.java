package com.blackswandata.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

@Data
@Entity
@EqualsAndHashCode(exclude = { "tasks"}, callSuper=false)
@ToString(exclude = { "tasks"})
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String firstName;
  private String lastName;

  @OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
  @JsonManagedReference(value="user-task")
  private Set<Task> tasks;

  public  User() {
    if (isNull(this.tasks)) {
      this.tasks = new HashSet<>();
    }
  }

  public User(String username, String firstName, String lastName) {
    this();
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void addTask(Task task) {
    if (this.tasks.contains(task)) {
      return;
    }
    this.tasks.add(task);
    task.setUser(this);
  }

  public void removeTask(Task task) {
    if (!this.tasks.contains(task)) {
      return;
    }
    this.tasks.remove(task);
    task.setUser(null);
  }
}
