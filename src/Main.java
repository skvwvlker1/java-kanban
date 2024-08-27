import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.FileBackedTaskManager;
import service.TaskManager;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        Path filePath = Path.of("tasks.csv");

        FileBackedTaskManager taskManager = new FileBackedTaskManager(filePath);

        Task task1 = new Task("Задача 10xx00", "Описание задачи 133", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи 2214", TaskStatus.IN_PROGRESS, Duration.ofMinutes(15), LocalDateTime.now().plusMinutes(20));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        task1.setStatus(TaskStatus.DONE);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1 Эпик 1", "Описание подзадачи 1", TaskStatus.NEW, epic1.getTaskId(), Duration.ofMinutes(20), LocalDateTime.now().plusHours(1));
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2 Эпик 1", "Описание подзадачи 2", TaskStatus.NEW, epic1.getTaskId(), Duration.ofMinutes(80), LocalDateTime.now().plusHours(2));
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3 Эпик 1", "Описание подзадачи 3", TaskStatus.NEW, epic1.getTaskId(), Duration.ofMinutes(2), LocalDateTime.now().plusHours(2).plusMinutes(10));
        taskManager.addSubtask(subtask3);
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.addEpic(epic2);
        Subtask subtask4 = new Subtask("Подзадача 1 Эпик 2", "Описание подзадачи 1", TaskStatus.NEW, epic2.getTaskId(), Duration.ofMinutes(20), LocalDateTime.now().plusHours(3));
        taskManager.addSubtask(subtask4);


        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        taskManager.getTaskById(task1.getTaskId());
        printAllTasks(taskManager);

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(filePath);
        System.out.println("******************************");
        printAllTasks(loadedManager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("\nTasks:\n");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }

        System.out.println("\nEpics:\n");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);

            for (Subtask subtask : manager.getSubtasksByEpicId(epic.getTaskId())) {
                System.out.println(">>> " + subtask);
            }
        }

        System.out.println("\nSubTasks:\n");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\nHistory:\n");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
