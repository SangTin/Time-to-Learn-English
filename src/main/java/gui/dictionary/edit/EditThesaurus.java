package gui.dictionary.edit;

import data.Dictionary;
import data.Thesaurus;
import data.dictionary.Word;
import data.enums.ThesaurusType;
import gui.GraphicalDictionary;
import gui.style.DisplayThesaurus;
import gui.style.WordEditor;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EditThesaurus extends DisplayThesaurus implements WordEditor {
    @FXML private VBox contentBox;
    @FXML private Label headerWord;
    @FXML private Button addThesaurusButton;

    private final ThesaurusType type;
    private Word editingWord;
    private ObservableList<Thesaurus> thesauruses;
    private final Dictionary dictionary;
    private boolean isModified = false;

    public EditThesaurus(ThesaurusType type, Dictionary dictionary) {
        this.type = type;
        this.dictionary = dictionary;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dictionary/edit/Thesaurus.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            System.out.println("Error loading Thesaurus.fxml");
            e.printStackTrace();
        }
    }

    public void initialize() {
        addThesaurusButton.setOnAction(event -> {
            Thesaurus thesaurus = new Thesaurus();
            thesaurus.setDictionary(dictionary);
            thesaurus.setType(type);
            thesauruses.add(thesaurus);
            display(editingWord, thesauruses);
        });
        contentBox.getChildren().addListener((ListChangeListener<? super Object>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Object x : c.getAddedSubList()) {
                        if (!(x instanceof SingleThesaurus thesaurus)) continue;
                        thesaurus.isModifiedProperty().addListener((o, oldValue, newValue) -> {
                            if (newValue) isModified = true;
                        });
                    }
                }
            }
        });
    }

    public void clear() {
        // Clear description
        contentBox.getChildren().clear();
        isModified = false;
    }

    public void display(Word word, ObservableList<Thesaurus> thesauruses) {
        clear();
        if (word == null) {
            return;
        }

        headerWord.setText(word.getWordTarget());

        this.editingWord = word;
        this.thesauruses = thesauruses;
        for (data.Thesaurus thesaurus : thesauruses) {
            try {
                SingleThesaurus singleThesaurus = new SingleThesaurus(word, thesaurus);
                contentBox.getChildren().add(singleThesaurus);
            } catch (NullPointerException ignored) {}
        }
        thesauruses.addListener((ListChangeListener<Thesaurus>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Thesaurus thesaurus : c.getAddedSubList()) {
                        try {
                            SingleThesaurus singleThesaurus = new SingleThesaurus(word, thesaurus);
                            contentBox.getChildren().add(singleThesaurus);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void save() {
        for (Thesaurus thesaurus : thesauruses) {
            GraphicalDictionary.getDatabaseInstance().deleteThesaurus(thesaurus);
            GraphicalDictionary.getDatabaseInstance().addThesaurus(editingWord, thesaurus);
        }
    }

    public boolean isModified() {
        return isModified;
    }
}
