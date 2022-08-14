import manager.Manager;
import task.*;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task = new Task("Купить хлеб", "В магните", "NEW");
        manager.createTask(task);

        Task task1 = new Task("Купить воду", "В пятерочке", "DONE");
        manager.createTask(task1);

        Epic epic = new Epic("Поход в магазин", "В макси", "NEW");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Покупка молока", "1л", "NEW", 3);
        manager.createSubtask(subtask);

        Subtask subtask1 = new Subtask("Покупка сахара", "1кг", "DONE", 3);
        manager.createSubtask(subtask1);

        Epic epic1 = new Epic("Поход в музей", "В центре", "NEW");
        manager.createEpic(epic1);
        System.out.println(epic1);
        epic1.setName("Поход в ТЦ");
        manager.updateEpic(epic1);

        Subtask subtask2 = new Subtask("Покупка одежды", "Брюки", "DONE", 6);
        manager.createSubtask(subtask2);

        System.out.println(manager);
        System.out.println("Здесь замена");
        subtask2.setStatus("NEW");
        manager.updateSubtask(subtask2);

        System.out.println(manager);

        Subtask subtaskNew = new Subtask("Покупка сахара", "2кг", "DONE", 3);
        subtaskNew.setId(5);
        manager.updateSubtask(subtaskNew);

        System.out.println(manager);

        manager.deleteTaskById(1);
        manager.deleteEpicById(6);
        System.out.println(manager);

        manager.deleteAllSubtask();
        System.out.println(manager);
    }
}