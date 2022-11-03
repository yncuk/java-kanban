package http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import task.*;

public class FromJsonConverter {

    public static Task fromJson(String tasks, int id) {
        String idString = String.valueOf(id);

        JsonElement jsonElement = JsonParser.parseString(tasks);
        if(!jsonElement.isJsonObject()) {
            System.out.println("Ответ от сервера не соответствует ожидаемому.");
            return null;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject currentTask = jsonObject.get(idString).getAsJsonObject();

        TaskType taskType = TaskType.valueOf(currentTask.get("taskType").getAsString());

        if (taskType.equals(TaskType.Task)) {
            String nameTask = currentTask.get("name").getAsString();
            String descriptionTask = currentTask.get("description").getAsString();
            TaskStatus statusTask = TaskStatus.valueOf(currentTask.get("status").getAsString());
            int idTask = currentTask.get("id").getAsInt();
            Task task = new Task(nameTask, descriptionTask, statusTask);
            task.setId(idTask);
            return task;
        } else if (taskType.equals(TaskType.Subtask)) {
            String nameSubtask = currentTask.get("name").getAsString();
            String descriptionSubtask = currentTask.get("description").getAsString();
            TaskStatus statusSubtask = TaskStatus.valueOf(currentTask.get("status").getAsString());
            int idSubtask = currentTask.get("id").getAsInt();
            int idSubtaskForEpic = currentTask.get("idSubtaskForEpic").getAsInt();
            Subtask subtask = new Subtask(nameSubtask, descriptionSubtask, statusSubtask, idSubtaskForEpic);
            subtask.setId(idSubtask);
            return subtask;
        } else if (taskType.equals(TaskType.Epic)) {
            String nameEpic = currentTask.get("name").getAsString();
            String descriptionEpic = currentTask.get("description").getAsString();
            TaskStatus statusEpic = TaskStatus.valueOf(currentTask.get("status").getAsString());
            int idEpic = currentTask.get("id").getAsInt();
            Epic epic = new Epic(nameEpic, descriptionEpic, statusEpic);
            epic.setId(idEpic);
            return epic;
        } else {
            System.out.println("Ошибка конвертации из Json");
            return null;
        }
    }
}
