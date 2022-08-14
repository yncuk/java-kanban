package manager;

import task.*;

import java.util.HashMap;

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
        for (Integer key : epicHashMap.keySet()) {
            epicHashMap.get(key).removeAllSubtaskForEpic();
            changeStatusEpic(epicHashMap.get(key));
        }
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
    //Очень толковые комментарии)) спасибо!
    public Task getTaskById(int idTask) {
        return taskHashMap.get(idTask);
    }

    public Subtask getSubtaskById(int idSubtask) {
        return subtaskHashMap.get(idSubtask);
    }

    public Epic getEpicById(int idEpic) {
        return epicHashMap.get(idEpic);
    }

    public Task createTask(Task task) {
        if (!taskHashMap.containsValue(task)) {
            task.setId(id);
            id++;
            taskHashMap.put(task.getId(), task);
        } else System.out.println("Такая задача уже есть");
        return task;
    }

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

    public Epic createEpic(Epic epic) {
        if (!epicHashMap.containsValue(epic)) {
            epic.setId(id);
            id++;
            changeStatusEpic(epic);
            epicHashMap.put(epic.getId(), epic);
        } else System.out.println("Такой эпик уже есть");
        return epic;
    }

    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        } else System.out.println("Задачи с таким ID нет, добавьте ее как новую");
    }

    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId()) &&
                epicHashMap.containsKey(subtask.getIdSubtaskForEpic()) &&
                subtaskHashMap.get(subtask.getId()).getIdSubtaskForEpic() == subtask.getIdSubtaskForEpic()) {

            Epic epic = getEpicById(subtask.getIdSubtaskForEpic());
            subtaskHashMap.put(subtask.getId(), subtask);
            changeStatusEpic(epic);
            epic.setSubtaskForEpic(subtask.getId(), subtask);

        } else if (subtaskHashMap.containsKey(subtask.getId()) &&
                epicHashMap.containsKey(subtask.getIdSubtaskForEpic()) &&
                subtaskHashMap.get(subtask.getId()).getIdSubtaskForEpic() != subtask.getIdSubtaskForEpic()) {

            deleteSubtaskById(subtask.getId());
            subtaskHashMap.put(subtask.getId(), subtask);
            Epic epic = getEpicById(subtask.getIdSubtaskForEpic());
            epic.setSubtaskForEpic(subtask.getId(), subtask);
            changeStatusEpic(epic);

        } else System.out.println("Подзадачи с таким ID нет, добавьте ее как новую или неверно указан ID эпика " +
                "в новой подзадаче");
    }

    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            epicHashMap.get(epic.getId()).setName(epic.getName());
            epicHashMap.get(epic.getId()).setDescription(epic.getDescription());
        } else System.out.println("Эпика с таким ID нет, добавьте новый эпик");
    }

    public void deleteTaskById(int idTask) {
        if (taskHashMap.containsKey(idTask)) {
            taskHashMap.remove(idTask);
        } else System.out.println("Задачи с таким ID нет");
    }

    public void deleteSubtaskById(int idSubtask) {
        if (subtaskHashMap.containsKey(idSubtask)) {
            subtaskHashMap.remove(idSubtask);
            getEpicById(getSubtaskById(idSubtask).getIdSubtaskForEpic()).removeSubtaskForEpic(idSubtask);
            changeStatusEpic(getEpicById(getSubtaskById(idSubtask).getIdSubtaskForEpic()));
        } else System.out.println("Подзадачи с таким ID нет");
    }

    public void deleteEpicById(int idEpic) {
        if (epicHashMap.containsKey(idEpic)) {
            for (int key : getAllSubtaskByEpic(getEpicById(idEpic)).keySet()) {
                subtaskHashMap.remove(key);
            }
            epicHashMap.remove(idEpic);
        } else System.out.println("Эпика с таким ID нет");
    }

    public HashMap<Integer, Subtask> getAllSubtaskByEpic(Epic epic) {
        return epic.getSubtaskForEpic();
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
                epic.setStatus("NEW");
            } else if (checkDone == listSubtask.size()) {
                epic.setStatus("DONE");
            } else epic.setStatus("IN_PROGRESS");
        } else {
            epic.setStatus("NEW");
        }
    }

    @Override
    public String toString() {
        return "manager.Manager{" +
                "Список обычных задач=" + getListOfAllTasks() +
                ", список подзадач=" + getListOfAllSubtasks() +
                ", список эпиков=" + getListOfAllEpic() +
                '}';
    }
}