package api;

// Imports the Google Cloud client library
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

import java.io.FileOutputStream;
import java.io.OutputStream;


public class TextToSpeech {

    /**
     * Create a MP3 file from source text (speech language: en-US).
     * @param path - path to locate MP3 file.
     * @param text - source text (must be english).
     * */
    public static void textToSpeech(String path, String text) throws Exception {
        SynthesisInput input = SynthesisInput.newBuilder()
                .setText(text).build();

        //Select languageCode: en-US (can be fixed) and voiceGender: NEUTRAL (can be fixed)
        VoiceSelectionParams voice =
                VoiceSelectionParams.newBuilder()
                        .setLanguageCode("en-US")
                        .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                        .build();

        //Type of audio: MP3.
        AudioConfig audioConfig =
                AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

        SynthesizeSpeechResponse response =
                CONST.TEXT_TO_SPEECH_CLIENT.synthesizeSpeech(input, voice, audioConfig);
        ByteString audioContents = response.getAudioContent();

        try (OutputStream out = new FileOutputStream(path)) {
            out.write(audioContents.toByteArray());
        }
    }
}