package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import connexion.Connexion; // Import the Connexion class

public class Main extends Application {

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginDesign.fxml"));
            StackPane root = new StackPane(); // Create a root node
            loader.setRoot(root); // Set the root node
            
            Parent content = loader.load(); // Load the FXML content
            
            Scene scene = new Scene(content, 600, 430); // Initialize the Scene object
            
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Check the connection after the JavaFX application is launched
            if (connexion.Connexion.getConn() != null) {
                System.out.println("Success: Connected to the database.");
            } else {
                System.out.println("Error: Failed to connect to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
