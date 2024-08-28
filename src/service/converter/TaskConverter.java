package service.converter;

import model.Subtask;
import model.Task;
import model.TaskStatus;
import model.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskConverter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private TaskConverter() {
    }

    public static String toString(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getTaskId()).append(",")
                .append(task.getType()).append(",")
                .append(task.getTitle()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getStartTime() != null ? task.getStartTime().format(DATE_TIME_FORMATTER) : "").append(",")
                .append(task.getDuration().toMinutes());

        if (task instanceof Subtask) {
            builder.append(",").append(((Subtask) task).getEpicId());
        }

        return builder.toString();
    }

    public static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String title = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        LocalDateTime startTime = fields[5].isEmpty() ? null : LocalDateTime.parse(fields[5], DATE_TIME_FORMATTER);
        Duration duration = Duration.ofMinutes(Long.parseLong(fields[6]));

        switch (type) {
            case TASK:
                Task task = new Task(id, title, description, status);
                task.setStartTime(startTime);
                task.setDuration(duration);
                return task;
            case SUBTASK:
                int epicId = Integer.parseInt(fields[7]);
                Subtask subtask = new Subtask(id, title, description, status, epicId);
                subtask.setStartTime(startTime);
                subtask.setDuration(duration);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}