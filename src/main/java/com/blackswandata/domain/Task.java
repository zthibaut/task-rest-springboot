package com.blackswandata.domain;

import com.blackswandata.enums.TaskStatusEnum;
import com.blackswandata.validator.TaskStatusValidator;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private LocalDateTime dateTime;

  @Getter(AccessLevel.NONE)
  private Boolean active;

  @Enumerated(EnumType.STRING)
  @TaskStatusValidator
  private TaskStatusEnum status;

  @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
  @JsonBackReference(value="user-task")
  @Setter(AccessLevel.NONE)
  private User user;

  Task() {
    this.active = true;
    this.status = TaskStatusEnum.pending;
  }

  public Task(User user, String name, String description, LocalDateTime dateTime) {
    this();
    this.user = user;
    this.name = name;
    this.description = description;
    this.dateTime = dateTime;
  }

  public void setUser(User user) {
    if (sameAsFormer(user)) {
      return;
    }
    User oldUser = this.user;
    this.user = user;
    if (oldUser!=null) {
      oldUser.removeTask(this);
    }

    if (user!=null) {
      user.addTask(this);
    }
  }

  private boolean sameAsFormer(User newUser) {
    return this.user==null? newUser==null : this.user.equals(newUser);
  }

  public Boolean isActive() {
    return active;
  }
}
