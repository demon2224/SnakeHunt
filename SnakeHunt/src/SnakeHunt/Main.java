package SnakeHunt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main class for the Snake Hunt game. Handles game states, UI, and key inputs.
 *
 * @author Le Anh Tuan - CE180905, Nguyễn Minh Khang - CE190728, Lim The Toan -
 * CE190616, Pham Gia Bao - CE191671
 */
public class Main extends JPanel implements ActionListener {

    // Constants for screen dimensions and game speed
    /**
     * Width of the game screen in pixels.
     */
    public static final int SCREEN_WIDTH = 1380;

    /**
     * Height of the game screen in pixels.
     */
    public static final int SCREEN_HEIGHT = 720;

    /**
     * Initial delay between game updates in milliseconds, controlling the snake
     * speed.
     */
    public static final int INITIAL_DELAY = 150;

    private int currentDelay; // Current speed, can be adjusted

    // Game objects and states
    Snake snake;  // Represents the snake in the game
    Hunt hunt;    // Represents the hunt object, linked to the snake
    Map map;      // Represents the game map/grid
    Timer timer;  // Controls game loop timing
    boolean running;  // Indicates if the game is actively running
    private boolean inMenu = true; // True if game is in menu state
    private boolean inSA = false;  // True if game is in "Showing About" state
    boolean gameOver = false;      // Tracks game-over state
    boolean inQuit = false;        // True if game is in quitting state
    private boolean inHowToPlay = false; // True if game is in "How to Play" state
    private boolean inComingSoon = false; // True if game is in "Coming Soon" state
    private long startTime;        // Tracks the start time of the game
    private int level = 1;         // Current level (1-3)
    private static final int MAX_LEVEL = 3; // Maximum number of levels
    private static final int[] WIN_SCORES = {5, 10, 15}; // Required scores to win each level
    private boolean levelCompleted = false; // True if current level is completed

    /**
     * Constructor to set up the game panel
     */
    public Main() {
        this.setBackground(Color.BLACK);   // Set background color
        this.setOpaque(true);              // Ensure correct background drawing
        this.addKeyListener(new MyKeyAdapter()); // Add KeyListener for key inputs
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Set panel size
        this.currentDelay = INITIAL_DELAY; // Initialize current delay
    }

    /**
     * Custom painting method that handles drawing different screens based on
     * the game state. This method determines what to render—menu, game, "About"
     * screen, or game over.
     *
     * @param g The Graphics object used for drawing on the panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inMenu) {
            drawMenu(g);
        } else if (inHowToPlay) {
            drawHowToPlay(g);
        } else if (inSA) {
            drawAbout(g);
        } else if (inQuit) {
            drawQuit(g);
        } else if (inComingSoon) {
            drawComingSoon(g);
        } else {
            if (running) {
                map.drawGrid(g);
                snake.draw(g);
                hunt.draw(g);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + snake.huntsEaten + "  Level: " + level,
                        (SCREEN_WIDTH - metrics.stringWidth("Score: " + snake.huntsEaten + "  Level: " + level)) / 2,
                        g.getFont().getSize());
                long elapsedMillis = System.currentTimeMillis() - startTime;
                String timerText = String.format("Time: %02d:%02d", (elapsedMillis / 60000) % 60, (elapsedMillis / 1000) % 60);
                g.setFont(new Font("Ink Free", Font.BOLD, 20));
                g.drawString(timerText, 10, 30);
            } else if (levelCompleted) {
                winScreen(g);
            } else {
                gameOver(g);
            }
        }
    }

    /**
     * Sets up the main menu screen by adjusting game state flags and triggering
     * a repaint to display the menu options.
     */
    public void Menu() {
        inMenu = true;
        running = false;
        inSA = false;
        inHowToPlay = false;
        inQuit = false;
        inComingSoon = false;
        gameOver = false;
        levelCompleted = false;
        level = 1;
        repaint();      // Repaint the panel to display the menu screen
    }

    /**
     * Initializes and starts the game
     */
    public void startGame() {
        map = new Map();           // Create new map
        snake = new Snake();       // Create new snake
        hunt = new Hunt(snake);    // Create new hunt for the snake
        hunt.newHunt();            // Generate a new hunt
        running = true;            // Set game state to running
        inMenu = false;            // Set state to in-game
        level = 1;                 // Start from level 1
        currentDelay = INITIAL_DELAY; // Reset speed to initial value
        timer = new Timer(currentDelay, this);
        timer.start();             // Start timer
        startTime = System.currentTimeMillis();
    }

