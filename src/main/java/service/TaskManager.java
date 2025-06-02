package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(Subtask subtask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void updateTask(Task task);

    void updateSubTask(Subtask subtask);

    void updateEpicTask(Epic epic);

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpicTasks();

    Task getTaskById(int id);

    Subtask getSubTaskById(int id);

    Epic getEpicTaskById(int id);

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteEpicTask(int id);

    List<Subtask> getEpicSubtasks(int epicId);

    List<Task> getHistory();

    boolean isTaskOverlap(Task task);

    List<Task> getPrioritizedTasks();
}
