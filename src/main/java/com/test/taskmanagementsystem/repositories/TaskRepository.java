package com.test.taskmanagementsystem.repositories;

import com.test.taskmanagementsystem.models.Task;
import com.test.taskmanagementsystem.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthor(User author);
    List<Task> findByExecutor(User executor);
    List<Task> findByAuthorAndExecutor(User author, User assignee);
}

