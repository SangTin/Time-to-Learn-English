package gui.components.translate_text;

import api.CONST;
import api.TextToSpeech;
import api.TranslateText;
import gui.components.VoiceInput;
import gui.style.SVGImage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public abstract class TranslateBase extends VBox {
    @FXML protected Button switchButton;
    @FXML protected Button voiceInputButton;
    @FXML protected Button voiceOutputButton;
    @FXML protected TextInputControl fromText;
    @FXML protected TextInputControl toText;
    @FXML protected ImageView fromLanguageImage;
    @FXML protected ImageView toLanguageImage;

    protected final SimpleStringProperty fromLanguage = new SimpleStringProperty();
    protected final SimpleStringProperty toLanguage = new SimpleStringProperty();

    protected final SVGImage fromLanguageSVGImage = new SVGImage();
    protected final SVGImage toLanguageSVGImage = new SVGImage();

    protected Timer timer = new Timer();
    private final MediaPlayer outputSound;

    public TranslateBase() {
        File soundFile = new File("src/main/resources/audio/translate.mp3");
        if (!soundFile.exists()) {
            try {
                soundFile.createNewFile();
            } catch (IOException ignored) {}
        }
        Media player = new Media(soundFile.toURI().toString());
        outputSound = new MediaPlayer(player);
    }

    public void initialize() {
        new Thread(() -> Platform.runLater(this::doInitialize)).start();
    }

    protected void doInitialize() {
        fromLanguage.addListener((observable, oldValue, newValue) -> {
            this.loadFromLanguageImage(newValue);
            fromText.setPromptText("Enter " + fromLanguage.get() + " text here");
            toText.setPromptText("Result in " + toLanguage.get());
        });

        toLanguage.addListener((observable, oldValue, newValue) -> {
            this.loadToLanguageImage(newValue);
            fromText.setPromptText("Enter " + fromLanguage.get() + " text here");
            toText.setPromptText("Result in " + toLanguage.get());
        });

        switchButton.setOnAction((event) -> this.switchLanguage());

        this.fromLanguageImage.imageProperty().bind(this.fromLanguageSVGImage.imageProperty());
        this.toLanguageImage.imageProperty().bind(this.toLanguageSVGImage.imageProperty());

        fromLanguage.set("English");
        toLanguage.set("Vietnamese");

        voiceInputButton.setOnAction((e) -> {
            System.out.println(getLanguageCode(fromLanguage.get()));
            VoiceInput voiceInput = new VoiceInput(getLanguageCode(fromLanguage.get()));
            voiceInput.setOnHidden((event) -> {
                fromText.setText(voiceInput.getTextResult().trim());
                fromText.requestFocus();
            });
            voiceInput.showAndWait();
        });

        voiceOutputButton.setOnAction((e) -> {
            System.out.println(getLanguageCode(toLanguage.get()));
            String text = toText.getText();
            TextToSpeech.textToSpeech(getLanguageCode(toLanguage.get()), text, "translate");
            outputSound.stop();
            outputSound.play();
        });

        fromText.textProperty().addListener((observable, oldValue, newValue) -> {
            timer.cancel();
            timer.purge();
            timer = new Timer();
            timer.schedule(new TranslateTask(), 2000);
        });
    }

    protected void loadFromLanguageImage(String fromLanguage) {
        this.fromLanguageSVGImage.load(getLanguagePath(fromLanguage));
    }

    protected void loadToLanguageImage(String toLanguage) {
        this.toLanguageSVGImage.load(getLanguagePath(toLanguage));
    }

    private static String getLanguagePath(String language) {
        String languageCode = getLanguageCode(language);
        String countryCode;
        try {
            countryCode = CONST.CONTRY_CODE.get(languageCode).getAsString();
        } catch (Exception e) {
            countryCode = "united_nations";
        }
        return "/img/flags/" + countryCode + ".svg";
    }

    protected static String getLanguageCode(String language) {
        return CONST.SUPPORTED_LANGUAGES.get(language).getAsString();
    }

    private void clear() {
        this.fromText.clear();
        this.toText.clear();
        outputSound.stop();
    }

    protected final void switchLanguage() {
        clear();

        String text = this.fromLanguage.get();
        this.fromLanguage.set(this.toLanguage.get());
        this.toLanguage.set(text);
    }

    protected final void doTranslate() {
        String text = this.fromText.getText();
        if (text.isEmpty()) {
            this.toText.clear();
            return;
        }
        String fromLanguage = getLanguageCode(this.fromLanguage.get());
        String toLanguage = getLanguageCode(this.toLanguage.get());
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
