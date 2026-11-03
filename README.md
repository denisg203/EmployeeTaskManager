# Employee Task Management System

A desktop application built in Java using **Swing** for the graphical user interface and **Maven** for build management. This system allows project managers to manage employees, organize simple and complex tasks hierarchically, assign tasks, track work status, and generate analytical reports.

## Features

* **Employee Management**: Add and maintain employee profiles.
* **Hierarchical Task Structure**: Create individual `SimpleTask` items or composite `ComplexTask` items containing multiple sub-tasks.
* **Task Assignment & Tracking**: Assign system tasks to specific employees and toggle task completion status.
* **Persistent Data**: Automatically serializes and saves application states using object streams (`tasks_data.ser`).
* **Statistics & Filtering**: Filter employees exceeding specific hour thresholds and review complete task status counts.

## Architecture

The project follows a clean separation of concerns separated into distinct packages:
* `org.tucn.pt.GUI`: Contains the Swing graphical interface (`View`) and event handling (`Controller`).
* `org.tucn.pt.BusinessLogic`: Core application logic, container management (`TasksManagement`), and reporting utilities (`Utility`).
* `org.tucn.pt.DataModel`: Domain entities including abstract sealed `Task`, `SimpleTask`, `ComplexTask`, and `Employee`.
* `org.tucn.pt.DataAccess`: Handles persistence operations via `DataManagement`.

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 21 or higher.
* Apache Maven.

### Building and Running

1. Clone the repository:
   ```bash
   git clone https://github.com/denisg203/EmployeeTaskManager.git
   cd EmployeeTaskManager
   ```

2. Compile and run the project using Maven:
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="org.tucn.pt.Main"
   ```

## Running the Application

* Launch the desktop application using the Maven execution commands above.
* Use the Management tab to add employees, create simple or complex system tasks, manage sub-tasks, assign tasks, modify statuses, and view generated statistics.
* View live rosters and system tasks in the System Overview tab.

## License

This project is developed for academic evaluation purposes.
