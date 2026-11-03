package org.tucn.pt.DataAccess;

import org.tucn.pt.BusinessLogic.TasksManagement;

import java.io.*;
import java.util.HashMap;

public class DataManagement {
    public void saveData(TasksManagement tasksManagement, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tasksManagement);
            oos.flush();
            oos.close();
            System.out.println("Data succesfully saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TasksManagement loadData(String fileName) throws IOException {
        TasksManagement tasksManagement = null;
        try (FileInputStream fis = new FileInputStream(fileName); ObjectInputStream ois = new ObjectInputStream(fis)) {
            tasksManagement = (TasksManagement) ois.readObject();
            System.out.println("Data successfully loaded from " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("No previous save file found.");
            return new TasksManagement(new HashMap<>());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new TasksManagement(new HashMap<>());
        }
        return tasksManagement;
    }
}
