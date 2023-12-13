package entry;

import gui.style.MenuStyle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Start extends Application {
   private Scene loadScene;
   private Stage primaryStage;
   private GraphicalUserInterface graphicalUserInterface;

   public static void main(String[] args) {
      launch(args);
   }

   private void customizeLoadScene() throws Exception {
      Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/fxml/Start.fxml")));
      this.loadScene = new Scene(root);
      Button CLIButton = (Button)root.lookup("#CLIButton");
      Button GUIButton = (Button)root.lookup("#GUIButton");
      CLIButton.setEffect(MenuStyle.getButtonShadowEffect());
      CLIButton.setOnAction((e) -> this.commandLineInterface());
      GUIButton.setEffect(MenuStyle.getButtonShadowEffect());
      GUIButton.setOnAction((e) -> this.graphicalUserInterface());
   }

   public void start(Stage stage) {
      this.primaryStage = stage;

      try {
         this.customizeLoadScene();
      } catch (Exception var3) {
         System.out.println("Error: " + var3.getMessage());
      }

      this.primaryStage.setTitle("Time to Learn English");
      this.primaryStage.setScene(this.loadScene);
      this.primaryStage.setResizable(false);
      this.primaryStage.show();

      Platform.runLater(() -> graphicalUserInterface = new GraphicalUserInterface());
   }

   public void commandLineInterface() {
      String var1 = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);

      try {
         Runtime.getRuntime().exec("cmd /c start cmd /k java -jar bin/CommandLine.jar");
         this.primaryStage.close();
      } catch (IOException var3) {
         System.out.println("Error: " + var3.getMessage());
      }
   }

   public void graphicalUserInterface() {
      this.graphicalUserInterface.show();
      this.primaryStage.close();
   }
}
