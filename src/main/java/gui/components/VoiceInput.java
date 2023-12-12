package gui.components;

import api.SpeechToText;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class VoiceInput extends Stage {
   @FXML
   private TextArea textResult;
   Service<Void> process;

   public VoiceInput() {
      this.setTitle("Voice Input");
      this.setResizable(false);
      this.centerOnScreen();
      this.initModality(Modality.APPLICATION_MODAL);

      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/components/VoiceInput.fxml"));
         loader.setController(this);
         loader.setRoot(this);
         loader.load();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void initialize() {
      SpeechToText.isStartProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue) {
            Media sound = new Media(Objects.requireNonNull(this.getClass().getResource("/audio/voice-start.mp3")).toExternalForm());
            (new javafx.scene.media.MediaPlayer(sound)).play();
         }

      });
      this.process = new Service<>() {
         @Override
         protected Task<Void> createTask() {
            return new Task<>() {
               @Override
               protected Void call() throws Exception {
                  SpeechToText.startRecord();
                  SpeechToText.textOfSpeechProperty().addListener((observable, oldValue, newValue) -> {
                     textResult.setText(newValue);
                  });
                  return null;
               }
            };
         }

         @Override
         public boolean cancel() {
            SpeechToText.stopStreaming();
            return super.cancel();
         }
      };
   }

   public String getTextResult() {
      return this.textResult.getText();
   }

   public void showAndWait() {
      this.process.restart();
      super.showAndWait();
   }

   public void hide() {
      this.process.cancel();
      super.hide();
   }

   private void microphoneAlert() {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Microphone Error");
      alert.setHeaderText("Microphone not found or not working");
      alert.getDialogPane().getStylesheets().add(this.getClass().getResource("/css/Alert.css").toExternalForm());
      ButtonType yesButton = new ButtonType("OK", ButtonData.OK_DONE);
      alert.getButtonTypes().setAll(yesButton);
      Node alertYes = alert.getDialogPane().lookupButton(yesButton);
      alertYes.setId("alertYes");
      alert.showAndWait();
   }

   private void timeWarning() {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Time Warning");
      alert.setHeaderText("Time limit exceeded");
      alert.setContentText("The time limit for voice input is 20 seconds. Do you want to use this result?");
      alert.getDialogPane().getStylesheets().add(this.getClass().getResource("/css/Alert.css").toExternalForm());
      ButtonType noButton = new ButtonType("Retry", ButtonData.NO);
      ButtonType yesButton = new ButtonType("Yes", ButtonData.OK_DONE);
      alert.getButtonTypes().setAll(yesButton, noButton);
      Node alertNo = alert.getDialogPane().lookupButton(noButton);
      alertNo.setId("alertCancel");
      Node alertYes = alert.getDialogPane().lookupButton(yesButton);
      alertYes.setId("alertYes");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == noButton) {
         this.showAndWait();
      }

   }
}
