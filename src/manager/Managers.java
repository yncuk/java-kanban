package manager;

public class Managers {
    // не знаю, может стоит этот класс в отдельный пакет вытащить?
    // чтобы он не принадлежал пакету с разными реализациями интерфейса
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
