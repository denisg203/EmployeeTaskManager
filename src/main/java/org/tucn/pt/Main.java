package org.tucn.pt;

import org.tucn.pt.BusinessLogic.TasksManagement;
import org.tucn.pt.DataAccess.DataManagement;
import org.tucn.pt.GUI.Controller;
import org.tucn.pt.GUI.View;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String saveFileName = "tasks_data.ser";
        DataManagement dataManagement = new DataManagement();

        try {
            TasksManagement tm = dataManagement.loadData(saveFileName);
            View view = new View("Task Management System");
            Controller controller = new Controller(view, tm, dataManagement);
            view.setController(controller);
            view.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}