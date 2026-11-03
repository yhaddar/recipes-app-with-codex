package com.recipemanager.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskRepository extends JpaRepository<UserTask, UUID> {

    List<UserTask> findAllByOwnerId(UUID ownerId);
}
