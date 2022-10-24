package managers;

import task.*;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    HashMap<Integer, Task> getListOfAllTasks();

    HashMap<Integer, Subtask> getListOfAllSubtasks();

    HashMap<Integer, Epic> getListOfAllEpic();

    void deleteAllTasks();

    void deleteAllSubtask();

    void deleteAllEpic();

    void deleteAllTypeTasks();

    Task getTaskById(int idTask);

    Subtask getSubtaskById(int idSubtask);

    Epic getEpicById(int idEpic);

    Task createTask(Task task);

    Subtask createSubtask(Subtask subtask);

    Epic createEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void deleteTaskById(int idTask);

    void deleteSubtaskById(int idSubtask);

    void deleteEpicById(int idEpic);

    HashMap<Integer, Subtask> getAllSubtaskByEpic(Epic epic);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}