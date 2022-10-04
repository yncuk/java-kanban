package managers.filebacked;

import managers.HistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.stream.Collectors;

public class ToCsvString {

    static public String toStringTask(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription();
    }

    static public String toStringSubtask(Subtask subtask) {
        return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getName() + "," + subtask.getStatus() +
                "," + subtask.getDescription() + ","  + subtask.getIdSubtaskForEpic();
    }

    static public String toStringEpic(Epic epic) {
        return epic.getId() + "," + epic.getTaskType() + "," + epic.getName() + "," + epic.getStatus() + "," +
                epic.getDescription();
    }

    static public String historyToString(HistoryManager manager) {
        return manager.getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
