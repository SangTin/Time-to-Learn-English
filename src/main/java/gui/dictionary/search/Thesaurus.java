package gui.dictionary.search;

import data.dictionary.Word;
import gui.style.DisplayThesaurus;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class Thesaurus extends DisplayThesaurus {
    @FXML private VBox contentBox;

    public Thesaurus() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dictionary/search/Thesaurus.fxml"));
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
        setDisable(true);

        // Clear description
        contentBox.getChildren().clear();
    }

    public void display(Word word,  ObservableList<data.Thesaurus> thesauruses) {
        clear();
        if (word == null) {
            return;
        }

        for (data.Thesaurus thesaurus : thesauruses) {
            try {
                SingleThesaurus singleThesaurus = new SingleThesaurus(thesaurus);
                contentBox.getChildren().add(singleThesaurus);
            } catch (NullPointerException ignored) {}
        }
        thesauruses.addListener(new ListChangeListener<data.Thesaurus>() {
            @Override
            public void onChanged(Change<? extends data.Thesaurus> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        for (data.Thesaurus thesaurus : c.getAddedSubList()) {
                            try {
                                SingleThesaurus singleThesaurus = new SingleThesaurus(thesaurus);
                                contentBox.getChildren().add(singleThesaurus);
                            } catch (NullPointerException ignored) {}
                        }
                    }
                }
                setDisable(contentBox.getChildren().isEmpty());
            }
        });
    }
}
