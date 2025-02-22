package model;

import service.InMemoryTaskManager;

public class Subtask extends Task {
    private final int epicId;

    public void changeSubtaskStatus(Status status) {
        setStatus(status);
        InMemoryTaskManager manager = InMemoryTaskManager.getInstance();
        manager.getEpicTaskById(epicId).changeStatus();
    }

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Subtask subtask = (Subtask) obj;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + epicId;
    }
}