package gui.components.translate_text;

import gui.components.TranslateBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Timer;

public class TranslateBar extends TranslateBase {
   @FXML private Label fromLanguageLabel;
   @FXML private Label toLanguageLabel;

   private Timer timer = new Timer();

   public TranslateBar() {
      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/components/TranslateBar.fxml"));
         loader.setRoot(this);
         loader.setController(this);
         loader.load();
      } catch (IOException var2) {
          System.out.println("Error loading TranslateBar.fxml");
      }
   }

    public void initialize() {
        super.initialize();
        fromText.textProperty().addListener((observable, oldValue, newValue) -> {
            timer.cancel();
            timer.purge();
            timer = new Timer();
            timer.schedule(new TranslateTask(), 2000);
        });
        fromLanguage = this.fromLanguageLabel.getText();
        toLanguage = this.toLanguageLabel.getText();
    }

    protected void switchLanguage() {
       super.switchLanguage();

       this.fromLanguageLabel.setText(fromLanguage);
       this.toLanguageLabel.setText(toLanguage);
    }
}
