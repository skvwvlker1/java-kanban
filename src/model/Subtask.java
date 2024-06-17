package model;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int taskId, String title, String description, TaskStatus status, int epicId) {
        super(taskId, title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, TaskStatus status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


}
