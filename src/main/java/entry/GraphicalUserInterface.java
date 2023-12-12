package entry;

import gui.GraphicalDictionary;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class GraphicalUserInterface {
   private final Stage primaryStage = new Stage();

   public GraphicalUserInterface() {
      GraphicalDictionary graphicalDictionary = new GraphicalDictionary();
      Scene loadScene = new Scene(graphicalDictionary);
      Image icon = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/TLE.png")));
      this.primaryStage.setTitle("Time to Learn English");
      this.primaryStage.getIcons().add(icon);
      this.primaryStage.setScene(loadScene);
      this.primaryStage.setResizable(false);
      this.primaryStage.setOnCloseRequest((e) -> {
         e.consume();
         this.quitAlert();
      });
   }

   private void quitAlert() {
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Quit");
      alert.setHeaderText("Are you sure you want to quit?");
      alert.getDialogPane().getStylesheets().add(this.getClass().getResource("/css/Alert.css").toExternalForm());
      ButtonType noButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
      ButtonType yesButton = new ButtonType("Yes", ButtonData.OK_DONE);
      alert.getButtonTypes().setAll(yesButton, noButton);
      Node quitAlertNo = alert.getDialogPane().lookupButton(noButton);
      quitAlertNo.setId("alertNo");
      Node quitAlertYes = alert.getDialogPane().lookupButton(yesButton);
      quitAlertYes.setId("alertYes");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == yesButton) {
         Platform.exit();
         System.exit(0);
      }
   }

   public static boolean inHierarchy(Node node, Node potentialHierarchyElement) {
      if (potentialHierarchyElement == null) {
         return true;
      }
      while (node != null) {
         if (node == potentialHierarchyElement) {
            return true;
         }
         node = node.getParent();
      }
      return false;
   }

    public void show() {
        this.primaryStage.show();
    }
}
