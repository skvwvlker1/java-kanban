package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.InvalidTaskException;
import exceptions.NotFoundException;
import model.Task;
import service.TaskManager;
import service.http.HttpTaskServer.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка запроса /tasks от клиента");
        try {
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    System.out.println("Началась обработка GET запроса /tasks от клиента");
                    handleGetMethod(exchange);
                    break;
                case "POST":
                    System.out.println("Началась обработка POST запроса /tasks от клиента");
                    handlePostMethod(exchange);
                    break;
                case "DELETE":
                    System.out.println("Началась обработка DELETE запроса /tasks от клиента");
                    handleDeleteMethod(exchange);
                    break;
                default:
                    exchange.sendResponseHeaders(405, 0);
                    break;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleGetMethod(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        if (id == null) {
            List<Task> tasks = taskManager.getTasks();
            String response = HttpTaskServer.getGson().toJson(tasks);
            sendText(exchange, response, 200);
        } else {
            try {
                Task task = taskManager.getTaskById(id);
                String response = HttpTaskServer.getGson().toJson(task);
                sendText(exchange, response, 200);
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Задача не найдена");
            }
        }
    }

    private void handlePostMethod(HttpExchange exchange) throws IOException {
        InputStream stream = exchange.getRequestBody();
        String body = new String(stream.readAllBytes());
        Task task = HttpTaskServer.getGson().fromJson(body, Task.class);
        try {
            if (task.getTaskId() != null) {
                taskManager.updateTask(task);
                sendText(exchange, "Задача обновлена", 200);
            } else {
                taskManager.addTask(task);
                sendText(exchange, "Задача создана", 201);
            }
        } catch (InvalidTaskException e) {
            sendHasInteractions(exchange, "Задача конфликтует с другой задачей");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void handleDeleteMethod(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        try {
            if (id != null) {
                taskManager.deleteTaskById(id);
                sendText(exchange, "Задача удалена", 200);
            } else {
                taskManager.clearTasks();
                sendText(exchange, "Все Задачи удалены", 200);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Задача не найдена");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
}