package game;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public abstract class GameBase extends AnchorPane {
   protected MediaPlayer backgroundMusic;
   SimpleBooleanProperty isGameFinished = new SimpleBooleanProperty(false);

   protected final void setBackgroundMusic(String musicFilePath) {
      Media sound = new Media(new File(musicFilePath).toURI().toString());
      backgroundMusic = new MediaPlayer(sound);
      backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
   }

   public abstract void startGame();

   protected void endGame() {
      this.isGameFinished.set(true);
      backgroundMusic.stop();
   }

   public static void playSound(String soundFilePath) {
      try {
         File soundFile = new File(soundFilePath);
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
         Clip clip = AudioSystem.getClip();
         clip.open(audioInputStream);

         // Lắng nghe sự kiện kết thúc để bắt đầu lại từ đầu
         clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
               clip.setMicrosecondPosition(0);
               clip.start();
            }
         });

         clip.start();
      } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
         e.printStackTrace();
      }
   }

   public SimpleBooleanProperty isGameFinishedProperty() {
      return this.isGameFinished;
   }
}
