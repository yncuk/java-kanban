package managers.factory;

import managers.http.HttpTaskManager;
import managers.HistoryManager;
import managers.filebacked.FileBackedTasksManager;
import managers.inmemory.InMemoryHistoryManager;
import managers.TaskManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault(String url) {
        return HttpTaskManager.loadFormServer(url);
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
