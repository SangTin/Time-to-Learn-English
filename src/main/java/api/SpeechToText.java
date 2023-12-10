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
    private static final int STREAMING_LIMIT = 6000;
    private static final int IDLE_LIMIT = 3000;
    private static ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
    private static ClientStream<StreamingRecognizeRequest> clientStream;
    private static StreamingRecognizeRequest request;
    private static TargetDataLine targetDataLine;
    private static SimpleStringProperty textOfSpeech;
    private static  SimpleBooleanProperty isStart;
    private static SimpleBooleanProperty isDone;
    private static SimpleBooleanProperty isPaused;
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
        clientStream = CONST.SPEECH_CLIENT.streamingRecognizeCallable().splitCall(responseObserver);
        InitializeFirstRequest();

        isDone.set(false);
        isPaused.set(false);
        isStart.set(true);
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
            long estimatedTime = 0;
            while(estimatedTime < STREAMING_LIMIT && !isPaused()) {
                estimatedTime = System.currentTimeMillis() - startTime;
                System.out.println(estimatedTime);
                if(getTextOfSpeech() != null && getTextOfSpeech() != "") {
                    isIdle = false;
                }
                if (estimatedTime >= IDLE_LIMIT && isIdle) {
                    break;
                }

                byte[] data = new byte[6400];
                audio.read(data);
                request = StreamingRecognizeRequest.newBuilder().setAudioContent(ByteString.copyFrom(data)).build();
                clientStream.send(request);
            }

            targetDataLine.stop();
            targetDataLine.close();
            isDone.set(true);
            isPaused.set(false);
            isStart.set(false);
        }
    }

    public static String getTextOfSpeech() {
        return textOfSpeech.get();
    }

    public static boolean isDone() {
        return isDone.get();
    }

    public static boolean isPaused() {
        return isPaused.get();
    }
    public static boolean isStart() {
        return isStart.get();
    }

    public static SimpleStringProperty textOfSpeechProperty() {
        return textOfSpeech;
    }

    public static void stopStreaming() {
        isPaused.set(true);
    }

    public static void startRecord() {
        startThread = new startStream();
        startThread.start();
    }

    public static void stopRecord() {
        stopThread = new stopStream();
        stopThread.start();
    }

    static {
        InitializeResponseObserver();
        textOfSpeech = new SimpleStringProperty();
        isDone = new SimpleBooleanProperty(false);
        isPaused = new SimpleBooleanProperty(false);
        isStart = new SimpleBooleanProperty(false);
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
            System.out.println(SpeechToText.getTextOfSpeech());
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}