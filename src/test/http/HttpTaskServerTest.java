package http;

import com.google.gson.Gson;
import http.kv.KVTaskClient;
import http.kv.server.KVServer;
import managers.TaskManagerTest;
import managers.http.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest extends TaskManagerTest<HttpTaskManager> {
    Gson gson = new Gson();
    HttpTaskServer httpTaskServer;
    KVServer server;
    KVTaskClient kvTaskClient;
    String URL = "http://localhost:8080";

    @BeforeEach
    void setUp() throws IOException {
        manager = new HttpTaskManager("http://localhost:8078/");
        server = new KVServer();
        httpTaskServer = new HttpTaskServer(manager);
        server.start();
        httpTaskServer.start();
        kvTaskClient = new KVTaskClient("http://localhost:8078/");
    }

    @Test
    @DisplayName("Test GET then list tasks is empty")
    void testGetThenListTasksIsEmpty() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET tasks then list tasks no empty")
    void testGetTasksThenListTasksNoEmpty() throws Exception {
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        manager.createTask(task);
        task = new Task("Купить воду", "В пятерочке", TaskStatus.DONE);
        manager.createTask(task);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{\"1\":{\"name\":\"Купить хлеб\",\"description\":\"В магните\",\"id\":1," +
                "\"status\":\"NEW\",\"taskType\":\"Task\"},\"2\":{\"name\":\"Купить воду\"," +
                "\"description\":\"В пятерочке\",\"id\":2,\"status\":\"DONE\",\"taskType\":\"Task\"}}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET subtasks then list subtasks is empty")
    void testGetSubtasksThenListSubtasksIsEmpty() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET subtasks then list subtasks no empty")
    void testGetSubtasksThenListSubtasksNoEmpty() throws Exception {
        manager.createEpic(new Epic("Поход в магазин", "В макси", TaskStatus.NEW));
        Subtask subtask = new Subtask("Покупка молока", "1л", TaskStatus.NEW, 1);
        manager.createSubtask(subtask);
        subtask = new Subtask("Покупка сахара", "1кг", TaskStatus.DONE, 1);
        manager.createSubtask(subtask);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{\"2\":{\"idSubtaskForEpic\":1,\"name\":\"Покупка молока\",\"description\":\"1л\"," +
                "\"id\":2,\"status\":\"NEW\",\"taskType\":\"Subtask\"},\"3\":{\"idSubtaskForEpic\":1," +
                "\"name\":\"Покупка сахара\",\"description\":\"1кг\",\"id\":3,\"status\":\"DONE\"," +
                "\"taskType\":\"Subtask\"}}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET epic then list epic is empty")
    void testGetEpicThenListEpicIsEmpty() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET epic then list epic no empty")
    void testGetEpicThenListEpicNoEmpty() throws Exception {
        manager.createEpic(new Epic("Поход в магазин", "В макси", TaskStatus.NEW));
        manager.createEpic(new Epic("Поход в музей", "В центре", TaskStatus.NEW));

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{\"1\":{\"subtaskForEpic\":{},\"name\":\"Поход в магазин\"," +
                "\"description\":\"В макси\",\"id\":1,\"status\":\"NEW\",\"taskType\":\"Epic\"}," +
                "\"2\":{\"subtaskForEpic\":{},\"name\":\"Поход в музей\",\"description\":\"В центре\"," +
                "\"id\":2,\"status\":\"NEW\",\"taskType\":\"Epic\"}}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET then id = 1 and list tasks is empty")
    void testGetTaskThenIdIs1AndListTasksIsEmpty() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/?id=1"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Задача не найдена", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test GET then id = 4 but 4 tasks not found")
    void testGetTaskThenIdIs4But4TaskNoExists() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/?id=4"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Задача не найдена", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test GET then id = 1 but 1 subtasks not found")
    void testGetSubtaskThenIdIs1But1SubtaskNoExists() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/?id=1"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Подзадача не найдена", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test GET then id = 1 but 1 tasks not found")
    void testGetEpicThenIdIs1But1EpicNoExists() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/?id=1"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Эпик не найден", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test GET then task id = 1 and the first task exists")
    void testGetTaskThenIdIs1And1TaskExists() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/?id=1"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{\"name\":\"Купить хлеб\",\"description\":\"В магните\",\"id\":1,\"status\":\"NEW\"," +
                "\"duration\":{\"seconds\":1800,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2022,\"month\":9," +
                "\"day\":1},\"time\":{\"hour\":12,\"minute\":0,\"second\":0,\"nano\":0}}," +
                "\"endTime\":{\"date\":{\"year\":2022,\"month\":9,\"day\":1},\"time\":{\"hour\":12,\"minute\":30," +
                "\"second\":0,\"nano\":0}},\"taskType\":\"Task\"}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET then subtask id = 4 and the 4 subtask exists")
    void testGetSubtaskThenIdIs4And4SubtaskExists() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/?id=4"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{\"idSubtaskForEpic\":3,\"name\":\"Покупка молока\",\"description\":\"1л\",\"id\":4," +
                "\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2021,\"month\":10,\"day\":24},\"time\":{\"hour\":11," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2021,\"month\":10," +
                "\"day\":24},\"time\":{\"hour\":11,\"minute\":30,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Subtask\"}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET then epic id = 3 and the 3 epic exists")
    void testGetEpicThenIdIs3And3EpicExists() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/?id=3"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{\"subtaskForEpic\":{\"4\":{\"idSubtaskForEpic\":3,\"name\":\"Покупка молока\"," +
                "\"description\":\"1л\",\"id\":4,\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2021,\"month\":10,\"day\":24},\"time\":{\"hour\":11," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2021,\"month\":10," +
                "\"day\":24},\"time\":{\"hour\":11,\"minute\":30,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Subtask\"},\"5\":{\"idSubtaskForEpic\":3,\"name\":\"Покупка сахара\"," +
                "\"description\":\"1кг\",\"id\":5,\"status\":\"DONE\",\"duration\":{\"seconds\":3600,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2022,\"month\":12,\"day\":30},\"time\":{\"hour\":15," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2022,\"month\":12," +
                "\"day\":30},\"time\":{\"hour\":16,\"minute\":0,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Subtask\"}},\"name\":\"Поход в магазин\",\"description\":\"В макси\"," +
                "\"id\":3,\"status\":\"IN_PROGRESS\",\"duration\":{\"seconds\":5400,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2021,\"month\":10,\"day\":24},\"time\":{\"hour\":11," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2022,\"month\":12," +
                "\"day\":30},\"time\":{\"hour\":16,\"minute\":0,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Epic\"}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET then id task is wrong")
    void testGetTaskThenIdIsWrong() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/?id=k"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("", accept);
        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Test GET then id subtask is wrong")
    void testGetSubtaskThenIdIsWrong() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/?id=k"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("", accept);
        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Test GET then id epic is wrong")
    void testGetEpicThenIdIsWrong() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/?id=k"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("", accept);
        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Test GET epic subtasks then id epic is wrong")
    void testGetEpicSubtasksThenIdIsWrong() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/epic/?id=k"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("", accept);
        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Test GET epic subtasks then epic id = 4 and 4 epic no exists")
    void testGetEpicSubtasksThenIdEpicIs4AndNoExists() throws Exception {
        initStandardBehavior();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/epic/?id=4"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Эпик не найден", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test GET epic subtasks then epic id = 3 and 3 epic exists")
    void testGetEpicSubtasksThenIdEpicIs3AndEpicExists() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/epic/?id=3"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("{\"4\":{\"idSubtaskForEpic\":3,\"name\":\"Покупка молока\",\"description\":\"1л\"," +
                "\"id\":4,\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2021,\"month\":10,\"day\":24},\"time\":{\"hour\":11," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2021,\"month\":10," +
                "\"day\":24},\"time\":{\"hour\":11,\"minute\":30,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Subtask\"},\"5\":{\"idSubtaskForEpic\":3,\"name\":\"Покупка сахара\"," +
                "\"description\":\"1кг\",\"id\":5,\"status\":\"DONE\",\"duration\":{\"seconds\":3600,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2022,\"month\":12,\"day\":30},\"time\":{\"hour\":15," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2022,\"month\":12," +
                "\"day\":30},\"time\":{\"hour\":16,\"minute\":0,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Subtask\"}}", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET history then history is empty")
    void testGetHistoryThenHistoryIsEmpty() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/history"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("[]", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET history then history is no empty")
    void testGetHistoryThenHistoryIsNoEmpty() throws Exception {
        initStandardBehavior();
        manager.getTaskById(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/history"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("[{\"name\":\"Купить хлеб\",\"description\":\"В магните\",\"id\":1," +
                "\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2022,\"month\":9,\"day\":1},\"time\":{\"hour\":12," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2022,\"month\":9," +
                "\"day\":1},\"time\":{\"hour\":12,\"minute\":30,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Task\"}]", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET prioritized tasks then history is empty")
    void testGetPrioritizedTasksThenHistoryIsEmpty() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("[]", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET prioritized tasks then history is no empty")
    void testGetPrioritizedTasksThenHistoryIsNoEmpty() throws Exception {
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.of(2022, Month.SEPTEMBER, 1, 12, 0));
        task.setDuration(30);
        manager.createTask(task);
        manager.createEpic(new Epic("Поход в магазин", "В макси", TaskStatus.NEW));
        Subtask subtask = new Subtask("Покупка молока", "1л", TaskStatus.NEW, 2);
        subtask.setStartTime(LocalDateTime.of(2021, Month.OCTOBER, 24, 11, 0));
        subtask.setDuration(30);
        manager.createSubtask(subtask);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("[{\"idSubtaskForEpic\":2,\"name\":\"Покупка молока\",\"description\":\"1л\"," +
                "\"id\":3,\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2021,\"month\":10,\"day\":24},\"time\":{\"hour\":11," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2021,\"month\":10," +
                "\"day\":24},\"time\":{\"hour\":11,\"minute\":30,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Subtask\"},{\"name\":\"Купить хлеб\",\"description\":\"В магните\"," +
                "\"id\":1,\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0}," +
                "\"startTime\":{\"date\":{\"year\":2022,\"month\":9,\"day\":1},\"time\":{\"hour\":12," +
                "\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2022,\"month\":9," +
                "\"day\":1},\"time\":{\"hour\":12,\"minute\":30,\"second\":0,\"nano\":0}}," +
                "\"taskType\":\"Task\"}]", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test GET then wrong path")
    void testGetThenWrongPath() throws Exception {
        initStandardBehavior();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/tasks"))
                .GET()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Not Found", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test POST task")
    void testPostTask() throws Exception {
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        manager.createTask(task);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertEquals(task, FromJsonConverter.fromJson(kvTaskClient.load("tasks"), 1));
        assertEquals("Задача создана", accept);
        assertEquals(201, statusCode);
    }

    @Test
    @DisplayName("Test POST subtask")
    void testPostSubtask() throws Exception {
        Epic epic = new Epic("Поход в магазин", "В макси", TaskStatus.NEW);
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Покупка молока", "1л", TaskStatus.NEW, 1);
        manager.createSubtask(subtask);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertEquals(subtask, FromJsonConverter.fromJson(kvTaskClient.load("subtasks"), 2));
        assertEquals("Подзадача создана", accept);
        assertEquals(201, statusCode);
    }

    @Test
    @DisplayName("Test POST epic")
    void testPostEpic() throws Exception {
        Epic epic = new Epic("Поход в магазин", "В макси", TaskStatus.NEW);
        manager.createEpic(epic);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertEquals(epic, FromJsonConverter.fromJson(kvTaskClient.load("epic"), 1));
        assertEquals("Эпик создан", accept);
        assertEquals(201, statusCode);
    }

    @Test
    @DisplayName("Test POST then wrong path")
    void testPostThenWrongPath() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/task"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(manager.getTaskById(1))))
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Not Found", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test DELETE all tasks")
    void testDeleteAllTasks() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertEquals("{}", kvTaskClient.load("tasks"));
        assertEquals("Все задачи удалены", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test DELETE all subtasks")
    void testDeleteAllSubtasks() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertEquals("{}", kvTaskClient.load("subtasks"));
        assertEquals("Все подзадачи удалены", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test DELETE all epic")
    void testDeleteAllEpic() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertEquals("{}", kvTaskClient.load("epic"));
        assertEquals("Все эпики удалены", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then task id = 1 and the first task exists")
    void testDeleteTaskThenIdIs1And1TaskExists() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/?id=1"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertNull(manager.getTaskById(1));
        assertEquals("Задача успешно удалена", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then subtask id = 4 and the 4 subtask exists")
    void testDeleteSubtaskThenIdIs4And4SubtaskExists() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/?id=4"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertNull(manager.getSubtaskById(4));
        assertEquals("Подзадача успешно удалена", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then epic id = 3 and the 3 epic exists")
    void testDeleteEpicThenIdIs3And3EpicExists() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/?id=3"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();

        assertNull(manager.getEpicById(3));
        assertEquals("Эпик успешно удален", accept);
        assertEquals(200, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then id task is wrong")
    void testDeleteTaskThenIdIsWrong() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/?id=k"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("", accept);
        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then id subtask is wrong")
    void testDeleteSubtaskThenIdIsWrong() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/?id=k"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("", accept);
        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then id epic is wrong")
    void testDeleteEpicThenIdIsWrong() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/?id=k"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("", accept);
        assertEquals(400, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then id = 4 but 4 task not found")
    void testDeleteTaskThenIdIs4But4TaskNoExists() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/?id=4"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Задача не найдена", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then id = 1 but 1 subtask not found")
    void testDeleteSubtaskThenIdIs1But1SubtaskNoExists() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/subtask/?id=1"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Подадача не найдена", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then id = 1 but 1 epic not found")
    void testDeleteEpicThenIdIs1But1EpicNoExists() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/epic/?id=1"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Эпик не найден", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test DELETE then wrong path")
    void testDeleteThenWrongPath() throws Exception {
        initStandardBehavior();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task/task"))
                .DELETE()
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Not Found", accept);
        assertEquals(404, statusCode);
    }

    @Test
    @DisplayName("Test query that doesn't work")
    void testQueryThatNotWork() throws Exception {
        initStandardBehavior();
        Task task = manager.getTaskById(1);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL + "/tasks/task1/"))
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();
        HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String accept = send.body();
        int statusCode = send.statusCode();
        assertEquals("Not Found", accept);
        assertEquals(405, statusCode);
    }

    @AfterEach
    void cleanUp() {
        httpTaskServer.stop();
        server.stop();
    }
}