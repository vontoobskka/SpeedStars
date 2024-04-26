import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    private static final int NUM_LANES = 8;
    private static final int TRACK_WIDTH = 1200;
    private static final int TRACK_HEIGHT = 800;
    private static final int LANE_HEIGHT = TRACK_HEIGHT / NUM_LANES;
    private static final int PLAYER_SIZE = 30;
    private static final int FINISH_LINE_X = TRACK_WIDTH - 50;
    private static final long WORLD_RECORD = 10000; // Placeholder for world record time (in milliseconds)

    private int playerLane = NUM_LANES / 2; // Player starts in the middle lane
    private double playerPositionX = 0; // Player's X position
    private long startTime;
    private long finishTime;
    private boolean raceStarted = false;
    private boolean raceFinished = false;
    private int requiredClicks;
    private int currentClicks = 0;
    private double speedMPH = 0;

    public Game(int clicks) {
        // Adjust required clicks for each race
        this.requiredClicks = clicks;

        setPreferredSize(new Dimension(TRACK_WIDTH, TRACK_HEIGHT));
        setBackground(Color.RED); // Change track color to red

        // Add key listener for spacebar presses
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!raceStarted && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startTime = System.currentTimeMillis();
                    raceStarted = true;
                    requestFocusInWindow(); // Set focus back to game panel to capture key events
                } else if (!raceFinished && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    currentClicks++;
                    movePlayer();
                    if (playerPositionX >= FINISH_LINE_X - PLAYER_SIZE) {
                        finishRace();
                    }
                }
            }
        });

        // Start the game loop
        Timer timer = new Timer(20, this);
        timer.start();
    }

    private void finishRace() {
        raceFinished = true;
        finishTime = System.currentTimeMillis() - startTime;
    }

    private void movePlayer() {
        // Calculate horizontal position based on speed
        double speed = calculateSpeedMPH();
        playerPositionX += speed / 50; // Move player horizontally based on speed
    }

    private void updateGame() {
        if (raceStarted && !raceFinished) {
            // Calculate speed in mph based on clicks per second
            speedMPH = calculateSpeedMPH();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw lanes
        g.setColor(Color.WHITE);
        for (int i = 1; i < NUM_LANES; i++) {
            int laneY = i * LANE_HEIGHT;
            g.drawLine(0, laneY, TRACK_WIDTH, laneY);
        }

        // Draw finish line
        g.setColor(Color.BLACK);
        g.fillRect(FINISH_LINE_X, 0, 5, TRACK_HEIGHT);

        // Draw player if race not finished
        if (!raceFinished) {
            g.setColor(Color.WHITE);
            g.fillRect((int) playerPositionX, playerLane * LANE_HEIGHT + (LANE_HEIGHT - PLAYER_SIZE) / 2, PLAYER_SIZE, PLAYER_SIZE);
        }

        // Draw timer
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if (raceStarted && !raceFinished) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            g.drawString("Time: " + formatTime(elapsedTime), TRACK_WIDTH - 150, 40);
        } else if (raceFinished) {
            g.drawString("Finish Time: " + formatTime(finishTime), TRACK_WIDTH - 200, 40);
            g.drawString("World Record: " + formatTime(WORLD_RECORD), TRACK_WIDTH - 200, 70);
            if (finishTime < WORLD_RECORD) {
                g.drawString("You beat the world record!", TRACK_WIDTH - 200, 100);
            } else {
                g.drawString("You did not beat the world record.", TRACK_WIDTH - 200, 100);
            }
        }

        // Draw speed in mph
        g.drawString("Speed: " + String.format("%.0f", speedMPH) + " mph", 20, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private String formatTime(long timeMillis) {
        long seconds = timeMillis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d:%03d", minutes, seconds, timeMillis % 1000);
    }

    private double calculateSpeedMPH() {
        // Here you can adjust the mapping from clicks per second to speed in mph
        // For example, if 10 clicks per second = 10 mph, you can use clicksPerSecond * 10
        double clicksPerSecond = currentClicks / ((System.currentTimeMillis() - startTime) / 1000.0);
        if (clicksPerSecond <= 3) {
            return 0;
        } else if (clicksPerSecond <= 5) {
            return 5;
        } else if (clicksPerSecond <= 8) {
            return 10;
        } else if (clicksPerSecond <= 10) {
            return 15;
        } else if (clicksPerSecond <= 12) {
            return 20;
        } else if (clicksPerSecond <= 14) {
            return 25;
        } else if (clicksPerSecond <= 16) {
            return 30;
        } else if (clicksPerSecond <= 18) {
            return 35;
        } else if (clicksPerSecond <= 20) {
            return 40;
        } else if (clicksPerSecond <= 22) {
            return 45;
        } else if (clicksPerSecond <= 24) {
            return 50;
        } else {
            return 55;
        }
    }
}
