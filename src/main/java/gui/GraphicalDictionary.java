package gui;

import data.Changes;
import data.Dictionary;
import data.SQLiteDatabase;
import data.dictionary.Word;
import data.enums.AppFunction;
import game.GameBase;
import game.crossword.CrossWord;
import game.guessword.GuessWord;
import gui.home.Home;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

public class GraphicalDictionary extends AnchorPane {
   private static final SQLiteDatabase databaseInstance;
   private static final Dictionary dictionaryInstance;
   private static final Changes changesInstance;
   private static final SimpleObjectProperty<Pair<AppFunction, Word>> appFunctionWithWord;

   @FXML private TabPane menuPane;
   @FXML private Tab homeTab;
   @FXML private Home homePane;
   @FXML private Tab dictionaryTab;
   @FXML private gui.dictionary.Dictionary dictionaryPane;
   @FXML private Tab gamingTab;
   @FXML private AnchorPane gamingPane;
   @FXML private Button crossWordButton;
   @FXML private Button guessWordButton;

   public static SQLiteDatabase getDatabaseInstance() {
      return databaseInstance;
   }

   public static Dictionary getDictionaryInstance() {
      return dictionaryInstance;
   }

   public static Changes getChangesInstance() {
      return changesInstance;
   }

   public static SimpleObjectProperty<Pair<AppFunction, Word>> appFunctionProperty() {
     return appFunctionWithWord;
   }

   public GraphicalDictionary() {
      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/GUIApplication.fxml"));
         loader.setController(this);
         loader.setRoot(this);
         loader.load();
      } catch (Exception e) {
            System.out.println("Error loading GUIApplication.fxml");
      }
   }

   public void initialize() {
      appFunctionProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue == null) {
             return;
         }
         switch(newValue.getKey()) {
            case SEARCH:
            case ADD:
            case FIX:
            case DELETE:{
               this.displaySearch(newValue.getKey(), newValue.getValue());
               break;
            }
            case GAMING: {
               break;
            }
         }
         appFunctionProperty().set(null);
      });

      javafx.beans.value.ChangeListener<? super Boolean> listener = (observable, oldValue, newValue) -> {
         if (newValue) {
            this.gamingTab.setContent(gamingPane);
         }
      };
      crossWord.isGameFinishedProperty().addListener(listener);
      guessWord.isGameFinishedProperty().addListener(listener);
      crossWordButton.setOnAction(event -> this.playCrossWord());
      guessWordButton.setOnAction(event -> this.playGuessWord());
   }

   private final GameBase crossWord = new CrossWord();
   private final GameBase guessWord = new GuessWord();
   private void playCrossWord() {
      this.menuPane.getSelectionModel().select(this.gamingTab);
      this.gamingTab.setContent(this.crossWord);
      crossWord.startGame();
   }

    private void playGuessWord() {
        this.menuPane.getSelectionModel().select(this.gamingTab);
        this.gamingTab.setContent(this.guessWord);
        guessWord.startGame();
    }

   private void displaySearch(AppFunction function, Word word) {
      this.menuPane.getSelectionModel().select(this.dictionaryTab);
      this.dictionaryPane.displaySearch(function, word);
   }

   static {
      databaseInstance = new SQLiteDatabase("dictionary.db");
      dictionaryInstance = new Dictionary(databaseInstance);
      new Thread(() -> {
         Platform.runLater(() -> {
            databaseInstance.importToDictionary(dictionaryInstance);
         });
      }).start();
      changesInstance = new Changes(dictionaryInstance, databaseInstance);
      appFunctionWithWord = new SimpleObjectProperty<>();
   }
}
