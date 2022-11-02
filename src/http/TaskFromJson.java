package http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

public class TaskFromJson {

    public static Task taskFromJson(String tasks, int id) {
        String idString = String.valueOf(id);

        JsonElement jsonElement = JsonParser.parseString(tasks);
        if(!jsonElement.isJsonObject()) {
            System.out.println("Ответ от сервера не соответствует ожидаемому.");
            return null;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject sub = jsonObject.get(idString).getAsJsonObject();
        String nameSub = sub.get("name").getAsString();
        String descriptionSub = sub.get("description").getAsString();
        String statusSub = sub.get("status").getAsString();
        TaskStatus subtaskStatus = TaskStatus.valueOf(statusSub);
        int idSubtask = sub.get("id").getAsInt();
        Task task = new Task(nameSub, descriptionSub, subtaskStatus);
        task.setId(idSubtask);
        return task;
    }

    public static Subtask subtaskFromJson(String subtasks, int id) {
        String idString = String.valueOf(id);

        JsonElement jsonElement = JsonParser.parseString(subtasks);
        if(!jsonElement.isJsonObject()) {
            System.out.println("Ответ от сервера не соответствует ожидаемому.");
            return null;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject sub = jsonObject.get(idString).getAsJsonObject();
        String nameSub = sub.get("name").getAsString();
        String descriptionSub = sub.get("description").getAsString();
        String statusSub = sub.get("status").getAsString();
        TaskStatus subtaskStatus = TaskStatus.valueOf(statusSub);
        int idSubtask = sub.get("id").getAsInt();
        int idSubtaskForEpic = sub.get("idSubtaskForEpic").getAsInt();
        Subtask subtask = new Subtask(nameSub, descriptionSub, subtaskStatus, idSubtaskForEpic);
        subtask.setId(idSubtask);
        return subtask;
    }

    public static Epic epicFromJson(String epics, int id) {
        String idString = String.valueOf(id);

        JsonElement jsonElement = JsonParser.parseString(epics);
        if(!jsonElement.isJsonObject()) {
            System.out.println("Ответ от сервера не соответствует ожидаемому.");
            return null;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject sub = jsonObject.get(idString).getAsJsonObject();
        String nameSub = sub.get("name").getAsString();
        String descriptionSub = sub.get("description").getAsString();
        String statusSub = sub.get("status").getAsString();
        TaskStatus subtaskStatus = TaskStatus.valueOf(statusSub);
        int idSubtask = sub.get("id").getAsInt();
        Epic epic = new Epic(nameSub, descriptionSub, subtaskStatus);
        epic.setId(idSubtask);
        return epic;
    }
}
