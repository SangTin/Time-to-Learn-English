package gui.components;

import gui.style.DisplayContent;
import gui.style.Synchronized;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public abstract class WordDisplay extends SplitPane implements DisplayContent, Synchronized {

    @FXML protected SearchBase searchPane;
    @FXML protected TabPane contentTabPane;
    @FXML protected Tab descriptionTab;
    @FXML protected Tab synonymTab;
    @FXML protected Tab antonymTab;

    protected data.SQLiteDatabase database;
    protected data.Dictionary dictionary;

    public abstract void displaySearch(data.Word word);
}
