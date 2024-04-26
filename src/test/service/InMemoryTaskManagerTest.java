package test.service;

import model.*;
import service.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void testEqualTasksSelf() {


        Task task = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        taskManager.addTask(task);
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask);

        assertEquals(task, taskManager.getTaskById(task.getTaskId()));
        assertEquals(subtask, taskManager.getSubtaskById(subtask.getTaskId()));
        assertEquals(epic, taskManager.getEpicById(epic.getTaskId()));
    }

    @Test
    public void testIdConflict() {

        Task taskSetId = new Task("Задача", "Описание", TaskStatus.NEW);
        taskSetId.setTaskId(1);
        Task taskUnSetId = new Task("Задача без инициализации id", "Описание", TaskStatus.NEW);

        taskManager.addTask(taskSetId);
        taskManager.addTask(taskUnSetId);

        assertNotEquals(taskSetId.getTaskId(), taskUnSetId.getTaskId());
    }

    @Test
    public void testDeleteSubtaskById() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("подзадачи", "описание подзадачи", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask);
        int subtaskId = subtask.getTaskId();

        assertTrue(taskManager.getSubtasks().contains(subtask));
        taskManager.deleteSubtaskById(subtaskId);
        assertFalse(taskManager.getSubtasks().contains(subtask));
    }
}