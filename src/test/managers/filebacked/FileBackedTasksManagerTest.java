package managers.filebacked;

import managers.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = new File("src/dataTest.csv");
        new FileWriter(file);
        manager = new FileBackedTasksManager(file); // С любым названием, возможно это происходит из-за того, что
    }                                               // создаю новые тесты, потом запускаю все и ругается
                                                    // (не так собирается мб), если запускать готовый набор тестов то все ок
    @Test
    @DisplayName("Save test with empty list tasks")
    void saveTestWithEmptyListTasks() {
        manager.getListOfAllTasks();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();
            assertNull(line);
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден", e);
        }
    }

    @Test
    @DisplayName("Save test in standard behavior")
    void saveTestInStandardBehavior() {
        initStandardBehavior();
        manager.getListOfAllTasks();
        manager.getListOfAllSubtasks();
        manager.getListOfAllEpic();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();
            assertEquals("id,type,name,status,duration,startTime,endTime,description,epic", line);
            line = bufferedReader.readLine();
            assertEquals("1,Task,Купить хлеб,NEW,PT30M,2022-09-01T12:00,2022-09-01T12:30,В магните", line);
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден", e);
        }
    }

    @Test
    @DisplayName("Load from file with empty list tasks")
    void loadFromFileWithEmptyListTasks() {
        manager.getListOfAllTasks();
        FileBackedTasksManager fileBackedTasksManage = FileBackedTasksManager.loadFromFile(file);
        assertAll(
                () -> assertEquals(0, fileBackedTasksManage.getListOfAllTasks().size()),
                () -> assertEquals(0, fileBackedTasksManage.getListOfAllSubtasks().size()),
                () -> assertEquals(0, fileBackedTasksManage.getListOfAllEpic().size()),
                () -> assertEquals(0, fileBackedTasksManage.getHistory().size())
        );
    }

    @Test
    @DisplayName("Load from file with standard behavior")
    void loadFromFileWithStandardBehavior() {
        initStandardBehavior();
        manager.getListOfAllTasks();
        manager.getListOfAllSubtasks();
        manager.getListOfAllEpic();
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(4);

        FileBackedTasksManager fileBackedTasksManage = FileBackedTasksManager.loadFromFile(file);
        assertAll(
                () -> assertEquals(2, fileBackedTasksManage.getListOfAllTasks().size()),
                () -> assertEquals(3, fileBackedTasksManage.getListOfAllSubtasks().size()),
                () -> assertEquals(2, fileBackedTasksManage.getListOfAllEpic().size()),
                () -> assertEquals(3, fileBackedTasksManage.getHistory().size())
        );
    }

    @AfterEach
    void cleanUp() {
        file.delete();
    }
}