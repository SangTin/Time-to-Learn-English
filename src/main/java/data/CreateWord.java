package data;

import api.TranslateText;
import data.enums.PartOfSpeech;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

public class CreateWord {
   private static final String baseLink = "https://www.oxfordlearnersdictionaries.com/definition/english/";

   private static void getDefinition(Element meaning, String word, StringBuilder description) throws IOException {
      String enMeaning = meaning.getElementsByClass("def").text().trim();
      String viMeaning = TranslateText.translateText("en", "vi", enMeaning);
      description.append(String.format("- %s\n", viMeaning));

      try {
         Element example = meaning.getElementsByClass("examples").getFirst();
         for (Element ex : example.getElementsByTag("li")) {
            try {
               Element grammar = ex.getElementsByClass("cf").getFirst();
               String enExample = "> ";
               String enGrammar = grammar.text();
               if (enGrammar.startsWith(word)) {
                   enGrammar = enGrammar.replace(word, "+");
               }

               enExample = enExample + String.format("( %s ) ", enGrammar);
               String enExampleText = ex.getElementsByClass("x").text().trim();
               enExample = enExample + enExampleText;
               description.append(String.format("%s\n", enExample));
               String viExample = "= " + TranslateText.translateText("en", "vi", enExampleText);
               description.append(String.format("%s\n", viExample));
           } catch (Exception ignored) {}
          }
      } catch (Exception ignored) {}
   }

   public static Word createWord(String word) {
      word = word.replaceAll("_", " ");
      if (!checkAvailable(word)) {
         return null;
      }
      Word newWord = new Word(word, null);
      word = word.replaceAll("\\s", "-");
      StringBuilder description = new StringBuilder();

      for(int i = 1; i < 10; ++i) {
         try {
            Document doc = Jsoup.connect(baseLink + word + "_" + i).get();
            Element topDef = doc.getElementsByClass("webtop").getFirst();
            PartOfSpeech pos = PartOfSpeech.fromEnglish(topDef.getElementsByClass("pos").text());
            if (pos == null) {
               break;
            }

            description.append(String.format("* %s\n", pos.toVietnamese()));

            try {
               String ukPhon = topDef.getElementsByClass("phons_br").getFirst().text();
               String usPhon = topDef.getElementsByClass("phons_n_am").getFirst().text();
               if (i == 1) {
                  newWord.setUkPron(ukPhon);
                  newWord.setUsPron(usPhon);
               } else {
                  if (!ukPhon.equals(newWord.getUkPron())) {
                     description.append(String.format("= UK: %s\n", ukPhon));
                  }

                  if (!usPhon.equals(newWord.getUsPron())) {
                     description.append(String.format("= US: %s\n", usPhon));
                  }
               }
            } catch (Exception var12) {
            }

            Element definition = doc.getElementsByTag("ol").getFirst();
            Iterator var15 = definition.getElementsByClass("sense").iterator();

            while(var15.hasNext()) {
               Element meaning = (Element)var15.next();
               getDefinition(meaning, word, description);
            }
         } catch (Exception var13) {
            break;
         }
      }

      newWord.setWordExplain(description.toString());
      return newWord;
   }
   public static boolean checkAvailable(String word) {
      if (word != null && !(word = word.trim()).isEmpty()) {
         Pattern pattern = Pattern.compile(".*[\\W&&[^-]&&\\S].*", 2);
         if (pattern.matcher(word).find()) {
            return false;
         } else {
            word = word.replaceAll("\\s", "-");
            try {
               Jsoup.connect(baseLink + word).get();
            } catch (Exception var5) {
               return false;
            }
            return true;
         }
      } else {
         return false;
      }
   }
}
