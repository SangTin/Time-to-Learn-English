package gui.style;

import data.dictionary.Word;
import data.enums.AppFunction;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;

public abstract class DisplayWord extends AnchorPane {
    protected SimpleObjectProperty<AppFunction> function = new SimpleObjectProperty<>(AppFunction.SEARCH);

    public final void display(Word word, AppFunction function) {
        this.function.set(function);
        display(word);
    }

    protected abstract void display(Word word);
    public abstract void clear();
}
