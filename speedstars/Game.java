package speedstars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Game extends JPanel implements KeyListener, Runnable {
    private static final int TRACK_WIDTH = 800;
    private static final int TRACK_HEIGHT = 600;
    private static final int NUM_LANES = 8;
    private static final int LANE_WIDTH = TRACK_WIDTH / NUM_LANES;

    private BufferedImage playerImage;
    private int playerLane = 4; // Player starts in the middle lane
    private int playerPositionY = TRACK_HEIGHT - 50; // Player's Y position at the bottom of the track
    private boolean[] laneOccupied = new boolean[NUM_LANES]; // Indicates if a lane is occupied by another character

    public Game() {
        setPreferredSize(new Dimension(TRACK_WIDTH, TRACK_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Load player image (you can replace this with your own image loading logic)
        playerImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = playerImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 50, 50);
        g2d.dispose();

        // Start a new thread to handle game updates
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw track with lanes
        g.setColor(Color.RED);
        for (int i = 0; i < NUM_LANES; i++) {
            int x = i * LANE_WIDTH;
            g.fillRect(x, 0, LANE_WIDTH, TRACK_HEIGHT);
        }

        // Draw other characters (small squares) in each lane
        g.setColor(Color.BLUE);
        for (int i = 0; i < NUM_LANES; i++) {
            if (laneOccupied[i]) {
                int x = i * LANE_WIDTH + (LANE_WIDTH - 10) / 2;
                g.fillRect(x, playerPositionY - 20, 10, 10);
            }
        }

        // Draw player character
        int playerX = playerLane * LANE_WIDTH + (LANE_WIDTH - 50) / 2;
        g.drawImage(playerImage, playerX, playerPositionY, this);
    }

    @Override
    public void run() {
        // Game loop
        while (true) {
            // Move other characters in each lane (simulate)
            for (int i = 0; i < NUM_LANES; i++) {
                if (laneOccupied[i]) {
                    // Move character upwards (towards the finish line)
                    playerPositionY -= 2;
                    if (playerPositionY <= 0) {
                        playerPositionY = TRACK_HEIGHT - 50; // Reset position at the bottom
                        laneOccupied[i] = false; // Character reached the finish line, lane is now free
                    }
                }
            }

            // Redraw the game
            repaint();

            // Pause for a short time (adjust as needed for desired frame rate)
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && playerLane > 0) {
            playerLane--;
        } else if (keyCode == KeyEvent.VK_RIGHT && playerLane < NUM_LANES - 1) {
            playerLane++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing for key released event
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing for key typed event
    }
}
