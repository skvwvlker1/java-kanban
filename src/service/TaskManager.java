package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int taskIdCounter = 0;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void addTask(Task task) {
        task.setTaskId(taskIdCounter++);
        tasks.put(task.getTaskId(), task);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setTaskId(taskIdCounter++);
        epics.get(subtask.getEpicId()).getSubtasksId().add(subtask.getTaskId());
        subtasks.put(subtask.getTaskId(), subtask);
        changeEpicStatus(epics.get(subtask.getEpicId()));
    }

    public void addEpic(Epic epic) {
        epic.setTaskId(taskIdCounter++);
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getTaskId(), epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        changeEpicStatus(epics.get(subtask.getEpicId()));
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            epic.setStatus(TaskStatus.NEW);
        }
        subtasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (int subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        ArrayList<Integer> subtaskIds = epic.getSubtasksId();
        subtaskIds.remove(Integer.valueOf(id));
        subtasks.remove(id);
        changeEpicStatus(epic);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Integer subtaskId : epics.get(epicId).getSubtasksId()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (!subtasksByEpic.contains(subtask)) {
                    subtasksByEpic.add(subtasks.get(subtaskId));
                }
            }
        }
        return subtasksByEpic;
    }

    private void changeEpicStatus(Epic epic) {
        int countNewTasks = 0;
        int countDoneTasks = 0;

        ArrayList<Integer> subtaskIds = epic.getSubtasksId();

        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        for (Integer id : subtaskIds) {
            if (subtasks.get(id).getStatus() == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            } else if (subtasks.get(id).getStatus() == TaskStatus.NEW) {
                countNewTasks++;
            } else if (subtasks.get(id).getStatus() == TaskStatus.DONE) {
                countDoneTasks++;
            }
        }

        if (countNewTasks == subtaskIds.size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (countDoneTasks == subtaskIds.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}