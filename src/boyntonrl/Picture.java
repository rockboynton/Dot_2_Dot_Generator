/*
 * CS2852 - 021
 * Spring 2018
 * Lab 1 - Dot 2 Dot Generator
 * Name: Rock Boynton
 * Created: 3/6/2018
 */

package boyntonrl;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds a list of Dots that describe a picture.
 */
public class Picture {

    private Logger LOGGER = Dot2Dot.LOGGER;

    private ArrayList<Dot> dots = new ArrayList<>();

    /**
     *  Loads all of the dots from a .dot file.
     * @param file the dot file to load in dots
     */
    public void load(File file) {

        double x;
        double y;

        try (Scanner fileIn = new Scanner(file)) {
            while (fileIn.hasNextLine()) {
                x = fileIn.nextDouble();
                y = fileIn.nextDouble();
                dots.add(new Dot(x, y));
            }
        } catch (IOException ioe) {
            showReadFailureAlert();
            LOGGER.log(Level.WARNING, "Could not open .dot file", ioe);
        }
    }

    /**
     * Draws each dot in the picture onto the canvas
     * @param canvas the canvas to draw dots
     */
    public void drawDots(Canvas canvas) {

    }

    /**
     * Draws lines between all neighboring dots in the picture on the canvas.
     * @param canvas the canvas to draw the lines
     */
    public void drawLines(Canvas canvas) {

    }

    private static void showReadFailureAlert() {
        Alert readFailureAlert = new Alert(Alert.AlertType.ERROR, "Error: Could not " +
                "read dots from specified file. File may be corrupt ");
        readFailureAlert.setTitle("Error Dialog");
        readFailureAlert.setHeaderText("Read Failure");
        readFailureAlert.showAndWait();
    }

}
