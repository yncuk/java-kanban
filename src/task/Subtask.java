package task;

public class Subtask extends Task {

    private final int idSubtaskForEpic;

    public int getIdSubtaskForEpic() {
        return idSubtaskForEpic;
    }

    public Subtask(String name, String description, String status, int idSubtaskForEpic) {
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