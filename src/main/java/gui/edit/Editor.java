package gui.edit;

import data.dictionary.Word;
import gui.GraphicalDictionary;
import gui.components.WordDisplay;
import javafx.fxml.FXMLLoader;

public class Editor extends WordDisplay {
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
    }

    public void displaySearch(Word word) {

    }
}
