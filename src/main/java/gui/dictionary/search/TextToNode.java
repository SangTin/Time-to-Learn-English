package gui.dictionary.search;

import data.Dictionary;
import data.dictionary.Word;
import data.enums.AppFunction;
import exception.editWord.NoSuchWordFoundException;
import gui.GraphicalDictionary;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class TextToNode {
    private static final String[] splitWordRegex = {"(?=\\W)[^'-]", "(?=\\W)[^'-]|'(.*)", "(?=\\W)[^-]", "(?=\\W)[^-](.*)"};

    public static String extractWord(String text, Dictionary dictionary) {
        if (dictionary == null) return null;
        text = text.replaceAll("_|\\s", " ");
        if (text.isEmpty()) return null;
        if (dictionary.have(text)) {
            return text;
        }

        for (String regex : splitWordRegex) {
            String tmp = text.replaceAll(regex, "");
            if (dictionary.have(tmp)) {
                return tmp;
            }
        }
        return null;
    }

    public static Node textToLink(String text, Dictionary dictionary) {
        if (text == null) return null;

        Node engWord = null;
        String wordTarget = extractWord(text, dictionary);
        if (wordTarget != null) {
            Hyperlink wordLink = new Hyperlink(text);
            wordLink.setOnAction(e -> {
                try {
                    Word newWord = dictionary.searchExactly(wordTarget);
                    GraphicalDictionary.appFunctionProperty().set(new Pair<>(AppFunction.SEARCH, newWord));
                } catch (NoSuchWordFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            });
            engWord = wordLink;
        } else {
            engWord = new Text(text);
        }
        return engWord;
    }
}