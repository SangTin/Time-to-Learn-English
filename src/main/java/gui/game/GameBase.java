package gui.game;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.AnchorPane;

public class GameBase extends AnchorPane {
   SimpleBooleanProperty isGameFinished = new SimpleBooleanProperty(false);

   public void initialize() {
   }

   protected void endGame() {
      this.isGameFinished.set(true);
   }

   public SimpleBooleanProperty isGameFinishedProperty() {
      return this.isGameFinished;
   }
}
