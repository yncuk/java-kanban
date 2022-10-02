package task;

public class Subtask extends Task {

    private final int idSubtaskForEpic;

    private final TaskType taskType = TaskType.Subtask;

    public int getIdSubtaskForEpic() {
        return idSubtaskForEpic;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    public Subtask(String name, String description, TaskStatus status, int idSubtaskForEpic) {
        super(name, description, status);
        this.idSubtaskForEpic = idSubtaskForEpic;
    }

    @Override
    public String toString() {
        return "task.Subtask{" +
                "idSubtaskForEpic=" + idSubtaskForEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}