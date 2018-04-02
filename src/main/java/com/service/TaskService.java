package com.service;

import com.data.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/29/2018.
 */
@Service
public class TaskService {

    private static final File STORAGE_FILE = new File("src/main/resources/tasks.json");
    private List<Task> tasks;

    private ObjectMapper objectMapper;

    public TaskService() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        if (!STORAGE_FILE.exists()) {
            if (!STORAGE_FILE.createNewFile()) {
                throw new IllegalStateException("Could not create file for storage");
            }
            Files.write(STORAGE_FILE.toPath(), "[]".getBytes());
        }
        tasks = objectMapper.readValue(STORAGE_FILE, new TypeReference<List<Task>>(){});
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void editTask(int id, Task task) throws IOException {
        tasks.set(id, task);
        persist();
    }

    public void removeTask(int id) throws IOException {
        tasks.remove(id);
        persist();
    }

    public void addTask(Task task) throws IOException {
        tasks.add(task);
        persist();
    }

    private void persist() throws IOException {
        objectMapper.writeValue(STORAGE_FILE, tasks);
    }
}
