package api;

// Imports the Google Cloud client library

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioInputStream;
import java.util.ArrayList;

public class SpeechToText {
    private static boolean stop = false;
    private static String textOfSpeech;

    private static ResponseObserver<StreamingRecognizeResponse> responseObserver = null;

    private static ClientStream<StreamingRecognizeRequest> clientStream;

    private static StreamingRecognizeRequest request;

    /**
     * If you want to use Speech to Text api, you need to call this function first.
     * This function initialize things which don't need to initialize more than once.
     */
    public static void Init() {
        InitializeResponseObserver();
        clientStream = CONST.SPEECH_CLIENT.streamingRecognizeCallable().splitCall(responseObserver);
        InitializeFirstRequest();
    }

    /**
     * Initialize Response Observer.
     */
    private static void InitializeResponseObserver() {
        responseObserver =
                new ResponseObserver<StreamingRecognizeResponse>() {
                    ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

                    public void onStart(StreamController controller) {
                    }

                    public void onResponse(StreamingRecognizeResponse response) {
                        responses.add(response);
                    }

                    public void onComplete() {
                        for (StreamingRecognizeResponse response : responses) {
                            StreamingRecognitionResult result = response.getResultsList().get(0);
                            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                            textOfSpeech = alternative.getTranscript();
                            stop = true;
                        }
                    }

                    public void onError(Throwable t) {
                        System.out.println(t);
                    }
                };
    }

    /**
     * Initialize first request.
     * Because the first request has to be a config, this function did this work.
     */
    private static void InitializeFirstRequest() {
        RecognitionConfig recognitionConfig =
                RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setLanguageCode("en-US")
                        .setSampleRateHertz(16000)
                        .build();

        StreamingRecognitionConfig streamingRecognitionConfig =
                StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).build();

        request =
                StreamingRecognizeRequest.newBuilder()
                        .setStreamingConfig(streamingRecognitionConfig)
                        .build();
        clientStream.send(request);
    }

    /**
     * This function convert the streaming into text.
     *
     * @return: - String "Microphone not supported" if the device has no microphone or did not support.
     * - String "You did not speak anything." if user has not spoken in 60 seconds.
     * - String: text that user spoke.
     */
    public static String streamingMicRecognize() throws Exception {
        stop = false;
        textOfSpeech = null;

        AudioFormat audioFormat =
                new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info targetInfo =
                new DataLine.Info(
                        TargetDataLine.class,
                        audioFormat);

        if (!AudioSystem.isLineSupported(targetInfo)) {
            return "Microphone not supported";
        }

        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
        targetDataLine.open(audioFormat);
        targetDataLine.start();
        System.out.println("Start speaking");
        long startTime = System.currentTimeMillis();

        // Audio Input Stream
        AudioInputStream audio = new AudioInputStream(targetDataLine);
        while (!stop) {
            long estimatedTime = System.currentTimeMillis() - startTime;
            byte[] data = new byte[6400];
            audio.read(data);
            if (estimatedTime > 60000) { // 60 seconds
                System.out.println("Stop speaking.");
                targetDataLine.stop();
                targetDataLine.close();
                break;
            }
            request =
                    StreamingRecognizeRequest.newBuilder()
                            .setAudioContent(ByteString.copyFrom(data))
                            .build();
            clientStream.send(request);
            responseObserver.onComplete();
            if (textOfSpeech != null) {
                return textOfSpeech;
            }
        }
        return "You did not speak anything.";
    }
}
