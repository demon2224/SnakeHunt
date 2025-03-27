package SnakeHunt;

/**
 * SnakeCoordinate holds the x and y coordinates for a segment of the snake. It
 * provides utility methods for setting, getting, and comparing coordinates,
 * used to track the snake position on the game map.
 *
 * @author Le Anh Tuan - CE180905, Nguyễn Minh Khang - CE190728, Lim The Toan -
 * CE190616, Pham Gia Bao - CE191671
 */
public class SnakeCoordinate {

    private int x; // x-coordinate on the map
    private int y; // y-coordinate on the map

    /**
     * Constructor to initialize the x and y coordinates for a snake segment.
     *
     * @param x the initial x-coordinate
     * @param y the initial y-coordinate
     */
    public SnakeCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x-coordinate.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Setter for x-coordinate.
     *
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter for y-coordinate.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for y-coordinate.
     *
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Compares this coordinate with another to check if they are the same.
     * Overrides Object's equals method to compare based on x and y values.
     *
     * @param obj the object to compare with this coordinate
     * @return true if the objects are SnakeCoordinate instances with identical
     *         x and y values, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SnakeCoordinate) {
            SnakeCoordinate other = (SnakeCoordinate) obj;
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }
}