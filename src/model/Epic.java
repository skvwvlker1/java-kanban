package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    private ArrayList<Integer> copySubtasksId = new ArrayList<>(subtasksId);

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);

    }
    public ArrayList<Integer> getSubtasksId() {
        return copySubtasksId;
    }
}
