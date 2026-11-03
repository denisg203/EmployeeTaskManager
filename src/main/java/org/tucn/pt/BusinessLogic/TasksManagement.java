package org.tucn.pt.BusinessLogic;

import org.tucn.pt.DataModel.ComplexTask;
import org.tucn.pt.DataModel.Employee;
import org.tucn.pt.DataModel.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksManagement implements Serializable {
    private Map<Employee, List<Task>> map;
    private List<Task> systemTasks;

    public TasksManagement(Map<Employee, List<Task>> map) {
        this.map = new HashMap<>(map);
        this.systemTasks = new ArrayList<>();
    }

    public Map<Employee, List<Task>> getMap() {
        return map;
    }
    public List<Task> getSystemTasks() {
        return systemTasks;
    }

    public void addEmployee(Employee employee) {
        for (Employee emp : this.map.keySet()) {
            if (emp.getIdEmployee() == employee.getIdEmployee()) {
                throw new IllegalArgumentException("Employee ID already exists.");
            }
        }
        this.map.putIfAbsent(employee, new ArrayList<>());
    }

    public void addSystemTask(Task task) {
        for (Task t : systemTasks) {
            if (t.getIdTask() == task.getIdTask()) {
                throw new IllegalArgumentException("Task ID already exists in the system.");
            }
        }
        systemTasks.add(task);
    }

    public Task getSystemTaskById(int taskId) {
        for (Task t : systemTasks) {
            if (t.getIdTask() == taskId) return t;
        }
        throw new IllegalArgumentException("Task ID " + taskId + " not found in system.");
    }

    public void assignTaskToEmployee(int idEmployee, int idTask) {
        Task taskToAssign = getSystemTaskById(idTask);
        boolean employeeFound = false;

        for (Employee employee : this.map.keySet()) {
            if (employee.getIdEmployee() == idEmployee) {
                for (Task existingTask : map.get(employee)) {
                    if (existingTask.getIdTask() == idTask) {
                        throw new IllegalArgumentException("Task already assigned to this employee.");
                    }
                }
                map.get(employee).add(taskToAssign);
                employeeFound = true;
                break;
            }
        }
        if (!employeeFound) throw new IllegalArgumentException("Employee not found");
    }

    public void addSubTaskToComplex(int complexId, int subTaskId) {
        Task parent = getSystemTaskById(complexId);
        Task child = getSystemTaskById(subTaskId);
        if (parent instanceof ComplexTask) {
            ((ComplexTask) parent).addTask(child);
        } else {
            throw new IllegalArgumentException("Parent Task is not a ComplexTask.");
        }
    }

    public void removeSubTaskFromComplex(int complexId, int subTaskId) {
        Task parent = getSystemTaskById(complexId);

        if (parent instanceof ComplexTask) {
            ComplexTask complexTask = (ComplexTask) parent;
            Task taskToRemove = null;
            for (Task t : complexTask.getTasks()) {
                if (t.getIdTask() == subTaskId) {
                    taskToRemove = t;
                    break;
                }
            }
            if (taskToRemove != null) {
                complexTask.deleteTask(taskToRemove);
            } else {
                throw new IllegalArgumentException("Sub-Task ID " + subTaskId + " not found inside Complex Task " + complexId);
            }
        } else {
            throw new IllegalArgumentException("Task " + complexId + " is not a ComplexTask.");
        }
    }

    public int calculateEmployeeWorkDuration(int idEmployee) {
        int duration=0;
        for (Employee employee : this.map.keySet()) {
            if (employee.getIdEmployee()==idEmployee) {
                for(Task task : map.get(employee)) {
                    if(task.getStatusTask().equals("Completed")) duration += task.estimateDuration();
                }
                return duration;
            }
        }
        throw new IllegalArgumentException("Employee not found");
    }

    public void modifyTaskStatus(int idEmployee, int idTask) {
        for(Employee employee : this.map.keySet()) {
            if (employee.getIdEmployee()==idEmployee) {
                for(Task task1 : map.get(employee)) {
                    if(task1.getIdTask()==idTask) {
                        task1.setStatusTask(task1.getStatusTask().equals("Completed") ? "Uncompleted" : "Completed");
                        return;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Employee or task not found");
    }
}