import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;

public class ToDoApp extends Application {

    private ListView<String> taskListView;
    private TextField taskTextField;
    private ObservableList<String> tasks;

    @Override
    public void start(Stage primaryStage) {
        // Initialize MongoDB
        DatabaseManager.connectToDatabase(); // Ensure DatabaseManager is in the same package or import it

        // Initialize UI components
        taskTextField = new TextField();
        taskTextField.setPromptText("Enter a new task");
        taskTextField.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #f2f2f2; -fx-border-radius: 5px;");
        
        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px;");
        addButton.setOnAction(e -> addTask());

        Button deleteButton = new Button("Delete Task");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px;");
        deleteButton.setOnAction(e -> deleteTask());

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px;");
        refreshButton.setOnAction(e -> refreshTaskList());

        taskListView = new ListView<>();
        tasks = FXCollections.observableArrayList(DatabaseManager.getAllTasks());
        taskListView.setItems(tasks);
        taskListView.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-border-radius: 5px; -fx-border-color: #ccc; -fx-border-width: 1px;");
        
        // Layout setup with a VBox
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(taskTextField, addButton, taskListView, deleteButton, refreshButton);

        // Scene setup
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setTitle("To-Do App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addTask() {
        String task = taskTextField.getText().trim();
        if (!task.isEmpty()) {
            // Save task to the database and add to observable list
            DatabaseManager.addTask(task);
            tasks.add(task);  // Directly add the task to the list
            taskTextField.clear();
            updateTaskIndexing();
        } else {
            showAlert("Input Error", "Task cannot be empty!", Alert.AlertType.ERROR);
        }
    }

    private void deleteTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            // Show confirmation dialog before deleting
            boolean confirmed = showConfirmationDialog("Confirm Deletion", "Are you sure you want to delete this task?");
            if (confirmed) {
                // Remove the task from database and observable list
                DatabaseManager.removeTask(selectedTask);
                tasks.remove(selectedTask);
                updateTaskIndexing();
            }
        } else {
            showAlert("Selection Error", "Please select a task to delete.", Alert.AlertType.ERROR);
        }
    }

    // This function ensures that the task list maintains a proper index after tasks are added or removed
    private void updateTaskIndexing() {
        ObservableList<String> indexedTasks = FXCollections.observableArrayList();
        for (int i = 0; i < tasks.size(); i++) {
            indexedTasks.add((i + 1) + ". " + tasks.get(i));  // Adds index to each task
        }
        taskListView.setItems(indexedTasks);
    }

    // Refreshes the task list by fetching the latest tasks from the database
    private void refreshTaskList() {
        tasks.clear();
        tasks.addAll(DatabaseManager.getAllTasks());  // Re-load tasks from MongoDB
        updateTaskIndexing();
    }

    private boolean showConfirmationDialog(String title, String message) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle(title);
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText(message);

        return confirmationDialog.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
