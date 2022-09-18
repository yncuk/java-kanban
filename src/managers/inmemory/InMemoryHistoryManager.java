package managers.inmemory;

import managers.HistoryManager;
import task.CustomLinkedList;
import task.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList customLinkedList = new CustomLinkedList(new HashMap<>());

    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(id);
    }
}