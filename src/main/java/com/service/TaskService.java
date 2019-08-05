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

    private File STORAGE_FILE = new File("src/main/resources/tasks.json");
    private List<Task> tasks;

    private ObjectMapper objectMapper;

    public TaskService() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        if (!STORAGE_FILE.exists()) {
            try {
                if (!createFile(STORAGE_FILE)) {
                    throw new IllegalStateException("Could not create file for storage");
                }
            } catch (Exception e) {
                STORAGE_FILE = File.createTempFile("", "");
            } finally {
                Files.write(STORAGE_FILE.toPath(), "[]".getBytes());
            }
        }
        System.out.println("Data file is at " + STORAGE_FILE.getAbsolutePath());
        tasks = objectMapper.readValue(STORAGE_FILE, new TypeReference<List<Task>>() {});
    }

    private boolean createFile(File file) throws IOException {
        file.getParentFile().mkdirs();
        if (file.createNewFile()) return false;
        return STORAGE_FILE.canWrite();
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
