package gui.dictionary.edit;

import data.dictionary.Word;
import gui.dictionary.search.Description;
import gui.style.DisplayWord;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EditWord extends DisplayWord {
    @FXML private TextField wordTarget;
    @FXML private TextField ipaUK;
    @FXML private TextField ipaUS;
    @FXML private TextArea wordExplain;
    @FXML private Button previewButton;
    @FXML private Button saveButton;
    @FXML private Button howToUseButton;

    private Word currentWord;
    private Word editingWord = new Word();
    private final Tooltip howToUse = new Tooltip();

    public EditWord() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit/content/EditWord.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            System.out.println("Error loading EditWord.fxml");
        }

        try {
            Parent tooltip = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/edit/content/EditWordToolTip.fxml")));
            howToUse.setGraphic(tooltip);
        } catch (Exception e) {
            System.out.println("Error loading EditWordToolTip.fxml");
        }
    }

    public void initialize() {
        previewButton.setOnAction(e -> {
            preview(editingWord);
        });
        wordTarget.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editingWord.setWordTarget(wordTarget.getText());
            }
        });
        ipaUK.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editingWord.setUkPron(ipaUK.getText());
            }
        });
        ipaUS.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editingWord.setUsPron(ipaUS.getText());
            }
        });
        wordExplain.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editingWord.setWordExplain(wordExplain.getText());
            }
        });

        howToUseButton.setTooltip(howToUse);
        howToUseButton.setOnAction(e -> {
            howToUse();
        });

        createNewWord();
        function.addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case ADD: {
                    wordTarget.setDisable(false);
                    break;
                }
                case FIX: {
                    wordTarget.setDisable(true);
                    break;
                }
            }
        });
    }

    public void createNewWord() {
        currentWord = new Word();
    }

    public void display(Word word) {
        currentWord = word;
        editingWord = new Word(word);

        wordTarget.setText(word.getWordTarget());
        ipaUK.setText(word.getUkPron());
        ipaUS.setText(word.getUsPron());
        wordExplain.setText(word.getWordExplain());
    }

    private static void preview(Word word) {
        Stage stage = new Stage();
        stage.setTitle("Preview word");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);

        Description description = new Description();
        description.display(word);
        Scene scene = new Scene(description);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void howToUse() {
        Stage stage = new Stage();
        stage.setTitle("How to use");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);

        Parent tooltip = null;
        try {
            tooltip = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/edit/content/EditWordToolTip.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(tooltip);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void clear() {
        
    }
}
