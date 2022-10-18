import managers.factory.Managers;
import managers.TaskManager;
import task.*;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        //TaskManager manager = Managers.getDefault();

        //TaskManager manager = Managers.getFileBakedTaskManagerFromFile(new File("src/data.csv"));
        //System.out.println(manager);
        //System.out.println(manager.getHistory());

        TaskManager manager = Managers.getFileBackedTaskManager(new File("src/data.csv"));
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        manager.createTask(task);

        Task task1 = new Task("Купить воду", "В пятерочке", TaskStatus.DONE);
        manager.createTask(task1);

        Epic epic = new Epic("Поход в магазин", "В макси", TaskStatus.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Покупка молока", "1л", TaskStatus.NEW, 3);
        manager.createSubtask(subtask);

        Subtask subtask1 = new Subtask("Покупка сахара", "1кг", TaskStatus.DONE, 3);
        manager.createSubtask(subtask1);

        Epic epic1 = new Epic("Поход в музей", "В центре", TaskStatus.NEW);
        manager.createEpic(epic1);
        System.out.println(epic1);
        epic1.setName("Поход в ТЦ");
        manager.updateEpic(epic1);

        Subtask subtask2 = new Subtask("Покупка одежды", "Брюки", TaskStatus.DONE, 6);
        manager.createSubtask(subtask2);

        System.out.println(manager);
        System.out.println("Здесь замена");
        subtask2.setStatus(TaskStatus.NEW);
        manager.updateSubtask(subtask2);

        System.out.println(manager);

        Subtask subtaskNew = new Subtask("Покупка сахара", "2кг", TaskStatus.DONE, 3);
        subtaskNew.setId(5);
        manager.updateSubtask(subtaskNew);

        System.out.println(manager);
        System.out.println("Проверка сабтаска в истории перед удалением");
        manager.getSubtaskById(5);
        System.out.println(manager.getHistory());

        System.out.println("Здесь удаление");
        //manager.deleteAllSubtask();
        System.out.println(manager);


        //manager.deleteEpicById(6);
        System.out.println(manager);

        System.out.println("_______________________________");
        manager.getTaskById(2);
        //System.out.println(manager.getHistory());
        manager.getEpicById(3);
        //System.out.println(manager.getHistory());

        //manager.deleteTaskById(2);
        manager.getTaskById(1);
        //System.out.println(manager.getHistory());

        manager.getSubtaskById(5);

        System.out.println(manager);
        System.out.println(manager.getHistory());

        //manager.deleteEpicById(3);
        //manager.deleteSubtaskById(5);
        //System.out.println(manager.getHistory());
    }
}