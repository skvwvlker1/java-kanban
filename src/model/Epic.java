package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    public void addSubtasksId(int subtasksId) {
        this.subtasksId.add(subtasksId);
    }

    public ArrayList<Integer> getSubtasksId() {
        return new ArrayList<>(subtasksId);
    }
}
