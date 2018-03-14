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

    /**
     * Returns the critical value for the dot based on the neighboring dots that are passed to
     * the method. The critical value of dot 2 is calculated as the sum of the distances from
     * dot 2 to dot 1 and from dot 2 to dot 3 minus the distance from dot 1 to dot 3, i.e.,
     * cv2=d12+d23−d13 where cvy is the critical value for dot y and dxz are the distances
     * between them.
     * @param previous dot1
     * @param next dot2
     * @return critical value of the dot
     */
    public double calculateCriticalValue(Dot previous, Dot next) {
        return calculateDistance(previous, this) + calculateDistance(this, next) -
                calculateDistance(previous, next);
    }

    private static double calculateDistance(Dot dotA, Dot dotB) {
        double xDistance = dotB.getX() - dotA.getX();
        double yDistance = dotB.getY() - dotA.getY();
        double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        return Math.abs(distance);
    }
}
