package managers.inmemory;

import managers.HistoryManager;
import managers.TaskManager;
import managers.factory.Managers;
import task.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicHashMap = new HashMap<>();

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected Set<Task> prioritizedTasks = createPrioritizedTasks();

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
        for (int key : taskHashMap.keySet()) {
            historyManager.remove(key);
        }
        taskHashMap.clear();
    }

    @Override
    public void deleteAllSubtask() {
        for (int key : subtaskHashMap.keySet()) {
            historyManager.remove(key);
        }
        subtaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.removeAllSubtaskForEpic();
            changeStatusEpic(epic);
        }
    }

    @Override
    public void deleteAllEpic() {
        for (int key : epicHashMap.keySet()) {
            historyManager.remove(key);
        }
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
        if (!taskHashMap.containsKey(task.getId()) && checkDuplicateTimeTasks(task)) {
            task.setId(id);
            id++;
            taskHashMap.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else if (taskHashMap.containsKey(task.getId())) {
            System.out.println("Задача с таким ID уже есть, обновите ее");
        }
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (!subtaskHashMap.containsKey(subtask.getId()) && epicHashMap.containsKey(subtask.getIdSubtaskForEpic()) &&
                checkDuplicateTimeTasks(subtask)) {
            subtask.setId(id);
            id++;
            subtaskHashMap.put(subtask.getId(), subtask);
            epicHashMap.get(subtask.getIdSubtaskForEpic()).setSubtaskForEpic(subtask.getId(), subtask);
            changeStatusEpic(epicHashMap.get(subtask.getIdSubtaskForEpic()));
            epicHashMap.get(subtask.getIdSubtaskForEpic()).getDuration();
            epicHashMap.get(subtask.getIdSubtaskForEpic()).getStartTime();
            epicHashMap.get(subtask.getIdSubtaskForEpic()).getEndTime();
            prioritizedTasks.add(subtask);
        } else if (subtaskHashMap.containsKey(subtask.getId())) {
            System.out.println("Подзадача с таким ID уже есть, обновите ее");
        } else if (!epicHashMap.containsKey(subtask.getIdSubtaskForEpic())) {
            System.out.println("Указан неправильный ID эпика");
        }
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (!epicHashMap.containsKey(epic.getId())) {
            epic.setId(id);
            id++;
            changeStatusEpic(epic);
            epicHashMap.put(epic.getId(), epic);
        } else System.out.println("Эпик с таким ID уже есть");
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId()) && checkDuplicateTimeTasks(task)) {
            prioritizedTasks.remove(taskHashMap.get(task.getId()));
            taskHashMap.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else if (!taskHashMap.containsKey(task.getId())) {
            System.out.println("Задачи с таким ID нет, добавьте ее как новую");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId()) &&
                epicHashMap.containsKey(subtask.getIdSubtaskForEpic()) &&
                subtaskHashMap.get(subtask.getId()).getIdSubtaskForEpic() == subtask.getIdSubtaskForEpic() &&
                checkDuplicateTimeTasks(subtask)) {

            Epic epic = epicHashMap.get(subtask.getIdSubtaskForEpic());
            prioritizedTasks.remove(subtaskHashMap.get(subtask.getId()));
            subtaskHashMap.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            changeStatusEpic(epic);
            epic.setSubtaskForEpic(subtask.getId(), subtask);
            epic.getDuration();
            epic.getStartTime();
        } else if (subtaskHashMap.containsKey(subtask.getId()) &&
                epicHashMap.containsKey(subtask.getIdSubtaskForEpic()) &&
                subtaskHashMap.get(subtask.getId()).getIdSubtaskForEpic() != subtask.getIdSubtaskForEpic() &&
                checkDuplicateTimeTasks(subtask)) {

            prioritizedTasks.remove(subtaskHashMap.get(subtask.getId()));
            deleteSubtaskById(subtask.getId());
            subtaskHashMap.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            Epic epic = epicHashMap.get(subtask.getIdSubtaskForEpic());
            epic.setSubtaskForEpic(subtask.getId(), subtask);
            changeStatusEpic(epic);
            epic.getDuration();
            epic.getStartTime();
        } else if (!subtaskHashMap.containsKey(subtask.getId())) {
            System.out.println("Подзадачи с таким ID нет, добавьте ее как новую");
        } else if (!epicHashMap.containsKey(subtask.getIdSubtaskForEpic())) {
            System.out.println("Неверно указан ID эпика в новой подзадаче");
        }
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
            historyManager.remove(idTask);
        } else System.out.println("Задачи с таким ID нет");
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        if (subtaskHashMap.containsKey(idSubtask)) {
            if (epicHashMap.containsKey(subtaskHashMap.get(idSubtask).getIdSubtaskForEpic())) {
                int idSubtaskForEpic = subtaskHashMap.get(idSubtask).getIdSubtaskForEpic();
                epicHashMap.get(idSubtaskForEpic).removeSubtaskForEpic(idSubtask);
                subtaskHashMap.remove(idSubtask);
                changeStatusEpic(epicHashMap.get(idSubtaskForEpic));
            } else {
                subtaskHashMap.remove(idSubtask);
            }
            historyManager.remove(idSubtask);
        } else System.out.println("Подзадачи с таким ID нет");
    }

    @Override
    public void deleteEpicById(int idEpic) {
        if (epicHashMap.containsKey(idEpic)) {
            for (int key : getAllSubtaskByEpic(epicHashMap.get(idEpic)).keySet()) {
                subtaskHashMap.remove(key);
                historyManager.remove(key);
            }
            epicHashMap.remove(idEpic);
            historyManager.remove(idEpic);
        } else System.out.println("Эпика с таким ID нет");
    }

    @Override
    public HashMap<Integer, Subtask> getAllSubtaskByEpic(Epic epic) {
        return epic.getSubtaskForEpic();
    }

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

    private Set<Task> createPrioritizedTasks() {
         Comparator<Task> comparator = Comparator
                 .comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                 .thenComparing(Task::getId);
        return new TreeSet<>(comparator);
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private boolean checkDuplicateTimeTasks(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }
        for (Task taskSort : prioritizedTasks) {
            if (taskSort.getStartTime() == null) {
                return true;
            }
            if (task.getId() != taskSort.getId() && (task.getStartTime().isAfter(taskSort.getStartTime()) ||
                    task.getStartTime().equals(taskSort.getStartTime())) &&
                    task.getStartTime().isBefore(taskSort.getEndTime())) {
                System.out.println("Задача пересекается с временем другой задачи");
                return false;
            }
        }
        return true;
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