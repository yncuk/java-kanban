package managers.filebacked;

import managers.HistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.stream.Collectors;

public class ToCsvConverter {

    static public String toCsvString(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDuration() + "," + task.getStartTime() + "," + task.getEndTime() + "," + task.getDescription();
    }

    static public String toCsvString(Subtask subtask) {
        return subtask.getId() + "," + subtask.getTaskType() + "," + subtask.getName() + "," + subtask.getStatus() +
                "," + subtask.getDuration() + "," + subtask.getStartTime() + "," + subtask.getEndTime() +
                "," + subtask.getDescription() + ","  + subtask.getIdSubtaskForEpic();
    }

    static public String toCsvString(Epic epic) {
        return epic.getId() + "," + epic.getTaskType() + "," + epic.getName() + "," + epic.getStatus() + "," +
                epic.getDuration() + "," + epic.getStartTime() + "," + epic.getEndTime() + "," + epic.getDescription();
    }

    static public String historyToString(HistoryManager manager) {
        return manager.getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
