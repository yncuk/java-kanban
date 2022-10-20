package task;

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