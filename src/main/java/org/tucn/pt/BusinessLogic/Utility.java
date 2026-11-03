package org.tucn.pt.BusinessLogic;

import org.tucn.pt.DataModel.Employee;
import org.tucn.pt.DataModel.Task;

import java.util.*;

public class Utility {
    public String filterEmployees(TasksManagement tasksManagement) {
        Map<Employee, List<Task>> map = tasksManagement.getMap();
        ArrayList<Employee> employees = new ArrayList<>();
        Map<Employee, Integer> employeeDurations = new HashMap<>();
        for(Employee employee: map.keySet()) {
            int duration = tasksManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());
            employeeDurations.put(employee, duration);
        }
        for (Employee employee : map.keySet()) {
            if(employeeDurations.get(employee) > 40) {
                employees.add(employee);
            }
        }
        employees.sort(Comparator.comparingInt(employeeDurations::get));
        StringBuilder result = new StringBuilder("<--- Employees with >40 Hours --->\n");
        for (Employee employee : employees) {
            result.append(employee.getName()).append(" works ").append(employeeDurations.get(employee)).append(" hours\n");
        }
        return result.toString();
    }

    public static Map<String, Map<String, Integer>> computeNumberOfTasks(Map<Employee, List<Task>> map) {
        Map<String, Map<String, Integer>> newMap = new HashMap<>();
        for(Employee employee : map.keySet()) {
            int completed = 0;
            int uncompleted = 0;
            for(Task task : map.get(employee)) {
                if(task.getStatusTask().equals("Completed")) {
                    completed++;
                }
                else if(task.getStatusTask().equals("Uncompleted")) {
                    uncompleted++;
                }
            }
            Map<String, Integer> taskCounts = new HashMap<>();
            taskCounts.put("Completed", completed);
            taskCounts.put("Uncompleted", uncompleted);
            newMap.put(employee.getName(), taskCounts);
        }
        return newMap;
    }
}
