package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {

    HashMap<Integer, Subtask> subtaskForEpic = new HashMap<>();

    public HashMap<Integer, Subtask> getSubtaskForEpic() {
        return subtaskForEpic;
    }

    public Duration getDuration() {
        duration = Duration.ofMinutes(0);
        for (Subtask subtask: subtaskForEpic.values()) {
            if (subtask.getDuration() != null) {
                duration = duration.plus(subtask.getDuration());
            }
        }
        return duration;
    }

    public LocalDateTime getStartTime() {
        startTime = LocalDateTime.MAX;
        for (Subtask subtask: subtaskForEpic.values()) {
            if (subtask.getStartTime() != null) {
                if (startTime.isAfter(subtask.getStartTime())) {
                    startTime = subtask.getStartTime();
                }
            }
        }
        return startTime;
    }

    public LocalDateTime getEndTime() {
        endTime = LocalDateTime.MIN;
        for (Subtask subtask : subtaskForEpic.values()) {
            if (subtask.getEndTime() != null) {
                if (endTime.isBefore(subtask.getEndTime())) {
                    endTime = subtask.getEndTime();
                }
            }
        }
        return endTime;
    }

    public void setDuration(int duration) {
        super.setDuration(duration);
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        super.setEndTime(endTime);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.Epic;
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
        taskType = TaskType.Epic;
    }

    @Override
    public String toString() {
        return "task.Epic{" +
                "Name=" + name +
                ", listSubtaskForEpic='" + subtaskForEpic + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicType='" + taskType + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}