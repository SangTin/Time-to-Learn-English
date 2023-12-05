package data;


import data.dictionary.Word;
import data.enums.PartOfSpeech;
import data.enums.ThesaurusType;
import me.tongfei.progressbar.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class GetThesaurus {
    static ProgressBar pb;
    private static final String url = "D:\\SangTin\\Java\\Data\\";
    private static final SQLiteDatabase database;
    private static final Dictionary dictionary;

    static {
        database = new SQLiteDatabase("dictionary.db");
        dictionary = new Dictionary(database);
        database.importToDictionary(dictionary);
    }
    public static void get(Word input) throws IOException {
        pb.step();
        String word = input.getWordTarget();
        File file = new File(url + word + ".txt");
        if (!file.exists()) {
            return;
        }
        Document doc = Jsoup.parse(file, "UTF-8");
        try {
            for (Element mean : doc.getElementsByClass("text-black border-1 border-blueGray-75 rounded-8 p-4 sm:p-5 racking-0 mt-3 sm:mt-4")) {
                String meaning = "";
                try {
                    meaning = mean.getElementsByClass("sm:text-xl sm:leading-8 text-base max-w-712").first().text();
                } catch (Exception ignored) {
                }

                String pos = "";
                try {
                    pos = mean.getElementsByClass("text-sm sm:text-base mt-0.5 sm:mt-1.75 italic lowercase").first().text();
                } catch (Exception ignored) {
                    continue;
                }
                //======================== Get synonyms ========================//
                Thesaurus thesaurus =
                        new Thesaurus(meaning, dictionary, PartOfSpeech.fromString(pos), ThesaurusType.SYNONYM);
                //Get most common synonyms
                for (Element synonym : mean.getElementsByClass("border-synonym-rel-level-0")) {
                    String syn = synonym.text();
                    try {
                        thesaurus.addMostUsedByWord(syn);
                    } catch (Exception e) {
//                        System.out.println("Not found: " + syn);
                    }
                }
                //Get less common synonyms
                for (int i = 1; i < 5; ++i) {
                    for (Element synonym : mean.getElementsByClass("border-synonym-rel-level-" + i)) {
                        String syn = synonym.text();
                        try {
                            thesaurus.addLessUsedByWord(syn);
                        } catch (Exception e) {
//                            System.out.println("Not found: " + syn);
                        }
                    }
                }
                database.addThesaurus(input, thesaurus);

                //======================== Get antonyms ========================//
                thesaurus =
                        new Thesaurus(meaning, dictionary, PartOfSpeech.fromString(pos), ThesaurusType.ANTONYM);
                //Get most common antonyms
                for (Element antonym : mean.getElementsByClass("border-antonym-rel-level-0")) {
                    String ant = antonym.text();
                    try {
                        thesaurus.addMostUsedByWord(ant);
                    } catch (Exception e) {
//                        System.out.println("Not found: " + ant);
                    }
                }
                //Get less common antonyms
                for (int i = 1; i < 5; ++i) {
                    for (Element antonym : mean.getElementsByClass("border-antonym-rel-level-" + i)) {
                        String ant = antonym.text();
                        try {
                            thesaurus.addLessUsedByWord(ant);
                        } catch (Exception e) {
//                            System.out.println("Not found: " + ant);
                        }
                    }
                }
                database.addThesaurus(input, thesaurus);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Word: " + word);
        }
    }
}
