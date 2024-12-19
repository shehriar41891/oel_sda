import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static final String MONGO_URI = "mongodb://localhost:27017"; // MongoDB URI
    private static final String DATABASE_NAME = "ToDoApp"; // Ensure this matches the case of your existing database
    private static final String COLLECTION_NAME = "tasks"; // Collection name

    // Connect to the MongoDB database
    public static void connectToDatabase() {
        try {
            mongoClient = MongoClients.create(MONGO_URI);
            // Check if the database exists by listing all databases and comparing
            boolean dbExists = mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(DATABASE_NAME);
            
            if (!dbExists) {
                // If the database doesn't exist, MongoDB will create it automatically when we insert documents.
                System.out.println("Database does not exist. MongoDB will create it when a document is added.");
            } else {
                System.out.println("Connected to existing database: " + DATABASE_NAME);
            }
            
            // Access the database
            database = mongoClient.getDatabase(DATABASE_NAME);
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    // Get all tasks from the database
    public static List<String> getAllTasks() {
        List<String> tasks = new ArrayList<>();
        try {
            for (Document doc : database.getCollection(COLLECTION_NAME).find()) {
                tasks.add(doc.getString("task"));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving tasks: " + e.getMessage());
        }
        return tasks;
    }

    // Add a task to the database
    public static void addTask(String task) {
        try {
            Document newTask = new Document("task", task);
            database.getCollection(COLLECTION_NAME).insertOne(newTask);
            System.out.println("Task added: " + task);
        } catch (Exception e) {
            System.err.println("Error adding task: " + e.getMessage());
        }
    }

    // Remove a task from the database
    public static void removeTask(String task) {
        try {
            database.getCollection(COLLECTION_NAME).deleteOne(new Document("task", task));
            System.out.println("Task removed: " + task);
        } catch (Exception e) {
            System.err.println("Error removing task: " + e.getMessage());
        }
    }

    // Close the database connection
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Database connection closed.");
        }
    }
}
