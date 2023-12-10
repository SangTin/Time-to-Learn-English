package gui.components;

import data.Dictionary;
import data.dictionary.Word;
import data.enums.AppFunction;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

import java.util.Timer;

public abstract class SearchBase extends AnchorPane {
    protected static final String MICROPHONE_ICON = "M12 2a3 3 0 0 0-3 3v7a3 3 0 1 0 6 0V5a3 3 0 0 0-3-3zM5 12a1 1 0 1 1 2 0 5 5 0 0 0 10 0 1 1 0 1 1 2 0 7.001 7.001 0 0 1-6 6.93V21a1 1 0 1 1-2 0v-2.07A7.001 7.001 0 0 1 5 12z";
    protected static final String CANCEL_ICON = "M542.142 25.467 547.8 31.125 553.458 25.467C553.93 24.995 554.678 24.951 555.237 25.387L555.333 25.475C555.849 25.991 555.849 26.826 555.333 27.342L549.675 33 555.333 38.658C555.805 39.13 555.849 39.878 555.413 40.437L555.325 40.533C554.809 41.049 553.974 41.049 553.458 40.533L547.8 34.875 542.142 40.533C541.67 41.005 540.922 41.049 540.363 40.613L540.267 40.525C539.751 40.009 539.751 39.174 540.267 38.658L545.925 33 540.267 27.342C539.795 26.87 539.751 26.122 540.187 25.563L540.275 25.467C540.791 24.951 541.626 24.951 542.142 25.467ZM541.219 40.26C541.215 40.26 541.212 40.26 541.208 40.26ZM541.307 40.253 541.278 40.256 541.278 40.256C541.287 40.255 541.297 40.254 541.307 40.253ZM541.111 40.253 541.123 40.254C541.119 40.254 541.115 40.253 541.111 40.253L541.111 40.253ZM541.028 40.235 541.038 40.238C541.035 40.237 541.031 40.236 541.028 40.235L541.028 40.235ZM541.39 40.235 541.355 40.244 541.355 40.244C541.367 40.241 541.379 40.238 541.39 40.235ZM541.465 40.208 541.438 40.219 541.438 40.219C541.447 40.216 541.456 40.212 541.465 40.208ZM540.952 40.208C540.955 40.21 540.953 40.209 540.952 40.208L540.952 40.208ZM540.874 40.169 540.804 40.122C540.833 40.144 540.864 40.164 540.896 40.182L540.874 40.169 540.874 40.169ZM541.542 40.169 541.521 40.181 541.521 40.181C541.528 40.178 541.535 40.174 541.542 40.169ZM554.982 39.904 554.969 39.926 554.969 39.926 554.922 39.996C554.944 39.967 554.964 39.936 554.982 39.904ZM555.018 39.823 555.008 39.848 555.008 39.848C555.012 39.84 555.015 39.832 555.018 39.823ZM555.043 39.739 555.035 39.772 555.035 39.772C555.038 39.761 555.041 39.75 555.043 39.739ZM555.056 39.661 555.053 39.689 555.053 39.689C555.054 39.68 555.055 39.671 555.056 39.661ZM554.392 26.4 547.8 32.992 541.208 26.4 541.2 26.408 547.792 33 541.2 39.592 541.208 39.6 547.8 33.008 554.392 39.6 554.4 39.592 547.808 33 554.4 26.408 554.392 26.4ZM555.06 39.581 555.06 39.6 555.06 39.6C555.06 39.594 555.06 39.587 555.06 39.581ZM555.053 39.493 555.056 39.522 555.056 39.522C555.055 39.513 555.054 39.503 555.053 39.493ZM555.035 39.41 555.044 39.445 555.044 39.445C555.041 39.433 555.038 39.421 555.035 39.41ZM555.008 39.335 555.019 39.362 555.019 39.362C555.016 39.353 555.012 39.344 555.008 39.335ZM554.969 39.258 554.981 39.279 554.981 39.279C554.978 39.272 554.974 39.265 554.969 39.258ZM540.619 26.721 540.629 26.739C540.625 26.733 540.622 26.727 540.618 26.721L540.619 26.721ZM540.581 26.638 540.59 26.66C540.587 26.653 540.584 26.645 540.581 26.637L540.581 26.638ZM540.556 26.555 540.562 26.578C540.56 26.57 540.558 26.563 540.556 26.555L540.556 26.555ZM540.543 26.475 540.546 26.494C540.545 26.488 540.544 26.481 540.543 26.475L540.543 26.475ZM540.54 26.389 540.54 26.408C540.54 26.402 540.54 26.396 540.54 26.389L540.54 26.389ZM540.547 26.311 540.546 26.323C540.546 26.319 540.547 26.315 540.547 26.311L540.547 26.311ZM540.565 26.228 540.562 26.238C540.563 26.235 540.564 26.231 540.565 26.228L540.565 26.228ZM540.592 26.152C540.59 26.155 540.591 26.153 540.592 26.152L540.592 26.152ZM540.631 26.074 540.678 26.004C540.656 26.033 540.636 26.064 540.618 26.096L540.631 26.074 540.631 26.074ZM541.521 25.818 541.543 25.831 541.543 25.831 541.613 25.878C541.583 25.856 541.553 25.836 541.521 25.818ZM554.079 25.819 554.061 25.829C554.067 25.825 554.073 25.822 554.079 25.818L554.079 25.819ZM540.896 25.819 540.878 25.829C540.884 25.825 540.89 25.822 540.896 25.818L540.896 25.819ZM554.704 25.818 554.726 25.831 554.726 25.831 554.796 25.878C554.767 25.856 554.736 25.836 554.704 25.818ZM540.977 25.782 540.956 25.79C540.963 25.787 540.97 25.784 540.977 25.782L540.977 25.782ZM541.437 25.781 541.465 25.792 541.465 25.792C541.456 25.788 541.447 25.784 541.437 25.781ZM554.162 25.781 554.14 25.79C554.147 25.787 554.155 25.784 554.163 25.781L554.162 25.781ZM554.623 25.782 554.648 25.792 554.648 25.792C554.64 25.788 554.632 25.785 554.623 25.782ZM541.061 25.757 541.038 25.762C541.046 25.76 541.053 25.758 541.061 25.757L541.061 25.757ZM541.355 25.756 541.39 25.765 541.39 25.765C541.379 25.762 541.367 25.759 541.355 25.756ZM554.245 25.756 554.222 25.762C554.23 25.76 554.237 25.758 554.245 25.756L554.245 25.756ZM554.539 25.757 554.572 25.765 554.572 25.765C554.561 25.762 554.55 25.759 554.539 25.757ZM541.142 25.743 541.123 25.746C541.129 25.745 541.135 25.744 541.141 25.743L541.142 25.743ZM541.275 25.743 541.306 25.747 541.306 25.747C541.296 25.746 541.285 25.744 541.275 25.743ZM554.325 25.743 554.306 25.746C554.312 25.745 554.319 25.744 554.325 25.743L554.325 25.743ZM554.459 25.743 554.489 25.747 554.489 25.747C554.479 25.746 554.469 25.744 554.459 25.743ZM554.373 25.74 554.411 25.74C554.398 25.74 554.385 25.74 554.373 25.74ZM541.189 25.74 541.227 25.74C541.215 25.74 541.202 25.74 541.189 25.74Z";
    protected static final double SUGGESTION_CELL_HEIGHT = 40.0D;

