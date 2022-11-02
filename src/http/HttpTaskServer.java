package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import http.kv.server.KVServer;
import managers.TaskManager;
import managers.http.HttpTaskManager;
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
    private final Gson gson = new Gson();
    private final TaskManager manager; // = new HttpTaskManager("http://localhost:8078/");
    private final HttpServer httpTaskServer;

    /*public static void main(String[] args) throws IOException {
        final KVServer kvServer = new KVServer();
        new HttpTaskServer().start();
        kvServer.start();
    }*/

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpTaskServer.start();
    }

    public void stop() {
        System.out.println("Сервер на порту " + PORT + " остановлен.");
        httpTaskServer.stop(1);
    }

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        httpTaskServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpTaskServer.createContext("/tasks/task/", this::tasks);
        httpTaskServer.createContext("/tasks/subtask/", this::subtasks);
        httpTaskServer.createContext("/tasks/epic/", this::epic);
        httpTaskServer.createContext("/tasks/subtask/epic/", this::subtasksEpic);
        httpTaskServer.createContext("/tasks/history", this::history);
        httpTaskServer.createContext("/tasks/", this::prioritizedTasks);
    }

    private void tasks(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String path = h.getRequestURI().getPath();
        String rawQuery = h.getRequestURI().getRawQuery();

        switch (method) {
            case "GET":
                String response;
                if (!path.equals("/tasks/task/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
                if (rawQuery == null) {
                    response = gson.toJson(manager.getListOfAllTasks());
                    if (manager.getListOfAllTasks().isEmpty()) {
                        System.out.println("Список задач пустой");
                    } else {
                        System.out.println("Список задач успешно возвращен!");
                    }
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /tasks/task/?id={id}");
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
                }
                break;
            case "POST":
                if (!path.equals("/tasks/task/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
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
                break;
            case "DELETE":
                if (!path.equals("/tasks/task/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
                if (rawQuery == null) {
                    manager.deleteAllTasks();
                    System.out.println("Все задачи успешно удалены!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Все задачи удалены".getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /tasks/task/?id={id}");
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
                }
                break;
            default:
                System.out.println("/tasks/task/ не работает с запросом: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write("Not Found".getBytes(DEFAULT_CHARSET));
                }
        }
    }

    private void subtasks(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String path = h.getRequestURI().getPath();
        String rawQuery = h.getRequestURI().getRawQuery();

        switch (method) {
            case "GET":
                String response;
                if (!path.equals("/tasks/subtask/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
                if (rawQuery == null) {
                    response = gson.toJson(manager.getListOfAllSubtasks());
                    if (manager.getListOfAllSubtasks().isEmpty()) {
                        System.out.println("Список подзадач пустой");
                    } else {
                        System.out.println("Список подзадач успешно возвращен!");
                    }
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /tasks/subtask/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    } else if (manager.getSubtaskById(Integer.parseInt(id)) == null) {
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
                }
                break;
            case "POST":
                if (!path.equals("/tasks/subtask/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
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
                break;
            case "DELETE":
                if (!path.equals("/tasks/subtask/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
                if (rawQuery == null) {
                    manager.deleteAllSubtask();
                    System.out.println("Все подзадачи успешно удалены!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Все подзадачи удалены".getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /tasks/subtask/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    } else if (manager.getSubtaskById(Integer.parseInt(id)) == null) {
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
                }
                break;
            default:
                System.out.println("/tasks/subtask/ не работает с запросом: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write("Not Found".getBytes(DEFAULT_CHARSET));
                }
        }
    }

    private void epic(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String path = h.getRequestURI().getPath();
        String rawQuery = h.getRequestURI().getRawQuery();
        switch (method) {
            case "GET":
                String response;
                if (!path.equals("/tasks/epic/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
                if (rawQuery == null) {
                    response = gson.toJson(manager.getListOfAllEpic());
                    if (manager.getListOfAllEpic().isEmpty()) {
                        System.out.println("Список эпиков пустой");
                    } else {
                        System.out.println("Список эпиков успешно возвращен!");
                    }
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /tasks/epic/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    } else if (manager.getEpicById(Integer.parseInt(id)) == null) {
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
                }
                break;
            case "POST":
                if (!path.equals("/tasks/epic/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
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
                break;
            case "DELETE":
                if (!path.equals("/tasks/epic/")) {
                    System.out.println("Not Found. На этом пути: " + path);
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Not Found".getBytes(DEFAULT_CHARSET));
                    }
                    return;
                }
                if (rawQuery == null) {
                    manager.deleteAllEpic();
                    System.out.println("Все эпики успешно удалены!");
                    h.sendResponseHeaders(200, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Все эпики удалены".getBytes(DEFAULT_CHARSET));
                    }
                } else {
                    String id = getId(h);
                    if (id.isEmpty() || !isParsable(id)) {
                        System.out.println("Не верно указан ID, указывается в пути числом: /epic/?id={id}");
                        h.sendResponseHeaders(400, 0);
                        try (OutputStream os = h.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        return;
                    } else if (manager.getEpicById(Integer.parseInt(id)) == null) {
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
                }
                break;
            default:
                System.out.println("/tasks/epic/ не работает с запросом: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write("Not Found".getBytes(DEFAULT_CHARSET));
                }
        }
    }

    private void subtasksEpic(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String path = h.getRequestURI().getPath();
        String rawQuery = h.getRequestURI().getRawQuery();

        if ("GET".equals(method)) {
            String response;
            if (!path.equals("/tasks/subtask/epic/")) {
                System.out.println("Not Found. На этом пути: " + path);
                h.sendResponseHeaders(404, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write("Not Found".getBytes(DEFAULT_CHARSET));
                }
                return;
            }
            if (rawQuery != null) {
                String id = getId(h);
                if (id.isEmpty() || !isParsable(id)) {
                    System.out.println("Не верно указан ID, указывается в пути числом: /tasks/subtask/epic/?id={id}");
                    h.sendResponseHeaders(400, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("".getBytes());
                    }
                    return;
                } else if (manager.getEpicById(Integer.parseInt(id)) == null) {
                    h.sendResponseHeaders(404, 0);
                    try (OutputStream os = h.getResponseBody()) {
                        os.write("Эпик не найден".getBytes());
                    }
                    return;
                }
                response = gson.toJson(manager.getAllSubtaskByEpic(manager.getEpicById(Integer.parseInt(id))));
                if (manager.getAllSubtaskByEpic(manager.getEpicById(Integer.parseInt(id))).isEmpty()) {
                    System.out.println("Список подзадач для эпика пуст");
                } else {
                    System.out.println("Значение для ID " + id + " успешно возвращено!");
                }
                h.sendResponseHeaders(200, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                System.out.println("Не верно введен путь, нужно указывать в формате: /tasks/subtask/epic/?id={id}");
                h.sendResponseHeaders(400, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write("".getBytes());
                }
            }
        } else {
            System.out.println("/tasks/subtask/epic/ не работает с запросом: " + h.getRequestMethod());
            h.sendResponseHeaders(405, 0);
            try (OutputStream os = h.getResponseBody()) {
                os.write("Not Found".getBytes(DEFAULT_CHARSET));
            }
        }
    }

    private void history(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String path = h.getRequestURI().getPath();

        if ("GET".equals(method)) {
            String response;
            if (!path.equals("/tasks/history")) {
                System.out.println("Not Found. На этом пути: " + path);
                h.sendResponseHeaders(404, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write("Not Found".getBytes(DEFAULT_CHARSET));
                }
                return;
            }
            response = gson.toJson(manager.getHistory());
            if (manager.getHistory().isEmpty()) {
                System.out.println("Пустая история успешно возвращена");
            } else {
                System.out.println("История успешно возвращена");
            }
            h.sendResponseHeaders(200, 0);
            try (OutputStream os = h.getResponseBody()) {
                os.write(response.getBytes(DEFAULT_CHARSET));
            }
        } else {
            System.out.println("/tasks/history не работает с запросом: " + h.getRequestMethod());
            h.sendResponseHeaders(405, 0);
            try (OutputStream os = h.getResponseBody()) {
                os.write("Not Found".getBytes(DEFAULT_CHARSET));
            }
        }
    }

    private void prioritizedTasks(HttpExchange h) throws IOException {
        String method = h.getRequestMethod();
        String path = h.getRequestURI().getPath();

        if ("GET".equals(method)) {
            String response;
            if (!path.equals("/tasks/")) {
                System.out.println("Not Found. На этом пути: " + path);
                h.sendResponseHeaders(404, 0);
                try (OutputStream os = h.getResponseBody()) {
                    os.write("Not Found".getBytes(DEFAULT_CHARSET));
                }
                return;
            }
            response = gson.toJson(manager.getPrioritizedTasks());
            if (manager.getPrioritizedTasks().isEmpty()) {
                System.out.println("Список приоритизированных задач пустой");
            } else {
                System.out.println("Список приоритизированных задач успешно возвращен");
            }
            h.sendResponseHeaders(200, 0);
            try (OutputStream os = h.getResponseBody()) {
                os.write(response.getBytes(DEFAULT_CHARSET));
            }
        } else {
            System.out.println("/tasks/history не работает с запросом: " + h.getRequestMethod());
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
