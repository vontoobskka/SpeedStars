package speedstars;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Main extends JFrame {
    private JPanel menuPanel;
    private Clip introClip;
    private Clip winClip;
    private Clip loseClip;
    private ImageIcon backgroundGif; // For the GIF background

    public Main() {
        super("Track and Field Game");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load the GIF
        backgroundGif = new ImageIcon("background.gif");

        // Create menu panel with background GIF
        menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the GIF as the background
                g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        menuPanel.setLayout(new GridBagLayout());
        addButtonsToMenuPanel();
        getContentPane().add(menuPanel);

        // Load sound clips
        try {
            introClip = AudioSystem.getClip();
            AudioInputStream introStream = AudioSystem.getAudioInputStream(new File("dream.wav"));
            introClip.open(introStream);

            winClip = AudioSystem.getClip();
            AudioInputStream winStream = AudioSystem.getAudioInputStream(new File("win.wav"));
            winClip.open(winStream);

            loseClip = AudioSystem.getClip();
            AudioInputStream loseStream = AudioSystem.getAudioInputStream(new File("lose.wav"));
            loseClip.open(loseStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        setVisible(true);
        introClip.start(); // Play intro sound
    }

    private void addButtonsToMenuPanel() {
        String[] eventLabels = {"100 meters", "200 meters", "400 meters", "800 meters", "1500 meters"};
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        for (String label : eventLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new EventButtonListener());
            menuPanel.add(button, gbc);
            gbc.gridy++;
        }
    }

    private class EventButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            int distance = Integer.parseInt(buttonText.split(" ")[0]);

            introClip.stop();
            startGame(distance);
        }
    }

    private void startGame(int distance) {
        getContentPane().removeAll();
        Game game = new Game(distance, distance, this);
        getContentPane().add(game);
        revalidate();
        game.requestFocusInWindow();
    }

    public void playWinSound() {
        winClip.start();
    }

    public void playLoseSound() {
        loseClip.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
