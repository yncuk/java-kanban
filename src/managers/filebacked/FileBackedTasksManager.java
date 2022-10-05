package managers.filebacked;

import managers.inmemory.InMemoryTaskManager;
import task.*;

import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();
            for (Task task : taskHashMap.values()) {
                bufferedWriter.write(ToCsvConverter.toCsvString(task));
                bufferedWriter.newLine();
            }
            for (Subtask subtask : subtaskHashMap.values()) {
                bufferedWriter.write(ToCsvConverter.toCsvString(subtask));
                bufferedWriter.newLine();
            }
            for (Epic epic : epicHashMap.values()) {
                bufferedWriter.write(ToCsvConverter.toCsvString(epic));
                bufferedWriter.newLine();
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(ToCsvConverter.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден", e);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                System.out.println(line);
                line = bufferedReader.readLine();
                while (!line.isBlank()) {
                    System.out.println(line);
                    Task task = FromCsvConverter.fromString(line);
                    if (task.getTaskType().equals(TaskType.Task)) {
                        fileBackedTasksManager.taskHashMap.put(task.getId(), task);
                    } else if (task.getTaskType().equals(TaskType.Subtask)) {
                        fileBackedTasksManager.subtaskHashMap.put(task.getId(), (Subtask) task);
                    } else if (task.getTaskType().equals(TaskType.Epic)) {
                        fileBackedTasksManager.epicHashMap.put(task.getId(), (Epic) task);
                    } else System.out.println("Неверный тип задачи");
                    line = bufferedReader.readLine();
                }
                System.out.println(line);
                line = bufferedReader.readLine();
                System.out.println(line);
                List<Integer> list = FromCsvConverter.historyFromString(line);
                for (Integer i : list) {
                    addToHistory(i, fileBackedTasksManager);
                }
                System.out.println("История сформирована");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден", e);
        }
        return fileBackedTasksManager;
    }

    private static void addToHistory(Integer i, FileBackedTasksManager fileBackedTasksManager) {
        if (fileBackedTasksManager.taskHashMap.containsKey(i)) {
            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.taskHashMap.get(i));
        } else if (fileBackedTasksManager.subtaskHashMap.containsKey(i)) {
            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtaskHashMap.get(i));
        } else if (fileBackedTasksManager.epicHashMap.containsKey(i)) {
            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epicHashMap.get(i));
        } else System.out.println("Такой задачи нет");
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
    // понял, спасибо :)
    @Override
    public Task getTaskById(int idTask) {
        Task task = super.getTaskById(idTask);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int idSubtask) {
        Subtask subtask = super.getSubtaskById(idSubtask);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(int idEpic) {
        Epic epic = super.getEpicById(idEpic);
        save();
        return epic;
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask newSubtask = super.createSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }
}
