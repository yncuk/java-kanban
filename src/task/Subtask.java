package task;

import java.util.Objects;

public class Subtask extends Task {

    private final int idSubtaskForEpic;

    public int getIdSubtaskForEpic() {
        return idSubtaskForEpic;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.Subtask;
    }

    public Subtask(String name, String description, TaskStatus status, int idSubtaskForEpic) {
        super(name, description, status);
        this.idSubtaskForEpic = idSubtaskForEpic;
        taskType = TaskType.Subtask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idSubtaskForEpic == subtask.idSubtaskForEpic;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), idSubtaskForEpic);
        result = 31 * result + result;
        return result;
    }

    @Override
    public String toString() {
        return "task.Subtask{" +
                "idSubtaskForEpic=" + idSubtaskForEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtaskType='" + taskType + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}