package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private static final InMemoryTaskManager instance = new InMemoryTaskManager();
    private final HistoryManager historyManager = Managers.getHistoryManager();

    public static InMemoryTaskManager getInstance() {
        return instance;
    }

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    //Убрал из интерфейса.
    //Я использую этот метод (сеттер) в тестах, чтобы сбросить счетчик nextId до еденицы для того чтобы сделать корректный
    //ран всех тестов из пакета java.test, неужели это сильно криминально?)
    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new IllegalArgumentException("Эпика с таким ID не существует: " + subtask.getEpicId());
        }
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            if (oldSubtask.getEpicId() == subtask.getEpicId()) {
                if (epics.containsKey(subtask.getEpicId())) {
                    subtasks.put(subtask.getId(), subtask);
                    epics.get(subtask.getEpicId()).changeStatus();
                }
            } else {
                throw new IllegalArgumentException("Нельзя вставлять в другой эпик!");
            }
        }
    }

    @Override
    public void updateEpicTask(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic existingEpic = epics.get(epic.getId());
            existingEpic.setTitle(epic.getTitle());
            existingEpic.setDescription(epic.getDescription());
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtasks();
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicTaskById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            if (epics.containsKey(epicId)) {
                epics.get(epicId).deleteSubtask(subtask);
                subtasks.remove(id);
            }
        }
    }

    @Override
    public void deleteEpicTask(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            List<Integer> subtasksToRemove = new ArrayList<>(subtasks.keySet());
            for (Integer key : subtasksToRemove) {
                if (subtasks.get(key).getEpicId() == id) {
                    subtasks.remove(key);
                }
            }
            epics.remove(id);
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = getEpicTaskById(epicId);
        return epic != null ? epic.getSubtasks() : new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}