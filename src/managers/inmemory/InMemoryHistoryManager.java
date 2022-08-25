package managers.inmemory;

import managers.HistoryManager;
import task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> listOfLastTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (listOfLastTasks.size() == 10) {
            listOfLastTasks.removeFirst();
        }
        listOfLastTasks.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return listOfLastTasks;
    }
}