    /**
     * Draws the main menu screen with game title and options for the user.
     *
     * @param g Graphics object used to draw text and shapes on the panel
     */
    private void drawMenu(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Segoe Script", Font.BOLD, 120));
        g.drawString("Snake Hunt", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Snake Hunt")) / 2, SCREEN_HEIGHT / 2 - 100);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));

        // Updated menu options
        g.drawString("Press 1 to Play", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 1 to Play")) / 2, SCREEN_HEIGHT / 2);
        g.drawString("Press 2 for How to Play", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 2 for How to Play")) / 2, SCREEN_HEIGHT / 2 + 70);
        g.drawString("Press 3 for About", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 3 for About")) / 2, SCREEN_HEIGHT / 2 + 140);
        g.drawString("Press 4 to Quit", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 4 to Quit")) / 2, SCREEN_HEIGHT / 2 + 210);
    }

    /**
     * Draws the game-over screen
     *
     * @param g
     */
    public void gameOver(Graphics g) {
        gameOver = true; // Set the gameOver state to true

        // Calculate elapsed time
        long elapsedMillis = System.currentTimeMillis() - startTime;
        String timerText = String.format("Time: %02d:%02d", (elapsedMillis / 60000) % 60, (elapsedMillis / 1000) % 60);

        // Set up vertical positions
        int baseY = SCREEN_HEIGHT / 2;
        int currentY = baseY - 150; // Start higher up

        // Draw timer
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics timerMetrics = g.getFontMetrics();
        g.drawString(timerText, (SCREEN_WIDTH - timerMetrics.stringWidth(timerText)) / 2, currentY);
        currentY += 80;

        // Draw score
        g.setColor(Color.RED);
        String scoreText = "Score: " + snake.huntsEaten;
        g.drawString(scoreText, (SCREEN_WIDTH - timerMetrics.stringWidth(scoreText)) / 2, currentY);
        currentY += 100;

        // Draw GAME OVER text
        g.setFont(new Font("Ink Free", Font.BOLD, 80));
        FontMetrics gameOverMetrics = g.getFontMetrics();
        g.drawString("GAME OVER", (SCREEN_WIDTH - gameOverMetrics.stringWidth("GAME OVER")) / 2, currentY);
        currentY += 100;

        // Draw menu options
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Press 1 to RePlay", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 1 to RePlay")) / 2, currentY);
        currentY += 70;
        g.drawString("Press 3 to get back Home", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 3 to get back Home")) / 2, currentY);
    }

    /**
     * Draws the victory screen displayed when a level is completed. Shows the
     * elapsed time, score, level completion message, and options to replay,
     * proceed to the next level, or return to the menu.
     *
     * @param g The Graphics object used to draw text and shapes on the panel.
     */
    private void winScreen(Graphics g) {
        int baseY = SCREEN_HEIGHT / 2;
        int currentY = baseY - 200; // Adjust for better centering

        // Player name
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = g.getFontMetrics();

        // Game duration
        long elapsedMillis = System.currentTimeMillis() - startTime;
        String timerText = String.format("Time: %02d:%02d", (elapsedMillis / 60000) % 60, (elapsedMillis / 1000) % 60);
        g.drawString(timerText, (SCREEN_WIDTH - metrics.stringWidth(timerText)) / 2, currentY);
        currentY += 80;

        // Score
        g.setColor(Color.RED);
        String scoreText = "Score: " + snake.huntsEaten;
        g.drawString(scoreText, (SCREEN_WIDTH - metrics.stringWidth(scoreText)) / 2, currentY);
        currentY += 100;

        // Victory message
        g.setFont(new Font("Ink Free", Font.BOLD, 80));
        g.drawString("Level " + level + " Completed!", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Level " + level + " Completed!")) / 2, currentY);
        currentY += 100;

        // Options
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Press 1 to Replay", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 1 to Replay")) / 2, currentY);
        currentY += 50;
        g.drawString("Press 2 to Next Level", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 2 to Next Level")) / 2, currentY);
        currentY += 50;
        g.drawString("Press 3 to get back Home", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 3 to get back Home")) / 2, currentY);
    }

    /**
     * Draws the "How to Play" screen with instructions for playing the game.
     * Displays the title and a list of instructions including controls,
     * objectives, and how to return to the menu.
     *
     * @param g The Graphics object used to draw text and shapes on the panel.
     */
    private void drawHowToPlay(Graphics g) {
        String title = "How to Play";
        String[] instructions = {
            "Use arrow keys or WASD to control the snake's direction",
            "Collect red dots to grow longer",
            "Avoid hitting yourself",
            "Reach " + WIN_SCORES[0] + ", " + WIN_SCORES[1] + ", " + WIN_SCORES[2] + " red dots to win each level",
            "Press 3 to return to menu"
        };

        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        g.drawString(title, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(title)) / 2, SCREEN_HEIGHT / 2 - 150);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.PLAIN, 35));

        int y = SCREEN_HEIGHT / 2 - 20;
        for (int i = 0; i < instructions.length; i++) {
            if (i == instructions.length - 1) { // "Press 3 to return to menu"
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
            }
            g.drawString(instructions[i], (SCREEN_WIDTH - g.getFontMetrics().stringWidth(instructions[i])) / 2, y);
            y += 70;
        }
    }

