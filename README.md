this is a readme file 
javac --module-path "lib/javafx-sdk-23.0.1/lib" -cp "lib/mongodb-driver-sync-5.1.1.jar;lib/mongodb-driver-core-5.1.1.jar;lib/bson-5.1.1.jar;lib/slf4j-api-1.7.36.jar;lib/slf4j-simple-1.7.36.jar" -d build --add-modules javafx.controls,javafx.fxml,javafx.graphics src/ToDoApp.java src/DatabaseManager.java


java --module-path "lib/javafx-sdk-23.0.1/lib" \
-cp "build;lib/mongodb-driver-sync-5.1.1.jar;lib/mongodb-driver-core-5.1.1.jar;lib/bson-5.1.1.jar;lib/slf4j-api-1.7.36.jar;lib/slf4j-simple-1.7.36.jar" \
--add-modules javafx.controls,javafx.fxml,javafx.graphics ToDoApp

