package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    protected TaskType taskType;

    public LocalDateTime getEndTime() {
        if (duration != null && startTime != null) {
            endTime = startTime.plus(duration);
        }
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = Duration.ofMinutes(duration);
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getTaskType() {
        return TaskType.Task;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.Task;
    }

    @Override
    public String toString() {
        return "task.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", taskType='" + taskType + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}