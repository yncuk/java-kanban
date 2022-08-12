import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Если хотите провести тестирование с ручным выбором характристик введите 1, " +
                "если хотите провести тестирование с полуавтоматическим заполнением введите 2: ");
        int test = scanner.nextInt();

        if(test == 1) {
            Manager manager = new Manager();

            Task task = new Task();
            manager.createTask(task);

            Task task1 = new Task();
            manager.createTask(task1);

            Epic epic = new Epic();
            manager.createEpic(epic);
            System.out.println("Номер эпика - " + epic.id);
            Subtask subtask = new Subtask();
            manager.createSubtask(subtask);
            Subtask subtask1 = new Subtask();
            manager.createSubtask(subtask1);

            Epic epic1 = new Epic();
            manager.createEpic(epic1);
            System.out.println("Номер эпика - " + epic1.id);
            Subtask subtask2 = new Subtask();
            manager.createSubtask(subtask2);

            System.out.println(manager);

            System.out.println("Здесь замена");
            subtask2.status = "NEW";
            manager.updateSubtask(subtask2);

            System.out.println(manager);
            manager.deleteTaskById(1);
            manager.deleteEpicById(6);
            System.out.println(manager);
        } else if (test == 2){
            ManagerCopy managerCopy = new ManagerCopy();

            Task task = new Task();
            managerCopy.createTask(task);

            Task task1 = new Task();
            managerCopy.createTask(task1);

            Epic epic = new Epic();
            managerCopy.createEpic(epic);
            System.out.println("Введите номер этого эпика - " + epic.id);
            Subtask subtask = new Subtask();
            managerCopy.createSubtask(subtask);
            Subtask subtask1 = new Subtask();
            managerCopy.createSubtask(subtask1);

            Epic epic1 = new Epic();
            managerCopy.createEpic(epic1);
            System.out.println("Введите номер этого эпика - " + epic1.id);
            Subtask subtask2 = new Subtask();
            managerCopy.createSubtask(subtask2);

            System.out.println(managerCopy);

            System.out.println("Здесь замена");
            subtask2.status = "NEW";
            managerCopy.updateSubtask(subtask2);

            System.out.println(managerCopy);
            managerCopy.deleteTaskById(1);
            managerCopy.deleteEpicById(6);
            System.out.println(managerCopy);
        } else System.out.println("Введите 1 или 2");
    }
}
