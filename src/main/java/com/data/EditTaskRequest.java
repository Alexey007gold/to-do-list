package com.data;

/**
 * Created on 3/28/2018.
 */
public class EditTaskRequest {

    private int id;
    private Task task;

    public EditTaskRequest() {
    }

    public EditTaskRequest(int id, Task task) {
        this.id = id;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
