package SnakeHunt;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Hunt holds the x and y coordinates for a hunt position on the map. The
 * hunt location is randomly generated and does not overlap with the snake.
 *
 * @author Le Anh Tuan - CE180905, Nguyễn Minh Khang - CE190728, Lim The Toan -
 * CE190616, Pham Gia Bao - CE191671
 */
public class Hunt {

    private int x; // x-coordinate of the hunt on the map
    private int y; // y-coordinate of the hunt on the map
    private Snake snake; // Reference to the Snake object, used to avoid hunt spawning on snake
    private Random random = new Random(); // Random object to generate random coordinates

    /**
     * Constructor that takes a Snake object to avoid overlap. Calls newHunt()
     * to generate the initial position of the hunt.
     */
    public Hunt(Snake snake) {
        this.snake = snake;
        newHunt(); // Generate the hunt's initial position
    }

    /**
     * Generates a new position for the hunt, ensuring it doesn't overlap with
     * the snake. Loops until a position not on the snake is found.
     */
    public void newHunt() {
        boolean huntOnSnake;

        do {
            // Calculate random coordinates within the screen bounds, aligned to grid units
            x = random.nextInt(Main.SCREEN_WIDTH / Map.UNIT_SIZE - 1) * Map.UNIT_SIZE;
            y = random.nextInt(Main.SCREEN_HEIGHT / Map.UNIT_SIZE - 1) * Map.UNIT_SIZE;

            // Check if the new position overlaps with the snake's body
            huntOnSnake = !checkHuntCordinate(snake.body);
        } while (huntOnSnake); // Continue looping until a valid position is found
    }

    /**
     * Checks if the hunt coordinates overlap with any segment of the snake.
     *
     * @param list the list of snake segments to check against
     * @return true if the hunt coordinates are not on the snake, false
     * otherwise
     */
    public boolean checkHuntCordinate(ArrayList<SnakeCoordinate> list) {
        for (SnakeCoordinate point : list) {
            // Return false if hunt's coordinates overlap with any snake segment
            if (point.getX() == x && point.getY() == y) {
                return false;
            }
        }
        return true;
    }

    /**
     * Draws the hunt on the screen as a red oval.
     *
     * @param g the Graphics object used to draw the hunt
     */
    public void draw(Graphics g) {
        g.setColor(Color.RED); // Set color to red
        g.fillOval(x, y, Map.UNIT_SIZE, Map.UNIT_SIZE); // Draw the hunt as a filled oval
    }

    /**
     * Getter for the x-coordinate of the hunt.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the y-coordinate of the hunt.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
}
