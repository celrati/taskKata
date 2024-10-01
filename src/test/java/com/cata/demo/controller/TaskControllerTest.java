package com.cata.demo.controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cata.demo.dto.TaskDTO;
import com.cata.demo.dto.TaskMapper;
import com.cata.demo.model.Task;
import com.cata.demo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for createTask method
    @Test
    void testCreateTask() throws Exception {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("Test Task");
        taskDTO.setDescription("Test Description");

        // Act & Assert
        mockMvc.perform(post("/api/manager/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("task is created to cart successfully."))
                .andDo(print());

        verify(taskService, times(1)).createTask(anyString(), anyString());
    }

    // Test for updateTask method
    @Test
    void testUpdateTask() throws Exception {
        // Arrange
        Long taskId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setStatus("IN_PROGRESS");

        // Act & Assert
        mockMvc.perform(put("/api/manager/task/{id}", taskId)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "secret"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andDo(print());

        verify(taskService, times(1)).updateTask(eq(taskId), anyString());
    }

    // Test for listTasks method
    @Test
    void testListTasks() throws Exception {
        // Arrange
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setName("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus("CREATE");
        tasks.add(task1);

        Task task2 = new Task();
        task2.setName("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus("IN_PROGRESS");
        tasks.add(task2);

        when(taskService.listTasks()).thenReturn(tasks);

        List<TaskDTO> taskDTOs = TaskMapper.taskstoTaskDTS(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/manager/task")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Task 1"))
                .andExpect(jsonPath("$[1].name").value("Task 2"))
                .andDo(print());

        verify(taskService, times(1)).listTasks();
    }
}
