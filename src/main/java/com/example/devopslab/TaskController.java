package com.example.devopslab;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger =
        LoggerFactory.getLogger(TaskController.class);

    private final List<String> tasks = new ArrayList<>();

    @GetMapping
    public List<String> getTasks() {
        logger.info("Fetching all tasks");
        return tasks;
    }

    @PostMapping
    public String addTask(@RequestBody String task) {
        tasks.add(task);
        logger.info("Task added successfully");
        return "Task added successfully";
    }
}
