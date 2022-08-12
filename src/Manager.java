import java.util.HashMap;
import java.util.Scanner;

public class Manager {
    private int id = 1;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();

    public HashMap<Integer, Task> getListOfAllTasks() {

        return taskHashMap;
    }

    public HashMap<Integer, Subtask> getListOfAllSubtasks() {

        return subtaskHashMap;
    }

    public HashMap<Integer, Epic> getListOfAllEpic() {

        return epicHashMap;
    }

    public void deleteAllTasks() {

        taskHashMap.clear();
    }

    public void deleteAllSubtask() {

        subtaskHashMap.clear();
    }

    public void deleteAllEpic() {
        epicHashMap.clear();
        deleteAllSubtask();
    }

    public void deleteAllTypeTasks() {
        deleteAllTasks();
        deleteAllSubtask();
        deleteAllEpic();
    }

    public Task getTaskById(int idTask) {
        for (int key : taskHashMap.keySet()) {
            if (key == idTask) {
                return taskHashMap.get(key);
            }
        }
        return null;
    }

    public Subtask getSubtaskById(int idSubtask) {
        for (int key : subtaskHashMap.keySet()) {
            if (key == idSubtask) {
                return subtaskHashMap.get(key);
            }
        }
        return null;
    }

    public Epic getEpicById(int idEpic) {
        for (int key : epicHashMap.keySet()) {
            if (key == idEpic) {
                return epicHashMap.get(key);
            }
        }
        return null;
    }

    public Task createTask(Task task) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя обычной задачи: ");
        task.setName(scanner.nextLine());
        System.out.println("Введите описание обычной задачи: ");
        task.setDescription(scanner.nextLine());
        task.setId(id);
        id++;
        while (true) {
            System.out.println("Введите статус обычной задачи: ");
            String status = scanner.nextLine();
            if (status.equals("NEW") || status.equals("DONE") || status.equals("IN_PROGRESS")) {
                task.setStatus(status);
                break;
            } else System.out.println("Введен неверный статус обычной задачи, " +
                    "доступные статусы NEW, DONE, IN_PROGRESS, повторите попытку");

        }

        taskHashMap.put(task.getId(), task);
        return task;
    }

    public Subtask createSubtask(Subtask subtask) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя подзадачи: ");
        subtask.setName(scanner.nextLine());
        System.out.println("Введите описание подзадачи: ");
        subtask.setDescription(scanner.nextLine());
        subtask.setId(id);
        id++;
        while (true) {
            System.out.println("Введите статус подзадачи: ");
            String status = scanner.nextLine();
            if (status.equals("NEW") || status.equals("DONE") || status.equals("IN_PROGRESS")) {
                subtask.setStatus(status);
                break;
            } else System.out.println("Введен неверный статус подзадачи, " +
                    "доступные статусы NEW, DONE, IN_PROGRESS, повторите попытку");
        }
        while (true) {
            System.out.println("Введите ID эпика: ");
            int checkEpic = scanner.nextInt();
            if (getEpicById(checkEpic) != null) {
                subtask.setIdSubtaskForEpic(checkEpic);
                subtaskHashMap.put(subtask.getId(), subtask);
                Epic epic = getEpicById(checkEpic);
                changeStatusEpic(epic);
                break;
            } else System.out.println("Введен неверный ID эпика, повторите попытку");
        }

        return subtask;
    }

    public Epic createEpic(Epic epic) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя эпика: ");
        epic.setName(scanner.nextLine());
        System.out.println("Введите описание эпика: ");
        epic.setDescription(scanner.nextLine());
        epic.setId(id);
        id++;
        changeStatusEpic(epic);

        epicHashMap.put(epic.getId(), epic);
        return epic;
    }

    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        Epic epic = getEpicById(subtask.getIdSubtaskForEpic());
        subtaskHashMap.put(subtask.getId(), subtask);
        changeStatusEpic(epic);
    }

    public void updateEpic(Epic epic) {
        epicHashMap.put(epic.getId(), epic);
    }

    public void deleteTaskById(int idTask) {
        taskHashMap.remove(getTaskById(idTask).getId());
    }

    public void deleteSubtaskById(int idSubtask) {
        Epic epic = getEpicById(getSubtaskById(idSubtask).getIdSubtaskForEpic());
        subtaskHashMap.remove(getSubtaskById(idSubtask).getId());
        changeStatusEpic(epic);
    }

    public void deleteEpicById(int idEpic) {
        for (Subtask subtask : getAllSubtaskByEpic(getEpicById(idEpic)).values()) {
            subtaskHashMap.remove(subtask.getId());
        }
        epicHashMap.remove(getEpicById(idEpic).getId());
    }

    public HashMap<Integer, Subtask> getAllSubtaskByEpic(Epic epic) {
        HashMap<Integer, Subtask> listSubtask = new HashMap<>();
        for (int key : subtaskHashMap.keySet()) {
            if (subtaskHashMap.get(key).getIdSubtaskForEpic() == epic.getId()) {
                listSubtask.put(key, subtaskHashMap.get(key));
            }
        }
        return listSubtask;
    }

    public void changeStatusEpic(Epic epic) {
        HashMap<Integer, Subtask> listSubtask = getAllSubtaskByEpic(epic);
        if (!listSubtask.isEmpty()) {
            int checkNew = 0;
            int checkDone = 0;

            for (Subtask sub : listSubtask.values()) {
                if (sub.getStatus().equals("NEW")) {
                    checkNew++;
                } else if (sub.getStatus().equals("DONE")) {
                    checkDone++;
                }
            }
            if (checkNew == listSubtask.size()) {
                epic.status = "NEW";
            } else if (checkDone == listSubtask.size()) {
                epic.status = "DONE";
            } else epic.status = "IN_PROGRESS";
        } else {
            epic.status = "NEW";
        }
    }

    @Override
    public String toString() {
        return "Manager{" +
                "Список обычных задач=" + getListOfAllTasks() +
                ", список подзадач=" + getListOfAllSubtasks() +
                ", список эпиков=" + getListOfAllEpic() +
                '}';
    }
}
