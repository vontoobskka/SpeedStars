package speedstars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Game extends JPanel implements ActionListener {
    private static final int NUM_LANES = 8;
    private static final int TRACK_WIDTH = 1200;
    private static final int TRACK_HEIGHT = 800;
    private static final int LANE_HEIGHT = TRACK_HEIGHT / NUM_LANES;
    private static final int PLAYER_SIZE = 30;
    private static final int FINISH_LINE_X = TRACK_WIDTH - 50;
    private static final long[] WORLD_RECORDS = {10000, 20000, 30000, 40000, 50000};

    private int player1Lane = 1;
    private int player2Lane = 2;
    private double player1PositionX = 0;
    private double player2PositionX = 0;
    private long startTime;
    private boolean raceStarted = false;
    private boolean raceFinished = false;
    private int requiredClicks;
    private int currentClicksPlayer1 = 0;
    private int currentClicksPlayer2 = 0;
    private double speedMPHPlayer1 = 0;
    private double speedMPHPlayer2 = 0;
    private Timer timer;
    private Main mainFrame;

    private Clip crowdClip;
    private Clip winClip;
    private Clip loseClip;

    private boolean player1Finished = false;
    private boolean player2Finished = false;
    private long player1FinishTime;
    private long player2FinishTime;

    public Game(int distance, int clicks, Main mainFrame) {
        this.requiredClicks = clicks;
        this.mainFrame = mainFrame;

        setPreferredSize(new Dimension(TRACK_WIDTH, TRACK_HEIGHT));
        setBackground(Color.RED);

        loadSounds();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!raceStarted && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    startTime = System.currentTimeMillis();
                    raceStarted = true;
                    crowdClip.loop(Clip.LOOP_CONTINUOUSLY);
                    requestFocusInWindow();
                    timer.start();
                } else if (!raceFinished) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        currentClicksPlayer1++;
                        movePlayer1();
                        if (player1PositionX >= FINISH_LINE_X - PLAYER_SIZE && !player1Finished) {
                            player1Finished = true;
                            player1FinishTime = System.currentTimeMillis();
                            checkRaceFinished();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        currentClicksPlayer2++;
                        movePlayer2();
                        if (player2PositionX >= FINISH_LINE_X - PLAYER_SIZE && !player2Finished) {
                            player2Finished = true;
                            player2FinishTime = System.currentTimeMillis();
                            checkRaceFinished();
                        }
                    }
                }
            }
        });

        timer = new Timer(20, this);
    }

    private void loadSounds() {
        try {
            crowdClip = AudioSystem.getClip();
            AudioInputStream crowdStream = AudioSystem.getAudioInputStream(new File("crowd.wav"));
            crowdClip.open(crowdStream);

            winClip = AudioSystem.getClip();
            AudioInputStream winStream = AudioSystem.getAudioInputStream(new File("win.wav"));
            winClip.open(winStream);

            loseClip = AudioSystem.getClip();
            AudioInputStream loseStream = AudioSystem.getAudioInputStream(new File("lose.wav"));
            loseClip.open(loseStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private void checkRaceFinished() {
        if (player1Finished && player2Finished) {
            raceFinished = true;
            timer.stop();
            crowdClip.stop();

            if (player1FinishTime < player2FinishTime) {
                mainFrame.playWinSound(); // Player 1 wins
                mainFrame.playLoseSound(); // Player 2 loses
            } else {
                mainFrame.playLoseSound(); // Player 1 loses
                mainFrame.playWinSound(); // Player 2 wins
            }
        } else if (player1Finished) {
            raceFinished = true;
            timer.stop();
            crowdClip.stop();
            mainFrame.playWinSound(); // Player 1 wins
            mainFrame.playLoseSound(); // Player 2 loses
        } else if (player2Finished) {
            raceFinished = true;
            timer.stop();
            crowdClip.stop();
            mainFrame.playLoseSound(); // Player 1 loses
            mainFrame.playWinSound(); // Player 2 wins
        }
    }

    private void movePlayer1() {
        double speed = calculateSpeedMPH(currentClicksPlayer1);
        player1PositionX += speed / 35;
    }

    private void movePlayer2() {
        double speed = calculateSpeedMPH(currentClicksPlayer2);
        player2PositionX += speed / 35;
    }

    private void updateGame() {
        if (raceStarted && !raceFinished) {
            speedMPHPlayer1 = calculateSpeedMPH(currentClicksPlayer1);
            speedMPHPlayer2 = calculateSpeedMPH(currentClicksPlayer2);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        for (int i = 1; i < NUM_LANES; i++) {
            int laneY = i * LANE_HEIGHT;
            g.drawLine(0, laneY, TRACK_WIDTH, laneY);
        }

        g.setColor(Color.BLACK);
        g.fillRect(FINISH_LINE_X, 0, 5, TRACK_HEIGHT);

        if (!raceFinished) {
            g.setColor(Color.WHITE);
            g.fillRect((int) player1PositionX, player1Lane * LANE_HEIGHT + (LANE_HEIGHT - PLAYER_SIZE) / 2, PLAYER_SIZE, PLAYER_SIZE);
            g.fillRect((int) player2PositionX, player2Lane * LANE_HEIGHT + (LANE_HEIGHT - PLAYER_SIZE) / 2, PLAYER_SIZE, PLAYER_SIZE);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if (raceStarted && !raceFinished) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            g.drawString("Time: " + formatTime(elapsedTime), TRACK_WIDTH - 150, 40);
        } else if (raceFinished) {
            g.drawString("Player 1 Finish Time: " + formatTime(player1FinishTime - startTime), TRACK_WIDTH - 200, 40);
            g.drawString("Player 2 Finish Time: " + formatTime(player2FinishTime - startTime), TRACK_WIDTH - 200, 70);
            g.drawString("World Record: " + formatTime(WORLD_RECORDS[requiredClicks / 50 - 1]), TRACK_WIDTH - 200, 100);
            if ((player1Finished && player1FinishTime - startTime < WORLD_RECORDS[requiredClicks / 50 - 1])
                    || (player2Finished && player2FinishTime - startTime < WORLD_RECORDS[requiredClicks / 50 - 1])) {
                g.drawString("You beat the world record!", TRACK_WIDTH - 200, 130);
            } else {
                g.drawString("You did not beat the world record.", TRACK_WIDTH - 200, 130);
            }
        }

        g.drawString("Player 1 Speed: " + String.format("%.0f", speedMPHPlayer1) + " mph", 20, 40);
        g.drawString("Player 2 Speed: " + String.format("%.0f", speedMPHPlayer2) + " mph", 20, 70);
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

    private double calculateSpeedMPH(int currentClicks) {
        double clicksPerSecond = currentClicks / ((System.currentTimeMillis() - startTime) / 1000.0);
        return clicksPerSecond * 10; // Placeholder formula, adjust as needed
    }
}
