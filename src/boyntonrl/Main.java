/*
 * CS2852 - 021
 * Spring 2018
 * Lab 1 - Dot 2 Dot Generator
 * Name: Rock Boynton
 * Created: 3/6/2018
 */

package boyntonrl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of a JavaFX dot-to-dot generator program to graphically display images as dots and
 * lines.
 */
public class Main extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    /**
     * Sets up the primary stage
     * @param primaryStage primary stage of the program
     * @throws Exception if anything goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Dot 2 Dot Generator");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
