package managers.inmemory;

import managers.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    @DisplayName("Standard behavior Get list of all tasks")
    void getListOfAllTasksStandardBehavior() {
        initStandardBehavior();
        assertAll(
                () -> assertEquals(manager.taskHashMap, manager.getListOfAllTasks()),
                () -> assertEquals(2, manager.getListOfAllTasks().size()),
                () -> assertEquals(manager.subtaskHashMap, manager.getListOfAllSubtasks()),
                () -> assertEquals(3, manager.getListOfAllSubtasks().size()),
                () -> assertEquals(manager.epicHashMap, manager.getListOfAllEpic()),
                () -> assertEquals(2, manager.getListOfAllEpic().size())
        );
    }

    @Test
    @DisplayName("Empty list tasks Get list of all tasks")
    void getListOfAllTasksEmptyListTasks() {
        assertAll(
                () -> assertEquals(0, manager.getListOfAllTasks().size()),
                () -> assertEquals(0, manager.getListOfAllSubtasks().size()),
                () -> assertEquals(0, manager.getListOfAllEpic().size())
        );
    }

    @Test
    @DisplayName("Standard behavior Delete all tasks")
    void deleteAllTasksStandardBehavior() {
        initStandardBehavior();
        manager.deleteAllTasks();
        assertEquals(0, manager.taskHashMap.size());
        manager.deleteAllSubtask();
        assertEquals(0, manager.subtaskHashMap.size());
        manager.deleteAllEpic();
        assertEquals(0, manager.epicHashMap.size());
    }

    @Test
    @DisplayName("Empty list tasks Delete all tasks")
    void deleteAllTasksEmptyListTasks() {
        manager.deleteAllTasks();
        assertEquals(0, manager.taskHashMap.size());
        manager.deleteAllSubtask();
        assertEquals(0, manager.subtaskHashMap.size());
        manager.deleteAllEpic();
        assertEquals(0, manager.epicHashMap.size());
    }

    @Test
    @DisplayName("Standard behavior Delete all type tasks")
    void deleteAllTypeTasksStandardBehavior() {
        initStandardBehavior();
        manager.deleteAllTypeTasks();
        getListOfAllTasksEmptyListTasks();
    }

    @Test
    @DisplayName("Empty list tasks Delete all type tasks")
    void deleteAllTypeTasksEmptyListTasks() {
        manager.deleteAllTypeTasks();
        getListOfAllTasksEmptyListTasks();
    }

    @Test
    @DisplayName("Standard behavior Get task by ID")
    void getTaskByIdStandardBehavior() {
        initStandardBehavior();
        Task task = manager.getTaskById(1);
        Subtask subtask = manager.getSubtaskById(4);
        Epic epic = manager.getEpicById(3);
        assertAll(
                () -> assertEquals(manager.taskHashMap.get(1), task),
                () -> assertEquals(manager.subtaskHashMap.get(4), subtask),
                () -> assertEquals(manager.epicHashMap.get(3), epic)
        );
    }

    @Test
    @DisplayName("Empty list tasks Get task by ID")
    void getTaskByIdEmptyListTasks() {
        Task task = manager.getTaskById(1);
        Subtask subtask = manager.getSubtaskById(4);
        Epic epic = manager.getEpicById(3);
        assertAll(
                () -> assertNull(task),
                () -> assertNull(subtask),
                () -> assertNull(epic)
        );
    }

    @Test
    @DisplayName("Standard behavior Create task")
    void createTaskStandardBehavior() {
        getListOfAllTasksStandardBehavior();
        manager.createTask(new Task("Купить соду", "В петровском", TaskStatus.IN_PROGRESS));
        manager.createEpic(new Epic("Поход в кафе", "В торговом центре", TaskStatus.NEW));
        manager.createSubtask(new Subtask("Покупка кофе", "1 кружка", TaskStatus.NEW, 9));
        assertAll(
                () -> assertEquals(3, manager.taskHashMap.size()),
                () -> assertEquals(4, manager.subtaskHashMap.size()),
                () -> assertEquals(3, manager.epicHashMap.size())
        );
    }

    @Test
    @DisplayName("Empty list tasks Create task")
    void createTaskEmptyListTasks() {
        getListOfAllTasksEmptyListTasks();
        manager.createTask(new Task("Купить соду", "В петровском", TaskStatus.IN_PROGRESS));
        manager.createEpic(new Epic("Поход в кафе", "В торговом центре", TaskStatus.NEW));
        manager.createSubtask(new Subtask("Покупка кофе", "1 кружка", TaskStatus.NEW, 2));
        assertAll(
                () -> assertEquals(1, manager.taskHashMap.size()),
                () -> assertEquals(1, manager.subtaskHashMap.size()),
                () -> assertEquals(1, manager.epicHashMap.size())
        );
    }

    @Test
    @DisplayName("Invalid ID subtasks Create task")
    void createTaskInvalidIdSubtasks() {
        getListOfAllTasksStandardBehavior();
        manager.createSubtask(new Subtask("Покупка кофе", "1 кружка", TaskStatus.NEW, 2));
        assertEquals(3, manager.subtaskHashMap.size());
    }

    @Test
    @DisplayName("Standard behavior Update task")
    void updateTaskStandardBehavior() {
        initStandardBehavior();
        Task task = new Task("Купить соду", "В петровском", TaskStatus.IN_PROGRESS);
        Epic epic = new Epic("Поход в кафе", "В торговом центре", TaskStatus.NEW);
        Subtask subtask = new Subtask("Покупка кофе", "1 кружка", TaskStatus.NEW, 3);
        task.setId(1);
        epic.setId(3);
        subtask.setId(4);
        manager.updateTask(task);
        manager.updateSubtask(subtask);
        manager.updateEpic(epic);
        assertAll(
                () -> assertEquals(task.getId(), manager.taskHashMap.get(1).getId()),
                () -> assertEquals(subtask.getId(), manager.subtaskHashMap.get(4).getId()),
                () -> assertEquals(epic.getId(), manager.epicHashMap.get(3).getId())
        );
    }

    @Test
    @DisplayName("Empty list tasks Update task")
    void updateTaskEmptyListTasks() {
        getListOfAllTasksEmptyListTasks();
        Task task = new Task("Купить соду", "В петровском", TaskStatus.IN_PROGRESS);
        Epic epic = new Epic("Поход в кафе", "В торговом центре", TaskStatus.NEW);
        Subtask subtask = new Subtask("Покупка кофе", "1 кружка", TaskStatus.NEW, 3);
        task.setId(1);
        epic.setId(3);
        subtask.setId(4);
        manager.updateTask(task);
        manager.updateSubtask(subtask);
        manager.updateEpic(epic);
        assertAll(
                () -> assertEquals(0, manager.taskHashMap.size()),
                () -> assertEquals(0, manager.subtaskHashMap.size()),
                () -> assertEquals(0, manager.epicHashMap.size())
        );
    }

    @Test
    @DisplayName("Standard behavior Delete task")
    void deleteTaskByIdStandardBehavior() {
        initStandardBehavior();
        manager.deleteTaskById(1);
        manager.deleteSubtaskById(4);
        manager.deleteEpicById(3);
        assertAll(
                () -> assertEquals(1, manager.taskHashMap.size()),
                () -> assertEquals(1, manager.subtaskHashMap.size()),
                () -> assertEquals(1, manager.epicHashMap.size())
        );
    }

    @Test
    @DisplayName("Empty list tasks Delete task")
    void deleteTaskByIdEmptyListTasks() {
        getListOfAllTasksEmptyListTasks();
        manager.deleteTaskById(1);
        manager.deleteSubtaskById(4);
        manager.deleteEpicById(3);
        assertAll(
                () -> assertEquals(0, manager.taskHashMap.size()),
                () -> assertEquals(0, manager.subtaskHashMap.size()),
                () -> assertEquals(0, manager.epicHashMap.size())
        );
    }

    @Test
    @DisplayName("Standard behavior Get all subtask by epic")
    void getAllSubtaskByEpicStandardBehavior() {
        initStandardBehavior();
        assertEquals(2, manager.getAllSubtaskByEpic(manager.epicHashMap.get(3)).size());
    }

    @Test
    @DisplayName("Empty list tasks Get all subtask by epic")
    void getAllSubtaskByEpicEmptyListTasks() {
        getListOfAllTasksEmptyListTasks();
        assertNull(manager.epicHashMap.get(3));
    }

    @Test
    @DisplayName("Standard behavior Get history")
    void getHistoryStandardBehavior() {
        initStandardBehavior();
        manager.getTaskById(1);
        manager.getTaskById(2);
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    @DisplayName("Empty list tasks Get history")
    void getHistoryEmptyListTasks() {
        manager.getTaskById(1);
        manager.getTaskById(2);
        assertEquals(0, manager.getHistory().size());
    }
}