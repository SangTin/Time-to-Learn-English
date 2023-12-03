package gui;

import data.Dictionary;
import data.SQLiteDatabase;
import data.Word;
import data.enums.AppFunction;
import gui.home.Home;
import gui.style.Synchronized;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

public class GraphicalDictionary extends AnchorPane {
   private static SQLiteDatabase databaseInstance = null;
   private static Dictionary dictionaryInstance = null;
   private static SimpleObjectProperty<Pair<AppFunction, Word>> appFunctionWithWord = null;

   @FXML private TabPane menuPane;
   @FXML private Tab homeTab;
   @FXML private Home homePane;
   @FXML private Tab dictionaryTab;
   @FXML private gui.dictionary.Dictionary dictionaryPane;
   @FXML private Tab editTab;
   @FXML private Tab gamingTab;

   public static SQLiteDatabase getDatabaseInstance() {
      return databaseInstance;
   }

   public static Dictionary getDictionaryInstance() {
      return dictionaryInstance;
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
         System.out.println(e.getMessage());
      }
   }

   public void initialize() {
      appFunctionProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue == null) {
             return;
         }
         switch(newValue.getKey()) {
            case SEARCH: {
               this.displaySearch(newValue.getValue());
               break;
            }
            case CREATE:
            case FIX:
            case DELETE: {
               break;
            }
            case GAMING:
            default:
         }
         appFunctionProperty().set(null);
      });
   }

   private void displaySearch(Word word) {
      this.menuPane.getSelectionModel().select(this.dictionaryTab);
      this.dictionaryPane.displaySearch(word);
   }

   public void refresh() {
      menuPane.getTabs().forEach(tab -> {
         if (tab.getContent() instanceof Synchronized) {
            ((Synchronized) tab.getContent()).synchronize();
         }
      });
   }

   static {
      databaseInstance = new SQLiteDatabase("dictionary.db");
      dictionaryInstance = new Dictionary(databaseInstance);
      databaseInstance.importToDictionary(dictionaryInstance);
      appFunctionWithWord = new SimpleObjectProperty<>();
   }
}
