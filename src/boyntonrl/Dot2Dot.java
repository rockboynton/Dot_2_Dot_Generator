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
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Dot2Dot class of a JavaFX dot-to-dot generator program to graphically display images as dots and
 * lines.
 */
public class Dot2Dot extends Application {

    /**
     * Logger for the Dot-to-Dot generator program
     */
    public static Logger LOGGER;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    /**
     * Sets up the primary stage and a logger
     * @param primaryStage primary stage of the program
     * @throws Exception if anything goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        setUpLogger();

        FXMLLoader primaryLoader = new FXMLLoader();
        Parent primaryRoot = primaryLoader.load(getClass().getResource("main.fxml").
                openStream());
        primaryStage.setTitle("Dot 2 Dot Generator");
        primaryStage.setScene(new Scene(primaryRoot, WIDTH, HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();

        Controller controller = primaryLoader.getController();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void setUpLogger() {
        LOGGER.setUseParentHandlers(false);
        try {
            FileHandler fh = new FileHandler(System.getProperty("user.dir") +
                    File.separator + "Dot2Dot.txt", true);
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            LOGGER.severe("Cannot create log file");
            Alert logFileAlert = new Alert(Alert.AlertType.ERROR, "Error with Log" +
                    " file ");
            logFileAlert.setTitle("Error Dialog");
            logFileAlert.setHeaderText("Log file error");
            logFileAlert.showAndWait();
        }
    }
}
