/*
 * CS2852 - 021
 * Spring 2018
 * Lab 3 - Dot 2 Dot Generator
 * Name: Rock Boynton
 * Created: 3/25/2018
 */

package boyntonrl;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dot2DotController class for the Dot 2 Dot Generator application
 */
public class Dot2DotController implements Initializable {

    private static final Logger LOGGER = Dot2Dot.LOGGER;
    /**
     * Width of canvas
     */
    public static final int CANVAS_WIDTH = 770;

    /**
     * Height of canvas
     */
    public static final int CANVAS_HEIGHT = 770;

    private Picture originalPicture;
    private Picture picture;

    @FXML
    private Canvas canvas;
    @FXML
    private MenuItem reloadOriginal;
    @FXML
    private MenuItem linesOnly;
    @FXML
    private MenuItem dotsOnly;
    @FXML
    private MenuItem dotsAndLines;
    @FXML
    private MenuItem removeDots;
    @FXML
    private Label timeLabel;

    @FXML
    private void save(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save New Dot File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Dot File", "*.dot"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                picture.save(file);
                LOGGER.info("User successfully saved file");
            } catch (IOException ioe) {
                showSaveFailureAlert();
                LOGGER.log(Level.WARNING, "Could not save .dot file: " + file.getPath(), ioe);
            }
        } else {
            LOGGER.log(Level.INFO, "User canceled saving Dot File");
        }
    }

    @FXML
    private void open(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Dot Files", "*.dot"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            canvas.getGraphicsContext2D().clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            originalPicture = new Picture(new ArrayList<>());
            try {
                originalPicture.load(file);
                originalPicture.drawLines(canvas);
                originalPicture.drawDots(canvas);
                LOGGER.info("User successfully loaded image" + file.getPath());
                reloadOriginal.setDisable(false);
                linesOnly.setDisable(false);
                dotsOnly.setDisable(false);
                dotsAndLines.setDisable(false);
                removeDots.setDisable(false);
                picture = originalPicture;
                timeLabel.setVisible(false);
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
    private void reloadOriginal(ActionEvent e) {
        picture = originalPicture;
        setDotsAndLines(e);
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

    @FXML
    private void removeDots(ActionEvent e) {
        Optional<String> result = getNumDotsToRemove();
        result.ifPresent(numDots -> {
            try {
                Optional<String> listAndAlgoChoice = showListAndRemovalAlgoAlert();
                listAndAlgoChoice.ifPresent(choice -> {
                    long time = 0;
                    if (choice.equals("ArrayList Index Only Methods")) {
                        picture = new Picture(originalPicture, new ArrayList<>());
                        time = picture.removeDots(Integer.parseInt(numDots));
                    } else if (choice.equals("LinkedList Index Only Methods")) {
                        picture = new Picture(originalPicture, new LinkedList<>());
                        time = picture.removeDots(Integer.parseInt(numDots));
                    } else if (choice.equals("ArrayList Iterator Only Methods")) {
                        picture = new Picture(originalPicture, new ArrayList<>());
                        time = picture.removeDots2(Integer.parseInt(numDots));
                    } else if (choice.equals("LinkedList Iterator Only Methods")) {
                        picture = new Picture(originalPicture, new LinkedList<>());
                        time = picture.removeDots2(Integer.parseInt(numDots));
                    }
                    // convert nanosecond time to hh:mm:ss.sss format and display
                    timeLabel.setText("Time to Remove Dots: " + formatTime(time));
                    timeLabel.setVisible(true);
                    setDotsAndLines(e);
                });
            } catch (IllegalArgumentException iae) {
                showInvalidNumRemainingDotsAlert();
            }
        });
    }

    private Optional<String> getNumDotsToRemove() {
        TextInputDialog dialog = new TextInputDialog("50");
        dialog.setTitle("Remove Dots");
        dialog.setHeaderText("How Many Dots Would You Like To Remain?");
        dialog.setContentText("Number of Dots:");
        return dialog.showAndWait();
    }

    /**
     * Initializes a blank originalPicture with an empty list od Dots
     * @param location URL location
     * @param resources ResourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        originalPicture = new Picture(new ArrayList<>());
    }

    private static void showReadFailureAlert() {
        Alert readFailureAlert = new Alert(Alert.AlertType.ERROR, "Error: Could not " +
                "read dots from specified file. File may be corrupt ");
        readFailureAlert.setTitle("Error Dialog");
        readFailureAlert.setHeaderText("Read Failure");
        readFailureAlert.showAndWait();
    }

    private static void showSaveFailureAlert() {
        Alert saveFailureAlert = new Alert(Alert.AlertType.ERROR, "Error: Could not " +
                "save dots to specified file.");
        saveFailureAlert.setTitle("Error Dialog");
        saveFailureAlert.setHeaderText("Save Failure");
        saveFailureAlert.showAndWait();
    }

    private static void showInvalidNumRemainingDotsAlert() {
        Alert invalidNumRemainingDots = new Alert(Alert.AlertType.ERROR, "Error: You need to " +
                "have at least 3 dots remaining in the file");
        invalidNumRemainingDots.setTitle("Error Dialog");
        invalidNumRemainingDots.setHeaderText("Remove Dots Failure");
        invalidNumRemainingDots.showAndWait();
    }

    private static Optional<String> showListAndRemovalAlgoAlert() {
        List<String> choices = new ArrayList<>();
        choices.add("ArrayList Index Only Methods");
        choices.add("ArrayList Iterator Only Methods");
        choices.add("LinkedList Index Only Methods");
        choices.add("LinkedList Iterator Only Methods");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("ArrayList Index Only Methods",
                choices);
        dialog.setTitle("List Type And Removal Algorithm");
        dialog.setHeaderText("Which list type would you like to use?");
        dialog.setContentText("Choose your option: ");

        return dialog.showAndWait();
    }

    private static String formatTime(final long nsTime) {
        final long hr = TimeUnit.NANOSECONDS.toHours(nsTime);
        final long min = TimeUnit.NANOSECONDS.toMinutes(nsTime - TimeUnit.HOURS.toNanos(hr));
        final long sec = TimeUnit.NANOSECONDS.toSeconds(nsTime - TimeUnit.HOURS.toNanos(hr) -
                TimeUnit.MINUTES.toNanos(min));
        final long ms = TimeUnit.NANOSECONDS.toNanos(nsTime - TimeUnit.HOURS.toNanos(hr) -
                TimeUnit.MINUTES.toNanos(min) - TimeUnit.SECONDS.toNanos(sec));
        // hh:mm:ss.sss
        return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
    }
}
