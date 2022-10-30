package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.kv.KVTaskClient;
import http.kv.server.KVServer;
import managers.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    KVServer server = new KVServer();
    HttpTaskServer httpTaskServer = new HttpTaskServer();


    HttpTaskManagerTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        server.start();
        httpTaskServer.start();
        manager = (HttpTaskManager) (httpTaskServer.manager = new HttpTaskManager("http://localhost:8078/"));
    }

    @Test
    @DisplayName("Save test in standard behavior")
    void saveTasksOnServerTest() {
        KVTaskClient taskClient = new KVTaskClient();
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

    @AfterEach
    void cleanUp() {
        server.stop();
        httpTaskServer.stop();
    }
}