package service;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldLoadEmptyFile() throws Exception {
        Path tempFile = tempDir.resolve("tempFile.csv");
        Files.createFile(tempFile);
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.getTasks().isEmpty());
    }

    @Test
    public void shouldLoadMultipleTasks() throws Exception {
        Path tempFile = tempDir.resolve("tempFile.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task1 = new Task(1, "Test task 1", "Description 1", TaskStatus.NEW);
        Task task2 = new Task(2, "Test task 2", "Description 2", TaskStatus.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(2, loadedManager.getTasks().size());
    }
}
