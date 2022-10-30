package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;
import task.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    public TaskManager manager;
    private final HttpServer httpTaskServer;

    public HttpTaskServer() throws IOException {
        httpTaskServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpTaskServer.createContext("/tasks", this::tasks);
    }

    //public static void main(String[] args) throws IOException {
    //    new HttpTaskServer().start();
    //}
    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpTaskServer.start();
    }

    public void stop() {
        System.out.println("Сервер на порту " + PORT + " остановлен.");
        httpTaskServer.stop(1);
    }

    private void tasks(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String path = h.getRequestURI().getPath();
        String rawQuery = h.getRequestURI().getRawQuery();

        switch (method) {
            case "GET":
                String response;
                if (path.endsWith("/task/") && rawQuery == null) {
                    if (manager.getListOfAllTasks().isEmpty()) {
                        System.out.println("Список задач пустой");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes(DEFAULT_CHARSET));
                        }
                        return;
                    }
                    response = gson.toJson(manager.getListOfAllTasks());
                    System.out.println("Список задач успешно возвращен!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/task/") && rawQuery != null) {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /task/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    }
                    if (manager.getTaskById(Integer.parseInt(id)) == null) {
                        h.sendResponseHeaders(404, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Задача не найдена".getBytes());
                        }
                        return;
                    }
                    response = gson.toJson(manager.getTaskById(Integer.parseInt(id)));
                    System.out.println("Значение для ID " + id + " успешно возвращено!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else if (path.endsWith("/subtask/") && rawQuery == null) {
                    if (manager.getListOfAllSubtasks().isEmpty()) {
                        System.out.println("Список подзадач пустой");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes(DEFAULT_CHARSET));
                        }
                        return;
                    }
                    response = gson.toJson(manager.getListOfAllSubtasks());
                    System.out.println("Список подзадач успешно возвращен!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/subtask/") && rawQuery != null) {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /subtask/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    }
                    if (manager.getSubtaskById(Integer.parseInt(id)) == null) {
                        h.sendResponseHeaders(404, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Подзадача не найдена".getBytes());
                        }
                        return;
                    }
                    response = gson.toJson(manager.getSubtaskById(Integer.parseInt(id)));
                    System.out.println("Значение для ID " + id + " успешно возвращено!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else if (path.endsWith("/subtask/epic/") && rawQuery != null) {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /subtask/epic/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    }
                    if (manager.getEpicById(Integer.parseInt(id)) == null) {
                        h.sendResponseHeaders(404, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Эпик не найден".getBytes());
                        }
                        return;
                    }
                    if (manager.getAllSubtaskByEpic(manager.getEpicById(Integer.parseInt(id))) == null) {
                        h.sendResponseHeaders(404, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Список подзадач для эпика пустой".getBytes());
                        }
                        return;
                    }
                    response = gson.toJson(manager.getAllSubtaskByEpic(manager.getEpicById(Integer.parseInt(id))));
                    System.out.println("Значение для ID " + id + " успешно возвращено!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else if (path.endsWith("/epic/") && rawQuery == null) {
                    if (manager.getListOfAllEpic().isEmpty()) {
                        System.out.println("Список эпиков пустой");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes(DEFAULT_CHARSET));
                        }
                        return;
                    }
                    response = gson.toJson(manager.getListOfAllEpic());
                    System.out.println("Список эпиков успешно возвращен!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/epic/") && rawQuery != null) {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /epic/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    }
                    if (manager.getEpicById(Integer.parseInt(id)) == null) {
                        h.sendResponseHeaders(404, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Эпик не найден".getBytes());
                        }
                        return;
                    }
                    response = gson.toJson(manager.getEpicById(Integer.parseInt(id)));
                    System.out.println("Значение для ID " + id + " успешно возвращено!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else if (path.endsWith("/history")) {
                    if (manager.getHistory().isEmpty()) {
                        System.out.println("История пустая");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes(DEFAULT_CHARSET));
                        }
                        return;
                    }
                    response = gson.toJson(manager.getHistory());
                    System.out.println("История успешно возвращена");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/tasks/")) {
                    if (manager.getPrioritizedTasks().isEmpty()) {
                        System.out.println("Список приоритизированных задач пустой");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes(DEFAULT_CHARSET));
                        }
                        return;
                    }
                    response = gson.toJson(manager.getPrioritizedTasks());
                    System.out.println("Список приоритизированных задач успешно возвращен");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                }
                break;
            case "POST":
                if (path.endsWith("/task/")) {
                    InputStream inputStream = h.getRequestBody();
                    String jsonString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(jsonString, Task.class);
                    if (manager.getTaskById(task.getId()) == null) {
                        manager.createTask(task);
                    } else {
                        manager.updateTask(task);
                    }
                    h.sendResponseHeaders(201, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Задача создана".getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/subtask/")) {
                    InputStream inputStream = h.getRequestBody();
                    String jsonString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Subtask subtask = gson.fromJson(jsonString, Subtask.class);
                    if (manager.getSubtaskById(subtask.getId()) == null) {
                        manager.createSubtask(subtask);
                    } else {
                        manager.updateSubtask(subtask);
                    }
                    h.sendResponseHeaders(201, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Подзадача создана".getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/epic/")) {
                    InputStream inputStream = h.getRequestBody();
                    String jsonString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Epic epic = gson.fromJson(jsonString, Epic.class);
                    if (manager.getEpicById(epic.getId()) == null) {
                        manager.createEpic(epic);
                    } else {
                        manager.updateEpic(epic);
                    }
                    h.sendResponseHeaders(201, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Эпик создан".getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                }
                break;
            case "DELETE":
                if (path.endsWith("/task/") && rawQuery == null) {
                    manager.deleteAllTasks();
                    System.out.println("Все задачи успешно удалены!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Все задачи удалены".getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/task/") && rawQuery != null) {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /task/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    }
                    if (manager.getTaskById(Integer.parseInt(id)) == null) {
                        h.sendResponseHeaders(404, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Задача не найдена".getBytes());
                        }
                        return;
                    }
                    manager.deleteTaskById(Integer.parseInt(id));
                    System.out.println("Задача с ID " + id + " успешно удалена!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Задача успешно удалена".getBytes());
                    }
                } else if (path.endsWith("/subtask/") && rawQuery == null) {
                    manager.deleteAllSubtask();
                    System.out.println("Все подзадачи успешно удалены!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Все подзадачи удалены".getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/subtask/") && rawQuery != null) {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /subtask/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    }
                    if (manager.getSubtaskById(Integer.parseInt(id)) == null) {
                        h.sendResponseHeaders(404, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Подадача не найдена".getBytes());
                        }
                        return;
                    }
                    manager.deleteSubtaskById(Integer.parseInt(id));
                    System.out.println("Подзадача с ID " + id + " успешно удалена!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Подзадача успешно удалена".getBytes());
                    }
                } else if (path.endsWith("/epic/") && rawQuery == null) {
                    manager.deleteAllEpic();
                    System.out.println("Все эпики успешно удалены!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Все эпики удалены".getBytes(DEFAULT_CHARSET));
                    }
                } else if (path.endsWith("/epic/") && rawQuery != null) {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /epic/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    }
                    if (manager.getEpicById(Integer.parseInt(id)) == null) {
                        h.sendResponseHeaders(404, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("Эпик не найден".getBytes());
                        }
                        return;
                    }
                    manager.deleteEpicById(Integer.parseInt(id));
                    System.out.println("Епик с ID " + id + " успешно удален!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Эпик успешно удален".getBytes());
                    }
                } else {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                }
                break;
            default:
                System.out.println("/tasks не работает с запросом: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write("Not Found".getBytes(DEFAULT_CHARSET));
                }
        }
    }

    private static boolean isParsable(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getId(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery.substring("id=".length());
    }
}
