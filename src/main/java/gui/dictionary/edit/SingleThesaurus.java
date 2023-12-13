package gui.dictionary.edit;

import data.Dictionary;
import data.Thesaurus;
import data.dictionary.Word;
import data.enums.ThesaurusType;
import exception.editWord.NoSuchWordFoundException;
import gui.style.AutoCompleteTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class SingleThesaurus extends VBox {
    @FXML private TextArea meaning;
    @FXML private TextField partOfSpeech;
    @FXML private AutoCompleteTextField mostCommonWord;
    @FXML private AutoCompleteTextField lessCommonWord;
    @FXML private VBox mostWordPane;
    @FXML private VBox lessWordPane;
    @FXML private FlowPane mostWordFlow;
    @FXML private FlowPane lessWordFlow;
    @FXML private Button deleteThesaurusButton;

    private final Word word;
    private Dictionary dictionary;
    private final Thesaurus thesaurus;
    private final SimpleBooleanProperty isModified = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty isDeleted = new SimpleBooleanProperty(false);

    public SingleThesaurus(Word word, Thesaurus thesaurus) {
        this.thesaurus = thesaurus;
        this.word = word;
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/dictionary/edit/SingleThesaurus.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            System.out.println("Error loading SingleThesaurus.fxml");
            e.printStackTrace();
        }
    }

    public void initialize() {
        setDictionary(thesaurus.getDictionary());
        mostCommonWord.textProperty().addListener((observable, oldValue, newValue) -> mostWordPane.getStyleClass().remove("required-field"));
        mostCommonWord.onActionProperty().set(event -> {
            try {
                String target = mostCommonWord.getText();
                Word word = thesaurus.addMostUsedByWord(target);
                canDeletedButton(word, 0, mostWordFlow, () -> thesaurus.deleteMostUsedByWord(word));
                mostCommonWord.clear();
                isModified.set(true);
            } catch (NoSuchWordFoundException e) {
                mostWordPane.getStyleClass().add("required-field");
            }
        });

        lessCommonWord.textProperty().addListener((observable, oldValue, newValue) -> lessWordPane.getStyleClass().remove("required-field"));
        lessCommonWord.onActionProperty().set(event -> {
            try {
                String target = lessCommonWord.getText();
                Word word = thesaurus.addLessUsedByWord(target);
                canDeletedButton(word, 1, lessWordFlow, () -> thesaurus.deleteLessUsedByWord(word));
                lessCommonWord.clear();
                isModified.set(true);
            } catch (NoSuchWordFoundException e) {
                mostWordPane.getStyleClass().add("required-field");
            }
        });

        if (thesaurus.getMeaning() != null) {
            meaning.setText(thesaurus.getMeaning());
        }
        meaning.textProperty().addListener((observable, oldValue, newValue) -> {
            thesaurus.setMeaning(newValue);
            isModified.set(true);
        });
        if (thesaurus.getPartOfSpeech() != null) {
            partOfSpeech.setText(thesaurus.getPartOfSpeech().toEnglish());
        }
        partOfSpeech.textProperty().addListener((observable, oldValue, newValue) -> {
            thesaurus.setPartOfSpeech(newValue);
            isModified.set(true);
        });

        for (Word word : thesaurus.getMostUsedWords()) {
            canDeletedButton(word, 0, mostWordFlow, () -> thesaurus.deleteMostUsedByWord(word));
        }
        for (Word word : thesaurus.getLessUsedWords()) {
            canDeletedButton(word, 1, lessWordFlow, () -> thesaurus.deleteLessUsedByWord(word));
        }
        deleteThesaurusButton.setOnAction(event -> isDeleted.set(true));
    }

    public void setDictionary(Dictionary dictionary) {
        mostCommonWord.setDictionary(dictionary);
        lessCommonWord.setDictionary(dictionary);
        this.dictionary = dictionary;
    }

    public void setType(ThesaurusType type) {
        thesaurus.setType(type);
    }

    public Thesaurus getThesaurus() {
        return thesaurus;
    }

    public SimpleBooleanProperty isModifiedProperty() {
        return isModified;
    }

    private static void canDeletedButton(Word word, int level, FlowPane pane, Runnable action) {
        Button button = createWordButton(word, level, action);
        pane.getChildren().add(button);
        button.setOnAction(event1 -> {
            action.run();
            pane.getChildren().remove(button);
        });
    }

    public SimpleBooleanProperty isDeletedProperty() {
        return isDeleted;
    }

    public static Button createWordButton(Word word, int level, Runnable action) {
        Button button = new Button(word.getWordTarget());
        button.getStyleClass().add("level-" + level);
        button.setOnAction(event -> action.run());
        Tooltip tooltip = new Tooltip();
        button.setTooltip(tooltip);
        switch (level) {
            case 0:
                tooltip.setText("Most common");
                break;
            case 1:
                tooltip.setText("Less common");
                break;
        }
        return button;
    }
}
