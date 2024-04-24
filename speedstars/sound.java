import javax.sound.sampled.*;
import java.io.*;

public class sound {
    public void playmusic(String filepath) {
        try {
            File soundFile = new File(filepath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}