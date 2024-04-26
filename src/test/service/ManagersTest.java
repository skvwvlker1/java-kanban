package test.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import service.*;

public class ManagersTest {

    @Test
    public void testManagersInitialization() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(taskManager);
        assertNotNull(historyManager);
    }
}