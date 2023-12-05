package gui.dictionary;

import data.dictionary.Word;
import data.enums.ThesaurusType;
import gui.GraphicalDictionary;
import gui.components.WordDisplay;
import gui.dictionary.content.Description;
import gui.dictionary.content.Thesaurus;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;

import java.util.LinkedHashSet;
import java.util.List;

public class Dictionary extends WordDisplay {
   private static final String OPEN_SIDE_ICON = "M4.8 4.8m0 2.4a2.4 2.4 0 0 1 2.4-2.4h14.4a2.4 2.4 0 0 1 2.4 2.4v14.4a2.4 2.4 0 0 1-2.4 2.4h-14.4a2.4 2.4 0 0 1-2.4-2.4zM10.8 4.8v19.2M16.8 12l2.4 2.4-2.4 2.4";
   private static final String CLOSE_SIDE_ICON = "M4.8 4.8m0 2.4a2.4 2.4 0 0 1 2.4-2.4h14.4a2.4 2.4 0 0 1 2.4 2.4v14.4a2.4 2.4 0 0 1-2.4 2.4h-14.4a2.4 2.4 0 0 1-2.4-2.4zM10.8 4.8v19.2M18 12l-2.4 2.4 2.4 2.4";

   @FXML private Button previousWord;
   @FXML private Button nextWord;
   @FXML private Description descriptionPane;
   @FXML private Thesaurus synonymPane;
   @FXML private Thesaurus antonymPane;
   @FXML protected Button sideTabButton;

   private final UniqueWordStack previous = new UniqueWordStack();
   private final UniqueWordStack next = new UniqueWordStack();
   protected final SimpleBooleanProperty isSideTabOpen = new SimpleBooleanProperty(false);

   public Dictionary() {
      try {
         FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/dictionary/Dictionary.fxml"));
         loader.setController(this);
         loader.setRoot(this);
         loader.load();
      } catch (Exception e) {
         System.out.println("Error loading Dictionary.fxml");
      }
   }

   public void initialize() {
      dictionary = GraphicalDictionary.getDictionaryInstance();
      database = GraphicalDictionary.getDatabaseInstance();

      this.searchPane.setDictionary(dictionary);
      this.previousWord.setOnAction(e -> back());
      this.nextWord.setOnAction(e -> forward());

      this.descriptionPane.setDictionary(dictionary);
      this.searchPane.selectedWordProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue != null) {
            this.displaySearch(newValue);
         }
      });

      Divider divider = this.getDividers().get(0);
      this.isSideTabOpen.addListener((observable, oldValue, newValue) -> {
         if (newValue) {
            divider.setPosition(0.5D);
            this.sideTabButton.getTooltip().setText("Close side tab");
            ((SVGPath)this.sideTabButton.getGraphic()).setContent(CLOSE_SIDE_ICON);
            this.sideTabButton.setOnAction((event) -> {
               this.isSideTabOpen.set(false);
            });
         } else {
            divider.setPosition(0.0D);
            this.sideTabButton.getTooltip().setText("Open side tab");
            ((SVGPath)this.sideTabButton.getGraphic()).setContent(OPEN_SIDE_ICON);
            this.sideTabButton.setOnAction((event) -> {
               this.isSideTabOpen.set(true);
            });
         }
      });
      this.isSideTabOpen.set(true);
   }

   public void displaySearch(Word word) {
      this.searchPane.selectedWordProperty().set(null);
      next.clear();
      if (word != null) {
         previous.push(word);
      }
      replay(word);
   }

   private void replay(Word word) {
      contentTabPane.getSelectionModel().selectFirst();
      synonymTab.setDisable(true);
      antonymTab.setDisable(true);
      descriptionTab.setDisable(true);

      // Set button state
      previousWord.setDisable(!canBack());
      nextWord.setDisable(!canForward());

      // Set description
      try {
         descriptionPane.displaySearch(word);
         descriptionTab.setDisable(false);
      } catch (Exception e) {
         return;
      }

      // Set thesaurus
      List<data.Thesaurus> synonyms = database.getThesaurus(word, ThesaurusType.SYNONYM, dictionary);
      try {
         synonymPane.display(word, synonyms);
         synonymTab.setDisable(false);
      } catch (Exception ignored) {}
      List<data.Thesaurus> antonyms = database.getThesaurus(word, ThesaurusType.ANTONYM, dictionary);
      try {
         antonymPane.display(word, antonyms);
         antonymTab.setDisable(false);
      } catch (Exception ignored) {}
   }

   public void back() {
      if (!canBack()) return;
      next.push(previous.pop());
      replay(previous.peek());
   }

   private boolean canBack() {
      return previous.size() > 1;
   }

   public void forward() {
      if (!canForward()) return;
      previous.push(next.pop());
      replay(previous.peek());
   }

   private boolean canForward() {
      return !next.isEmpty();
   }

   public void clear() {
      previous.clear();
      next.clear();
      descriptionPane.clear();
      contentTabPane.getSelectionModel().selectFirst();
   }

   private static class UniqueWordStack extends LinkedHashSet<Word> {
      public Word pop() {
         return super.removeLast();
      }

      public Word peek() {
         return super.getLast();
      }

      public void push(Word e) {
         remove(e);
         e.deletedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
               this.remove(e);
            }
         });
         add(e);
      }
   }
}
