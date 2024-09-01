package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import exceptions.TaskTimeValidateException;
import model.Subtask;
import service.TaskManager;
import service.http.HttpTaskServer.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка запроса /subtasks от клиента");
        try {
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    System.out.println("Началась обработка GET запроса /subtasks от клиента");
                    handleGetMethod(exchange);
                    break;
                case "POST":
                    System.out.println("Началась обработка POST запроса /subtasks от клиента");
                    handlePostMethod(exchange);
                    break;
                case "DELETE":
                    System.out.println("Началась обработка DELETE запроса /subtasks от клиента");
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
            List<Subtask> subtasks = taskManager.getSubtasks();
            String response = HttpTaskServer.getGson().toJson(subtasks);
            sendText(exchange, response, 200);
        } else {
            try {
                Subtask subtask = taskManager.getSubtaskById(id);
                String response = HttpTaskServer.getGson().toJson(subtask);
                sendText(exchange, response, 200);
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Подзадача не найдена");
            }
        }
    }

    private void handlePostMethod(HttpExchange exchange) throws IOException {
        InputStream stream = exchange.getRequestBody();
        String body = new String(stream.readAllBytes());
        Subtask subtask = HttpTaskServer.getGson().fromJson(body, Subtask.class);
        try {
            if (subtask.getTaskId() != null) {
                taskManager.updateSubtask(subtask);
                sendText(exchange, "Подзадача обновлена", 200);
            } else {
                taskManager.addSubtask(subtask);
                sendText(exchange, "Подзадача создана", 201);
            }
        } catch (TaskTimeValidateException e) {
            sendHasInteractions(exchange, "Подзача пересекается с другой подзадачей");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }

    private void handleDeleteMethod(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        try {
            if (id != null) {
                taskManager.deleteEpicById(id);
                sendText(exchange, "Подзадача удалена", 200);
            } else {
                taskManager.clearSubtasks();
                sendText(exchange, "Все Подзадача удалены", 200);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Подзадача не найдена");
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
}
