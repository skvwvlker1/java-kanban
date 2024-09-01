package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;
import service.http.HttpTaskServer.HttpTaskServer;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetMethod(exchange);
        } else {
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        }
    }

    private void handleGetMethod(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        String response = HttpTaskServer.getGson().toJson(history);
        sendText(exchange, response, 200);
    }
}
