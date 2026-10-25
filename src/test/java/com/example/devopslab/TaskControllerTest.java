package com.example.devopslab;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    @Test
    void testEmptyTaskList() {
        TaskController controller = new TaskController();

        assertTrue(controller.getTasks().isEmpty());
    }

    @Test
    void testAddTask() {
        TaskController controller = new TaskController();

        String result = controller.addTask("Learn Docker");

        assertEquals("Task added successfully", result);
        assertEquals(1, controller.getTasks().size());
        assertEquals("Learn Docker", controller.getTasks().get(0));
    }
}
