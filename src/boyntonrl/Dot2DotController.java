/*
 * CS2852 - 021
 * Spring 2018
 * Lab 1 - Dot 2 Dot Generator
 * Name: Rock Boynton
 * Created: 3/6/2018
 */

package boyntonrl;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dot2DotController class for the Dot 2 Dot Generator application
 */
public class Dot2DotController implements Initializable{

    private static final Logger LOGGER = Dot2Dot.LOGGER;

    /**
     * Width of canvas
     */
    public static final int CANVAS_WIDTH = 600;

    /**
     * Height of canvas
     */
    public static final int CANVAS_HEIGHT = 600;

    private Picture picture;

    @FXML
    private Canvas canvas;
    @FXML
    private MenuItem linesOnly;
    @FXML
    private MenuItem dotsOnly;
    @FXML
    private MenuItem dotsAndLines;

    @FXML
    private void open(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Dot Files", "*.dot"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            canvas.getGraphicsContext2D().clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            picture = new Picture(new ArrayList<>());
            try {
                picture.load(file);
                picture.drawLines(canvas);
                picture.drawDots(canvas);
                LOGGER.info("User successfully loaded image" + file.getPath());
                linesOnly.setDisable(false);
                dotsOnly.setDisable(false);
                dotsAndLines.setDisable(false);
            } catch (IOException ioe) {
                showReadFailureAlert();
                LOGGER.log(Level.WARNING, "Could not open .dot file: " + file.getPath(), ioe);
            } catch (NumberFormatException nfe) {
                showReadFailureAlert();
                LOGGER.log(Level.WARNING, "Could not open .dot file: " + file.getPath(), nfe);
            }
        } else {
            LOGGER.log(Level.INFO, "User canceled loading image");
        }
    }

    @FXML
    private void close(ActionEvent e) {
        LOGGER.log(Level.INFO, "User closed the program");
        Platform.exit();
    }

    @FXML
    private void setLinesOnly(ActionEvent e) {
        canvas.getGraphicsContext2D().clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        picture.drawLines(canvas);
    }

    @FXML
    private void setDotsOnly(ActionEvent e) {
        canvas.getGraphicsContext2D().clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        picture.drawDots(canvas);
    }

    @FXML
    private void setDotsAndLines(ActionEvent e) {
        canvas.getGraphicsContext2D().clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        picture.drawDots(canvas);
        picture.drawLines(canvas);
    }

    /**
     * Initializes a blank picture with an empty list od Dots
     * @param location URL location
     * @param resources ResourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        picture = new Picture(new ArrayList<>());
    }

    private static void showReadFailureAlert() {
        Alert readFailureAlert = new Alert(Alert.AlertType.ERROR, "Error: Could not " +
                "read dots from specified file. File may be corrupt ");
        readFailureAlert.setTitle("Error Dialog");
        readFailureAlert.setHeaderText("Read Failure");
        readFailureAlert.showAndWait();
    }
}
