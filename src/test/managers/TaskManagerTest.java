package managers;

import task.*;

public abstract class TaskManagerTest <T extends TaskManager> {
    protected T manager;

    protected void initEpic() {
        manager.createEpic(new Epic("Поход в магазин", "В макси", TaskStatus.NEW));
    }

    protected void initStandardBehavior() {
        manager.createTask(new Task("Купить хлеб", "В магните", TaskStatus.NEW));
        manager.createTask(new Task("Купить воду", "В пятерочке", TaskStatus.DONE));
        manager.createEpic(new Epic("Поход в магазин", "В макси", TaskStatus.NEW));
        manager.createSubtask(new Subtask("Покупка молока", "1л", TaskStatus.NEW, 3));
        manager.createSubtask(new Subtask("Покупка сахара", "1кг", TaskStatus.DONE, 3));
        manager.createEpic(new Epic("Поход в музей", "В центре", TaskStatus.NEW));
        manager.createSubtask(new Subtask("Покупка одежды", "Брюки", TaskStatus.DONE, 6));
    }
}