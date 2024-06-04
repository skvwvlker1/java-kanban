package test.service;

import model.*;
import service.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    public void testAddTaskToHistory() {
        Task task = new Task("task1", "task1 desc", TaskStatus.NEW);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    public void testRemoveTaskFromHistory() {
        Task task = new Task("task1", "task1 desc", TaskStatus.NEW);
        historyManager.add(task);
        historyManager.remove(task.getTaskId());
        assertTrue(historyManager.getHistory().isEmpty());
    }
    @Test
    public void testNoDuplicatesInHistory() {
        Task task = new Task("task1", "task1 desc", TaskStatus.NEW);
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }
}