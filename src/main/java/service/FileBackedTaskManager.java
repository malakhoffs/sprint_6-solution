package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final File file;

    public FileBackedTaskManager(String path) {
        file = new File(path);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.getPath());
        fileBackedTaskManager.parseFile();
        return fileBackedTaskManager;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,title,description,status,epicId\n");
            for (Task task : getTasks()) {
                writer.write(taskToCSV(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(taskToCSV(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(taskToCSV(subtask) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Ошибка сохранения в файл " + e.getMessage());
        }
    }

    private String taskToCSV(Task task) {
        String epicId;
        if (task instanceof Subtask) {
            epicId = String.valueOf(((Subtask) task).getEpicId());
        } else {
            epicId = "";
        }

        return String.join(",", String.valueOf(task.getId()), task.getClass().getSimpleName().toUpperCase(),
                task.getTitle(), task.getDescription(), String.valueOf(task.getStatus()), epicId);
    }

    private void parseFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String type = parts[1];
                String title = parts[2];
                String description = parts[3];
                Status status = Status.valueOf(parts[4]);
                switch (type) {
                    case "TASK":
                        Task task = new Task(title, description, status);
                        task.setId(id);
                        tasks.put(id, task);
                        break;
                    case "EPIC":
                        Epic epic = new Epic(title, description);
                        epic.setId(id);
                        epics.put(id, epic);
                        break;
                    case "SUBTASK":
                        int epicId = Integer.parseInt(parts[5]);
                        Subtask subtask = new Subtask(title, description, status, epicId);
                        subtask.setId(id);
                        subtasks.put(id, subtask);
                        break;
                }
                if (id >= nextId) {
                    nextId = id + 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке из файла: " + e.getMessage());
        }
    }


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(Subtask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpicTask(int id) {
        super.deleteEpicTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(Epic epic) {
        super.updateEpicTask(epic);
        save();
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = super.getSubTaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicTaskById(int id) {
        Epic epic = super.getEpicTaskById(id);
        save();
        return epic;
    }
}