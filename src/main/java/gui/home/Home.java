package gui.home;

import data.Dictionary;
import data.dictionary.Word;
import data.enums.AppFunction;
import gui.GraphicalDictionary;
import gui.components.search.SearchBase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Pair;

public class Home extends ScrollPane {
   @FXML
   private SearchBase searchBar;
   @FXML
   private ListView<Word> historyView;
   @FXML
   private ListView<Word> favouriteView;
   @FXML
   private Hyperlink clearHistory;
   @FXML
   private Hyperlink clearFavourite;

   public Home() {
      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/home/Home.fxml"));
         loader.setRoot(this);
         loader.setController(this);
         loader.load();
      } catch (Exception e) {
         System.out.println("Error in Home.java");
         e.printStackTrace();
      }
   }

   public void initialize() {
      new Thread(() -> {
         Platform.runLater(this::doInitialize);
      }).start();
   }

   public void doInitialize() {
      this.searchBar.setDictionary(GraphicalDictionary.getDictionaryInstance());
      this.searchBar.selectedWordProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue != null) {
            GraphicalDictionary.appFunctionProperty().set(new Pair<>(searchBar.getAppFunction(), newValue));
         }
      });
      Dictionary dictionary = GraphicalDictionary.getDictionaryInstance();

      this.historyView.setItems(dictionary.getHistorySearch().getWords());
      this.historyView.setPlaceholder(new Label("No history"));
      customRecentSearch(this.historyView);
      this.clearHistory.setOnAction((event) -> {
         dictionary.clearHistorySearch();
         this.historyView.refresh();
      });

      this.favouriteView.setItems(dictionary.getFavouriteSearch());
      this.favouriteView.setPlaceholder(new Label("No favourite"));
      customRecentSearch(this.favouriteView);
      this.clearFavourite.setOnAction((event) -> {
         dictionary.clearFavouriteSearch();
         this.favouriteView.refresh();
      });
   }

   private static void customRecentSearch(ListView<Word> listView) {
      listView.setCellFactory((param) -> new ListCell<Word>() {
        {
           prefWidthProperty().bind(listView.widthProperty().subtract(2));
           setMaxWidth(Control.USE_PREF_SIZE);
        }

        @Override
        protected void updateItem(Word item, boolean empty) {
           super.updateItem(item, empty);
           this.setText(null);
           if (item != null && !empty) {
              this.setText(item.getWordTarget());
           }
        }
      });
      listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
             GraphicalDictionary.appFunctionProperty().set(new Pair<>(AppFunction.SEARCH, newValue));
          }
      });
   }
}
