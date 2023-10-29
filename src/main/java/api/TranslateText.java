package api;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.FileInputStream;
import java.io.IOException;

public class TranslateText {
    private static final Translate translate;

    static {
        try {
            translate = TranslateOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials
                            .fromStream(new FileInputStream("src/main/resources/json/client_secret.json")))
                    .build().getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String detectText(String text) {
        Translation translation = translate.translate(text);
        return translation.getTranslatedText();
    }
}
