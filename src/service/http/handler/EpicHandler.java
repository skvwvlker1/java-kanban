package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import exceptions.InvalidTaskException;
import exceptions.NotFoundException;
import model.Epic;
import model.Subtask;
import service.TaskManager;
import service.http.HttpTaskServer.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка запроса /epics от клиента");
        try {
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    System.out.println("Началась обработка GET запроса /epics от клиента");
                    handleGetMethod(exchange);
                    break;
                case "POST":
                    System.out.println("Началась обработка POST запроса /epics от клиента");
                    handlePostMethod(exchange);
                    break;
                case "DELETE":
                    System.out.println("Началась обработка DELETE запроса /epics от клиента");
                    handleDeleteMethod(exchange);
                    break;
                default:
                    System.out.println("Ждем метод GET, POST или DELETE, а получили" + requestMethod);
                    exchange.sendResponseHeaders(405, 0);
                    break;
            }
        } catch (RuntimeException exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void handleGetMethod(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        try {
            if (Pattern.matches("^/subtasks$", exchange.getRequestURI().getPath())) {
                try {
                    List<Subtask> subtaskByEpic = taskManager.getSubtasksByEpicId(id);
                    if (subtaskByEpic.isEmpty()) {
                        sendNotFound(exchange, "Подзадачи эпика не найдены");
                    } else {
                        String response = HttpTaskServer.getGson().toJson(subtaskByEpic);
                        sendText(exchange, response, 200);
                    }
                } catch (NotFoundException exception) {
                    sendNotFound(exchange, "Подзадачи эпика не найдены");
                }
            } else {
                if (id == null) {
                    List<Epic> epics = taskManager.getEpics();
                    String response = HttpTaskServer.getGson().toJson(epics);
                    sendText(exchange, response, 200);
                } else {
                    Epic epic = taskManager.getEpicById(id);
                    String response = HttpTaskServer.getGson().toJson(epic);
                    sendText(exchange, response, 200);
                }
            }
        } catch (NotFoundException exception) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }

    public void handlePostMethod(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        InputStream stream = exchange.getRequestBody();
        String body = new String(stream.readAllBytes());
        try {
            if (id != null) {
                Epic epic = HttpTaskServer.getGson().fromJson(body, Epic.class);
                taskManager.updateEpic(epic);
                sendText(exchange, "Эпик успешно обновлен", 200);
            } else {
                Epic epic = HttpTaskServer.getGson().fromJson(body, Epic.class);
                taskManager.addEpic(epic);
                sendText(exchange, "Эпик успешно создан", 201);
            }
        } catch (InvalidTaskException exception) {
            sendHasInteractions(exchange, "Эпик пересекается с другой задачей");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }

    public void handleDeleteMethod(HttpExchange exchange) throws IOException {
        Integer id = getIdFromPath(exchange.getRequestURI().getPath());
        try {
            if (id != null) {
                taskManager.deleteEpicById(id);
                System.out.println("Удален эпик с id: " + id);
                sendText(exchange, "Эпик успешно удален", 200);
            } else {
                taskManager.clearEpics();
                System.out.println("Удалены все эпики");
                sendText(exchange, "Все эпики успешно удалены", 200);
            }
        } catch (NotFoundException exception) {
            sendNotFound(exchange, "Эпик не найден");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }
}
