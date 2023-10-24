package gui.dictionary;

import java.io.IOException;
import java.util.ArrayList;

import data.Dictionary;
import data.Word;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;

public class SearchMenu extends AnchorPane {
    private static final String SEARCH_MENU_FXML = "/fxml/dictionary/SearchMenu.fxml";
    private static final String CLIPBOARD_ICON = "M9 5h-2a2 2 0 0 0 -2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-12a2 2 0 0 0 -2 -2h-2 M9 3m0 2a2 2 0 0 1 2 -2h2a2 2 0 0 1 2 2v0a2 2 0 0 1 -2 2h-2a2 2 0 0 1 -2 -2z M9 12h6 M9 16h6";
    private static final String CANCEL_ICON = "M18 6l-12 12 M6 6l12 12";
    private static final double SUGGESTION_CELL_HEIGHT = 40;

    @FXML private TextField searchText;
    @FXML private AnchorPane searchPane;
    @FXML private Button searchButton;
    @FXML private SVGPath searchBarIcon;
    @FXML private ListView<Word> searchResult;

    private Dictionary dictionary;
    private Content content;
    private ArrayList<Word> historyWords;
    
    private static class SuggestionCell extends ListCell<Word> {
        private static final String MAGNIFYING_GLASS_ICON = "M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z";
        private static final String HISTORY_ICON = "M12 8l0 4l2 2 M3.05 11a9 9 0 1 1 .5 4m-.5 5v-5h5";
        private static final String PASTE_ICON = "M19.5 19.5l-15-15m0 0v11.25m0-11.25h11.25";

        public static enum CellType {
            HISTORY, SUGGESTION
        }

        private AnchorPane row = new AnchorPane();
        private SVGPath icon = new SVGPath();
        private TextField word = new TextField();
        private SVGPath pasteIcon = new SVGPath();
        private Button pasteButton = new Button();

        public SuggestionCell(CellType type) {
            super();
            
            word.setEditable(false);
            word.getStyleClass().add("cell-word");
            word.mouseTransparentProperty().set(true);
            AnchorPane.setLeftAnchor(word, 30.0);
            double wordCenter = (row.getHeight() - word.getHeight()) / 2;
            AnchorPane.setTopAnchor(word, wordCenter);
            AnchorPane.setBottomAnchor(word, wordCenter);

            icon.setFill(javafx.scene.paint.Color.TRANSPARENT);
            icon.setStroke(javafx.scene.paint.Color.BLACK);
            icon.setStrokeWidth(2);
            switch (type) {
                case HISTORY:
                    icon.setContent(HISTORY_ICON);
                    break;
                case SUGGESTION:
                    icon.setContent(MAGNIFYING_GLASS_ICON);
                    break;
            }
            AnchorPane.setLeftAnchor(icon, 0.0);
            AnchorPane.setTopAnchor(icon, 10.0);
            AnchorPane.setBottomAnchor(icon, 10.0);

            pasteIcon.setFill(javafx.scene.paint.Color.TRANSPARENT);
            pasteIcon.setStroke(javafx.scene.paint.Color.BLACK);
            pasteIcon.setStrokeWidth(2);
            pasteIcon.setContent(PASTE_ICON);
            
            pasteButton.getStyleClass().add("cell-paste-button");
            pasteButton.setMinHeight(SUGGESTION_CELL_HEIGHT);
            pasteButton.setPickOnBounds(true);
            pasteButton.setGraphic(pasteIcon);
            pasteButton.setOnAction(e -> {
                TextField textField = (TextField) getListView().getScene().lookup("#searchText");
                textField.setText(word.getText());
            });
            AnchorPane.setRightAnchor(pasteButton, -6.5);

            row.setMinHeight(SUGGESTION_CELL_HEIGHT);
            row.getChildren().addAll(icon, word, pasteButton);
        }

        @Override
        protected void updateItem(Word word, boolean empty) {
            super.updateItem(word, empty);
            setText(null);
            setGraphic(null);

            if (word != null && !empty) {
                this.word.setText(word.getWordTarget());
                setGraphic(row);
            }
        }
    }

    public SearchMenu() {
        historyWords = new ArrayList<>();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(SEARCH_MENU_FXML));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // Set the search bar button's function and tooltip
        searchButton.setOnAction(e -> {
            if (searchText.getText() == null || searchText.getText().isEmpty()) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                searchText.setText(clipboard.getString());
            } else {
                searchText.clear();
            }
            searchText.requestFocus();
        });

        // Set the icon of the search bar
        searchText.textProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                history();
                searchBarIcon.setContent(CLIPBOARD_ICON);
                searchButton.getTooltip().setText("Paste from the clipboard");
            } else {
                search();
                searchBarIcon.setContent(CANCEL_ICON);
                searchButton.getTooltip().setText("Clear the text");
            }
        });

        // Set the visibility of the search result list
        searchText.focusedProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal || searchResult.focusedProperty().get()) {
                searchResult.visibleProperty().set(true);
            } else {
                searchResult.visibleProperty().set(false);
            }
        });
        searchResult.focusedProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal || searchText.focusedProperty().get()) {
                searchResult.visibleProperty().set(true);
            } else {
                searchResult.visibleProperty().set(false);
            }
        });
        
        // Set the height of the search result list
        searchResult.setFixedCellSize(SUGGESTION_CELL_HEIGHT);
        searchResult.getItems().addListener(new ListChangeListener<Word>() {
            @Override
            public void onChanged(Change<? extends Word> c) {
                searchResult.setPrefHeight(SUGGESTION_CELL_HEIGHT * searchResult.getItems().size());
            }
        });

        // Set the search result list's function
        searchResult.setOnMouseClicked(e -> {
            Word word = searchResult.getSelectionModel().getSelectedItem();
            if (word != null) {
                content.display(word);
            }
        });
    }

    public void reset() {
        searchResult.getItems().clear();
        searchText.clear();
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void setContent(Content content) {
        this.content = content;
        content.setDictionary(dictionary);
    }

    private void search() {
        Word[] result = dictionary.search(searchText.getText().trim());
        searchResult.getItems().setAll(result);
        searchResult.setCellFactory(param -> new SuggestionCell(SuggestionCell.CellType.SUGGESTION));
    }

    private void history() {
        searchResult.getItems().setAll(historyWords);
        searchResult.setCellFactory(param -> new SuggestionCell(SuggestionCell.CellType.HISTORY));
    }
}
