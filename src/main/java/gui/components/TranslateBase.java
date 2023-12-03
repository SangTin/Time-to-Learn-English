package gui.components;

import api.CONST;
import api.TranslateText;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.TimerTask;

public abstract class TranslateBase extends VBox {
    @FXML protected Button switchButton;
    @FXML protected TextInputControl fromText;
    @FXML protected TextInputControl toText;
//    @FXML protected SVGImage fromLanguageImage;
//    @FXML protected SVGImage toLanguageImage;

    protected String fromLanguage;
    protected String toLanguage;

    public void initialize() {
        switchButton.setOnAction((event) -> {
            this.switchLanguage();
        });

//        this.fromLanguageImage.imageProperty().bind(this.fromLanguageSVGImage.imageProperty());
//        this.toLanguageImage.imageProperty().bind(this.toLanguageSVGImage.imageProperty());
    }

    protected void loadImage(String fromLanguage, String toLanguage) {
//        this.fromLanguageImage.load(this.getLanguagePath(fromLanguage));
//        this.toLanguageImage.load(this.getLanguagePath(toLanguage));
    }

    private static String getLanguagePath(String language) {
        String languageCode = getLanguageCode(language);
        String countryCode = CONST.CONTRY_CODE.get(languageCode).getAsString();
        return "/img/flags/" + countryCode + ".svg";
    }

    protected static String getLanguageCode(String language) {
        return CONST.SUPPORTED_LANGUAGES.get(language).getAsString();
    }


    protected void switchLanguage() {
        this.fromText.clear();
        this.toText.clear();

        String promptText = this.fromText.getPromptText();
        this.fromText.setPromptText(this.toText.getPromptText());
        this.toText.setPromptText(promptText);

        String text = this.fromLanguage;
        this.fromLanguage = this.toLanguage;
        this.toLanguage = text;

        loadImage(fromLanguage, toLanguage);
    }

    protected final void doTranslate() {
        String text = this.fromText.getText();
        if (text.isEmpty()) {
            this.toText.clear();
            return;
        }
        String fromLanguage = getLanguageCode(this.fromLanguage);
        String toLanguage = getLanguageCode(this.toLanguage);
        this.toText.setText(this.translate(fromLanguage, toLanguage, text));
    }

    protected final String translate(String fromLanguage, String toLanguage, String text) {
        try {
            return TranslateText.translateText(fromLanguage, toLanguage, text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class TranslateTask extends TimerTask {
        @Override
        public void run() {
            Platform.runLater(TranslateBase.this::doTranslate);
        }
    }
}
