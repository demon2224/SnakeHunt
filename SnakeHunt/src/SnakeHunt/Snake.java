package SnakeHunt;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Snake class handles the snake coordinates, growth, movement, and drawing.
 * It manages the snake body as a list of coordinates and updates its state
 * based on user input and game events.
 *
 * @author Le Anh Tuan - CE180905, Nguyễn Minh Khang - CE190728, Lim The Toan -
 * CE190616, Pham Gia Bao - CE191671
 */
public class Snake {

    // A list representing the body of the snake, with each part stored as a SnakeCoordinate (x, y)
    public ArrayList<SnakeCoordinate> body;

    // The current direction the snake is moving: 'U' for up, 'D' for down, 'L' for left, 'R' for right
    public char direction = 'R'; // Default direction is right

    // The current number of body parts in the snake (starts with 1)
    private int bodyParts = 1;

    // Tracks the number of hunts eaten by the snake
    public int huntsEaten;

    /**
     * Constructor initializes the snake body with one part at (0, 0).
     */
    public Snake() {
        body = new ArrayList<>();
        body.add(new SnakeCoordinate(0, 0)); // Initial head position
    }

    /**
     * Moves the snake body and head based on the current direction.
     */
    public void move() {
        // Move each body part to the position of the part in front of it
        for (int i = bodyParts - 1; i > 0; i--) {
            body.get(i).setX(body.get(i - 1).getX());
            body.get(i).setY(body.get(i - 1).getY());
        }

        // Move the snake's head according to the current direction
        SnakeCoordinate head = body.get(0);
        switch (direction) {
            case 'U':
                head.setY(head.getY() - Map.UNIT_SIZE); // Move up by one unit
                // Commented-out wrapping logic (optional screen edge behavior)
//                if (head.getY() < 0) {
//                    head.setY(Main.SCREEN_HEIGHT - Map.UNIT_SIZE); // Wrap to bottom if past top edge
//                }
                break;
            case 'D':
                head.setY(head.getY() + Map.UNIT_SIZE); // Move down by one unit
//                if (head.getY() >= Main.SCREEN_HEIGHT) {
//                    head.setY(0); // Wrap to top if past bottom edge
//                }
                break;
            case 'L':
                head.setX(head.getX() - Map.UNIT_SIZE); // Move left by one unit
//                if (head.getX() < 0) {
//                    head.setX(Main.SCREEN_WIDTH - Map.UNIT_SIZE); // Wrap to right if past left edge
//                }
                break;
            case 'R':
                head.setX(head.getX() + Map.UNIT_SIZE); // Move right by one unit
//                if (head.getX() >= Main.SCREEN_WIDTH) {
//                    head.setX(0); // Wrap to left if past right edge
//                }
                break;
        }
    }

    /**
     * Draws the snake on the screen. The head is rendered as a green pentagon
     * oriented based on direction, while the body parts are ovals with random
     * shades of green.
     *
     * @param g the Graphics object used for drawing
     */
    public void draw(Graphics g) {
        for (int i = 0; i < body.size(); i++) {
            if (i == 0) {
                g.setColor(Color.GREEN); // The snake's head is green
                int xCenter = body.get(i).getX() + Map.UNIT_SIZE / 2; // Center x of pentagon
                int yCenter = body.get(i).getY() + Map.UNIT_SIZE / 2; // Center y of pentagon
                int radius = Map.UNIT_SIZE / 2; // Radius for pentagon

                // Define arrays for pentagon vertices
                int[] xPoints = new int[5];
                int[] yPoints = new int[5];
                double angleOffset = 0; // Default rotation angle for right-facing head
                switch (direction) {
                    case 'U':
                        angleOffset = -Math.PI / 2; // Rotate 90° counterclockwise for up
                        break;
                    case 'D':
                        angleOffset = Math.PI / 2;  // Rotate 90° clockwise for down
                        break;
                    case 'L':
                        angleOffset = Math.PI;      // Rotate 180° for left
                        break;
                    case 'R':
                        angleOffset = 0;            // No rotation for right
                        break;
                }

                // Calculate pentagon points based on center, radius, and direction
                for (int j = 0; j < 5; j++) {
                    double angle = 2 * Math.PI / 5 * j + angleOffset; // Angle for each vertex
                    xPoints[j] = (int) (xCenter + radius * Math.cos(angle)); // X-coordinate
                    yPoints[j] = (int) (yCenter + radius * Math.sin(angle)); // Y-coordinate
                }

                // Draw the head as a filled pentagon
                g.fillPolygon(xPoints, yPoints, 5);
            } else {
                // Generate random color for body parts with emphasis on green
                Random random = new Random();
                int red = random.nextInt(100);       // Red color range: 0 - 99
                int green = random.nextInt(100) + 155; // Green color range: 155 - 255
                int blue = random.nextInt(100);      // Blue color range: 0 - 99
                g.setColor(new Color(red, green, blue)); // Set color for body part
                // Draw each body part as a filled oval
                g.fillOval(body.get(i).getX(), body.get(i).getY(), Map.UNIT_SIZE, Map.UNIT_SIZE);
            }
        }
    }

    /**
     * Grows the snake by adding a new body part. The new part is positioned
     * opposite the current direction of movement to simulate natural growth.
     */
    public void grow() {
        // Get the coordinates of the last part of the snake
        SnakeCoordinate lastPart = body.get(body.size() - 1);

        int newX = lastPart.getX();
        int newY = lastPart.getY();

        // Position the new part based on the current direction
        switch (direction) {
            case 'U':
                newY += Map.UNIT_SIZE; // Add below if moving up
                break;
            case 'D':
                newY -= Map.UNIT_SIZE; // Add above if moving down
                break;
            case 'L':
                newX += Map.UNIT_SIZE; // Add to the right if moving left
                break;
            case 'R':
                newX -= Map.UNIT_SIZE; // Add to the left if moving right
                break;
        }

        body.add(new SnakeCoordinate(newX, newY)); // Add new part to the body
        huntsEaten++; // Increment hunts eaten
        bodyParts++;  // Increase body length
    }

    /**
     * Checks for collisions with the snake own body or the screen boundaries.
     *
     * @return true if no collision occurs, false if a collision is detected
     */
    public boolean checkCollisions() {
        SnakeCoordinate head = body.get(0); // Head of the snake

        // Check for self-collision
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return false; // Collision with itself
            }
        }

        // Check for wall collisions (out of bounds)
        if (head.getX() < 0 || head.getX() >= Main.SCREEN_WIDTH
                || head.getY() < 0 || head.getY() >= Main.SCREEN_HEIGHT) {
            return false; // Collision with wall
        }

        return true; // No collision
    }

    /**
     * Checks if the snake head has collided with a hunt. If a collision
     * occurs, the snake grows and a new hunt is generated.
     *
     * @param hunt the Hunt object to check for collision with
     */
    public void checkHunt(Hunt hunt) {
        SnakeCoordinate head = body.get(0); // Head of the snake

        // Check if the head is at the same position as the hunt
        if (head.getX() == hunt.getX() && head.getY() == hunt.getY()) {
            grow();       // Snake grows
            hunt.newHunt(); // Generate a new hunt
        }
    }
}
