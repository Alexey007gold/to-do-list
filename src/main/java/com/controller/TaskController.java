package com.controller;

import com.data.EditTaskRequest;
import com.data.Task;
import com.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 3/28/2018.
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET, value = "/getAllTasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addTask")
    public void addTask(@RequestBody Task task) throws IOException {
        taskService.addTask(task);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteTask")
    public void removeTask(@RequestParam(value = "id") int id) throws IOException {
        taskService.removeTask(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/editTask")
    public void editTask(@RequestBody EditTaskRequest editTaskRequest) throws IOException {
        taskService.editTask(editTaskRequest.getId(), editTaskRequest.getTask());
    }
}
