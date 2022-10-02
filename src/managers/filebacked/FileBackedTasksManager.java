package managers.filebacked;

import managers.HistoryManager;
import managers.inmemory.InMemoryTaskManager;
import task.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    static void main(String[] args) { // Не совсем понял, зачем нужен этот метод в классе
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("src/data.csv"));
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + ",";
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();

            for (Task task : taskHashMap.values()) {
                bufferedWriter.write(toString(task));
                bufferedWriter.newLine();
            }
            for (Subtask subtask : subtaskHashMap.values()) {
                bufferedWriter.write(toString(subtask) + subtask.getIdSubtaskForEpic());
                bufferedWriter.newLine();
            }
            for (Epic epic : epicHashMap.values()) {
                bufferedWriter.write(toString(epic));
                bufferedWriter.newLine();
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            new ManagerSaveException("Файл не найден");
        }
    }

    private static class ManagerSaveException extends IOException {
        public ManagerSaveException(final String message) {
            super(message);
        }
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder lineOfHistory = new StringBuilder();
        for (Task task : manager.getHistory()) {
            lineOfHistory.append(task.getId()).append(",");
        }
        return lineOfHistory.toString();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                System.out.println(line);
                if (line.startsWith("id") || line.isBlank()) {
                    System.out.println("Начальная или пробельная строка");
                } else if (fromString(line) == null) {
                    List<Integer> list = historyFromString(line);
                    for (Integer i : list) {
                        if (fileBackedTasksManager.taskHashMap.containsKey(i)) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.taskHashMap.get(i));
                        } else if (fileBackedTasksManager.subtaskHashMap.containsKey(i)) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtaskHashMap.get(i));
                        } else if (fileBackedTasksManager.epicHashMap.containsKey(i)) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epicHashMap.get(i));
                        } else System.out.println("Такой задачи нет");
                    }
                    System.out.println("История сформирована");
                } else if (fromString(line).getTaskType().equals(TaskType.Task)) {
                    fileBackedTasksManager.taskHashMap.put(fromString(line).getId(), fromString(line));
                } else if (fromString(line).getTaskType().equals(TaskType.Subtask)) {
                    fileBackedTasksManager.subtaskHashMap.put(fromString(line).getId(), (Subtask) fromString(line));
                } else if (fromString(line).getTaskType().equals(TaskType.Epic)) {
                    fileBackedTasksManager.epicHashMap.put(fromString(line).getId(), (Epic) fromString(line));
                } else System.out.println("Неверный тип задачи");
            }
        } catch (IOException e) {
            new ManagerSaveException("Файл не найден");
        }
        return fileBackedTasksManager;
    }

    static Task fromString(String value) {
        String[] split = value.split(",");
        Task task;
        if (isNumeric(split[1])) {
            task = null;
        } else if (TaskType.valueOf(split[1]).equals(TaskType.Task)) {
            task = new Task(split[2], split[4], TaskStatus.valueOf(split[3]));
            task.setId(Integer.parseInt(split[0]));
            task.setTaskType(TaskType.valueOf(split[1]));
        } else if (TaskType.valueOf(split[1]).equals(TaskType.Subtask)) {
            task = new Subtask(split[2], split[4], TaskStatus.valueOf(split[3]), Integer.parseInt(split[5]));
            task.setId(Integer.parseInt(split[0]));
            task.setTaskType(TaskType.valueOf(split[1]));
        } else {
            task = new Epic(split[2], split[4], TaskStatus.valueOf(split[3]));
            task.setId(Integer.parseInt(split[0]));
            task.setTaskType(TaskType.valueOf(split[1]));
        }
        return task;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        List<Integer> history = new LinkedList<>();
        for (String current : split) {
            history.add(Integer.parseInt(current));
        }
        return history;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllTypeTasks() {
        super.deleteAllTypeTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int idTask) {
        super.deleteTaskById(idTask);
        save();
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        super.deleteSubtaskById(idSubtask);
        save();
    }

    @Override
    public void deleteEpicById(int idEpic) {
        super.deleteEpicById(idEpic);
        save();
    }

    @Override
    public Task getTaskById(int idTask) {
        super.getTaskById(idTask);
        save();
        return taskHashMap.get(idTask);
    }

    @Override
    public Subtask getSubtaskById(int idSubtask) {
        super.getSubtaskById(idSubtask);
        save();
        return subtaskHashMap.get(idSubtask);
    }

    @Override
    public Epic getEpicById(int idEpic) {
        super.getEpicById(idEpic);
        save();
        return epicHashMap.get(idEpic);
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }
}
