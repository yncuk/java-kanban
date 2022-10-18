package task;

import managers.TaskManagerTest;
import managers.inmemory.InMemoryTaskManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EpicStatusTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        initEpic();
    }

    @Test
    @Order(1)
    @DisplayName("0 subtask")
    void epicStatusCalculationWhenNoSubtask() {
        manager.changeStatusEpic(manager.getEpicById(1));
        assertEquals(TaskStatus.NEW, manager.getEpicById(1).getStatus());
    }

    @Test
    @Order(2)
    @DisplayName("All subtask NEW")
    void epicStatusCalculationWhenAllSubtaskNew() {
        manager.createSubtask(new Subtask("Покупка молока", "1л", TaskStatus.NEW, 1));
        manager.createSubtask(new Subtask("Покупка сахара", "1кг", TaskStatus.NEW, 1));
        manager.changeStatusEpic(manager.getEpicById(1));

        assertEquals(TaskStatus.NEW, manager.getEpicById(1).getStatus());
    }

    @Test
    @Order(3)
    @DisplayName("All subtask DONE")
    void epicStatusCalculationWhenAllSubtaskDone() {
        manager.createSubtask(new Subtask("Покупка молока", "1л", TaskStatus.DONE, 1));
        manager.createSubtask(new Subtask("Покупка сахара", "1кг", TaskStatus.DONE, 1));
        manager.changeStatusEpic(manager.getEpicById(1));

        assertEquals(TaskStatus.DONE, manager.getEpicById(1).getStatus());
    }

    @Test
    @Order(4)
    @DisplayName("Subtask NEW and DONE")
    void epicStatusCalculationWhenAllSubtaskNewAndDone() {
        manager.createSubtask(new Subtask("Покупка молока", "1л", TaskStatus.NEW, 1));
        manager.createSubtask(new Subtask("Покупка сахара", "1кг", TaskStatus.DONE, 1));
        manager.changeStatusEpic(manager.getEpicById(1));

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicById(1).getStatus());
    }

    @Test
    @Order(5)
    @DisplayName("All subtask IN_PROGRESS")
    void epicStatusCalculationWhenAllSubtaskInProgress() {
        manager.createSubtask(new Subtask("Покупка молока", "1л", TaskStatus.IN_PROGRESS, 1));
        manager.createSubtask(new Subtask("Покупка сахара", "1кг", TaskStatus.IN_PROGRESS, 1));
        manager.changeStatusEpic(manager.getEpicById(1));

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicById(1).getStatus());
    }
}