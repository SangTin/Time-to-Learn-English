package api;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.translate.v3.TranslationServiceSettings;

import java.io.IOException;

public class CONST {
    static final String PROJECT_ID = "dictionary-403316";
    static final FixedCredentialsProvider CREDENTIALS_PROVIDER;
    static {
        try {
            CREDENTIALS_PROVIDER = FixedCredentialsProvider
                    .create(ServiceAccountCredentials
                            .fromStream(TextToSpeech.class.getResourceAsStream("/json/client_secret.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static final TranslationServiceSettings TRANSLATION_SERVICE_SETTINGS;
    static {
        try {
            TRANSLATION_SERVICE_SETTINGS = TranslationServiceSettings
                    .newBuilder().setCredentialsProvider(CONST.CREDENTIALS_PROVIDER).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static final TranslationServiceClient TRANSLATION_SERVICE_CLIENT;
    static {
        try {
            TRANSLATION_SERVICE_CLIENT = TranslationServiceClient
                    .create(CONST.TRANSLATION_SERVICE_SETTINGS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static final TextToSpeechSettings TEXT_TO_SPEECH_SETTINGS;
    static {
        try {
            TEXT_TO_SPEECH_SETTINGS = TextToSpeechSettings
                    .newBuilder().setCredentialsProvider(CREDENTIALS_PROVIDER).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static final TextToSpeechClient TEXT_TO_SPEECH_CLIENT;

    static {
        try {
            TEXT_TO_SPEECH_CLIENT = TextToSpeechClient.create(CONST.TEXT_TO_SPEECH_SETTINGS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static final SpeechSettings SPEECH_SETTINGS;
    static {
        try {
            SPEECH_SETTINGS = SpeechSettings.newBuilder().setCredentialsProvider(CREDENTIALS_PROVIDER).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static final SpeechClient SPEECH_CLIENT;

    static {
        try {
            SPEECH_CLIENT = SpeechClient.create(CONST.SPEECH_SETTINGS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
