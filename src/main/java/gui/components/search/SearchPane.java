package gui.components.search;

import data.Dictionary;
import data.dictionary.Word;
import data.enums.AppFunction;
import data.enums.SearchResultType;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.Timer;

public class SearchPane extends SearchBase {
   @FXML private ListView<Word> historyResult;
   @FXML private ListView<Word> favouriteResult;

   public SearchPane() {
      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/components/search/SearchPane.fxml"));
         loader.setController(this);
         loader.setRoot(this);
         loader.load();
      } catch (Exception e) {
         System.out.println("Error loading SearchPane.fxml");
      }

   }

   public void initialize() {
      super.initialize();
   }

   protected void doInitialize() {
      super.doInitialize();
      setRecentSearch(this.searchResult, SearchResultType.SUGGESTION);
      setRecentSearch(this.favouriteResult, SearchResultType.FAVOURITE);
      setRecentSearch(this.historyResult, SearchResultType.HISTORY);

      Platform.runLater(this::search);
   }

   private void setRecentSearch(ListView<Word> listView, SearchResultType type) {
      setResultList(listView);
      listView.setCellFactory((param) -> new SuggestionCell(type));
   }

   public void setDictionary(Dictionary dictionary) {
      super.setDictionary(dictionary);

      this.historyResult.setItems(this.dictionary.getHistorySearch().getWords());
      this.favouriteResult.setItems(this.dictionary.getFavouriteSearch());
   }

   protected void search() {
      ObservableList<Word> result = this.dictionary.search(this.searchText.getText(), -1);
      this.searchResult.setItems(result);
      this.searchResult.setCellFactory((param) -> new SuggestionCell(SearchResultType.SUGGESTION));
      if (result.isEmpty()) {
         createWordTimer = new Timer();
         createWordTimer.schedule(new SearchBar.CreateWordTask(() -> {
            this.searchResult.setCellFactory((param) -> new SuggestionCell(SearchResultType.CREATE));
         }), 1000);
      }
   }

   protected void history() {
      search();
   }

   private class SuggestionCell extends SearchCell {
      public SuggestionCell(SearchResultType type) {
         this.word.setEditable(false);
         this.word.getStyleClass().add("cell-word");
         this.word.mouseTransparentProperty().set(true);
         HBox.setHgrow(this.word, Priority.ALWAYS);

         this.row.setAlignment(Pos.CENTER_LEFT);
         this.row.setSpacing(10.0D);
         this.row.setMaxHeight(SUGGESTION_CELL_HEIGHT);
         this.row.setPrefWidth(SearchPane.this.searchResult.getWidth() - 20.0D);

         Button button = switch (type) {
            case FAVOURITE -> createButton(DELETE_ICON, "Delete from favourite", () -> {
               dictionary.removeFavourite(getItem());
            });
            case HISTORY -> createButton(DELETE_ICON, "Delete from history", () -> {
               dictionary.removeHistory(getItem());
            });
            case SUGGESTION -> createButton(PASTE_ICON, "Paste to search bar", () -> {
               SearchPane.this.searchText.setText(getItem().getWordTarget());
            });
            case CREATE -> createButton(CREATE_ICON, "Create new word", () -> {
               SearchPane.this.searchText.setText(getItem().getWordTarget());
            });
         };
         this.row.getChildren().addAll(this.word, button);
         switch (type) {
            case SUGGESTION: {
               this.selectedProperty().addListener((observable, oldVal, newVal) -> {
                  Platform.runLater(() -> {
                     SearchPane.this.dictionary.addHistory(getItem());
                  });
               });
            }
            case HISTORY:
            case FAVOURITE: {
               this.selectedProperty().addListener((observable, oldVal, newVal) -> {
                  appFunction = AppFunction.SEARCH;
                  selectedWord.set(this.getItem());
               });
               break;
            }
            case CREATE: {
               this.selectedProperty().addListener((observable, oldVal, newVal) -> {
                  appFunction = AppFunction.ADD;
                  selectedWord.set(this.getItem());
               });
               break;
            }
         }
      }
   }
}
