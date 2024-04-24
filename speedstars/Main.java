package speedstars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JPanel menuPanel;

    public Main() {
        super("Track and Field Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create menu panel with background image
        menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw background image
                ImageIcon backgroundImage = new ImageIcon("background.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        menuPanel.setLayout(new GridBagLayout());
        addButtonsToMenuPanel();
        getContentPane().add(menuPanel);

        setVisible(true);
    }

    private void addButtonsToMenuPanel() {
        // Create and add buttons for selecting distances
        String[] distanceLabels = {"100 meters", "200 meters", "400 meters", "800 meters", "1500 meters"};
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        for (String label : distanceLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new DistanceButtonListener());
            menuPanel.add(button, gbc);
            gbc.gridy++;
        }
    }

    private class DistanceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            int distance = Integer.parseInt(buttonText.split(" ")[0]); // Extract distance from button text
            startGame(distance);
        }
    }

    private void startGame(int distance) {
        // Create and display the game with the selected distance
        getContentPane().removeAll(); // Remove menu panel
        Game game = new Game();
        getContentPane().add(game);
        revalidate(); // Refresh frame
        game.requestFocusInWindow(); // Set focus to game panel for keyboard input
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
