package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, Status status, Duration duration, LocalDateTime startTime, int epicId) {
        super(title, description, status, duration, startTime);
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

    @Override
    public String toString() {
        return getId() + ", Subtask, " + getTitle() + ", " + getStatus()
                + ", " + getDescription() + ", " + getDuration().toMinutes() + ", " + getStartTime() + ", " + epicId;
    }
}