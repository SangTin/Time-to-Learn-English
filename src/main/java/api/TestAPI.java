package api;

import java.io.IOException;

public class TestAPI {
    static void testTranslate() throws IOException {
//        String sourceLanguage = "en";
        String sourceLanguage = "zh";

        String targetLanguage = "vi";

//        String text = "How are you today?";
        String text = "你今天过得怎么样？";
//        String translatedText = TranslateTextAdvanced.translateText(targetLanguage, text);
        String translatedText = TranslateText.translateText(sourceLanguage, targetLanguage, text);
        System.out.println(translatedText);
    }

    static void testTextToSpeech() throws Exception {
        String path = "src/main/resources/audio/output3.mp3";
        String text = "Hello, how are you?";
        TextToSpeech.textToSpeech(path, text);
    }

    static void testSpeechToText() throws Exception {
//        System.out.println(SpeechToText.streamingMicRecognize());
    }

    static void testSpeechtoTextButoon() {
        if(SpeechToText.isPaused() && !SpeechToText.isDone()) {
            return;
        }
        if(SpeechToText.isStart() && !SpeechToText.isPaused()) {
            SpeechToText.stopRecord();
            return;
        }
        SpeechToText.startRecord();
    }

    public static void main(String[] args) throws Exception {
//        testTranslate();
//        testTextToSpeech();
//        testSpeechToText();
    }
}
