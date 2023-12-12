package data.dictionary;

import com.sun.javafx.collections.VetoableListDecorator;
import javafx.collections.FXCollections;

import java.util.ArrayDeque;
import java.util.List;

public class AutoDeleteWordList extends VetoableListDecorator<Word> {
    public AutoDeleteWordList() {
        super(FXCollections.observableArrayList(new ArrayDeque<>()));
    }

    protected void onProposedChange(List<Word> toBeAdded, int... indexes) {
        for (Word word : toBeAdded) {
            word.deletedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    this.remove(word);
                }
            });
        }
    }

    public void addFirst(Word word) {
        this.add(0, word);
    }
}
