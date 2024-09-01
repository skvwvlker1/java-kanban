package http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import service.InMemoryTaskManager;
import service.TaskManager;
import service.http.HttpTaskServer.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTasksTest {
    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;
    private HttpClient client;

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpics();
        taskServer.start();
        gson = HttpTaskServer.getGson();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 10xx00", "Описание задачи 133", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.now());
        manager.addTask(task);

        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();



        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Задача 10xx00", tasksFromManager.get(0).getTitle());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 10xx00", "Описание задачи 133", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.now());
        manager.addTask(task);
        Task task1 = new Task(task.getTaskId(),"Обновленная задача", "Обновленное описание", TaskStatus.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(10),Duration.ofMinutes(20));
        String taskJson = gson.toJson(task1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Обновленная задача", tasksFromManager.get(0).getTitle());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Testing task",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getTaskId()))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();
        assertTrue(tasksFromManager.isEmpty());
    }

    @Test
    public void testTaskById() throws IOException, InterruptedException {
        Task task = new Task("Задача 10xx00", "Описание задачи 133", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.now());
        manager.addTask(task);

        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager);
        assertEquals(1, tasksFromManager.size());
        assertEquals("Задача 10xx00", tasksFromManager.get(0).getTitle());
    }
}
