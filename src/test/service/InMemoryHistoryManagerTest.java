package test.service;

import model.*;
import service.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    public void testAddAndRetrieveHistory() {
        Task task1 = new Task("задача 1", "описание 1", TaskStatus.NEW);
        historyManager.add(task1);
        Task task2 = new Task("задача 2", "описание 2", TaskStatus.IN_PROGRESS);
        historyManager.add(task2);
        Task task3 = new Task("задача 3", "описание 3", TaskStatus.DONE);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size());

        assertEquals(task3, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task1, history.get(2));
    }
}