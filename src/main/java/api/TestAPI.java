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
        String translatedText = TranslateTextAdvanced.translateText(sourceLanguage, targetLanguage, text);
        System.out.println(translatedText);
    }

    static void testTextToSpeech() throws Exception {
        String path = "src/main/resources/audio/output4.mp3";
        String text = "dynasty";
        TextToSpeech.textToSpeech(path, text);
    }

    static void testSpeechToText() throws Exception {
//        SpeechToText.Init();
        System.out.println(SpeechToText.streamingMicRecognize());
    }

    public static void main(String[] args) throws Exception {
        System.out.println("hi");
//        testTranslate();
//        testTextToSpeech();
//        testSpeechToText();
        Thread start = new startStream();
        Thread stop = new stopStream();
        start.start();
        stop.start();
//        testSpeechToText();
    }
}

class stopStream extends Thread {
    public void run()
    {
        try {
            Thread.sleep(3000);
            System.out.println("stop\n");
            SpeechToText.stopStreaming();
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}

class startStream extends Thread {
    public void run()
    {
        try {
            System.out.println("start\n");
            SpeechToText.streamingMicRecognize();
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}