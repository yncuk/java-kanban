import managers.factory.Managers;
import managers.TaskManager;
import task.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {

    public static void main(String[] args) throws IOException {

        //TaskManager manager = Managers.getDefault();

        //TaskManager manager = Managers.getFileBakedTaskManagerFromFile(new File("src/data.csv"));
        //System.out.println(manager);
        //System.out.println(manager.getHistory());

        TaskManager manager = Managers.getFileBackedTaskManager(new File("src/data.csv"));
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.of(2019, Month.SEPTEMBER,1,12,0));
        task.setDuration(30);
        manager.createTask(task);

        Task taskCopy = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        taskCopy.setStartTime(LocalDateTime.of(2025, Month.SEPTEMBER,1,12,0));
        taskCopy.setDuration(30);
        taskCopy.setId(1);
        //manager.updateTask(taskCopy);

        Task task1 = new Task("Купить воду", "В пятерочке", TaskStatus.DONE);
        task1.setStartTime(LocalDateTime.of(2020, Month.JULY,7,16,0));
        task1.setDuration(90);
        manager.createTask(task1);

        Epic epic = new Epic("Поход в магазин", "В макси", TaskStatus.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Покупка молока", "1л", TaskStatus.NEW, 3);
        subtask.setStartTime(LocalDateTime.of(2021, Month.OCTOBER,24,11,0));
        subtask.setDuration(30);
        manager.createSubtask(subtask);

        Subtask subtask1 = new Subtask("Покупка сахара", "1кг", TaskStatus.DONE, 3);
        subtask1.setStartTime(LocalDateTime.of(2022, Month.DECEMBER,30,15,0));
        subtask1.setDuration(60);
        manager.createSubtask(subtask1);

        Epic epic1 = new Epic("Поход в музей", "В центре", TaskStatus.NEW);
        manager.createEpic(epic1);
        System.out.println(epic1);
        epic1.setName("Поход в ТЦ");

        Subtask subtask2 = new Subtask("Покупка одежды", "Брюки", TaskStatus.DONE, 6);
        subtask2.setStartTime(LocalDateTime.of(2020, Month.DECEMBER,21,21,0));
        subtask2.setDuration(45);
        manager.createSubtask(subtask2);

        System.out.println(manager);
        System.out.println("Здесь замена");
        Subtask subtask3 = new Subtask("Покупка одежды", "Брюки", TaskStatus.DONE, 6);
        subtask3.setId(7);
        subtask3.setStartTime(LocalDateTime.of(2030, Month.DECEMBER,21,21,0));
        subtask3.setStatus(TaskStatus.NEW);
        //manager.updateSubtask(subtask3);

        System.out.println(manager);

        //Subtask subtaskNew = new Subtask("Покупка сахара", "2кг", TaskStatus.DONE, 3);
        //subtaskNew.setId(5);
        //manager.updateSubtask(subtaskNew);

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

        //System.out.println(manager.getPrioritizedTasks());
        System.out.println("_______________________________");
        Task taskDuplicate = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        taskDuplicate.setStartTime(LocalDateTime.of(2020, Month.JULY,7,17,0));
        taskDuplicate.setDuration(30);
        manager.createTask(taskDuplicate);

        System.out.println("_______________________________");
        for (Task taskSort: manager.getPrioritizedTasks()) {
            System.out.println(taskSort.getStartTime());
        }

        //manager.deleteEpicById(3);
        //manager.deleteSubtaskById(5);
        //System.out.println(manager.getHistory());
    }
}