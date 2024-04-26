package test.model;

import static org.junit.jupiter.api.Assertions.*;

import model.*;
import org.junit.jupiter.api.Test;

class EpicTest {

    Epic epic1 = new Epic("Эпик 1", "Описание 1");
    Epic epic2 = new Epic("Эпик 2", "Описание 2");

    @Test
    public void testEpicEqualBySelf() {
        assertEquals(epic1, epic1);
    }

    @Test
    public void testEpicEqualById() {
        assertEquals(epic1.getTaskId(), epic2.getTaskId());
    }
}