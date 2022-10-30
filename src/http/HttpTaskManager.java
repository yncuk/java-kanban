package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.kv.KVTaskClient;
import managers.filebacked.FileBackedTasksManager;


public class HttpTaskManager extends FileBackedTasksManager {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    KVTaskClient taskClient = new KVTaskClient();

    private final String URL;

    public HttpTaskManager(String url) {
        super(null);
        URL = url;
    }

    @Override
    protected void save() {
        taskClient.put("tasks", gson.toJson(taskHashMap));
        taskClient.put("subtasks", gson.toJson(subtaskHashMap));
        taskClient.put("epic", gson.toJson(epicHashMap));
        taskClient.put("history", gson.toJson(historyManager));
        System.out.println(taskClient.load("tasks"));
    }
}
