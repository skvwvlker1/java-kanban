import model.Task;
import model.Subtask;
import model.Epic;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic1.getTaskId());
        taskManager.addSubtask(subtask1);
        epic1.getSubtasksId().add(subtask1.getTaskId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.IN_PROGRESS, epic1.getTaskId());
        taskManager.addSubtask(subtask2);
        epic1.getSubtasksId().add(subtask2.getTaskId());

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.NEW, epic2.getTaskId());
        taskManager.addSubtask(subtask3);
        epic2.getSubtasksId().add(subtask3.getTaskId());


        Task task3 = new Task("Задача 3", "Описание задачи 3", TaskStatus.NEW);
        Task task4 = new Task("Задача 4", "Описание задачи 4", TaskStatus.NEW);
        Task task5 = new Task("Задача 5", "Описание задачи 5", TaskStatus.NEW);
        Task task6 = new Task("Задача 6", "Описание задачи 6", TaskStatus.NEW);
        Task task7 = new Task("Задача 7", "Описание задачи 7", TaskStatus.NEW);
        Task task8 = new Task("Задача 8", "Описание задачи 8", TaskStatus.NEW);
        Task task9 = new Task("Задача 9", "Описание задачи 9", TaskStatus.NEW);
        Task task10 = new Task("Задача 10", "Описание задачи 10", TaskStatus.NEW);

        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addTask(task5);
        taskManager.addTask(task6);
        taskManager.addTask(task7);
        taskManager.addTask(task8);
        taskManager.addTask(task9);
        taskManager.addTask(task10);

        taskManager.getTaskById(task1.getTaskId());
        taskManager.getTaskById(task2.getTaskId());
        taskManager.getTaskById(task3.getTaskId());
        taskManager.getTaskById(task4.getTaskId());
        taskManager.getTaskById(task5.getTaskId());
        taskManager.getTaskById(task6.getTaskId());
        taskManager.getTaskById(task7.getTaskId());
        taskManager.getTaskById(task8.getTaskId());
        taskManager.getTaskById(task9.getTaskId());
        taskManager.getTaskById(task10.getTaskId());
        taskManager.getTaskById(task1.getTaskId());
        taskManager.getEpicById(epic2.getTaskId());
        taskManager.getSubtaskById(subtask2.getTaskId());
        printAllTasks(taskManager);
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

        System.out.println("\n>>>NUM<<<\n");
        for (int i = 0; i < manager.getHistory().size(); i++) {
            Task task = manager.getHistory().get(i);
            System.out.println((i + 1) + " " + task);
        }
    }
}
