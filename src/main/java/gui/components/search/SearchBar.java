package gui.components.search;

import data.Word;
import data.enums.AppFunction;
import data.enums.SearchResultType;
import entry.GraphicalUserInterface;
import gui.components.SearchBase;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;

import java.util.Timer;
import java.util.TimerTask;

public class SearchBar extends SearchBase {
   private final SimpleBooleanProperty isSearching = new SimpleBooleanProperty(false);

   public SearchBar() {
      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/components/search/SearchBar.fxml"));
         loader.setController(this);
         loader.setRoot(this);
         loader.load();
      } catch (Exception var2) {
         System.out.println("Error loading SearchBar.fxml");
      }
   }

   public void initialize() {
      super.initialize();
      this.searchText.focusedProperty().addListener((observable, oldVal, newVal) -> {
         if (newVal && this.searchText.getText().isEmpty()) {
            this.history();
         }
      });
      this.searchResult.setMaxHeight(400.0D);
      this.searchResult.getItems().addListener(new ListChangeListener<Word>() {
         public void onChanged(Change<? extends Word> c) {
            SearchBar.this.searchResult.setPrefHeight(40.0D * (double)SearchBar.this.searchResult.getItems().size());
         }
      });

      isSearching.addListener((observable, oldVal, newVal) -> {
         this.searchResult.setVisible(newVal);
         if (newVal) {
            this.searchText.requestFocus();
         }
      });

      Platform.runLater(() -> {
         this.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, (event) -> {
            isSearching.set(GraphicalUserInterface.inHierarchy(event.getPickResult().getIntersectedNode(), this));
         });
      });
   }

   public void synchronize() {
      this.search();
   }

   protected void search() {
      Word[] result = this.dictionary.search(this.searchText.getText());
      this.searchResult.getItems().setAll(result);
      this.searchResult.setCellFactory((param) ->
              new SuggestionCell(SearchResultType.SUGGESTION));
      if (result.length == 0) {
         createWordTimer = new Timer();
         createWordTimer.schedule(new CreateWordTask(), 1000);
      }
   }

   protected void history() {
      this.searchResult.getItems().setAll(this.dictionary.getHistorySearch().getWords());
      this.searchResult.setCellFactory((param) ->
              new SuggestionCell(SearchResultType.HISTORY));
   }

   class SuggestionCell extends SearchCell {
      public SuggestionCell(SearchResultType type) {
         SVGPath icon = new SVGPath();
         icon.getStyleClass().add("cell-icon");

         this.word.setEditable(false);
         this.word.getStyleClass().add("cell-word");
         this.word.mouseTransparentProperty().set(true);
         HBox.setHgrow(this.word, Priority.ALWAYS);

         Button pasteButton = createButton(PASTE_ICON, "Paste", () -> {
            SearchBar.this.searchText.setText(this.word.getText());
            SearchBar.this.searchText.requestFocus();
         });
         this.row.setAlignment(Pos.CENTER_LEFT);
         this.row.setSpacing(10.0D);
         this.row.setMaxHeight(SUGGESTION_CELL_HEIGHT);
         this.row.setPrefWidth(SearchBar.this.searchResult.getWidth() - 20.0D);
         switch(type) {
            case HISTORY: {
               Button deleteHistoryButton = createButton(DELETE_ICON, "Delete", () -> {
                  SearchBar.this.dictionary.getHistorySearch().remove(this.getItem());
                  SearchBar.this.searchResult.getItems().remove(this.getItem());
               });
               icon.setContent(HISTORY_ICON);
               this.row.getChildren().addAll(icon, this.word, deleteHistoryButton, pasteButton);
               break;
            }
            case SUGGESTION: {
               icon.setContent(MAGNIFYING_GLASS_ICON);
               this.row.getChildren().addAll(icon, this.word, pasteButton);
               break;
            }
            case CREATE: {
               icon.setContent(CREATE_ICON);
               this.row.getChildren().addAll(icon, this.word);
            }
         }
      }
   }

   private class CreateWordTask extends TimerTask {
      @Override
      public void run() {
         Platform.runLater(this::createWord);
      }

      private void createWord() {
         appFunction = AppFunction.CREATE;
         Word word = new Word(SearchBar.this.searchText.getText(), "");
         SearchBar.this.searchResult.getItems().setAll(word);
         SearchBar.this.searchResult.setCellFactory((param) ->
                 new SuggestionCell(SearchResultType.CREATE));
      }
   }
}
