package service.http.HttpTaskServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;
import service.http.adapter.DurationAdapter;
import service.http.adapter.LocalDateTimeAdapter;
import service.http.adapter.TaskStatusAdapter;
import service.http.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.httpServer.createContext("/tasks", new TaskHandler(taskManager));
        this.httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        this.httpServer.createContext("/epics", new EpicHandler(taskManager));
        this.httpServer.createContext("/history", new HistoryHandler(taskManager));
        this.httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(TaskStatus.class, new TaskStatusAdapter());
        return gsonBuilder.create();
    }

    public void start() {
        System.out.println("Сервер запущен на порту " + PORT);
        System.out.println("http://localhost:" + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановлен сервер на порту: " + PORT);
    }
}
