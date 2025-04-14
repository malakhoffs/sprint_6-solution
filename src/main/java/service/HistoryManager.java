package service;

import model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    void historyWipe();

    List<Task> getHistory();
}