package org.tucn.pt.GUI;

import org.tucn.pt.BusinessLogic.TasksManagement;
import org.tucn.pt.BusinessLogic.Utility;
import org.tucn.pt.DataAccess.DataManagement;
import org.tucn.pt.DataModel.Employee;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Controller implements ActionListener {

    private View view;
    private TasksManagement tasksManagement;
    private DataManagement dataManagement;
    private String saveFileName = "tasks_data.ser";

    public Controller(View view, TasksManagement tm, DataManagement dm) {
        this.view = view;
        this.tasksManagement = tm;
        this.dataManagement = dm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("ADD_EMPLOYEE")) handleAddEmployee();
        else if (command.equals("ADD_TASK")) handleAddTaskToSystem();
        else if (command.equals("ADD_SUBTASK")) handleAddSubTask();
        else if (command.equals("REMOVE_SUBTASK")) handleRemoveSubTask();
        else if (command.equals("ASSIGN_TASK")) handleAssignTask();
        else if (command.equals("VIEW_ALL")) handleViewAll();
        else if (command.equals("MODIFY_STATUS")) handleModifyStatus();
        else if (command.equals("VIEW_STATS")) handleStatistics();
        refreshOverviewTab();
    }

    public void refreshOverviewTab() {
        StringBuilder empSb = new StringBuilder("<--- All Employees --->\n\n");
        for (Employee emp : tasksManagement.getMap().keySet()) {
            empSb.append("ID: ").append(emp.getIdEmployee()).append(" , Name: ").append(emp.getName()).append("\n");
        }
        view.getEmployeesArea().setText(empSb.toString());
        StringBuilder taskSb = new StringBuilder("<--- All System Tasks --->\n\n");
        for (org.tucn.pt.DataModel.Task t : tasksManagement.getSystemTasks()) {
            String type = (t instanceof org.tucn.pt.DataModel.SimpleTask) ? "Simple" : "Complex";
            taskSb.append("ID: ").append(t.getIdTask()).append(" ").append(type)
                    .append(" , Hours: ").append(t.estimateDuration()).append("\n");
        }
        view.getSystemTasksArea().setText(taskSb.toString());
    }

    private void handleAddEmployee() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter Employee ID:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr);
            String name = JOptionPane.showInputDialog("Enter Employee Name:");
            if (name == null) return;
            tasksManagement.addEmployee(new Employee(id, name));
            view.getDisplayArea().append("Added employee: " + name + "\n");
            dataManagement.saveData(tasksManagement, saveFileName);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID must be a number.");
        } catch (IOException ex) {
            view.getDisplayArea().append("Error saving data.\n");
        }
    }

    private void handleAddTaskToSystem() {
        try {
            String[] options = {"Simple Task", "Complex Task"};
            int choice = JOptionPane.showOptionDialog(null, "Select Task Type:", "Add System Task",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                int taskId = Integer.parseInt(JOptionPane.showInputDialog("Enter Task ID:"));
                int start = Integer.parseInt(JOptionPane.showInputDialog("Enter Start Hour:"));
                int end = Integer.parseInt(JOptionPane.showInputDialog("Enter End Hour:"));
                tasksManagement.addSystemTask(new org.tucn.pt.DataModel.SimpleTask(taskId, "Uncompleted", start, end));
                view.getDisplayArea().append("Added Simple Task " + taskId + " to SYSTEM.\n");
            } else if (choice == 1) {
                int taskId = Integer.parseInt(JOptionPane.showInputDialog("Enter Complex Task ID:"));
                tasksManagement.addSystemTask(new org.tucn.pt.DataModel.ComplexTask(taskId, "Uncompleted", new java.util.ArrayList<>()));
                view.getDisplayArea().append("Added Complex Task " + taskId + " to SYSTEM.\n");
            }
            dataManagement.saveData(tasksManagement, saveFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void handleAddSubTask() {
        try {
            int parentId = Integer.parseInt(JOptionPane.showInputDialog("Enter Parent Complex Task ID:"));
            int childId = Integer.parseInt(JOptionPane.showInputDialog("Enter Sub-Task ID to add:"));
            tasksManagement.addSubTaskToComplex(parentId, childId);
            view.getDisplayArea().append("Added Task " + childId + " inside Complex Task " + parentId + "\n");
            dataManagement.saveData(tasksManagement, saveFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void handleRemoveSubTask() {
        try {
            int parentId = Integer.parseInt(JOptionPane.showInputDialog("Enter Parent Complex Task ID:"));
            int childId = Integer.parseInt(JOptionPane.showInputDialog("Enter Sub-Task ID to remove:"));
            tasksManagement.removeSubTaskFromComplex(parentId, childId);
            view.getDisplayArea().append("Successfully removed Task " + childId + " from Complex Task " + parentId + ".\n");
            dataManagement.saveData(tasksManagement, saveFileName);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void handleAssignTask() {
        try {
            int empId = Integer.parseInt(JOptionPane.showInputDialog("Enter Employee ID:"));
            int taskId = Integer.parseInt(JOptionPane.showInputDialog("Enter Task ID (From System):"));
            tasksManagement.assignTaskToEmployee(empId, taskId);
            view.getDisplayArea().append("Assigned system task " + taskId + " to employee " + empId + "\n");
            dataManagement.saveData(tasksManagement, saveFileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void handleViewAll() {
        StringBuilder sb = new StringBuilder("Employee Roster & Tasks\n");
        for (Employee emp : tasksManagement.getMap().keySet()) {
            sb.append("Employee: ").append(emp.getName()).append(" (ID: ").append(emp.getIdEmployee()).append(")\n");
            java.util.List<org.tucn.pt.DataModel.Task> tasks = tasksManagement.getMap().get(emp);
            if (tasks.isEmpty()) {
                sb.append("   [No tasks assigned]\n");
            } else {
                for (org.tucn.pt.DataModel.Task t : tasks) {
                    sb.append("   -> Task ID: ").append(t.getIdTask())
                            .append(" | Status: ").append(t.getStatusTask())
                            .append(" | Duration: ").append(t.estimateDuration()).append("h\n");
                }
            }
            sb.append("\n");
        }
        view.getDisplayArea().setText(sb.toString());
    }

    private void handleModifyStatus() {
        try {
            int empId = Integer.parseInt(JOptionPane.showInputDialog("Enter Employee ID:"));
            int taskId = Integer.parseInt(JOptionPane.showInputDialog("Enter Task ID:"));
            tasksManagement.modifyTaskStatus(empId, taskId);
            view.getDisplayArea().setText("Task status updated!\n");
            dataManagement.saveData(tasksManagement, saveFileName);
            handleViewAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (Exception ex) {
            view.getDisplayArea().setText("Error modifying status.\n");
        }
    }

    private void handleStatistics() {
        Utility utility = new Utility();
        StringBuilder sb = new StringBuilder("<--- Statistics --->\n\n");
        sb.append(utility.filterEmployees(tasksManagement)).append("\n");
        sb.append("<--- Task Status Counts --->\n");
        var taskCountsMap = Utility.computeNumberOfTasks(tasksManagement.getMap());
        if (taskCountsMap.isEmpty()) {
            sb.append("No employees found.\n");
        } else {
            for (String empName : taskCountsMap.keySet()) {
                sb.append("Employee: ").append(empName).append("\n");
                sb.append("  Completed: ").append(taskCountsMap.get(empName).get("Completed")).append("\n");
                sb.append("  Uncompleted: ").append(taskCountsMap.get(empName).get("Uncompleted")).append("\n\n");
            }
        }
        view.getDisplayArea().setText(sb.toString());
    }
}