package test.model;

import model.*;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void testEqualityBySelf() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask2);


        assertEquals(subtask1, subtask1);
    }

    @Test
    public void testSubtasksNotEqual() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask2);

        assertNotEquals(subtask1, subtask2);
    }

    @Test
    public void testSubtasksNotEqualByTaskId() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask2);

        assertNotEquals(subtask1.getTaskId(), subtask2.getTaskId());

    }

    @Test
    public void testSubtasksEqualByEpicId() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, epic.getTaskId());
        taskManager.addSubtask(subtask2);

        assertEquals(subtask1.getEpicId(), subtask2.getEpicId());

    }
}