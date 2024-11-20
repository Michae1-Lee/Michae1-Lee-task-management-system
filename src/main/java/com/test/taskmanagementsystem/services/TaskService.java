package com.test.taskmanagementsystem.services;

import com.test.taskmanagementsystem.dto.TaskDto;
import com.test.taskmanagementsystem.exceptions.AuthorNotFound;
import com.test.taskmanagementsystem.exceptions.ExecutorNotFound;
import com.test.taskmanagementsystem.exceptions.TaskNotFound;
import com.test.taskmanagementsystem.exceptions.UserNotFound;
import com.test.taskmanagementsystem.models.Task;
import com.test.taskmanagementsystem.models.User;
import com.test.taskmanagementsystem.repositories.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository,UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> getTasks(User author, User executor) {
        if (author == null && executor == null) {
            return taskRepository.findAll();
        }

        if (author != null && executor == null) {
            return taskRepository.findByAuthor(author);
        }

        if (author == null) {
            return taskRepository.findByExecutor(executor);
        }

        return taskRepository.findByAuthorAndExecutor(author, executor);
    }

    public TaskDto createTask(TaskDto taskDto) {
        User author;
        User executor;

        try {
            author = userService.getUserByUsername(taskDto.getAuthor());
        } catch (UserNotFound e) {
            throw new AuthorNotFound();
        }

        try {
            executor = userService.getUserByUsername(taskDto.getExecutor());
        } catch (UserNotFound e) {
            throw new ExecutorNotFound();
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setAuthor(author);
        task.setExecutor(executor);

        Task savedTask = taskRepository.save(task);

        TaskDto savedTaskDto = new TaskDto();
        savedTaskDto.setTitle(savedTask.getTitle());
        savedTaskDto.setDescription(savedTask.getDescription());
        savedTaskDto.setStatus(savedTask.getStatus());
        savedTaskDto.setPriority(savedTask.getPriority());
        savedTaskDto.setAuthor(savedTask.getAuthor().getUsername());
        savedTaskDto.setExecutor(savedTask.getExecutor().getUsername());

        return savedTaskDto;
    }

    public void deleteTask(Long taskId) {
        taskRepository.findById(taskId).orElseThrow(TaskNotFound::new);
        taskRepository.deleteById(taskId);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(TaskNotFound::new);
    }

    public Task updateTask(Long taskId, TaskDto task) {

        Task newTask = taskRepository.findById(taskId).orElseThrow(TaskNotFound::new);
        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setPriority(task.getPriority());
        newTask.setAuthor(userService.getUserByUsername(task.getAuthor()));
        newTask.setExecutor(userService.getUserByUsername(task.getExecutor()));
        newTask.setPriority(task.getPriority());

        return taskRepository.save(newTask);
    }
    public Task updateTaskStatus(Long taskId, String status, String username) {
        Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFound::new);

        if (!task.getExecutor().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not the executor of this task");
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }
}
