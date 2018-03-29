/*
 * CS2852 - 021
 * Spring 2018
 * Lab 1 - Dot 2 Dot Generator
 * Name: Rock Boynton
 * Created: 3/6/2018
 */

package boyntonrl;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds a list of Dots that describe a picture.
 */
public class Picture {

    /**
     * Size of the dots
     */
    public static final int DOT_SIZE = 5;

    private static final Logger LOGGER = Dot2Dot.LOGGER;
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
                // create an array out of the read line: the first element is the x value, the
                // second element is the y value
                List<String> xAndY = Arrays.asList(fileIn.nextLine().split(","));
                if (xAndY.size() != 2) { // This would mean file is formatted incorrectly
                    throw new NumberFormatException();
                }
                x = Double.valueOf(xAndY.get(0)) * Dot2DotController.CANVAS_WIDTH;
                y = Math.abs(Double.valueOf(xAndY.get(1)) * Dot2DotController.CANVAS_HEIGHT -
                        Dot2DotController.CANVAS_HEIGHT); // not sure why the Y values are inverted
                dots.add(new Dot(x, y));
            }
            LOGGER.info("User successfully loaded image" + file.getPath());
        } catch (IOException ioe) {
            showReadFailureAlert();
            LOGGER.log(Level.WARNING, "Could not open .dot file", ioe);
        } catch (NumberFormatException nfe) {
            showReadFailureAlert();
            LOGGER.log(Level.WARNING, "Could not open .dot file", nfe);
        }
    }

    /**
     * Draws each dot in the picture onto the canvas
     * @param canvas the canvas to draw dots
     */
    public void drawDots(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (Dot dot : dots) {
            gc.setFill(Color.BLACK);
            //centers oval on dot and fills it to a diameter of DOT_SIZE
            gc.fillOval(dot.getX() - DOT_SIZE / 2.0, dot.getY() - DOT_SIZE / 2.0, DOT_SIZE,
                    DOT_SIZE);
        }
    }

    /**
     * Draws lines between all neighboring dots in the picture on the canvas.
     * @param canvas the canvas to draw the lines
     */
    public void drawLines(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.beginPath();
        for (int i = 0; i < dots.size() - 1; i++) {
            Dot dot1 = dots.get(i);
            Dot dot2 = dots.get(i + 1);
            gc.moveTo(dot1.getX(), dot1.getY());
            gc.lineTo(dot2.getX(), dot2.getY());
        }
        gc.closePath();
        gc.stroke();

    }

    private static void showReadFailureAlert() {
        Alert readFailureAlert = new Alert(Alert.AlertType.ERROR, "Error: Could not " +
                "read dots from specified file. File may be corrupt ");
        readFailureAlert.setTitle("Error Dialog");
        readFailureAlert.setHeaderText("Read Failure");
        readFailureAlert.showAndWait();
    }

}
