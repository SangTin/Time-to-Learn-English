package gui.components.translate_text;

import api.CONST;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.IOException;

public class TranslatePane extends TranslateBase{
    @FXML private ComboBox<String> fromLanguageComboBox;
    @FXML private ComboBox<String> toLanguageComboBox;
    @FXML private Button copyButton;

    public TranslatePane() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/components/translate_text/TranslatePane.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException var2) {
            System.out.println("Error loading TranslatePane.fxml");
        }
    }

    public void initialize() {
        super.initialize();
    }

    protected void doInitialize() {
        fromLanguageComboBox.getItems().addAll(CONST.SUPPORTED_LANGUAGES.keySet());
        toLanguageComboBox.getItems().addAll(CONST.SUPPORTED_LANGUAGES.keySet());

        fromLanguage.addListener((observable, oldValue, newValue) -> fromLanguageComboBox.getSelectionModel().select(newValue));
        toLanguage.addListener((observable, oldValue, newValue) -> toLanguageComboBox.getSelectionModel().select(newValue));

        fromLanguageComboBox.setOnAction((event) -> fromLanguage.set(fromLanguageComboBox.getSelectionModel().getSelectedItem()));
        toLanguageComboBox.setOnAction((event) -> toLanguage.set(toLanguageComboBox.getSelectionModel().getSelectedItem()));

        copyButton.setOnAction((event) -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(toText.getText());
            clipboard.setContent(content);
        });

        super.doInitialize();
    }
}
