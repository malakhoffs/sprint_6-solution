package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;
import model.Epic;
import model.Subtask;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        try {
            switch (method) {
                case "GET" -> {
                    if (pathParts.length == 4) {
                        handleGetAllEpicSubTasks(exchange, pathParts[2]);
                    } else if (pathParts.length == 3) {
                        handleGetEpicById(exchange, pathParts[2]);
                    } else if (pathParts.length == 2) {
                        handleGetAllEpic(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                }
                case "POST" -> handlePostEpic(exchange);
                case "DELETE" -> {
                    if (pathParts.length == 3) {
                        handleDeleteEpicById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                }
                default -> sendText(exchange, "Метод не поддерживается", 404);
            }
        } catch (Exception e) {
            sendServerError(exchange, e.getMessage());
        }
    }

    private void handleGetAllEpicSubTasks(HttpExchange exchange, String taskIdStr) {
        try {
            int epicId = Integer.parseInt(taskIdStr);
            List<Subtask> allEpicSubtasks = taskManager.getEpicSubtasks(epicId);
            if (allEpicSubtasks == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(allEpicSubtasks);
                sendText(exchange, response, 200);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            sendNotFound(exchange);
        }
    }


    private void handleGetAllEpic(HttpExchange exchange) {
        try {
            String response = gson.toJson(taskManager.getEpics());
            sendText(exchange, response, 200);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            sendNotFound(exchange);
        }
    }

    private void handleGetEpicById(HttpExchange exchange, String taskIdStr) {
        try {
            int epicId = Integer.parseInt(taskIdStr);
            Epic epicTask = taskManager.getEpicTaskById(epicId);
            if (epicTask == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(epicTask, Epic.class);
                sendText(exchange, response, 200);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            sendNotFound(exchange);
        }
    }

    private void handlePostEpic(HttpExchange exchange) {
        try {
            InputStreamReader inputReader = new InputStreamReader(exchange.getRequestBody(),
                    StandardCharsets.UTF_8);
            Epic epicTask = gson.fromJson(inputReader, Epic.class);
            taskManager.addEpic(epicTask);
            sendText(exchange, "Задача добавлена", 201);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteEpicById(HttpExchange exchange, String taskIdStr) {
        try {
            int epicId = Integer.parseInt(taskIdStr);
            if (taskManager.getEpicTaskById(epicId) == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteEpicTask(epicId);
                sendText(exchange, "Задача удалена", 204);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}