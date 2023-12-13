package gui;

import data.Changes;
import data.Dictionary;
import data.SQLiteDatabase;
import data.dictionary.Word;
import data.enums.AppFunction;
import game.GameBase;
import game.crossword.CrossWord;
import game.guessword.GuessWord;
import gui.components.GameIntroduction;
import gui.components.translate_text.TranslatePane;
import gui.home.Home;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Objects;
import java.util.Optional;

public class GraphicalDictionary extends AnchorPane {
   private static final SQLiteDatabase databaseInstance;
   private static final Dictionary dictionaryInstance;
   private static final Changes changesInstance;
   private static final SimpleObjectProperty<Pair<AppFunction, Object>> appFunctionWithWord;

   @FXML private TabPane menuPane;
   @FXML private Tab homeTab;
   @FXML private Home homePane;
   @FXML private Tab dictionaryTab;
   @FXML private Tab gamingTab;
   @FXML private Tab translateTab;
   @FXML private VBox gamingPane;
   @FXML private GameIntroduction crossWordButton;
   @FXML private GameIntroduction guessWordButton;
   private gui.dictionary.Dictionary dictionaryPane;
   private TranslatePane translatePane;

   public static SQLiteDatabase getDatabaseInstance() {
      return databaseInstance;
   }

   public static Dictionary getDictionaryInstance() {
      return dictionaryInstance;
   }

   public static Changes getChangesInstance() {
      return changesInstance;
   }

   public static SimpleObjectProperty<Pair<AppFunction, Object>> appFunctionProperty() {
      return appFunctionWithWord;
   }

   public static void setAppFunction(AppFunction function, Object word) {
      appFunctionWithWord.set(new Pair<>(function, word));
   }

   public GraphicalDictionary() {
      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/GUIApplication.fxml"));
         loader.setController(this);
         loader.setRoot(this);
         loader.load();
      } catch (Exception e) {
            System.out.println("Error loading GUIApplication.fxml");
            e.printStackTrace();
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
               this.displaySearch(newValue.getKey(), (Word) newValue.getValue());
               break;
            }
            case GAMING: {
               this.playGame((Integer) newValue.getValue());
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

      crossWordButton.setLogo("/img/crossword-logo.png");
      crossWordButton.setTitle("Crossword Puzzle Game");
      crossWordButton.setDescription("Welcome to crossword puzzles! Solve clues to fill a grid with words, both across and down. It's a fun way to test your vocabulary and problem-solving skills. Ready to dive into the world of words? Let's get started!");
      crossWordButton.setOnAction(event -> this.playCrossWord());

      guessWordButton.setLogo("/img/guessword-logo.png");
      guessWordButton.setTitle("Guessword Puzzle Game");
      guessWordButton.setDescription("Welcome to the Guessword puzzles! In this game, your task is to look at the images and deduce the word to be guessed. Each picture serves as a clue, helping you expand your vocabulary and test your recognition skills. Ready to challenge your cognitive abilities? Let the adventure of catching words through pictures begin now!");
      guessWordButton.setOnAction(event -> this.playGuessWord());

      new Thread(() -> {
         this.dictionaryPane = new gui.dictionary.Dictionary();
         this.dictionaryTab.setContent(this.dictionaryPane);
         this.translatePane = new TranslatePane();
         this.translateTab.setContent(this.translatePane);
      }).start();
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

   private void playGame(Integer gameType) {
      switch (gameType) {
         case 1: {
            playCrossWord();
            break;
         }
         case 2: {
            playGuessWord();
            break;
         }
      }
   }

   static {
      databaseInstance = new SQLiteDatabase("dictionary.db");
      dictionaryInstance = new Dictionary(databaseInstance);
      changesInstance = new Changes(dictionaryInstance, databaseInstance);
      appFunctionWithWord = new SimpleObjectProperty<>();
      new Thread(() -> databaseInstance.importToDictionary(dictionaryInstance)).start();
   }



   public static void alert(String title, String message, Alert.AlertType type, Runnable yesAction, Runnable noAction) {
      Alert alert = new Alert(type);
      alert.setTitle(title);
      alert.setHeaderText(message);
      alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(GraphicalDictionary.class.getResource("/css/Alert.css")).toExternalForm());
      ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
      ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
      alert.getButtonTypes().setAll(yesButton, noButton);
      if (Objects.requireNonNull(type) == Alert.AlertType.CONFIRMATION) {
         ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
         alert.getButtonTypes().add(cancelButton);
         Node alertCancel = alert.getDialogPane().lookupButton(cancelButton);
         alertCancel.setId("alertCancel");
      }
      Node alertNo = alert.getDialogPane().lookupButton(noButton);
      alertNo.setId("alertNo");
      Node alertYes = alert.getDialogPane().lookupButton(yesButton);
      alertYes.setId("alertYes");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == yesButton) {
         yesAction.run();
      }
      if (result.get() == noButton) {
         noAction.run();
      }
   }
}
