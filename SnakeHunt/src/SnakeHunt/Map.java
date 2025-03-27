package SnakeHunt;

import java.awt.*;
import javax.swing.*;

/**
 * Represents the game map, extending JPanel to handle the graphical rendering
 * of the game grid. This class is responsible for drawing the background and
 * grid lines that form the playing field for the Snake Hunt game.
 *
 * @author Le Anh Tuan - CE180905, Nguyễn Minh Khang - CE190728, Lim The Toan -
 * CE190616, Pham Gia Bao - CE191671
 */
public class Map extends JPanel {

    public static final int UNIT_SIZE = 30; // Size of each grid unit in pixels

    /**
     * Constructor to initialize the map with a black background.
     */
    public Map() {
        this.setBackground(Color.BLACK); // Set background color for the map
    }

    /**
     * Draws the grid on the map using dark gray lines, creating a tiled effect
     * for the game field.
     *
     * @param g the Graphics object used to render the grid
     */
    public void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Enable anti-aliasing for smoother lines
        g2d.setColor(Color.DARK_GRAY); // Set line color to dark gray

        // Draw vertical lines across the width of the screen
        for (int i = 0; i < Main.SCREEN_WIDTH / UNIT_SIZE; i++) {
            g2d.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, Main.SCREEN_HEIGHT);
        }

        // Draw horizontal lines across the height of the screen
        for (int i = 0; i < Main.SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g2d.drawLine(0, i * UNIT_SIZE, Main.SCREEN_WIDTH, i * UNIT_SIZE);
        }
    }
}
