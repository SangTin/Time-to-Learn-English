package gui.edit;

import gui.GraphicalDictionary;
import gui.components.WordDisplay;
import gui.components.search.SearchPane;
import gui.style.Synchronized;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class Editor extends WordDisplay implements Synchronized {
    @FXML private SearchPane searchPane;

    public Editor() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/edit/Editor.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            System.out.println("Error loading Editor.fxml");
        }
    }

    public void initialize() {
        dictionary = GraphicalDictionary.getDictionaryInstance();
        database = GraphicalDictionary.getDatabaseInstance();

        searchPane.setDictionary(dictionary);
    }

    public void displaySearch(data.Word word) {

    }

    public void synchronize() {

    }
}
