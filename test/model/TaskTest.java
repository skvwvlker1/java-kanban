package model;

import model.*;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void testEqualBySelf() {

        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task);

        assertEquals(task, task);
    }

    @Test
    public void testNotEqualBetweenTwoTasks() {

        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW);
        taskManager.addTask(task2);

        assertNotEquals(task1, task2);
    }

    @Test
    public void testNotEqualBetweenTwoTasksById() {

        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW);
        taskManager.addTask(task2);

        assertNotEquals(task1.getTaskId(), task2.getTaskId());
    }

    @Test
    public void testEqualTaskTitleBySelf() {
        Task task = new Task("Задача", "Описание", TaskStatus.NEW);

        assertEquals("Задача", task.getTitle());
    }

    @Test
    public void testNotEqualByTitleDescriptionStatus() {
        Task task = new Task("Задача", "Описание", TaskStatus.NEW);
        int trueId = task.getTaskId();

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
        Task task = new Task("Задача", "Описание", TaskStatus.NEW);
        int trueId = task.getTaskId();

        assertEquals(trueId, task.getTaskId());
    }

}