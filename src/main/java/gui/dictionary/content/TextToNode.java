package gui.dictionary.content;

import data.Dictionary;
import data.Word;
import gui.dictionary.Content;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;

public class TextToNode {
    private static final String[] splitWordRegex = {"", "(?=\\W)[^'-]", "(?=\\W)[^'-]|'(.*)", "(?=\\W)[^-]", "(?=\\W)[^-](.*)"};
    
    public static String extractWord(String text, Dictionary dictionary) {
        text = text.replaceAll("_|\\s", " ");
        if (text.isEmpty()) return null;

        for (String regex : splitWordRegex) {
            String tmp = text.replaceAll(regex, "");
            if (dictionary.have(tmp)) {
                return tmp;
            }
        }
        return null;
    }

    public static Node textToLink(String text, Dictionary dictionary, Content owner) {
        if (text == null) return null;

        Node engWord = null;
        String wordTarget = extractWord(text, dictionary);
        if (wordTarget != null) {
            Hyperlink wordLink = new Hyperlink(text);
            wordLink.setOnAction(e -> {
                Word newWord = dictionary.searchExactly(wordTarget);
                owner.display(newWord);
            });
            engWord = wordLink;
        } else {
            Text wordText = new Text(text);
            engWord = wordText;
        }
        return engWord;
    }
}
