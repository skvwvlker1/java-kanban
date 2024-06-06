package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import service.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void setup() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

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
    public void testAddTask() {
        Task task = new Task("task1", "task1 desc", TaskStatus.NEW);
        taskManager.addTask(task);
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    public void testDeleteTaskById() {
        Task task = new Task("test", "task1 desc", TaskStatus.NEW);
        taskManager.addTask(task);
        taskManager.deleteTaskById(task.getTaskId());
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    public void testHistoryManager() {
        Task task = new Task("task1", "task1 desc", TaskStatus.NEW);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    public void testRemoveFromHistory() {
        Task task = new Task("task1", "task1 desc", TaskStatus.NEW);
        historyManager.add(task);
        historyManager.remove(task.getTaskId());
        assertTrue(historyManager.getHistory().isEmpty());
    }
}