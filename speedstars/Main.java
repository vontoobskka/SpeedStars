package speedstars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private JPanel menuPanel;

    public Main() {
        super("Track and Field Game");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create menu panel with background image
        menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw background image
                ImageIcon backgroundImage = new ImageIcon("uiobb.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        menuPanel.setLayout(new GridBagLayout());
        addButtonsToMenuPanel();
        getContentPane().add(menuPanel);

        setVisible(true);
    }

    private void addButtonsToMenuPanel() {
        // Create and add buttons for selecting events
        String[] eventLabels = {"100 meters", "200 meters", "400 meters", "800 meters", "1500 meters"};
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around buttons
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
            int distance = Integer.parseInt(buttonText.split(" ")[0]); // Extract distance from button text
            startGame(distance);
        }
    }

    private void startGame(int distance) {
        // Create and display the game with the selected distance
        getContentPane().removeAll(); // Remove menu panel
        Game game = new Game(distance, distance);
        getContentPane().add(game);
        revalidate(); // Refresh frame
        game.requestFocusInWindow(); // Set focus to game panel for keyboard input
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
