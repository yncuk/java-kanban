package managers.inmemory;

import managers.HistoryManagerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    @DisplayName("Add tasks in history")
    void addTest() {
        // given (BeforeEach)
        // when
        assertEquals(0, historyManager.customLinkedList.getTasks().size());
        historyManager.customLinkedList.linkLast(new Task("Купить хлеб", "В магните", TaskStatus.NEW));
        // then
        assertEquals(1, historyManager.customLinkedList.getTasks().size());
    }

    @Test
    @DisplayName("Add tasks in history with duplicate")
    void addTestWithDuplicate() {
        // given (BeforeEach)
        // when
        assertEquals(0, historyManager.customLinkedList.getTasks().size());
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        historyManager.customLinkedList.linkLast(task);
        assertEquals(1, historyManager.customLinkedList.getTasks().size());
        // then
        historyManager.customLinkedList.linkLast(task);
        assertEquals(1, historyManager.customLinkedList.getTasks().size());
    }

    @Test
    @DisplayName("Get history")
    void getHistoryTest() {
        // given (BeforeEach)
        // when
        historyManager.customLinkedList.linkLast(new Task("Купить хлеб", "В магните", TaskStatus.NEW));
        // then
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Get history from empty history")
    void getHistoryTestFromEmptyHistory() {
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Remove tasks from history")
    void removeTest() {
        // given (BeforeEach)
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        task.setId(1);
        // when
        historyManager.customLinkedList.linkLast(task);
        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(1);
        // then
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    @DisplayName("Remove tasks from history from different positions")
    void removeTestFromDifferentPositions() {
        // given (BeforeEach)
        Task task = new Task("Купить хлеб", "В магните", TaskStatus.NEW);
        Task task2 = new Task("Купить хлеб1", "В магните1", TaskStatus.DONE);
        Task task3 = new Task("Купить хлеб2", "В магните2", TaskStatus.DONE);
        Task task4 = new Task("Купить хлеб3", "В магните3", TaskStatus.NEW);
        task.setId(1);
        task2.setId(2);
        task3.setId(3);
        task4.setId(4);
        // when
        historyManager.customLinkedList.linkLast(task);
        historyManager.customLinkedList.linkLast(task2);
        historyManager.customLinkedList.linkLast(task3);
        historyManager.customLinkedList.linkLast(task4);
        List<Task> taskList = historyManager.customLinkedList.getTasks();
        // then
        assertEquals(4, taskList.size());
        historyManager.remove(1);
        taskList = historyManager.customLinkedList.getTasks();
        assertEquals(3, taskList.size());
        assertTrue(taskList.contains(task2));
        assertTrue(taskList.contains(task3));
        assertTrue(taskList.contains(task4));

        historyManager.remove(3);
        taskList = historyManager.customLinkedList.getTasks();
        assertEquals(2, taskList.size());
        assertTrue(taskList.contains(task2));
        assertTrue(taskList.contains(task4));

        historyManager.remove(4);
        taskList = historyManager.customLinkedList.getTasks();
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(task2));
    }

    @Test
    @DisplayName("Remove tasks from empty history")
    void removeTestWhenHistoryIsEmpty() {
        // given (BeforeEach)
        // when
        historyManager.remove(1);
        // then
        assertEquals(0, historyManager.getHistory().size());
    }
}