package managers.filebacked;

import managers.inmemory.InMemoryTaskManager;
import task.*;

import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    //Да он был в задании как рекомендация
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();
            for (Task task : taskHashMap.values()) {
                bufferedWriter.write(ToCsvString.toStringTask(task));
                bufferedWriter.newLine();
            }
            for (Subtask subtask : subtaskHashMap.values()) {
                bufferedWriter.write(ToCsvString.toStringSubtask(subtask));
                bufferedWriter.newLine();
            }
            for (Epic epic : epicHashMap.values()) {
                bufferedWriter.write(ToCsvString.toStringEpic(epic));
                bufferedWriter.newLine();
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(ToCsvString.historyToString(historyManager));
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
                    Task task = ToStringCsv.fromString(line);
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
                List<Integer> list = ToStringCsv.historyFromString(line);
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
            }
        } catch (IOException e) {
            throw  new ManagerSaveException("Файл не найден", e);
        }
        return fileBackedTasksManager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllTypeTasks() {
        super.deleteAllTypeTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTaskById(int idTask) {
        super.deleteTaskById(idTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        super.deleteSubtaskById(idSubtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEpicById(int idEpic) {
        super.deleteEpicById(idEpic);
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }
    // если возвращать super, то последнее действие не попадет в файл, так как save() нужно писать до
    // return, может можно как то по другому сделать?
    @Override
    public Task getTaskById(int idTask) {
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.getTaskById(idTask);
    }

    @Override
    public Subtask getSubtaskById(int idSubtask) {
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.getSubtaskById(idSubtask);
    }

    @Override
    public Epic getEpicById(int idEpic) {
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.getEpicById(idEpic);
    }

    @Override
    public Task createTask(Task task) {
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.createTask(task);
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.createSubtask(subtask);
    }

    @Override
    public Epic createEpic(Epic epic) {
        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        return super.createEpic(epic);
    }
}
