package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int taskId;
    private String title;
    private String description;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(int taskId, String title, String description, TaskStatus status) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = Duration.ZERO;
        this.startTime = null;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.duration = Duration.ZERO;
        this.startTime = null;
    }

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(0);
        this.startTime = null;
    }

    public Task(String title, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int taskId, String title, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId &&
                Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                status == task.status &&
                Objects.equals(duration, task.duration) &&
                Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, title, description, status);
    }
}