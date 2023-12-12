package gui.style;

import data.Thesaurus;
import data.dictionary.Word;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;

public abstract class DisplayThesaurus extends ScrollPane {
    public abstract void display(Word word, ObservableList<Thesaurus> thesauruses);
    public abstract void clear();
}
