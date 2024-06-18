package service;

import exceptions.LoadFromFileException;
import exceptions.ManagerSaveException;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path filePath;

    public FileBackedTaskManager(Path filePath) {
        this.filePath = filePath;
    }

    private void save() {
        try (FileWriter writer = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                writer.write(task.toOutString() + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(epic.toOutString() + "\n");
                for (Subtask subtask : getSubtasksByEpicId(epic.getTaskId())) {
                    writer.write(subtask.toOutString() + "\n");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи файла", e);
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    private static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String title = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        switch (type) {
            case TASK:
                return new Task(id, title, description, status);
            case EPIC:
                return new Epic(id, title, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]);
                return new Subtask(id, title, description, status, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public static FileBackedTaskManager loadFromFile(Path filePath) {
        FileBackedTaskManager manager = new FileBackedTaskManager(filePath);
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                Task task = fromString(lines.get(i));
                switch (task.getClass().getSimpleName()) {
                    case "Task":
                        manager.addTask((Task) task);
                        break;
                    case "Epic":
                        manager.addEpic((Epic) task);
                        break;
                    case "Subtask":
                        manager.addSubtask((Subtask) task);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new LoadFromFileException("Файл не найден");
        } catch (IOException e) {
            throw new LoadFromFileException("Ошибка при чтении файла");
        }
        return manager;
    }
}