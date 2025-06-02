package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static final InMemoryTaskManager instance = new InMemoryTaskManager();
    protected final HistoryManager historyManager = Managers.getHistoryManager();

    public static InMemoryTaskManager getInstance() {
        return instance;
    }

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>();
    protected int nextId = 1;

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        addTaskByPriority(task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        updateEpicStartTime(epic.getId());
        updateEpicEndTime(epic.getId());
        updateEpicDuration(epic.getId());
        addTaskByPriority(epic);
    }

    @Override
    public void addSubTask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new IllegalArgumentException("Эпика с таким ID не существует: " + subtask.getEpicId());
        }
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask);
        updateEpicStartTime(subtask.getEpicId());
        updateEpicEndTime(subtask.getEpicId());
        updateEpicDuration(subtask.getEpicId());
        addTaskByPriority(subtask);
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
            prioritizedTasks.remove(task);
            addTaskByPriority(task);
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
                    updateEpicStartTime(subtask.getId());
                    updateEpicEndTime(subtask.getId());
                    updateEpicDuration(subtask.getId());
                    prioritizedTasks.remove(subtask);
                    addTaskByPriority(subtask);
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
            updateEpicStartTime(epic.getId());
            updateEpicEndTime(epic.getId());
            updateEpicDuration(epic.getId());
            prioritizedTasks.remove(epic);
            addTaskByPriority(epic);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.values().forEach(task -> historyManager.remove(task.getId()));
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subtasks.keySet().forEach(subtask -> prioritizedTasks.remove(subtasks.get(subtask)));
        subtasks.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            entry.getValue().deleteAllSubtasks();
            int id = entry.getValue().getId();
            updateEpicStartTime(id);
            updateEpicEndTime(id);
            updateEpicDuration(id);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
        epics.values().forEach(prioritizedTasks::remove);
        subtasks.values().forEach(prioritizedTasks::remove);
        epics.values().forEach(epic -> historyManager.remove(epic.getId()));
        subtasks.values().forEach(subtask -> historyManager.remove(subtask.getId()));
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
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            if (epics.containsKey(epicId)) {
                prioritizedTasks.remove(subtasks.get(id));
                epics.get(epicId).deleteSubtask(subtask);
                subtasks.remove(id);
                historyManager.remove(id);
            }
        }
    }

    @Override
    public void deleteEpicTask(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            List<Integer> subtasksToRemove = new ArrayList<>(subtasks.keySet());
            for (Integer key : subtasksToRemove) {
                Task subtask = subtasks.get(key);
                if (subtasks.get(key).getEpicId() == id) {
                    prioritizedTasks.remove(subtask);
                    subtasks.remove(key);
                }
            }
            prioritizedTasks.remove(epics.get(id));
            epics.remove(id);
            historyManager.remove(id);
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

    @Override
    public boolean isTaskOverlap(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        return prioritizedTasks.stream()
                .filter(prioritizedTask -> prioritizedTask.getStartTime() != null)
                .anyMatch(prioritizedTask -> !endTime.isBefore(prioritizedTask.getStartTime())
                        && !prioritizedTask.getEndTime().isBefore(startTime));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void addTaskByPriority(Task task) {
        if (task.getStartTime() != null && !isTaskOverlap(task)) {
            prioritizedTasks.add(task);
        }
    }

    protected void updateEpicStartTime(int epicId) {
        Epic epicTask = epics.get(epicId);
        List<Subtask> subTasks = epicTask.getSubtasks();
        epicTask.setStartTime(calculateEpicStartTime(subTasks));
    }

    private LocalDateTime calculateEpicStartTime(List<Subtask> subTasks) {
        return subTasks.stream()
                .map(Task::getStartTime)
                .min(Comparator.naturalOrder()).orElse(null);
    }

    protected void updateEpicEndTime(int epicId) {
        Epic epicTask = epics.get(epicId);
        List<Subtask> subTasks = epicTask.getSubtasks();
        epicTask.setEndTime(calculateEpicEndTime(subTasks));
    }

    private LocalDateTime calculateEpicEndTime(List<Subtask> subTasks) {
        return subTasks.stream()
                .map(Task::getEndTime)
                .max(Comparator.naturalOrder()).orElse(null);
    }

    protected void updateEpicDuration(int epicId) {
        Epic epicTask = epics.get(epicId);
        List<Subtask> subTasks = epicTask.getSubtasks();
        epicTask.setDuration(calculateEpicDuration(subTasks));
    }

    private Duration calculateEpicDuration(List<Subtask> subTasks) {
        return subTasks.stream()
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

}