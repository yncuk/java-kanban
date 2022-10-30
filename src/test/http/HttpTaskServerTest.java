package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.kv.server.KVServer;
import managers.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest extends TaskManagerTest<HttpTaskManager> {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    HttpTaskServer httpTaskServer = new HttpTaskServer();
    KVServer server = new KVServer();
    String URL = "http://localhost:8080";

    HttpTaskServerTest() throws IOException {
    }


    @BeforeEach
    void setUp() {
        httpTaskServer.start();
        server.start();
        manager = (HttpTaskManager) (httpTaskServer.manager = new HttpTaskManager("http://localhost:8078/"));
    }

    @Test
    @DisplayName("Test GET then list tasks is empty")
    void testGetThenListTasksIsEmpty() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET tasks then list tasks no empty")
    void testGetTasksThenListTasksNoEmpty() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getListOfAllTasks()), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET subtasks then list subtasks is empty")
    void testGetSubtasksThenListSubtasksIsEmpty() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET subtasks then list subtasks no empty")
    void testGetSubtasksThenListSubtasksNoEmpty() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getListOfAllSubtasks()), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET epic then list epic is empty")
    void testGetEpicThenListEpicIsEmpty() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET epic then list epic no empty")
    void testGetEpicThenListEpicNoEmpty() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getListOfAllEpic()), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then id = 1 and list tasks is empty")
    void testGetTaskThenIdIs1AndListTasksIsEmpty() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/?id=1"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Задача не найдена", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then id = 4 but 4 tasks not found")
    void testGetTaskThenIdIs4But4TaskNoExists() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/?id=4"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Задача не найдена", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then id = 1 but 1 subtasks not found")
    void testGetSubtaskThenIdIs1But1SubtaskNoExists() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/?id=1"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Подзадача не найдена", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then id = 1 but 1 tasks not found")
    void testGetEpicThenIdIs1But1EpicNoExists() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/?id=1"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Эпик не найден", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then task id = 1 and the first task exists")
    void testGetTaskThenIdIs1And1TaskExists() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/?id=1"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getTaskById(1)), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then subtask id = 4 and the 4 subtask exists")
    void testGetSubtaskThenIdIs4And4SubtaskExists() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/?id=4"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getSubtaskById(4)), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then epic id = 3 and the 3 epic exists")
    void testGetEpicThenIdIs3And3EpicExists() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/?id=3"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getEpicById(3)), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then id task is wrong")
    void testGetTaskThenIdIsWrong() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/?id=k"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then id subtask is wrong")
    void testGetSubtaskThenIdIsWrong() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/?id=k"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then id epic is wrong")
    void testGetEpicThenIdIsWrong() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/?id=k"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET epic subtasks then id epic is wrong")
    void testGetEpicSubtasksThenIdIsWrong() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/epic/?id=k"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET epic subtasks then epic id = 4 and 4 epic no exists")
    void testGetEpicSubtasksThenIdEpicIs4AndNoExists() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/epic/?id=4"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Эпик не найден", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET epic subtasks then epic id = 3 and 3 epic exists")
    void testGetEpicSubtasksThenIdEpicIs3AndEpicExists() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/epic/?id=3"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getAllSubtaskByEpic(manager.getEpicById(3))), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET history then history is empty")
    void testGetHistoryThenHistoryIsEmpty() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/history"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET history then history is no empty")
    void testGetHistoryThenHistoryIsNoEmpty() {
        initStandardBehavior();
        manager.getTaskById(1);
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/history"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getHistory()), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET prioritized tasks then history is empty")
    void testGetPrioritizedTasksThenHistoryIsEmpty() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET prioritized tasks then history is no empty")
    void testGetPrioritizedTasksThenHistoryIsNoEmpty() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals(gson.toJson(manager.getPrioritizedTasks()), accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test GET then wrong path")
    void testGetThenWrongPath() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/tasks"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Not Found", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test POST task")
    void testPostTask() {
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.of(2019, Month.SEPTEMBER, 1, 12, 0));
        task.setDuration(30);
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Задача создана", accept);
            assertEquals(201, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test POST subtask")
    void testPostSubtask() {
        Epic epic = new Epic("Поход в магазин", "В макси", TaskStatus.NEW);
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Покупка молока", "1л", TaskStatus.NEW, 1);
        subtask.setStartTime(LocalDateTime.of(2021, Month.OCTOBER, 24, 11, 0));
        subtask.setDuration(30);
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Подзадача создана", accept);
            assertEquals(201, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test POST epic")
    void testPostEpic() {
        Epic epic = new Epic("Поход в магазин", "В макси", TaskStatus.NEW);
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Эпик создан", accept);
            assertEquals(201, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test POST then wrong path")
    void testPostThenWrongPath() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task1/"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(manager.getTaskById(1))))
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Not Found", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE all tasks")
    void testDeleteAllTasks() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Все задачи удалены", accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE all subtasks")
    void testDeleteAllSubtasks() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Все подзадачи удалены", accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE all epic")
    void testDeleteAllEpic() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Все эпики удалены", accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then task id = 1 and the first task exists")
    void testDeleteTaskThenIdIs1And1TaskExists() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/?id=1"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Задача успешно удалена", accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then subtask id = 4 and the 4 subtask exists")
    void testDeleteSubtaskThenIdIs4And4SubtaskExists() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/?id=4"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Подзадача успешно удалена", accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then epic id = 3 and the 3 epic exists")
    void testDeleteEpicThenIdIs3And3EpicExists() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/?id=3"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Эпик успешно удален", accept);
            assertEquals(200, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then id task is wrong")
    void testDeleteTaskThenIdIsWrong() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/?id=k"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then id subtask is wrong")
    void testDeleteSubtaskThenIdIsWrong() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/?id=k"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then id epic is wrong")
    void testDeleteEpicThenIdIsWrong() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/?id=k"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("", accept);
            assertEquals(400, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then id = 4 but 4 task not found")
    void testDeleteTaskThenIdIs4But4TaskNoExists() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task/?id=4"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Задача не найдена", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then id = 1 but 1 subtask not found")
    void testDeleteSubtaskThenIdIs1But1SubtaskNoExists() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/subtask/?id=1"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Подадача не найдена", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then id = 1 but 1 epic not found")
    void testDeleteEpicThenIdIs1But1EpicNoExists() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/epic/?id=1"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Эпик не найден", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test DELETE then wrong path")
    void testDeleteThenWrongPath() {
        initStandardBehavior();
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task1/"))
                    .DELETE()
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Not Found", accept);
            assertEquals(404, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    @DisplayName("Test query that doesn't work")
    void testQueryThatNotWork() {
        initStandardBehavior();
        Task task = manager.getTaskById(1);
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "/tasks/task1/"))
                    .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                    .build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String accept = send.body();
            int error = send.statusCode();
            assertEquals("Not Found", accept);
            assertEquals(405, error);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @AfterEach
    void cleanUp() {
        httpTaskServer.stop();
        server.stop();
    }
}