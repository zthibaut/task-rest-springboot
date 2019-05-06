package com.blackswandata.repository;

import com.blackswandata.domain.Task;
import com.blackswandata.enums.TaskStatusEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {
  @Query("Select t From Task t inner join t.user u where u.id = :userId and t.active = :active")
  Iterable<Task> findByUserId(@Param("userId") Long userId, @Param("active") boolean active);

  @Query("Select t From Task t inner join t.user u where u.id = :userId and t.id= :taskId")
  Optional<Task> findByUserIdAndId(@Param("userId") Long userId, @Param("taskId") Long taskId);

  @Query("Select t From Task t where t.status = :status and t.dateTime < :dateTimeCap")
  Iterable<Task> findByStatusDueInPast(@Param("status") TaskStatusEnum status, @Param("dateTimeCap") LocalDateTime dateTimeCap);
}
