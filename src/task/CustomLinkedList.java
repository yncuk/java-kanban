package task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList {

    public CustomLinkedList() {

        this.nodeMap = new HashMap<>();
    }

    private final Map<Integer, Node> nodeMap;

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

    public void linkLast(Task task) {
        Node newNode = new Node(task, tail, null);
        if (nodeMap.containsKey(newNode.task.getId())) {
            removeNode(newNode.task.getId());
        }
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        nodeMap.put(task.getId(), tail);
    }

    public List<Task> getTasks() {
        List<Task> listOfLastTasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            listOfLastTasks.add(current.task);
            current = current.next;
        }
        return listOfLastTasks;
    }

    public void removeNode(int id) {
        if (nodeMap.get(id) != null) {
            Node node = nodeMap.get(id);
            if (nodeMap.size() == 1) {
                head = null;
                tail = null;
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
        } else System.out.println("Нет такой задачи в истории");
    }
}
