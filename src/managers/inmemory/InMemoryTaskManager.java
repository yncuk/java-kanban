package managers.inmemory;

import managers.HistoryManager;
import managers.TaskManager;
import managers.factory.Managers;
import task.*;

import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public HashMap<Integer, Task> getListOfAllTasks() {
        return taskHashMap;
    }

    @Override
    public HashMap<Integer, Subtask> getListOfAllSubtasks() {
        return subtaskHashMap;
    }

    @Override
    public HashMap<Integer, Epic> getListOfAllEpic() {
        return epicHashMap;
    }

    @Override
    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubtask() {
        subtaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.removeAllSubtaskForEpic();
            changeStatusEpic(epic);
        }
    }

    @Override
    public void deleteAllEpic() {
        epicHashMap.clear();
        deleteAllSubtask();
    }

    @Override
    public void deleteAllTypeTasks() {
        deleteAllTasks();
        deleteAllSubtask();
        deleteAllEpic();
    }

    @Override
    public Task getTaskById(int idTask) {
        if (taskHashMap.containsKey(idTask)) {
            historyManager.add(taskHashMap.get(idTask));
        } else System.out.println("Такой задачи нет или она была удалена");
        return taskHashMap.get(idTask);
    }

    @Override
    public Subtask getSubtaskById(int idSubtask) {
        if (subtaskHashMap.containsKey(idSubtask)) {
            historyManager.add(subtaskHashMap.get(idSubtask));
        } else System.out.println("Такой задачи нет или она была удалена");
        return subtaskHashMap.get(idSubtask);
    }

    @Override
    public Epic getEpicById(int idEpic) {
        if (epicHashMap.containsKey(idEpic)) {
            historyManager.add(epicHashMap.get(idEpic));
        } else System.out.println("Такой задачи нет или она была удалена");
        return epicHashMap.get(idEpic);
    }

    @Override
    public Task createTask(Task task) {
        if (!taskHashMap.containsValue(task)) {
            task.setId(id);
            id++;
            taskHashMap.put(task.getId(), task);
        } else System.out.println("Такая задача уже есть");
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (!subtaskHashMap.containsValue(subtask) && epicHashMap.containsKey(subtask.getIdSubtaskForEpic())) {
            subtask.setId(id);
            id++;
            subtaskHashMap.put(subtask.getId(), subtask);
            epicHashMap.get(subtask.getIdSubtaskForEpic()).setSubtaskForEpic(subtask.getId(), subtask);
            changeStatusEpic(epicHashMap.get(subtask.getIdSubtaskForEpic()));
        } else System.out.println("Такая подзадача уже есть или указан неправильный ID эпика");
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (!epicHashMap.containsValue(epic)) {
            epic.setId(id);
            id++;
            changeStatusEpic(epic);
            epicHashMap.put(epic.getId(), epic);
        } else System.out.println("Такой эпик уже есть");
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        } else System.out.println("Задачи с таким ID нет, добавьте ее как новую");
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId()) &&
                epicHashMap.containsKey(subtask.getIdSubtaskForEpic()) &&
                subtaskHashMap.get(subtask.getId()).getIdSubtaskForEpic() == subtask.getIdSubtaskForEpic()) {

            Epic epic = epicHashMap.get(subtask.getIdSubtaskForEpic());
            subtaskHashMap.put(subtask.getId(), subtask);
            changeStatusEpic(epic);
            epic.setSubtaskForEpic(subtask.getId(), subtask);

        } else if (subtaskHashMap.containsKey(subtask.getId()) &&
                epicHashMap.containsKey(subtask.getIdSubtaskForEpic()) &&
                subtaskHashMap.get(subtask.getId()).getIdSubtaskForEpic() != subtask.getIdSubtaskForEpic()) {

            deleteSubtaskById(subtask.getId());
            subtaskHashMap.put(subtask.getId(), subtask);
            Epic epic = epicHashMap.get(subtask.getIdSubtaskForEpic());
            epic.setSubtaskForEpic(subtask.getId(), subtask);
            changeStatusEpic(epic);

        } else System.out.println("Подзадачи с таким ID нет, добавьте ее как новую или неверно указан ID эпика " +
                "в новой подзадаче");
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            epicHashMap.get(epic.getId()).setName(epic.getName());
            epicHashMap.get(epic.getId()).setDescription(epic.getDescription());
        } else System.out.println("Эпика с таким ID нет, добавьте новый эпик");
    }

    @Override
    public void deleteTaskById(int idTask) {
        if (taskHashMap.containsKey(idTask)) {
            taskHashMap.remove(idTask);
        } else System.out.println("Задачи с таким ID нет");
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        if (subtaskHashMap.containsKey(idSubtask)) {
            subtaskHashMap.remove(idSubtask);
            epicHashMap.get(subtaskHashMap.get(idSubtask).getIdSubtaskForEpic()).removeSubtaskForEpic(idSubtask);
            changeStatusEpic(epicHashMap.get(subtaskHashMap.get(idSubtask).getIdSubtaskForEpic()));
        } else System.out.println("Подзадачи с таким ID нет");
    }

    @Override
    public void deleteEpicById(int idEpic) {
        if (epicHashMap.containsKey(idEpic)) {
            for (int key : getAllSubtaskByEpic(epicHashMap.get(idEpic)).keySet()) {
                subtaskHashMap.remove(key);
            }
            epicHashMap.remove(idEpic);
        } else System.out.println("Эпика с таким ID нет");
    }

    @Override
    public HashMap<Integer, Subtask> getAllSubtaskByEpic(Epic epic) {
        return epic.getSubtaskForEpic();
    }

    @Override
    public void changeStatusEpic(Epic epic) {
        HashMap<Integer, Subtask> listSubtask = getAllSubtaskByEpic(epic);
        if (!listSubtask.isEmpty()) {
            int checkNew = 0;
            int checkDone = 0;

            for (Subtask sub : listSubtask.values()) {
                if (sub.getStatus() == TaskStatus.NEW) {
                    checkNew++;
                } else if (sub.getStatus() == TaskStatus.DONE) {
                    checkDone++;
                }
            }
            if (checkNew == listSubtask.size()) {
                epic.setStatus(TaskStatus.NEW);
            } else if (checkDone == listSubtask.size()) {
                epic.setStatus(TaskStatus.DONE);
            } else epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public String toString() {
        return "manager.Manager{" +
                " ListOfTasks=" + getListOfAllTasks() +
                ", ListOfSubtasks=" + getListOfAllSubtasks() +
                ", ListOfEpic=" + getListOfAllEpic() +
                '}';
    }
}