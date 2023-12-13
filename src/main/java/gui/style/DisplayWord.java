package gui.style;

import data.Dictionary;
import data.dictionary.Word;
import data.enums.AppFunction;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;

public abstract class DisplayWord extends AnchorPane {
    protected final SimpleObjectProperty<AppFunction> function = new SimpleObjectProperty<>(AppFunction.SEARCH);
    protected Dictionary dictionary;

    public final void display(Word word, AppFunction function) {
        this.function.set(function);
        display(word);
    }

    protected abstract void display(Word word);
    public abstract void clear();

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}
