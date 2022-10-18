package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {

    HashMap<Integer, Subtask> subtaskForEpic = new HashMap<>();

    private final Duration duration = getSumDurationSubtask();
    private final LocalDateTime startTime = getStartTime();
    private final LocalDateTime endTime = getStartTime();
    private final TaskType taskType = TaskType.Epic;

    public HashMap<Integer, Subtask> getSubtaskForEpic() {
        return subtaskForEpic;
    }

    public Duration getSumDurationSubtask() {
        Duration sum = Duration.ofMinutes(0);
        for (Subtask subtask: subtaskForEpic.values()) {
            sum = sum.plus(subtask.getDuration());
        }
        return sum;
    }

    public LocalDateTime getStartTime() {
        LocalDateTime start = LocalDateTime.MAX;
        for (Subtask subtask: subtaskForEpic.values()) {
            if (start.isAfter(subtask.getStartTime())) {
                start = subtask.getStartTime();
            }
        }
        return start;
    }

    public LocalDateTime getEndTime() {
        LocalDateTime end = LocalDateTime.MIN;
        for (Subtask subtask: subtaskForEpic.values()) {
            if (end.isBefore(subtask.getEndTime())) {
                end = subtask.getEndTime();
            }
        }
        return end;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    public void setSubtaskForEpic(Integer id, Subtask subtask) {
        this.subtaskForEpic.put(id, subtask);
    }

    public void removeSubtaskForEpic(int idSubtask) {
        this.subtaskForEpic.remove(idSubtask);
    }

    public void removeAllSubtaskForEpic() {
        this.subtaskForEpic.clear();
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return "task.Epic{" +
                "Name=" + name +
                ", listSubtaskForEpic='" + subtaskForEpic + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}