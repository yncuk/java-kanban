package managers;

import task.*;

import java.time.LocalDateTime;
import java.time.Month;

public abstract class TaskManagerTest <T extends TaskManager> {
    protected T manager;

    protected void initEpic() {
        manager.createEpic(new Epic("Поход в магазин", "В макси", TaskStatus.NEW));
    }

    protected void initStandardBehavior() {
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.of(2022, Month.SEPTEMBER,1,12,0));
        task.setDuration(30);
        manager.createTask(task);
        task = new Task("Купить воду", "В пятерочке", TaskStatus.DONE);
        task.setStartTime(LocalDateTime.of(2022, Month.JULY,7,16,0));
        task.setDuration(90);
        manager.createTask(task);
        manager.createEpic(new Epic("Поход в магазин", "В макси", TaskStatus.NEW));
        Subtask subtask = new Subtask("Покупка молока", "1л", TaskStatus.NEW, 3);
        subtask.setStartTime(LocalDateTime.of(2021, Month.OCTOBER,24,11,0));
        subtask.setDuration(30);
        manager.createSubtask(subtask);
        subtask = new Subtask("Покупка сахара", "1кг", TaskStatus.DONE, 3);
        subtask.setStartTime(LocalDateTime.of(2022, Month.DECEMBER,30,15,0));
        subtask.setDuration(60);
        manager.createSubtask(subtask);
        manager.createEpic(new Epic("Поход в музей", "В центре", TaskStatus.NEW));
        subtask = new Subtask("Покупка одежды", "Брюки", TaskStatus.DONE, 6);
        subtask.setStartTime(LocalDateTime.of(2020, Month.DECEMBER,21,21,0));
        subtask.setDuration(45);
        manager.createSubtask(subtask);
    }
}