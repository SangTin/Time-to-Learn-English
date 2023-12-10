package gui.components.search;

import data.Dictionary;
import data.dictionary.Word;
import data.enums.SearchResultType;
import gui.components.SearchBase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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
      setRecentSearch(this.searchResult, SearchResultType.SUGGESTION);
      setRecentSearch(this.favouriteResult, SearchResultType.FAVOURITE);
      setRecentSearch(this.historyResult, SearchResultType.HISTORY);

      Platform.runLater(this::search);
   }

   private void setRecentSearch(ListView<Word> listView, SearchResultType type) {
      setResultList(listView);
      MultipleSelectionModel<Word> selectionModel = listView.getSelectionModel();
      listView.setCellFactory((param) -> {
         SearchCell cell = new SuggestionCell(type);
         cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            listView.requestFocus();
            if (!cell.isEmpty()) {
               selectionModel.clearSelection();
            }
         });
         return cell ;
      });
   }

   public void setDictionary(Dictionary dictionary) {
      super.setDictionary(dictionary);

      this.historyResult.setItems(this.dictionary.getHistorySearch().getWords());
      this.favouriteResult.setItems(this.dictionary.getFavouriteSearch());
   }

   protected void search() {
      int limit = (int) (searchResult.getHeight() / SUGGESTION_CELL_HEIGHT);
      Word[] result = this.dictionary.search(this.searchText.getText(), limit);
      this.searchResult.getItems().setAll(result);
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
            default -> null;
         };
         this.row.getChildren().addAll(this.word, button);
      }
   }
}
