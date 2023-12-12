package gui.components.translate_text;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class TranslateBar extends TranslateBase {
   @FXML private Label fromLanguageLabel;
   @FXML private Label toLanguageLabel;

   public TranslateBar() {
      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/components/translate_text/TranslateBar.fxml"));
         loader.setRoot(this);
         loader.setController(this);
         loader.load();
      } catch (IOException var2) {
          System.out.println("Error loading TranslateBar.fxml");
      }
   }

    public void initialize() {
        super.initialize();
    }

    protected void doInitialize() {
        super.doInitialize();
    }

    protected void switchLanguage() {
       super.switchLanguage();

       this.fromLanguageLabel.setText(fromLanguage.get());
       this.toLanguageLabel.setText(toLanguage.get());
    }
}
