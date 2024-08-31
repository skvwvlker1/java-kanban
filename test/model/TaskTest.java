package model;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void testEqualBySelf() {

        Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        taskManager.addTask(task);

        assertEquals(task, task);
    }

    @Test
    public void testNotEqualBetweenTwoTasks() {

        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.IN_PROGRESS, Duration.ofMinutes(15), LocalDateTime.now().plusMinutes(20));
        taskManager.addTask(task2);

        assertNotEquals(task1, task2);
    }

    @Test
    public void testNotEqualBetweenTwoTasksById() {

        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.IN_PROGRESS, Duration.ofMinutes(15), LocalDateTime.now().plusMinutes(20));
        taskManager.addTask(task2);

        assertNotEquals(task1.getTaskId(), task2.getTaskId());
    }

    @Test
    public void testEqualTaskTitleBySelf() {
        Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        assertEquals("Задача 1", task.getTitle());
    }

    @Test
    public void testNotEqualByTitleDescriptionStatus() {
        Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        Integer trueId = task.getTaskId();

        task.setTitle("new task");
        task.setDescription("new desc");
        task.setStatus(TaskStatus.IN_PROGRESS);

        assertNotEquals("task", task.getTitle());
        assertNotEquals("desc", task.getDescription());
        assertNotEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(trueId, task.getTaskId());
    }

    @Test
    public void testEqualByTaskId() {
        Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        Integer trueId = task.getTaskId();

        assertEquals(trueId, task.getTaskId());
    }

}