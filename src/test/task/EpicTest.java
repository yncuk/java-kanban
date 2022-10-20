package task;

import managers.TaskManagerTest;
import managers.inmemory.InMemoryTaskManager;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EpicTest extends TaskManagerTest<InMemoryTaskManager> {

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

    @Test
    @Order(6)
    @DisplayName("Check Duration and Start/End time")
    void epicCheckDurationAndStartEndTime() {
        Subtask subtask = new Subtask("Покупка сахара", "1кг", TaskStatus.DONE, 1);
        subtask.setStartTime(LocalDateTime.of(2021, Month.OCTOBER,24,11,0));
        subtask.setDuration(30);
        manager.createSubtask(subtask);
        subtask = new Subtask("Покупка молока", "1л", TaskStatus.IN_PROGRESS, 1);
        subtask.setStartTime(LocalDateTime.of(2022, Month.DECEMBER,30,15,0));
        subtask.setDuration(60);
        manager.createSubtask(subtask);

        assertAll(
                () -> assertEquals(Duration.parse("PT1H30M") ,
                        manager.getEpicById(1).getDuration()),
                () -> assertEquals(LocalDateTime.parse("2021-10-24T11:00") ,
                        manager.getEpicById(1).getStartTime()),
                () -> assertEquals(LocalDateTime.parse("2022-12-30T16:00"),
                        manager.getEpicById(1).getEndTime())
        );
    }
}