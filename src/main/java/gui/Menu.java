package gui;

import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import style.MenuStyle;

public class Menu extends LinkedStage {
    private Button quitButton;
    private GraphicalDictionary graphicalDictionary;

    public Menu() {
        super();
        
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/menu.fxml"));
            mainScene = new Scene(root);
            setTitle("Time to Learn English - GUI");
            getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/TLE.png")));

            //Add CSS and effects
            mainScene.getStylesheets().add(getClass().getResource("/css/start.css").toExternalForm());
            root.lookupAll(".button").forEach(b -> b.setEffect(MenuStyle.getButtonShadowEffect()));

            setScene(mainScene);
            setResizable(false);
            prepare();
            setOnCloseRequest(e -> {
                e.consume();
                quitAlert();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepare() {
        quitButton = (Button) mainScene.lookup("#quitButton");
        quitButton.setOnAction(e -> quitAlert());

        Button dictionaryButton = (Button) mainScene.lookup("#dictionaryButton");
        dictionaryButton.setOnAction(e -> {
            if (graphicalDictionary == null) {
                graphicalDictionary = new GraphicalDictionary(this);
            } else {
                graphicalDictionary.show();
            }
        });
    }

    public void quitAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        //Add CSS ID
        Node quitAlertNo = alert.getDialogPane().lookupButton(noButton);
        quitAlertNo.setId("quitAlertNo");
        Node quitAlertYes = alert.getDialogPane().lookupButton(yesButton);
        quitAlertYes.setId("quitAlertYes");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            close();
        }
    }
}
