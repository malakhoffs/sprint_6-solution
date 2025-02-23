package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        head = new Node();
        tail = new Node();
        head.setNext(tail);
        tail.setPrev(head);
    }

    @Override
    public void add(Task task) {
        Task taskCopy = new Task();
        taskCopy.setId(task.getId());
        taskCopy.setTitle(task.getTitle());
        taskCopy.setDescription(task.getDescription());
        taskCopy.setStatus(task.getStatus());
        remove(task.getId());
        Node newNode = new Node(taskCopy);
        linkLast(newNode);
        history.put(task.getId(), newNode);
    }

    private void linkLast(Node node) {
        node.setPrev(tail.getPrev());
        node.setNext(tail);
        tail.getPrev().setNext(node);
        tail.setPrev(node);
        if (head.getNext() == tail) {
            head.setNext(node);
        }
    }

    public void remove(int id) {
        if (history.get(id) != null) {
            removeNode(history.get(id));
        }
    }

    private void removeNode(Node node) {
        if (node != null) {
            history.remove(node.getTask().getId());
            Node prevNode = node.getPrev();
            Node nextNode = node.getNext();

            if (prevNode != null) {
                prevNode.setNext(nextNode);
            } else {
                head = nextNode;
            }

            if (nextNode != null) {
                nextNode.setPrev(prevNode);
            } else {
                tail = prevNode;
            }
        }
    }

    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node current = head.getNext();
        while (current != tail) {
            history.add(current.getTask());
            current = current.getNext();
        }
        return history;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void historyWipe() {
        history.clear();
        head.setNext(tail);
        tail.setPrev(head);
    }
}
