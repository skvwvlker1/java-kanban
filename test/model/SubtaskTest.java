package model;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SubtaskTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void testEqualityBySelf() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1 Эпик 1", "Описание подзадачи 1", TaskStatus.NEW, epic.getTaskId(), Duration.ofMinutes(15), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Подзадача 1 Эпик 1", "Описание подзадачи 1", TaskStatus.NEW, epic.getTaskId(), Duration.ofMinutes(80), LocalDateTime.now().plusMinutes(60));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);


        assertEquals(subtask1, subtask1);
    }

    @Test
    public void testSubtasksNotEqual() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1 Эпик 1", "Описание подзадачи 1", TaskStatus.NEW, epic.getTaskId(), Duration.ofMinutes(80), LocalDateTime.now().plusMinutes(60));
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, epic.getTaskId(), Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.addSubtask(subtask2);

        assertNotEquals(subtask1, subtask2);
    }

    @Test
    public void testSubtasksNotEqualByTaskId() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1 Эпик 1", "Описание подзадачи 1", TaskStatus.NEW, epic.getTaskId(), Duration.ofMinutes(80), LocalDateTime.now().plusMinutes(60));
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, epic.getTaskId(), Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.addSubtask(subtask2);

        assertNotEquals(subtask1.getTaskId(), subtask2.getTaskId());

    }

    @Test
    public void testSubtasksEqualByEpicId() {

        Epic epic = new Epic("Эпик", "описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1 Эпик 1", "Описание подзадачи 1", TaskStatus.NEW, epic.getTaskId(), Duration.ofMinutes(80), LocalDateTime.now().plusMinutes(60));
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", TaskStatus.NEW, epic.getTaskId(), Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.addSubtask(subtask2);

        assertEquals(subtask1.getEpicId(), subtask2.getEpicId());

    }
}