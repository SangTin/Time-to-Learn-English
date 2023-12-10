package crossword;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    String path;
    public Sound() {

    }

    public Sound(String path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void playSound() {
        try {
            File soundFile = new File(path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
