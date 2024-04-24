package speedstars;

import java.awt.image.BufferedImage;

public class Player {
    private BufferedImage image;

    public Player(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }
}
