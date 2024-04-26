package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();
    private static final int HISTORY_MAX_SIZE = 10;

    @Override
    public void add(Task task) {
        history.add(0, task);
        if (history.size() > HISTORY_MAX_SIZE) {
            history.remove(history.size() - 1);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
