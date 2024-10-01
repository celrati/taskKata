package com.cata.demo.service;

import com.cata.demo.model.Task;
import com.cata.demo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask() {
        // Arrange
        String name = "Test Task";
        String description = "Test Description";

        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setStatus("CREATE");

        when(taskRepository.save(any())).thenReturn(task);

        // Act
        taskService.createTask(name, description);

        // Assert
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask() {
        Long taskId = 1L;
        String status = "IN_PROGRESS";

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus("CREATE");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Act
        taskService.updateTask(taskId, status);

        // Assert
        assertEquals(status, existingTask.getStatus());
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void listTasks() {
        List<Task> taskList = new ArrayList<>();
        Task task1 = new Task();
        task1.setName("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus("CREATE");

        Task task2 = new Task();
        task2.setName("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus("IN_PROGRESS");

        taskList.add(task1);
        taskList.add(task2);

        when(taskRepository.findAll()).thenReturn(taskList);

        // Act
        List<Task> result = taskService.listTasks();

        // Assert
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }
}