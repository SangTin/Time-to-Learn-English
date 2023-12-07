package api;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.TargetDataLine;
import java.util.ArrayList;

public class SpeechToText {
    private static final int STREAMING_LIMIT = 20000;
    private static final int IDLE_LIMIT = 6000;
    private static ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
    private static final ClientStream<StreamingRecognizeRequest> clientStream;
    private static StreamingRecognizeRequest request;
    private static TargetDataLine targetDataLine;
    private static final SimpleStringProperty textOfSpeech;
    private static final SimpleBooleanProperty isDone;
    private static boolean isIdle;

    private static Thread startThread, stopThread;

    private static void InitializeResponseObserver() {
        responseObserver = new ResponseObserver<>() {
            final ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

            public void onStart(StreamController controller) {
            }

            public void onResponse(StreamingRecognizeResponse response) {
                this.responses.add(response);
                StreamingRecognitionResult result = response.getResultsList().get(0);
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                SpeechToText.textOfSpeech.set(alternative.getTranscript());
                SpeechToText.isDone.set(result.getIsFinal());

                SpeechToText.isIdle = false;
            }

            public void onComplete() {
            }

            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }
        };
    }

    private static void InitializeFirstRequest() {
        RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16).setLanguageCode("en-US").setSampleRateHertz(16000).build();
        StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).setInterimResults(true).build();
        request = StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingRecognitionConfig).build();
        clientStream.send(request);
    }

    public static void streamingMicRecognize() throws Exception {
        textOfSpeech.set("");
        isDone.set(false);
        isIdle = true;
        AudioFormat audioFormat = new AudioFormat(16000.0F, 16, 1, true, false);
        Info targetInfo = new Info(TargetDataLine.class, audioFormat);
        if (!AudioSystem.isLineSupported(targetInfo)) {
            throw new UnsupportedOperationException("Microphone not supported");
        } else {
            targetDataLine = (TargetDataLine)AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            AudioInputStream audio = new AudioInputStream(targetDataLine);
            long startTime = System.currentTimeMillis();

            while(!isDone()) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                System.out.println(estimatedTime);
                if (estimatedTime >= IDLE_LIMIT && isIdle || estimatedTime >= STREAMING_LIMIT) {
                    stopStreaming();
//                    break;
                }

                byte[] data = new byte[6400];
                audio.read(data);
                request = StreamingRecognizeRequest.newBuilder().setAudioContent(ByteString.copyFrom(data)).build();
                clientStream.send(request);
            }

            targetDataLine.stop();
            targetDataLine.close();
//            clientStream.closeSend();
//            if (isDone()) {
//                return getTextOfSpeech();
//            } else {
//                throw new RuntimeException("You did not speak anything.");
//            }
        }
    }

    public static String getTextOfSpeech() {
        return textOfSpeech.get();
    }

    public static boolean isDone() {
        return isDone.get();
    }

    public static SimpleStringProperty textOfSpeechProperty() {
        return textOfSpeech;
    }

    public static void stopStreaming() {
        isDone.set(true);
    }

    public static void startRecord() {
        startThread.start();
    }

    public static String stopRecord() {
        stopThread.start();
        while(!isDone()) {}
        return getTextOfSpeech();
    }

    static {
        System.out.println("initialize");
        InitializeResponseObserver();
        clientStream = CONST.SPEECH_CLIENT.streamingRecognizeCallable().splitCall(responseObserver);
        InitializeFirstRequest();
        textOfSpeech = new SimpleStringProperty();
        isDone = new SimpleBooleanProperty(false);
    }
}

class stopStream extends Thread {
    public void run()
    {
        try {
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
            SpeechToText.streamingMicRecognize();
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}