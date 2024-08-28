package service;

import exceptions.AlreadyExistException;
import exceptions.NotFoundException;
import exceptions.TaskTimeValidateException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator
            .comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Task::getTaskId));
    private final HistoryManager historyManager = Managers.getDefaultHistory();
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
        if (tasks.containsKey(task.getTaskId())) {
            throw new AlreadyExistException("Задача с таким ID уже существует: " + task.getTaskId());
        }
        if (!isTaskOverlapping(task)) {
            task.setTaskId(taskIdCounter++);
            tasks.put(task.getTaskId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        } else {
            throw new TaskTimeValidateException("Задача пересекается по времени с другой задачей.");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setTaskId(taskIdCounter++);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getTaskId());
            subtasks.put(subtask.getTaskId(), subtask);
            changeEpicStatus(epic);
            recalculateEpic(epic);

            if (subtask.getStartTime() != null) {
                recalculateEpic(epic);
                prioritizedTasks.add(subtask);
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setTaskId(taskIdCounter++);
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getTaskId(), epic);
        recalculateEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(tasks.get(task.getTaskId()));
        tasks.put(task.getTaskId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
        changeEpicStatus(epic);
        recalculateEpic(epic);
        prioritizedTasks.remove(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            changeEpicStatus(epic);
            recalculateEpic(epic);
        }
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        } else {
            prioritizedTasks.remove(subtask);
        }
    }

    @Override
    public void clearTasks() {
        for (int taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
        prioritizedTasks.removeIf(task -> tasks.containsKey(task.getTaskId()));
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getTaskId());
            for (int subtaskId : epic.getSubtasksId()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
        }
        epics.clear();
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
            recalculateEpic(epic);
        }
        prioritizedTasks.removeIf(task -> subtasks.containsKey(task.getTaskId()));
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            historyManager.remove(id);
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtasksId()) {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(subtask);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasksId().remove(Integer.valueOf(id));
                changeEpicStatus(epic);
                recalculateEpic(epic);
            }
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с ID " + id + " не найдена.");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
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

        List<Integer> subtaskIds = epic.getSubtasksId();

        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        for (Integer id : subtaskIds) {
            Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                TaskStatus status = subtasks.get(id).getStatus();
                if (status == TaskStatus.IN_PROGRESS) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                    return;
                } else if (status == TaskStatus.NEW) {
                    countNewTasks++;
                } else if (status == TaskStatus.DONE) {
                    countDoneTasks++;
                }
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

    public void recalculateEpic(Epic epic) {
        Duration totalDuration = Duration.ZERO;
        LocalDateTime epicStartTime = null;
        LocalDateTime epicEndTime = null;

        for (int subtaskId : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                LocalDateTime subtaskStartTime = subtask.getStartTime();
                LocalDateTime subtaskEndTime = subtask.getEndTime();

                if (subtaskStartTime != null) {
                    if (epicStartTime == null || subtaskStartTime.isBefore(epicStartTime)) {
                        epicStartTime = subtaskStartTime;
                    }
                }

                if (subtaskEndTime != null) {
                    if (epicEndTime == null || subtaskEndTime.isAfter(epicEndTime)) {
                        epicEndTime = subtaskEndTime;
                    }
                }

                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }

        epic.setStartTime(epicStartTime);
        epic.setDuration(totalDuration);
        epic.setEndTime(epicEndTime);
    }

    private boolean isTaskOverlapping(Task newTask) {
        return prioritizedTasks.stream().anyMatch(task -> tasksOverlap(newTask, task));
    }

    private boolean tasksOverlap(Task t1, Task t2) {
        LocalDateTime t1Start = t1.getStartTime();
        LocalDateTime t1End = t1.getEndTime();
        LocalDateTime t2Start = t2.getStartTime();
        LocalDateTime t2End = t2.getEndTime();

        if (t1Start == null || t1End == null || t2Start == null || t2End == null) {
            return false;
        }

        return !(t1End.isBefore(t2Start) || t1Start.isAfter(t2End));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}