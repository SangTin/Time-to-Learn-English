package gui.edit.content;

import data.Word;
import gui.dictionary.content.Description;
import gui.style.DisplayContent;
import gui.style.Synchronized;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EditWord extends AnchorPane implements DisplayContent, Synchronized {
    @FXML private TextField wordTarget;
    @FXML private TextField ipaUK;
    @FXML private TextField ipaUS;
    @FXML private TextArea wordExplain;
    @FXML private Button previewButton;
    @FXML private Button saveButton;
    @FXML private Button howToUseButton;

    private Word currentWord;
    private Word editedWord = new Word();
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
            preview(editedWord);
        });
        wordTarget.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editedWord.setWordTarget(wordTarget.getText());
            }
        });
        ipaUK.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editedWord.setUkPron(ipaUK.getText());
            }
        });
        ipaUS.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editedWord.setUsPron(ipaUS.getText());
            }
        });
        wordExplain.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                editedWord.setWordExplain(wordExplain.getText());
            }
        });

        howToUseButton.setTooltip(howToUse);
        howToUseButton.setOnAction(e -> {
            howToUse();
        });

        createNewWord();
    }

    public void createNewWord() {
        currentWord = new Word();
        synchronize();
    }

    public void displaySearch(Word word) {
        currentWord = word;
        editedWord = new Word(word);
        synchronize();
    }

    public void synchronize() {
        wordTarget.setText(editedWord.getWordTarget());
        ipaUK.setText(editedWord.getUkPron());
        ipaUS.setText(editedWord.getUsPron());
        wordExplain.setText(editedWord.getWordExplain());
    }

    private static void preview(Word word) {
        Stage stage = new Stage();
        stage.setTitle("Voice Input");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);

        Description description = new Description();
        description.displaySearch(word);
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
}
