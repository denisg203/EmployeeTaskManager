package org.tucn.pt.DataModel;

import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task {
    private List<Task> tasks;

    public ComplexTask(int idTask, String statusTask, List<Task> tasks) {
        super(idTask, statusTask);
        this.tasks = new ArrayList<Task>(tasks);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    @Override
    public int estimateDuration() {
        int duration = 0;
        for (Task task : tasks) {
            duration += task.estimateDuration();
        }
        return duration;
    }
}
