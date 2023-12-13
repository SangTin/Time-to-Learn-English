package api;

// Imports the Google Cloud Translation library.

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;

import java.io.IOException;

public class TranslateText {

    /**
     * Translate source text into target language.
     *
     * @param sourceLanguage - language that the source text is.
     * @param targetLanguage - language that the source text is translated into.
     *                       further information about supported languages: <a href="https://cloud.google.com/translate/docs/languages">...</a>
     * @param text           - source text needed to be translated.
     * @return The text after translate to target language.
     */
    public static String translateText(String sourceLanguage, String targetLanguage, String text)
            throws IOException {

        //Supported location: global.
        LocationName parent = LocationName.of(CONST.PROJECT_ID, "global");

        //Initialize a request.

        TranslateTextRequest request =
                TranslateTextRequest.newBuilder()
                        .setParent(parent.toString())
                        .setMimeType("text/plain")
                        .setSourceLanguageCode(sourceLanguage)
                        .setTargetLanguageCode(targetLanguage)
                        .addContents(text)
                        .build();

        TranslateTextResponse response = CONST.TRANSLATION_SERVICE_CLIENT.translateText(request);

        return response.getTranslationsList().get(0).getTranslatedText();
    }
}