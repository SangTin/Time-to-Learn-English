package gui;

import data.Dictionary;
import data.SQLiteDatabase;
import gui.dictionary.Content;
import gui.dictionary.SearchMenu;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class GraphicalDictionary {
    private static ProgressBar loadingProgressBar = new ProgressBar();

    private LinkedStage owner;
    private Scene scene;

    private SQLiteDatabase database;
    private Dictionary dictionary;

    private SearchMenu searchMenu;
    private Content content;
    private AnchorPane mainPane;
    
    public static ProgressBar getLoadingProgressBar() {
        return loadingProgressBar;
    }

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
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        loadingProgressBar.setPrefWidth(100);
        loadingProgressBar.setPrefHeight(10);
        AnchorPane.setBottomAnchor(loadingProgressBar, 5.0);
        AnchorPane.setRightAnchor(loadingProgressBar, 5.0);

        mainPane.getChildren().add(loadingProgressBar);

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