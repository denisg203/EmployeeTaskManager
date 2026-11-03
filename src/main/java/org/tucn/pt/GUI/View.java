package org.tucn.pt.GUI;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private JButton btnAddEmployee, btnAddTask, btnAssignTask, btnViewAll;
    private JButton btnModifyStatus, btnViewStats, btnAddSubTask, btnRemoveSubTask;
    private JTextArea displayArea, employeesArea, systemTasksArea;
    private Controller controller;

    public View(String title) {
        super(title);
        prepareGui();
    }

    private void prepareGui() {
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Management", createManagementTab());
        tabbedPane.addTab("System Overview", createOverviewTab());
        add(tabbedPane);
    }

    private JPanel createManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new GridLayout(8, 1, 10, 10)); // 7 buttons now
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        btnAddEmployee = createButton("Add Employee", "ADD_EMPLOYEE", leftPanel);
        btnAddTask = createButton("Add Task to System", "ADD_TASK", leftPanel);
        btnAddSubTask = createButton("Add Sub-Task to Complex", "ADD_SUBTASK", leftPanel);
        btnRemoveSubTask = createButton("Remove Sub-Task", "REMOVE_SUBTASK", leftPanel);
        btnAssignTask = createButton("Assign Task to Employee", "ASSIGN_TASK", leftPanel);
        btnViewAll = createButton("View Employees & Tasks", "VIEW_ALL", leftPanel);
        btnModifyStatus = createButton("Modify Task Status", "MODIFY_STATUS", leftPanel);
        btnViewStats = createButton("View Statistics", "VIEW_STATS", leftPanel);
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOverviewTab() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        employeesArea = new JTextArea("<--- All Employees --->\n");
        employeesArea.setEditable(false);
        systemTasksArea = new JTextArea("<--- All System Tasks --->\n");
        systemTasksArea.setEditable(false);
        panel.add(new JScrollPane(employeesArea));
        panel.add(new JScrollPane(systemTasksArea));
        return panel;
    }

    private JButton createButton(String text, String command, JPanel panel) {
        JButton btn = new JButton(text);
        btn.setActionCommand(command);
        panel.add(btn);
        return btn;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        btnAddEmployee.addActionListener(controller);
        btnAddTask.addActionListener(controller);
        btnAddSubTask.addActionListener(controller);
        btnRemoveSubTask.addActionListener(controller);
        btnAssignTask.addActionListener(controller);
        btnViewAll.addActionListener(controller);
        btnModifyStatus.addActionListener(controller);
        btnViewStats.addActionListener(controller);
    }

    public JTextArea getDisplayArea() { return displayArea; }
    public JTextArea getEmployeesArea() { return employeesArea; }
    public JTextArea getSystemTasksArea() { return systemTasksArea; }
}