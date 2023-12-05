package gui.dictionary.content;

import data.dictionary.Word;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class Thesaurus extends ScrollPane {
    @FXML private VBox contentBox;

    public Thesaurus() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dictionary/content/Thesaurus.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Thesaurus.fxml");
        }
    }

    public void initialize() {
        clear();
    }

    public void clear() {
        // Clear description
        contentBox.getChildren().clear();
    }

    public void display(Word word,  List<data.Thesaurus> thesauruses) throws NullPointerException {
        clear();
        if (word == null) {
            throw new NullPointerException("Word is null");
        }
        boolean haveThesaurus = false;

        for (data.Thesaurus thesaurus : thesauruses) {
            try {
                SingleThesaurus singleThesaurus = new SingleThesaurus(thesaurus);
                contentBox.getChildren().add(singleThesaurus);
                haveThesaurus = true;
            } catch (NullPointerException ignored) {
            }
        }
        if (!haveThesaurus) {
            throw new NullPointerException("No thesaurus found");
        }
    }
}
