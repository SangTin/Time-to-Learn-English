package gui.style;

import data.Dictionary;
import data.dictionary.Word;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.List;

public class AutoCompleteTextField extends TextField {
    private Dictionary dictionary;

    private final ObjectProperty<Word> lastSelectedItem = new SimpleObjectProperty<>();

    /**
     * The popup used to select an entry.
     */
    private final ContextMenu entriesPopup;
    private boolean popupHidden = false;

    /**
     * The maximum Number of entries displayed in the popup.<br>
     * Default: 10
     */
    private int maxEntries = 10;

    /**
     * Construct a new AutoCompleteTextField.
     */
    public AutoCompleteTextField()
    {
        entriesPopup = new ContextMenu();

        textProperty().addListener((ObservableValue<? extends String> observableValue, String s, String s2) ->
        {
            if (getText() == null || getText().isEmpty()) {
                entriesPopup.hide();
            } else {
                String text1 = getText();
                List<Word> searchResult = dictionary.search(text1);
                if (!searchResult.isEmpty()) {
                    //Only show popup if not in filter mode
                    if (!isPopupHidden()) {
                        populatePopup(searchResult, text1);
                        if (!entriesPopup.isShowing()) {
                            entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                        }
                    }
                } else {
                    entriesPopup.hide();
                }
            }
        });

        focusedProperty().addListener((ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) ->
                entriesPopup.hide());

    }

    /**
     * Set the dictionary to be used by this AutoCompleteTextField
     * @param dictionary The dictionary to be used by this AutoCompleteTextField
     */
    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Populate the entry set with the given search results. Display is limited
     * to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<Word> searchResult, String text) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int count = Math.min(searchResult.size(), getMaxEntries());
        for (int i = 0; i < count; i++) {
            final Word itemObject = searchResult.get(i);

            Text result = new Text(searchResult.get(i).getWordTarget());

            CustomMenuItem item = new CustomMenuItem(result, true);
            item.setOnAction((ActionEvent actionEvent) -> {
                lastSelectedItem.set(itemObject);
                setText(itemObject.getWordTarget());
                entriesPopup.hide();
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    public Word getLastSelectedObject() {
        return lastSelectedItem.get();
    }

    public ContextMenu getEntryMenu()
    {
        return entriesPopup;
    }

    public boolean isPopupHidden()
    {
        return popupHidden;
    }

    public void setPopupHidden(boolean popupHidden) {
        this.popupHidden = popupHidden;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }
}