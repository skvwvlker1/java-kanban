package service;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    @TempDir
    Path tempDir;

    @Test
    public void shouldSaveAndLoadTasks() throws Exception {
        Path tempFile = tempDir.resolve("tempFile.csv");
        try {
            Files.createFile(tempFile);
            FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

            Task task = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
            manager.addTask(task);

            FileBackedTaskManager load = new FileBackedTaskManager(tempFile);

            assertEquals(1, load.getTasks().size());
        } catch (Exception e) {
            throw e;
        }
    }
}
