package managers.http;

import com.google.gson.Gson;
import http.kv.KVTaskClient;
import managers.HistoryManager;
import managers.filebacked.FileBackedTasksManager;

import java.util.HashMap;


public class HttpTaskManager extends FileBackedTasksManager {
    private final String URL;

    public HttpTaskManager(String URL) {
        super(null);
        this.URL = URL;
    }

    public static HttpTaskManager loadFormServer(String URL) {
        final Gson gson = new Gson();
        final KVTaskClient taskClient = new KVTaskClient(URL);
        HttpTaskManager httpTaskManager = new HttpTaskManager(URL);
        if (gson.fromJson(taskClient.load("tasks"), HashMap.class) != null) {
            httpTaskManager.taskHashMap.putAll(gson.fromJson(taskClient.load("tasks"), HashMap.class));
        }
        if (gson.fromJson(taskClient.load("subtasks"), HashMap.class) != null) {
            httpTaskManager.subtaskHashMap.putAll(gson.fromJson(taskClient.load("subtasks"), HashMap.class));
        }
        if (gson.fromJson(taskClient.load("epic"), HashMap.class) != null) {
            httpTaskManager.epicHashMap.putAll(gson.fromJson(taskClient.load("epic"), HashMap.class));
        }
        if (gson.fromJson(taskClient.load("history"), HistoryManager.class) != null) {
            httpTaskManager.historyManager = gson.fromJson(taskClient.load("history"), HistoryManager.class);
        }
        return httpTaskManager;
    }

    @Override
    protected void save() {
        final KVTaskClient taskClient = new KVTaskClient(URL);
        final Gson gson = new Gson();

        taskClient.put("tasks", gson.toJson(taskHashMap));
        taskClient.put("subtasks", gson.toJson(subtaskHashMap));
        taskClient.put("epic", gson.toJson(epicHashMap));
        taskClient.put("history", gson.toJson(historyManager.getHistory()));
        //System.out.println(taskClient.load("tasks"));
    }
}
