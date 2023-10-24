package gui.dictionary;

import java.util.Stack;

import data.Dictionary;
import data.Word;
import gui.dictionary.content.Description;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class Content extends AnchorPane {
    private static final String CONTENT_FXML = "/fxml/dictionary/Content.fxml";

    @FXML private Description engViet;
    @FXML private Button closeButton;
    @FXML private Button previousWord;
    @FXML private Button nextWord;

    Dictionary dictionary;
    Stack<Word> previous = new Stack<>();
    Stack<Word> next = new Stack<>();

    public Content() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTENT_FXML));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        engViet.setOwner(this);
        reset();

        previousWord.setOnAction(e -> back());
        nextWord.setOnAction(e -> forward());
        closeButton.setOnAction(e -> close());
    }

    public void reset() {
        setVisible(true);
        previous.clear();
        next.clear();
        engViet.clear();

        previousWord.setDisable(true);
        nextWord.setDisable(true);
    }

    public void back() {
        if (!canBack()) return;
        next.push(previous.pop());
        replay(previous.peek());
    }

    private boolean canBack() {
        return previous.size() > 1;
    }

    public void forward() {
        if (!canForward()) return;
        previous.push(next.pop());
        replay(previous.peek());
    }

    private boolean canForward() {
        return !next.isEmpty();
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
        engViet.setDictionary(dictionary);
    }

    private void replay(Word word) {
        // Set button state
        if (canBack()) {
            previousWord.setDisable(false);
        } else {
            previousWord.setDisable(true);
        }
        if (canForward()) {
            nextWord.setDisable(false);
        } else {
            nextWord.setDisable(true);
        }

        engViet.setWord(word);
    }

    public void display(Word word) {
        if (previous.isEmpty() || !previous.peek().equals(word)) {
            previous.push(word);
            next.clear();
            replay(word);
        }
    }

    private void close() {
        setVisible(false);
    }
}
