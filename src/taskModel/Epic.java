package taskModel;
import java.util.HashMap;

public class Epic extends Task {

    HashMap<Integer, Subtask> subtaskForEpic = new HashMap<>();

    public HashMap<Integer, Subtask> getSubtaskForEpic() {
        return subtaskForEpic;
    }

    public void setSubtaskForEpic(Integer id, Subtask subtask) {
        this.subtaskForEpic.put(id, subtask);
    }

    public void removeSubtaskForEpic(int idSubtask) {
        this.subtaskForEpic.remove(idSubtask);
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return "taskModel.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}