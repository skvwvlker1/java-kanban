import model.Task;
import model.Subtask;
import model.Epic;
import model.TaskStatus;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1 Эпик 1", "Описание подзадачи 1", TaskStatus.NEW, epic1.getTaskId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2 Эпик 1", "Описание подзадачи 2", TaskStatus.IN_PROGRESS, epic1.getTaskId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3 Эпик 1", "Описание подзадачи 3", TaskStatus.IN_PROGRESS, epic1.getTaskId());
        taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.addEpic(epic2);


        taskManager.getTaskById(task1.getTaskId());
        taskManager.getTaskById(task2.getTaskId());
        taskManager.getTaskById(task1.getTaskId());
        taskManager.getTaskById(task1.getTaskId());

        taskManager.deleteTaskById(task1.getTaskId());

        taskManager.getEpicById(epic1.getTaskId());
        taskManager.deleteEpicById(epic1.getTaskId());

        taskManager.getEpicById(epic2.getTaskId());
        taskManager.getSubtaskById(subtask1.getTaskId());
        taskManager.getSubtaskById(subtask2.getTaskId());
        taskManager.getSubtaskById(subtask3.getTaskId());

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

    }
}
