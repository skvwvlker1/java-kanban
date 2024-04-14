import model.Task;
import model.Subtask;
import model.Epic;
import model.TaskStatus;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic1.getTaskId());
        taskManager.addSubtask(subtask1);
        epic1.getSubtasksId().add(subtask1.getTaskId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.IN_PROGRESS, epic1.getTaskId());
        taskManager.addSubtask(subtask2);
        epic1.getSubtasksId().add(subtask2.getTaskId());

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", TaskStatus.NEW);
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.NEW, epic2.getTaskId());
        taskManager.addSubtask(subtask3);
        epic2.getSubtasksId().add(subtask3.getTaskId());

        System.out.println("Список task");
        System.out.println(taskManager.getTasks());
        System.out.println("Список epic");
        System.out.println(taskManager.getEpics());
        System.out.println("Список subtask");
        System.out.println(taskManager.getSubtasks());

        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);

        System.out.println("Updated список task");
        System.out.println(taskManager.getTasks());
        System.out.println("Updated список epic");
        System.out.println(taskManager.getEpics());
        System.out.println("Updated список subtask:");
        System.out.println(taskManager.getSubtasks());

        taskManager.deleteTaskById(task2.getTaskId());

        taskManager.deleteEpicById(epic2.getTaskId());

        System.out.println("Updated#2 список task");
        System.out.println(taskManager.getTasks());
        System.out.println("Updated#2 список epic");
        System.out.println(taskManager.getEpics());
        System.out.println("Updated#2 список subtask:");
        System.out.println(taskManager.getSubtasks());

        System.out.println(taskManager.getSubtasksByEpicId(epic1.getTaskId()));
        System.out.println(taskManager.getSubtasks());
       // taskManager.clearSubtasks();

        System.out.println("");

        System.out.println(epic1.getTitle() + ", status: " + epic1.getStatus() + "subtasks: " + taskManager.getSubtasksByEpicId(epic1.getTaskId()) );
    }
}