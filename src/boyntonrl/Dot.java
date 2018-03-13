/*
 * CS2852 - 021
 * Spring 2018
 * Lab 1 - Dot 2 Dot Generator
 * Name: Rock Boynton
 * Created: 3/6/2018
 */

package boyntonrl;

/**
 * Represents a dot in the picture.
 */
public class Dot {
    private final double x;
    private final double y;

    /**
     * Constructor for a Dot object
     * @param x The horizontal component of the dot
     * @param y The vertical component of the dot.
     */
    public Dot(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
