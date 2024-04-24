package speedstars;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Track extends JPanel {
    private Player player;
    private int distance;

    public Track(Player player, int distance) {
        this.player = player;
        this.distance = distance;
        setPreferredSize(new Dimension(800, 200)); // Adjust panel size as needed
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int trackWidth = getWidth() - 100; // Width of the track area
        int trackHeight = getHeight() / 2; // Height of the track area
        int trackX = 50; // X-coordinate of the track
        int trackY = getHeight() / 4; // Y-coordinate of the track
        g.setColor(Color.GREEN);
        g.fillRect(trackX, trackY, trackWidth, trackHeight); // Draw the track

        // Calculate player position based on track distance
        double ratio = (double) distance / Distance.MAX_DISTANCE;
        int playerX = (int) (trackX + ratio * trackWidth - player.getWidth() / 2); // Center the player on the track
        int playerY = trackY - player.getHeight(); // Place the player above the track

        // Draw the player image
        g.drawImage(player.getImage(), playerX, playerY, this);
    }
}
