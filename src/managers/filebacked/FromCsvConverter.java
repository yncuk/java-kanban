package managers.filebacked;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FromCsvConverter {
    public static Task fromString(String value) {
        String[] split = value.split(",");
        Task task;
        if (TaskType.valueOf(split[1]).equals(TaskType.Task)) {
            task = new Task(split[2], split[7], TaskStatus.valueOf(split[3]));
            task.setId(Integer.parseInt(split[0]));
            task.setTaskType(TaskType.valueOf(split[1]));
            task.setDuration(Duration.parse(split[4]));
            task.setStartTime(LocalDateTime.parse(split[5]));
            task.setEndTime(LocalDateTime.parse(split[6]));
        } else if (TaskType.valueOf(split[1]).equals(TaskType.Subtask)) {
            task = new Subtask(split[2], split[7], TaskStatus.valueOf(split[3]), Integer.parseInt(split[8]));
            task.setId(Integer.parseInt(split[0]));
            task.setTaskType(TaskType.valueOf(split[1]));
            task.setDuration(Duration.parse(split[4]));
            task.setStartTime(LocalDateTime.parse(split[5]));
            task.setEndTime(LocalDateTime.parse(split[6]));
        } else {
            task = new Epic(split[2], split[7], TaskStatus.valueOf(split[3]));
            task.setId(Integer.parseInt(split[0]));
            task.setTaskType(TaskType.valueOf(split[1]));
            task.setDuration(Duration.parse(split[4]));
            task.setStartTime(LocalDateTime.parse(split[5]));
            task.setEndTime(LocalDateTime.parse(split[6]));
        }
        return task;
    }

    public static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String current : split) {
            history.add(Integer.parseInt(current));
        }
        return history;
    }
}
