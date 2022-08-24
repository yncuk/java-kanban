package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> listOfLast10Tasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (listOfLast10Tasks.size() == 10) {
            listOfLast10Tasks.remove(0);
        }
        listOfLast10Tasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return listOfLast10Tasks;
    }
}
