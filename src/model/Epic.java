package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int taskId, String title, String description, TaskStatus status) {
        super(taskId, title, description, status);
    }

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    public Epic(int taskId, String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(taskId, title, description, status, startTime, duration);
    }

    public void addSubtaskId(int subtaskId) {
        this.subtasksId.add(subtaskId);
    }

    public ArrayList<Integer> getSubtasksId() {
        return new ArrayList<>(subtasksId);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}