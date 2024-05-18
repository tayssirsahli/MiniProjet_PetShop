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
            
            
            Parent content = loader.load();
            
            Scene scene = new Scene(content, 600, 430); 
            
            primaryStage.setScene(scene);
            primaryStage.show();
            
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
    
    
    public static void checkDatabaseConnection() {
        if (connexion.Connexion.getConn() != null) {
            System.out.println("Success: Connected to the database.");
        } else {
            System.out.println("Error: Failed to connect to the database.");
        }
    }
}
