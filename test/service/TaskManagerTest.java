package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void testAddAndRetrieveTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.IN_PROGRESS, Duration.ofMinutes(15), LocalDateTime.now().plusMinutes(20));
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskById(task.getTaskId()));
    }

    @Test
    void testEpicStatusCalculation() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1 Эпик 1", "Описание подзадачи 1", TaskStatus.NEW, epic1.getTaskId(), Duration.ofMinutes(20), LocalDateTime.now().plusHours(1));
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2 Эпик 1", "Описание подзадачи 2", TaskStatus.NEW, epic1.getTaskId(), Duration.ofMinutes(80), LocalDateTime.now().plusHours(2));
        taskManager.addSubtask(subtask2);

        assertEquals(TaskStatus.NEW, taskManager.getEpicById(epic1.getTaskId()).getStatus());

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateEpic(epic1);
        assertEquals(TaskStatus.DONE, taskManager.getEpicById(epic1.getTaskId()).getStatus());

        subtask2.setStatus(TaskStatus.NEW);
        taskManager.updateSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epic1.getTaskId()).getStatus());

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epic1.getTaskId()).getStatus());
    }


}