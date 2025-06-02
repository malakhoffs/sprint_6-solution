package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, Status.NEW, Duration.ofSeconds(0), LocalDateTime.now());
        subtasks = new ArrayList<>();
    }


    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        changeStatus();
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        changeStatus();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        changeStatus();
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void changeStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
        } else {
            boolean allDone = true;
            boolean allNew = true;

            for (Subtask subtask : subtasks) {
                if (subtask.getStatus() != Status.DONE) {
                    allDone = false;
                }
                if (subtask.getStatus() != Status.NEW) {
                    allNew = false;
                }
            }

            if (allDone) {
                setStatus(Status.DONE);
            } else if (allNew) {
                setStatus(Status.NEW);
            } else {
                setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Epic epic = (Epic) obj;
        return subtasks.equals(epic.subtasks);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + subtasks.hashCode();
    }

    @Override
    public String toString() {
        return getId() + ", Epic, " + getTitle() + ", " + getStatus()
                + ", " + getDescription() + ", " + getDuration().toMinutes() + ", " + getStartTime();
    }
}