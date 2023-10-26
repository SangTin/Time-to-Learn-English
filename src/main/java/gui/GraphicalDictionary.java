package gui;

import data.Dictionary;
import data.SQLiteDatabase;
import gui.dictionary.Content;
import gui.dictionary.SearchMenu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class GraphicalDictionary {
    private LinkedStage owner;
    private Scene scene;

    private SQLiteDatabase database;
    private Dictionary dictionary;

    private SearchMenu searchMenu;
    private Content content;
    private AnchorPane mainPane;

    public GraphicalDictionary(LinkedStage owner) {
        this.owner = owner;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dictionary/Dictionary.fxml"));
            
            scene = new Scene(loader.load());
            searchMenu = (SearchMenu) scene.lookup("#searchMenu");
            content = (Content) scene.lookup("#content");
            mainPane = (AnchorPane) scene.lookup("#mainPane");
            initialize();
            
            //Add CSS and effects
            scene.getStylesheets().add(getClass().getResource("/css/dictionary.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        mainPane.getChildren().add(LoadingProgressBar.getLoadingProgressBar());

        database = new SQLiteDatabase("dictionary.db");
        dictionary = new Dictionary(database.importFromDatabase());

        searchMenu.setDictionary(dictionary);
        searchMenu.setContent(content);

        content.visibleProperty().addListener((observable, oldVal, newVal) -> {
            if (!newVal) {
                close();
            }
        });
    }

    public void show() {
        owner.setScene(scene);
        content.reset();
        searchMenu.reset();
    }

    public void close() {
        owner.backToStage();
    }
}