package game;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public abstract class GameBase extends AnchorPane {
   protected MediaPlayer backgroundMusic;
   protected SimpleBooleanProperty isGameFinished = new SimpleBooleanProperty(false);

   protected final void setBackgroundMusic(String musicFilePath) {
      Media sound = new Media(new File(musicFilePath).toURI().toString());
      backgroundMusic = new MediaPlayer(sound);
      backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
   }

   public void startGame() {
      isGameFinished.set(false);
   }

   protected void endGame() {
      this.isGameFinished.set(true);
      backgroundMusic.stop();
   }

   public SimpleBooleanProperty isGameFinishedProperty() {
      return this.isGameFinished;
   }
}
