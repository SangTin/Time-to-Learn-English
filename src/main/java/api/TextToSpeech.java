package api;

// Imports the Google Cloud client library

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class TextToSpeech {
    private static TextToSpeechTask textToSpeechTask;

    /**
     * @param sourceLanguage - source language (must be english).
     * @param text - source text (must be english).
     * @param fileName - name of the file to be saved.
     * */
    public static void textToSpeech(String sourceLanguage, String text, String fileName) {
        if (textToSpeechTask != null) {
            textToSpeechTask.cancel();
        }
        textToSpeechTask = new TextToSpeechTask(sourceLanguage, text, fileName);
        textToSpeechTask.run();
    }

    private static class TextToSpeechTask extends Task<Void> {
        private final String sourceLanguage;
        private final String text;
        private final String fileName;

        public TextToSpeechTask(String sourceLanguage, String text, String fileName) {
            this.sourceLanguage = sourceLanguage;
            this.text = text;
            this.fileName = fileName;
        }

        @Override
        protected Void call() {
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text).build();

            //Select languageCode: en-US (can be fixed) and voiceGender: NEUTRAL (can be fixed)
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode(sourceLanguage)
                            .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                            .build();

            //Type of audio: MP3.
            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            SynthesizeSpeechResponse response =
                    CONST.TEXT_TO_SPEECH_CLIENT.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

            try {
                File file = new File("src/main/resources/audio/" + fileName + ".mp3");
                OutputStream out = new FileOutputStream(file);
                out.write(audioContents.toByteArray());
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}