/*
 * CS2852 - 021
 * Spring 2018
 * Lab 2 - Dot 2 Dot Generator
 * Name: Rock Boynton
 * Created: 3/15/2018
 */

package boyntonrl;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Holds a list of Dots that describe a picture.
 */
public class Picture {

    /**
     * Size of the dots
     */
    public static final int DOT_SIZE = 5;

    private List<Dot> dots;

    /**
     * Constructor that uses the list emptyList passed to it to store the dots for this picture.
     * @param emptyList list to store dots for the picture
     */
    public Picture(List<Dot> emptyList) {
        this.dots = emptyList;
    }

    /**
     * Constructor that copies the dots from original into emptyList and uses it to store the
     * dots for this picture.
     * @param original list to be copied
     * @param emptyList list copied from original list to store dots for the picture
     */
    public Picture(Picture original, List<Dot> emptyList) {
        emptyList.addAll(original.dots);
        this.dots = emptyList;
    }

    /**
     *  Loads all of the dots from a .dot file.
     * @param file the dot file to load in dots
     * @throws IOException if there's an error with the file
     */
    public void load(File file) throws IOException{
        double x;
        double y;

        try (Scanner fileIn = new Scanner(file)) {
            while (fileIn.hasNextLine()) {
                // create an array out of the read line: the first element is the x value, the
                // second element is the y value
                List<String> xAndY = Arrays.asList(fileIn.nextLine().split(","));
                if (xAndY.size() != 2) { // This would mean file is formatted incorrectly
                    throw new NumberFormatException("File is formatted incorrectly: Must have " +
                            "only X and Y coordinates");
                }
                if (Double.valueOf(xAndY.get(0)) < 0 || Double.valueOf(xAndY.get(0)) > 1 ||
                        Double.valueOf(xAndY.get(1)) < 0 || Double.valueOf(xAndY.get(1)) > 1) {
                    throw new NumberFormatException("File is formatted incorrectly: point " +
                            "coordinates not between 0 and 1");
                }
                x = Double.valueOf(xAndY.get(0)) * Dot2DotController.CANVAS_WIDTH;
                y = Math.abs(Double.valueOf(xAndY.get(1)) * Dot2DotController.CANVAS_HEIGHT -
                        Dot2DotController.CANVAS_HEIGHT);
                dots.add(new Dot(x, y));
            }
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
        Dot dot1;
        Dot dot2;

        gc.beginPath();
        for (int i = 0; i < dots.size() - 1; i++) {
            dot1 = dots.get(i);
            dot2 = dots.get(i + 1);
            drawLine(gc, dot1, dot2);
        }
        // draw final line from the last dot to the first
        dot1 = dots.get(dots.size() - 1);
        dot2 = dots.get(0);
        drawLine(gc, dot1, dot2);

        gc.closePath();
        gc.stroke();
    }

    /**
     * Saves the picture to .dot file that is compatible with the format described in lab 1
     * description: http://msoe.us/taylor/cs2852/Lab1.
     * @param file file to be written to
     * @throws IOException if there's an error with the file
     */
    public void save(File file) throws IOException{
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Dot dot : dots) {
                // subtract y from 1 to flip the image; divide x and y by the height and width of
                // the canvas to get values between 0 and 1
                writer.println((dot.getX() / Dot2DotController.CANVAS_WIDTH) + ","
                        + (1 - (dot.getY() / Dot2DotController.CANVAS_HEIGHT)));
            }
        }
    }

    /**
     * Removes all but the numberDesired most critical dots. If the picture does not have more
     * than numberDesired dots, the method returns without changing the picture. If numberDesired
     * < 3, an IllegalArgumentException is thrown. This method uses index based methods to
     * access elements of the list.
     * @param numberDesired amount of dots to be left in the picture after the method is called.
     * @return The time this method took to complete (in nanoseconds)
     * @throws IllegalArgumentException if numberDesired < 3
     */
    public long removeDots(int numberDesired) throws IllegalArgumentException{
        long startTime = System.nanoTime();
        long endTime;
        if (numberDesired < 3) {
            throw new IllegalArgumentException("The number of dots required has to be greater " +
                    "than 2");
        } else {
            double lowestCriticalValue;
            double currentCriticalValue;
            int lowestCriticalIndex;
            while (dots.size() > numberDesired) {
                // calculate first dot's critical value; first dot will always initially have the
                // lowest critical value
                lowestCriticalValue = dots.get(0).calculateCriticalValue(dots.get(dots.size() - 1),
                        dots.get(1));
                lowestCriticalIndex = 0;
                // calculate rest of dot's critical values except last dot
                for (int i = 1; i < dots.size() - 2; i++) {
                    currentCriticalValue = dots.get(i).calculateCriticalValue(dots.get(i - 1), dots
                            .get(i + 1));
                    if (currentCriticalValue < lowestCriticalValue) {
                        lowestCriticalValue = currentCriticalValue;
                        lowestCriticalIndex = i;
                    }
                }
                // calculate last dot's critical value
                currentCriticalValue = dots.get(dots.size() - 1).calculateCriticalValue(dots.get(
                        dots.size() - 2), dots.get(0));
                if (currentCriticalValue < lowestCriticalValue) {
                    lowestCriticalIndex = dots.size() - 1;
                } // doing it this way handles the edge cases saving two conditional checks every
                // loop

                dots.remove(lowestCriticalIndex);
            }
        }
        endTime = System.nanoTime();
        return endTime - startTime;
    }

    private void drawLine(GraphicsContext gc, Dot dot1, Dot dot2) {
        gc.moveTo(dot1.getX(), dot1.getY());
        gc.lineTo(dot2.getX(), dot2.getY());
    }

    /**
     * /**
     * Removes all but the numberDesired most critical dots. If the picture does not have more
     * than numberDesired dots, the method returns without changing the picture. If numberDesired
     * < 3, an IllegalArgumentException is thrown. Makes use of Iterators to traverse list of
     * dots. This method does not use any index based methods to access elements of the list.
     * @param numberDesired amount of dots to be left in the picture after the method is called.
     * @return The time this method took to complete (in nanoseconds)
     * @throws IllegalArgumentException if numberDesired < 3
     */
    public long removeDots2(int numberDesired) {
        long startTime = System.nanoTime();
        long endTime;
        if (numberDesired < 3) {
            throw new IllegalArgumentException("The number of dots required has to be greater " +
                    "than 2");
        } else {
            // Use Collection<Dot> reference to point to list of dots to have the compiler throw
            // an error if trying to use any index-based methods
//            Collection<Dot> dots = this.dots;

            Iterator<Dot> itr = dots.iterator();
            double lowestCriticalValue;
            double currentCriticalValue;
            Dot leastCriticalDot;
            Dot previousDot;
            Dot nextDot = null;
            Dot firstDot;
            Dot secondDot;
            Dot lastDot;
            Dot currentDot = null;
            Dot secondLastDot;
            while (dots.size() > numberDesired) {
                firstDot = itr.next();
                secondDot = itr.next();
                lowestCriticalValue = secondDot.calculateCriticalValue(firstDot, itr.next());
                leastCriticalDot = secondDot;
                previousDot = secondDot;
                // calculate rest of dot's critical values except last and first dots
                for (Dot dot : dots.subList(2, dots.size() - 1)) {
                    currentDot = dot;
                    nextDot = itr.next();
                    currentCriticalValue = currentDot.calculateCriticalValue(previousDot, nextDot);
                    if (currentCriticalValue < lowestCriticalValue) {
                        lowestCriticalValue = currentCriticalValue;
                        leastCriticalDot = currentDot;
                    }
                    previousDot = currentDot;
                }
                // calculate last dot's critical value
                secondLastDot = currentDot;
                lastDot = nextDot;
                currentCriticalValue = lastDot.calculateCriticalValue(secondLastDot, firstDot);
                if (currentCriticalValue < lowestCriticalValue) {
                    leastCriticalDot = lastDot;
                }
                currentCriticalValue = firstDot.calculateCriticalValue(lastDot, secondDot);
                if (currentCriticalValue < lowestCriticalValue) {
                    leastCriticalDot = firstDot;
                }

                dots.remove(leastCriticalDot);
            }
        }
        endTime = System.nanoTime();
        return endTime - startTime;
//        return 0;
    }
}
