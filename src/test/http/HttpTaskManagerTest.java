package http;

import com.google.gson.Gson;
import http.kv.KVTaskClient;
import http.kv.server.KVServer;
import managers.TaskManagerTest;
import managers.http.HttpTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    Gson gson = new Gson();

    @BeforeEach
    void setUp() throws IOException {
        KVServer server = new KVServer();
        manager = new HttpTaskManager("http://localhost:8078/");
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        server.start();
        httpTaskServer.start();

    }

    @Test
    @DisplayName("Save test in standard behavior")
    void saveTasksOnServerTest() {
        KVTaskClient taskClient = new KVTaskClient("http://localhost:8078/");
        initStandardBehavior();
        taskClient.put("tasks", gson.toJson(manager.getListOfAllTasks()));
        taskClient.put("subtasks", gson.toJson(manager.getListOfAllSubtasks()));
        taskClient.put("epic", gson.toJson(manager.getListOfAllEpic()));
        taskClient.put("history", gson.toJson(manager.getHistory()));
        String tasks = taskClient.load("tasks");
        String subtasks = taskClient.load("subtasks");
        String epic = taskClient.load("epic");
        String history = taskClient.load("history");

        assertAll(
                () -> assertEquals(gson.toJson(manager.getListOfAllTasks()), tasks),
                () -> assertEquals(gson.toJson(manager.getListOfAllSubtasks()), subtasks),
                () -> assertEquals(gson.toJson(manager.getListOfAllEpic()), epic),
                () -> assertEquals(gson.toJson(manager.getHistory()), history)
        );
    }
}