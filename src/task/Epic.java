package task;

import java.util.HashMap;

public class Epic extends Task {

    HashMap<Integer, Subtask> subtaskForEpic = new HashMap<>();

    private final TaskType taskType = TaskType.Epic;

    public HashMap<Integer, Subtask> getSubtaskForEpic() {
        return subtaskForEpic;
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