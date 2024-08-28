package service;

import exceptions.ManagerIOException;
import model.Epic;
import model.Subtask;
import model.Task;
import service.converter.TaskConverter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path filePath;

    public FileBackedTaskManager(Path filePath) {
        this.filePath = filePath;
        loadFromFile();
    }


    private void loadFromFile() {
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                return;
            }

            Iterator<String> iterator = lines.iterator();
            if (iterator.hasNext()) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                String line = iterator.next();
                if (line.isEmpty()) {
                    continue;
                }

                Task task = fromString(line);
                if (task instanceof Epic epic) {
                    epics.put(epic.getTaskId(), epic);

                } else if (task instanceof Subtask subtask) {
                    subtasks.put(subtask.getTaskId(), subtask);
                    Epic epic = epics.get(subtask.getEpicId());
                    if (epic != null) {
                        epic.addSubtaskId(subtask.getTaskId());
                    }
                } else {
                    tasks.put(task.getTaskId(), task);
                }
            }

            for (Epic epic : epics.values()) {
                recalculateEpic(epic);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке данных из файла", e);
        }
    }


    private Task fromString(String value) {
        return TaskConverter.fromString(value);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        saveToFile();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        saveToFile();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        saveToFile();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        saveToFile();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        saveToFile();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        saveToFile();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        saveToFile();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        saveToFile();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        saveToFile();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        saveToFile();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        saveToFile();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        saveToFile();
    }

    private void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            fileHeader(writer);
            writer.newLine();
            for (Task task : getPrioritizedTasks()) {
                writer.write(TaskConverter.toString(task));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerIOException("Ошибка при сохранении данных в файл", e);
        }
    }

    private void fileHeader(BufferedWriter writer) throws IOException {
        writer.write("id,type,title,status,description,startTime,duration,epicId");
    }
}