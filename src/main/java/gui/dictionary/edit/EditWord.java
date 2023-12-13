package gui.dictionary.edit;

import data.dictionary.Word;
import data.enums.AppFunction;
import gui.GraphicalDictionary;
import gui.dictionary.search.Description;
import gui.style.DisplayWord;
import gui.style.WordEditor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EditWord extends DisplayWord implements WordEditor {
    @FXML private TextField wordTarget;
    @FXML private TextField ipaUK;
    @FXML private TextField ipaUS;
    @FXML private TextArea wordExplain;
    @FXML private Button previewButton;
    @FXML private Button howToUseButton;
    @FXML private VBox wordPane;
    @FXML private VBox descriptionPane;

    private Word editingWord = new Word();
    private final Tooltip howToUse = new Tooltip();
    private final ArrayList<VBox> requiredFields = new ArrayList<>();
    private boolean isModified = false;

    public EditWord() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dictionary/edit/EditWord.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            System.out.println("Error loading EditWord.fxml");
        }

        try {
            Parent tooltip = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/dictionary/edit/EditWordToolTip.fxml")));
            howToUse.setGraphic(tooltip);
        } catch (Exception e) {
            System.out.println("Error loading EditWordToolTip.fxml");
        }
    }

    public void initialize() {
        previewButton.setOnAction(e -> {
            preview(editingWord);
        });

        wordTarget.textProperty().addListener((observable, oldValue, newValue) -> {
            editingWord.setWordTarget(wordTarget.getText());
            isModified = true;
        });
        ipaUK.textProperty().addListener((observable, oldValue, newValue) -> {
            editingWord.setUkPron(ipaUK.getText());
            isModified = true;
        });
        ipaUS.textProperty().addListener((observable, oldValue, newValue) -> {
            editingWord.setUsPron(ipaUS.getText());
            isModified = true;
        });
        wordExplain.textProperty().addListener((observable, oldValue, newValue) -> {
            editingWord.setWordExplain(wordExplain.getText());
            isModified = true;
        });

        howToUseButton.setTooltip(howToUse);
        howToUseButton.setOnAction(e -> {
            howToUse();
        });

        function.set(AppFunction.ADD);
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
        setRequiredField(wordTarget, wordPane);
        setRequiredField(wordExplain, descriptionPane);

        wordTarget.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (function.get() == AppFunction.ADD && dictionary.have(newValue)) {
                    wordPane.getStyleClass().add("required-field");
                    Label label = (Label) wordPane.getChildren().getLast();
                    label.setText("This word already exists");
                }
            }
        });
    }

    public void setRequiredField(TextInputControl text, VBox pane) {
        pane.getStyleClass().add("required-field");
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            pane.getStyleClass().removeAll("required-field");
            if (newValue.isEmpty()) {
                Label label = (Label) wordPane.getChildren().getLast();
                label.setText("This field is required");
                pane.getStyleClass().add("required-field");
            }
        });
        requiredFields.add(pane);
    }

    public void display(Word word) {
        editingWord = new Word(word);

        wordTarget.setText(word.getWordTarget());
        ipaUK.setText(word.getUkPron());
        ipaUS.setText(word.getUsPron());
        wordExplain.setText(word.getWordExplain());
        isModified = false;
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
            tooltip = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/dictionary/edit/EditWordToolTip.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(tooltip);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public boolean canSave() {
        for (VBox pane : requiredFields) {
            if (pane.getStyleClass().contains("required-field")) {
                alert("Please fill in all required fields");
                return false;
            }
        }
        switch (function.get()) {
            case ADD: {
                if (dictionary.have(editingWord.getWordTarget())) {
                    alert("This word already exists");
                    return false;
                }
                break;
            }
            case FIX: {
                if (!dictionary.have(editingWord.getWordTarget())) {
                    alert("This word does not exist");
                    return false;
                }
                break;
            }
        }

        return true;
    }

    public void save() {
        if (!canSave()) {
            return;
        }
        try {
            switch (function.get()) {
                case ADD: {
                    GraphicalDictionary.getChangesInstance().insert(editingWord);
                    GraphicalDictionary.setAppFunction(AppFunction.FIX, editingWord);
                    break;
                }
                case FIX: {
                    GraphicalDictionary.getChangesInstance().fix(editingWord);
                    break;
                }
            }
        } catch (Exception e) {
            alert(e.getMessage());
        }
        isModified = false;
    }

    public boolean isModified() {
        return isModified;
    }

    public void clear() {
        wordTarget.clear();
        ipaUK.clear();
        ipaUS.clear();
        wordExplain.clear();
        function.set(AppFunction.ADD);
    }

    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong. Please check your input.\n" + message);
        alert.showAndWait();
    }
}
