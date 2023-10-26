package gui.dictionary.content;

import data.Dictionary;
import data.Word;
import gui.dictionary.Content;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;

public class TextToNode {
    private static final String[] splitWordRegex = {"", "(?=\\W)[^'-]", "(?=\\W)[^'-]|'(.*)", "(?=\\W)[^-]", "(?=\\W)[^-](.*)"};
    
    public static Node textToLink(String text, Dictionary dictionary, Content owner) {
        if (text == null) return null;

        text = text.replaceAll("_|\\s", " ");
        if (text.isEmpty()) return null;

        String real = null;
        for (String regex : splitWordRegex) {
            String tmp = text.replaceAll(regex, "");
            if (dictionary.have(tmp)) {
                real = tmp;
                break;
            }
        }


        Node engWord = null;
        String wordTarget = real;
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
