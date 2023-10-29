package api;

import java.io.IOException;

public class TestAPI {
    static void testTranslate() throws IOException {
        String targetLanguage = "vi";
        String text = "It's time to learn English.";
        String translatedText = TranslateTextAdvanced.translateText(targetLanguage, text);
        System.out.println(translatedText);
    }

    static void testTextToSpeech() throws Exception {
        String path = "src/main/resources/audio/output2.mp3";
        String text = "you can say something more than that";
        TextToSpeech.textToSpeech(path, text);
    }

    static void testSpeechToText() throws Exception {
        SpeechToText.Init();
        System.out.println(SpeechToText.streamingMicRecognize());
    }
    public static void main(String[] args) throws Exception {
//        testTranslate();
//        testTextToSpeech();
//        testSpeechToText();
    }
}
