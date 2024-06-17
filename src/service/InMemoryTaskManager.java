package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int taskIdCounter = 1;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void addTask(Task task) {
        task.setTaskId(taskIdCounter++);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setTaskId(taskIdCounter++);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtasksId(subtask.getTaskId());
            subtasks.put(subtask.getTaskId(), subtask);
            changeEpicStatus(epic);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setTaskId(taskIdCounter++);
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        changeEpicStatus(epics.get(subtask.getEpicId()));
    }

    @Override
    public void clearTasks() {
        for (int taskId : epics.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (int epicId : epics.keySet()) {
            historyManager.remove(epicId);
            for (int subtaskId : epics.get(epicId).getSubtasksId()) {
                historyManager.remove(subtaskId);
            }
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (int subtaskId : subtasks.keySet()) {
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            changeEpicStatus(epic);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (int subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        ArrayList<Integer> subtaskIds = epic.getSubtasksId();
        subtaskIds.remove(Integer.valueOf(id));
        subtasks.remove(id);
        historyManager.remove(id);
        changeEpicStatus(epic);
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasksId()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    subtasksByEpic.add(subtask);
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