package managers.factory;

import http.HttpTaskManager;
import managers.HistoryManager;
import managers.filebacked.FileBackedTasksManager;
import managers.inmemory.InMemoryHistoryManager;
import managers.inmemory.InMemoryTaskManager;
import managers.TaskManager;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault(String url) {
        return new HttpTaskManager(url);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(File file) {
        return new FileBackedTasksManager(file);
    }

    public static TaskManager getFileBakedTaskManagerFromFile(File file) {
        return FileBackedTasksManager.loadFromFile(file);
    }
}