    /**
     * Draws the "About" screen with information about the game and author
     * names.
     *
     * @param g Graphics object used to draw text and shapes on the panel
     */
    public void drawAbout(Graphics g) {
        setBackground(Color.BLACK);
        String title = "About Us";
        String[] description = {"FPT University - SE1903 - SP25 - CSD201"};
        String mentorHeader = "Our Mentor:";
        String mentor = "Le Thi Phuong Dung";
        String authorsHeader = "Our Authors:";
        String[] authors = {
            "Pham Gia Bao - CE191671",
            "Le Anh Tuan - CE180905",
            "Nguyen Minh Khang - CE190728",
            "Lim The Toan - CE190616"
        };

        // Draw title
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        int y = SCREEN_HEIGHT / 2 - 210;
        g.drawString(title, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(title)) / 2, y);

        // Draw description
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.PLAIN, 30));
        y += 50;
        for (String line : description) {
            g.drawString(line, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(line)) / 2, y);
        }

        // Draw mentor section
        y += 50;
        g.drawString(mentorHeader, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(mentorHeader)) / 2, y);
        y += 50;
        g.drawString(mentor, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(mentor)) / 2, y);

        // Draw authors section
        y += 50;
        g.drawString(authorsHeader, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(authorsHeader)) / 2, y);
        y += 50;
        for (String author : authors) {
            g.drawString(author, (SCREEN_WIDTH - g.getFontMetrics().stringWidth(author)) / 2, y);
            y += 50;
        }

        // Return to menu instruction
        y += 50;
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Press 3 to return to menu",
                (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 3 to return to menu")) / 2, y);
    }

    /**
     * Draws the quit screen displayed when the player chooses to exit the game.
     * Shows a goodbye message and automatically closes the application after a
     * 3-second delay.
     *
     * @param g The Graphics object used to draw text and shapes on the panel.
     */
    private void drawQuit(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        g.drawString("Goodbye", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Goodbye")) / 2, SCREEN_HEIGHT / 2 - 50);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("See you again!", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("See you again!")) / 2, SCREEN_HEIGHT / 2 + 50);

        new Timer(2000, e -> System.exit(0)).start();
    }

    /**
     * Draws the "Coming Soon" screen displayed when the player attempts to
     * access an unavailable feature or level. Shows a "Coming Soon" message and
     * an option to return to the main menu.
     *
     * @param g The Graphics object used to draw text and shapes on the panel.
     */
    private void drawComingSoon(Graphics g) {
        int baseY = SCREEN_HEIGHT / 2;
        int currentY = baseY - 50;
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        g.drawString("Coming Soon", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Coming Soon")) / 2, SCREEN_HEIGHT / 2 - 50);
        currentY += 70;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Press 3 to get back Home", (SCREEN_WIDTH - g.getFontMetrics().stringWidth("Press 3 to get back Home")) / 2, currentY);
    }

    /**
     * Invoked periodically by the Timer to update the game state. Moves the
     * snake, checks for hunt consumption, and handles collisions.
     *
     * @param e ActionEvent triggered by the Timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            snake.move();
            snake.checkHunt(hunt);
            // Check if level is completed
            if (snake.huntsEaten >= WIN_SCORES[level - 1]) {
                running = false;
                timer.stop();
                levelCompleted = true; // Mark level as completed
            } else if (!snake.checkCollisions()) {
                running = false;
                timer.stop();
                gameOver = true;
            }
        }
        repaint();
    }

    /**
     * Advances to the next level by resetting game objects and increasing
     * difficulty.
     */
    private void nextLevel() {
        level++;
        snake = new Snake();
        hunt.newHunt();
        running = true;
        levelCompleted = false;
        currentDelay = INITIAL_DELAY - (level - 1) * 50;
        if (currentDelay < 0) {
            currentDelay = 0;
        }
        timer = new Timer(currentDelay, this);
        timer.start();
        startTime = System.currentTimeMillis();
    }

    /**
     * Replays the current level by resetting game objects while keeping the
     * same level.
     */
    private void replayLevel() {
        snake = new Snake();
        hunt.newHunt();
        running = true;
        levelCompleted = false;
        currentDelay = INITIAL_DELAY - (level - 1) * 50;
        if (currentDelay < 0) {
            currentDelay = 0;
        }
        timer = new Timer(currentDelay, this);
        timer.start();
        startTime = System.currentTimeMillis();
    }

    /**
     * Key adapter class for handling key events
     */
    public class MyKeyAdapter extends KeyAdapter {

        private boolean keyPressedFlag = false; // Track key press state

        @Override
        public void keyPressed(KeyEvent e) {
            if (keyPressedFlag && !inMenu) {
                return;
            }
            int keyCode = e.getKeyCode();

            if (inMenu) {
                handleMenu(keyCode);
            } else if (inHowToPlay) {
                if (keyCode == KeyEvent.VK_3) {
                    Menu();
                }
            } else if (inSA) {
                if (keyCode == KeyEvent.VK_3) {
                    Menu();
                }
            } else if (inQuit) {
            } else if (inComingSoon) {
                if (keyCode == KeyEvent.VK_3) {
                    Menu();
                }
            } else if (!running && !inMenu) {
                if (keyCode == KeyEvent.VK_1) {
                    if (levelCompleted) {
                        replayLevel();
                    } else {
                        gameOver = false;
                        startGame();
                    }
                } else if (keyCode == KeyEvent.VK_2 && levelCompleted) {
                    if (level < MAX_LEVEL) {
                        nextLevel();
                    } else {
                        inComingSoon = true;
                        repaint();
                    }
                } else if (keyCode == KeyEvent.VK_3) {
                    if (levelCompleted) {
                        Menu(); // Return to menu when level is won
                    } else {
                        Menu(); // Return to menu when game is lost
                        gameOver = false;
                    }
                    levelCompleted = false;
                }
            } else if (running) {
                handleGameControls(keyCode);
            }
        }

        /**
         * Handles key inputs when the game is in the main menu state. Processes
         * the user's selection to start the game, show instructions, display
         * the about screen, or quit the game.
         *
         * @param keyCode The integer code of the key pressed by the user, as
         * defined in KeyEvent.
         */
        private void handleMenu(int keyCode) {
            switch (keyCode) {
                case KeyEvent.VK_1:
                    startGame();
                    break;
                case KeyEvent.VK_2:
                    showHowToPlay();
                    break;
                case KeyEvent.VK_3:
                    showAbout();
                    break;
                case KeyEvent.VK_4:
                    inQuit = true;
                    inMenu = false;
                    repaint();
                    break;
            }
        }

        /**
         * Handles key inputs for controlling the snake movement during game
         * play. Maps arrow keys and WASD keys to change the snake direction,
         * preventing reverse movement.
         *
         * @param keyCode The integer code of the key pressed by the user, as
         * defined in KeyEvent.
         */
        private void handleGameControls(int keyCode) {
            switch (keyCode) {
                case KeyEvent.VK_LEFT:    // Left arrow
                case KeyEvent.VK_A:       // A key
                    if (snake.direction != 'R') {
                        snake.direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:   // Right arrow
                case KeyEvent.VK_D:       // D key
                    if (snake.direction != 'L') {
                        snake.direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:      // Up arrow
                case KeyEvent.VK_W:       // W key
                    if (snake.direction != 'D') {
                        snake.direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:    // Down arrow
                case KeyEvent.VK_S:       // S key
                    if (snake.direction != 'U') {
                        snake.direction = 'D';
                    }
                    break;
            }
        }

        /**
         * Handles the release of a key by resetting the key press flag. Ensures
         * that subsequent key presses can be processed after the current key is
         * released.
         *
         * @param e The KeyEvent object containing information about the key
         * release event.
         */
        @Override
        public void keyReleased(KeyEvent e) {
            keyPressedFlag = false;
        }
    }

    /**
     * Sets up the game state to display the "How to Play" screen. Transitions
     * the game from the current state to showing gameplay instructions and
     * triggers a repaint.
     */
    private void showHowToPlay() {
        inHowToPlay = true;
        inMenu = false;
        inSA = false;
        running = false;
        repaint();
    }

    /**
     * Sets up the game state to display the "About" screen.
     */
    private void showAbout() {
        inSA = true;     // Sets the state to "Showing About"
        inMenu = false;  // Exits the menu state
        inHowToPlay = false;  // Exits How to Play state
        running = false; // Stops the game from running
        repaint();       // Triggers repaint to display the "About" screen
    }

    /**
     * Main method program
     *
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Hunt"); // Create the main application window titled "Snake Hunt"
        Main main = new Main();                       // Instantiate the Main game panel
        frame.add(main);                              // Add the game panel to the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application when the frame is closed
        frame.setResizable(false);                     // Prevent the frame from being resized
        frame.pack();                                  // Adjust the frame size to fit the content
        frame.setLocationRelativeTo(null);             // Center the frame on the screen
        frame.setVisible(true);                        // Make the frame visible
        main.setFocusable(true);                       // Allow the game panel to receive keyboard focus
        main.requestFocusInWindow();                   // Request focus for the game panel to ensure it can capture input
        main.Menu();                                   // Display the main menu when the game starts
    }
}
