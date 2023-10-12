package entry;

import java.io.IOException;

import gui.Menu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import style.MenuStyle;

public class Start extends Application {
    private Scene loadScene;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    private void customizeLoadScene() throws Exception {
        //Load scene, add fonts and css
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/start.fxml"));
        loadScene = new Scene(root);
        Font.loadFont(getClass().getResourceAsStream("/fonts/JetBrainsMono.ttf"), 15);
        loadScene.getStylesheets().add(getClass().getResource("/css/word_display.css").toExternalForm());

        Button CLIButton = (Button) root.lookup("#CLIButton");
        Button GUIButton = (Button) root.lookup("#GUIButton");
        CLIButton.setEffect(MenuStyle.getButtonShadowEffect());
        CLIButton.setOnAction(e -> commandLineInterface());
        GUIButton.setEffect(MenuStyle.getButtonShadowEffect());
        GUIButton.setOnAction(e -> graphicalUserInterface());
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        try {
            customizeLoadScene();
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Time to Learn English");
        primaryStage.setScene(loadScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void commandLineInterface() {
        String filename = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
        try {
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd", "/k"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void graphicalUserInterface() {
        Menu menu = new Menu();
        primaryStage.close();
        menu.show();
    }
}