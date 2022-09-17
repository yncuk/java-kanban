package managers.inmemory;

import managers.HistoryManager;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap = new HashMap<>();

    private Node head;
    private Node tail;

    private static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task, tail, null);
        if (nodeMap.containsKey(newNode.task.getId())) {
            remove(newNode.task.getId());
        }
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    private List<Task> getTasks() {
        List<Task> listOfLastTasks = new ArrayList<>();
        Node last = head;
        while (last != null) {
            listOfLastTasks.add(last.task);
            last = last.next;
        }
        return listOfLastTasks;
    }

    private void removeNode(Node node) {
        if (nodeMap.size() == 1) {
            nodeMap.remove(node.task.getId());
            head = null;
            tail = null;
            return;
        } else if (node.equals(head)) {
            head.next.prev = null;
            head = head.next;
        } else if (node.equals(tail)) {
            tail.prev.next = null;
            tail = tail.prev;
        } else {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        }
        nodeMap.remove(node.task.getId());
    }

    @Override
    public void add(Task task) {
        linkLast(task);
        nodeMap.put(task.getId(), tail);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (nodeMap.get(id) != null) {
            removeNode(nodeMap.get(id));
        } else System.out.println("Нет такой задачи в истории");
    }
}
