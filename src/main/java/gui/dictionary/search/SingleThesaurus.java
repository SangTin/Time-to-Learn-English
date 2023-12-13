package gui.dictionary.search;

import data.Thesaurus;
import data.dictionary.Word;
import data.enums.AppFunction;
import gui.GraphicalDictionary;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static gui.dictionary.edit.SingleThesaurus.createWordButton;


public class SingleThesaurus extends VBox {
    @FXML private Text meaning;
    @FXML private Text partOfSpeech;
    @FXML private Text thesaurusType;
    @FXML private FlowPane wordFlow;

    private final Thesaurus thesaurus;

    public SingleThesaurus(Thesaurus thesaurus) throws NullPointerException {
        super();
        if (thesaurus == null) {
            throw new NullPointerException();
        }
        if ((thesaurus.getMostUsedWords().isEmpty() && thesaurus.getLessUsedWords().isEmpty())) {
            throw new NullPointerException();
        }
        this.thesaurus = thesaurus;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dictionary/search/SingleThesaurus.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        meaning.setText(thesaurus.getMeaning());
//        partOfSpeech.setText(thesaurus.getPartOfSpeech().toEnglish());
        thesaurusType.setText(thesaurus.getType().toString() + "s:");

        for (Word word : thesaurus.getMostUsedWords()) {
            wordFlow.getChildren().add(createWordButton(word, 0, () ->
                    GraphicalDictionary.setAppFunction(AppFunction.SEARCH, word)));
        }
        for (Word word : thesaurus.getLessUsedWords()) {
            wordFlow.getChildren().add(createWordButton(word, 1, () ->
                    GraphicalDictionary.setAppFunction(AppFunction.SEARCH, word)));
        }
    }
}