    @FXML protected TextField searchText;
    @FXML protected Button searchBarButton;
    @FXML protected SVGPath searchBarIcon;
    @FXML protected ListView<Word> searchResult;

    protected final SimpleObjectProperty<Word> selectedWord = new SimpleObjectProperty<>();
    protected AppFunction appFunction = null;
    protected Dictionary dictionary;
    protected Timer createWordTimer = new Timer();

    protected static void setSearchButton(Button searchBarButton, TextField searchText) {
        searchBarButton.setOnAction((e) -> {
            if (!searchText.getText().isEmpty()) {
                searchText.clear();
            } else {
                VoiceInput voiceInput = new VoiceInput();
                voiceInput.setOnHidden((event) -> {
                    searchText.setText(voiceInput.getTextResult().trim());
                    searchText.requestFocus();
                });
                voiceInput.showAndWait();
            }
        });
    }

    public void initialize() {
        // Set up search bar
        setSearchButton(this.searchBarButton, this.searchText);
        this.searchText.textProperty().addListener((observable, oldVal, newVal) -> {
            appFunction = AppFunction.SEARCH;
            createWordTimer.cancel();
            createWordTimer.purge();

            if (newVal != null && !newVal.isEmpty()) {
                this.search();
                this.searchBarIcon.setContent(CANCEL_ICON);
                this.searchBarButton.getTooltip().setText("Clear the text");
            } else {
                this.history();
                this.searchBarIcon.setContent(MICROPHONE_ICON);
                this.searchBarButton.getTooltip().setText("Input from microphone");
            }
        });

        // Set up search result list view
        setResultList(this.searchResult);
        this.searchResult.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null) {
                this.dictionary.getHistorySearch().add(newVal);
                this.selectedWord.set(newVal);
            }
        });
    }


    protected static void setResultList(ListView<Word> listView) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setFixedCellSize(SUGGESTION_CELL_HEIGHT);
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public SimpleObjectProperty<Word> selectedWordProperty() {
        return this.selectedWord;
    }

    public Word getSelectedWord() {
        return this.selectedWord.get();
    }

    public AppFunction getAppFunction() {
        return appFunction;
    }

    protected abstract void search();

    protected abstract void history();

    protected static abstract class SearchCell extends ListCell<Word> {
        protected static final String MAGNIFYING_GLASS_ICON = "M10 10m-7 0a7 7 0 1 0 14 0a7 7 0 1 0 -14 0 M21 21l-6 -6";
        protected static final String HISTORY_ICON = "M12 8l0 4l2 2 M3.05 11a9 9 0 1 1 .5 4m-.5 5v-5h5";
        protected static final String CREATE_ICON = "M3 12a9 9 0 1 0 18 0a9 9 0 0 0 -18 0 M9 12h6 M12 9v6";
        protected static final String DELETE_ICON = "M17.82 17.82 11.88 11.88M11.88 11.88 5.94 5.94M11.88 11.88 17.82 5.94M11.88 11.88 5.94 17.82";
        protected static final String PASTE_ICON = "M17.55 17.55l-13.5-13.5m0 0v10.125m0-10.125h10.125";

        protected final HBox row = new HBox();
        protected final TextField word = new TextField();

        protected static Button createButton(String icon, String tooltip, Runnable action) {
            SVGPath svgIcon = new SVGPath();
            svgIcon.getStyleClass().add("cell-icon");
            svgIcon.setContent(icon);
            Button button = new Button();
            button.getStyleClass().add("cell-button");
            button.setGraphic(svgIcon);
            button.setTooltip(new Tooltip(tooltip));
            button.setOnAction((e) -> {
                action.run();
            });
            return button;
        }

        protected void updateItem(Word word, boolean empty) {
            super.updateItem(word, empty);
            this.setText(null);
            this.setGraphic(null);
            if (word != null && !empty) {
                this.word.setText(word.getWordTarget());
                this.setGraphic(this.row);
            }
        }

    }
}
