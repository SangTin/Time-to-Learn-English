package gui.home.content;

import data.enums.AppFunction;
import gui.GraphicalDictionary;
import gui.style.AnimatedGif;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class DictionaryFunctionBox extends GridPane {
    @FXML private ImageView searchIcon;
    @FXML private Button searchButton;
    @FXML private ImageView addIcon;
    @FXML private Button addButton;
    @FXML private ImageView deleteIcon;
    @FXML private Button deleteButton;
    @FXML private ImageView editIcon;
    @FXML private Button editButton;

    public DictionaryFunctionBox() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/home/content/DictionaryFunctionBox.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in DictionaryFunctionBox.java");
        }
    }

    public void initialize() {
        // Set icon for buttons
        setButtonIcon(searchButton, searchIcon, "/img/gif/search.gif");
        setButtonIcon(editButton, editIcon, "/img/gif/edit.gif");
        setButtonIcon(addButton, addIcon, "/img/gif/add.gif");
        setButtonIcon(deleteButton, deleteIcon, "/img/gif/delete.gif");

        // Set action for buttons
        searchButton.setOnAction(e ->
            GraphicalDictionary.setAppFunction(AppFunction.SEARCH, null)
        );
    }

    private void setButtonIcon(Button button, ImageView icon, String iconPath) {
        AnimatedGif animatedGif = new AnimatedGif(iconPath, 1500, 50, 50);
        animatedGif.setCycleCount(Animation.INDEFINITE);
        animatedGif.setAutoReverse(true);
        icon.imageProperty().bind(animatedGif.getView().imageProperty());
        button.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                animatedGif.play();
            } else {
                animatedGif.stop();
            }
        });
    }
}
