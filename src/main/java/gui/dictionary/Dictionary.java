package gui.dictionary;

import data.Changes;
import data.dictionary.AutoDeleteWordList;
import data.dictionary.Word;
import data.enums.AppFunction;
import data.enums.ThesaurusType;
import exception.editWord.EditWordException;
import gui.GraphicalDictionary;
import gui.components.search.SearchBase;
import gui.dictionary.edit.EditThesaurus;
import gui.dictionary.edit.EditWord;
import gui.dictionary.search.Description;
import gui.dictionary.search.Thesaurus;
import gui.style.DisplayThesaurus;
import gui.style.DisplayWord;
import gui.style.WordEditor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.shape.SVGPath;

import java.util.ArrayList;

public class Dictionary extends SplitPane {
   private static final String OPEN_SIDE_ICON = "M4.8 4.8m0 2.4a2.4 2.4 0 0 1 2.4-2.4h14.4a2.4 2.4 0 0 1 2.4 2.4v14.4a2.4 2.4 0 0 1-2.4 2.4h-14.4a2.4 2.4 0 0 1-2.4-2.4zM10.8 4.8v19.2M16.8 12l2.4 2.4-2.4 2.4";
   private static final String CLOSE_SIDE_ICON = "M4.8 4.8m0 2.4a2.4 2.4 0 0 1 2.4-2.4h14.4a2.4 2.4 0 0 1 2.4 2.4v14.4a2.4 2.4 0 0 1-2.4 2.4h-14.4a2.4 2.4 0 0 1-2.4-2.4zM10.8 4.8v19.2M18 12l-2.4 2.4 2.4 2.4";
   private static final String EDIT_ICON = "M21.67 7.986 7.668 22.045C7.262 22.451 6.711 22.68 6.137 22.68H4.325C3.725 22.68 3.24 22.19 3.24 21.592V19.762C3.24 19.192 3.467 18.644 3.87 18.239L17.876 4.176C21.128 1.455 24.381 5.265 21.67 7.986ZM14.04 22.68H22.68M16.535 5.735 20.226 9.426";
   private static final String DISPLAY_ICON = "M4 4m0 1a1 1 0 0 1 1 -1h14a1 1 0 0 1 1 1v2a1 1 0 0 1 -1 1h-14a1 1 0 0 1 -1 -1z M4 12m0 1a1 1 0 0 1 1 -1h4a1 1 0 0 1 1 1v6a1 1 0 0 1 -1 1h-4a1 1 0 0 1 -1 -1z M14 12l6 0 M14 16l6 0 M14 20l6 0";

   @FXML private Button previousWord;
   @FXML private Button nextWord;
   @FXML private Button sideTabButton;
   @FXML private SearchBase searchPane;
   @FXML private TabPane contentTabPane;
   @FXML private Button changeModeButton;
   @FXML private Button deleteButton;
   @FXML private Button saveButton;

   private final ArrayList<Tab> descriptionTabs = new ArrayList<>();
   private final ArrayList<Tab> editorTabs = new ArrayList<>();
   private DisplayWord descriptionTabContent;
   private DisplayThesaurus synonymTabContent;
   private DisplayThesaurus antonymTabContent;

   private final UniqueWordStack wordFlow = new UniqueWordStack();
   private final SimpleBooleanProperty isSideTabOpen = new SimpleBooleanProperty(false);
   private final SimpleObjectProperty<AppFunction> appFunction = new SimpleObjectProperty<>();
   private static final data.SQLiteDatabase database = GraphicalDictionary.getDatabaseInstance();
   private static final data.Dictionary dictionary = GraphicalDictionary.getDictionaryInstance();
   private static final Changes changes = GraphicalDictionary.getChangesInstance();

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
      new Thread(this::doInitialize).start();
   }

   public void doInitialize() {
      searchPane.setDictionary(dictionary);
      previousWord.setOnAction(e -> back());
      nextWord.setOnAction(e -> forward());
      searchPane.selectedWordProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue != null) {
            this.displaySearch(searchPane.getAppFunction(), newValue);
         }
      });

      wordFlow.currentProperty().addListener((observable, oldValue, newValue) -> {
         previousWord.setDisable(newValue.intValue() <= 0);
         nextWord.setDisable(newValue.intValue() >= wordFlow.size() - 1);
      });
      wordFlow.addListener((ListChangeListener.Change<? extends Word> c) -> {
         previousWord.setDisable(wordFlow.currentProperty().get() <= 0);
         nextWord.setDisable(wordFlow.currentProperty().get() >= wordFlow.size() - 1);
      });

      Divider divider = this.getDividers().get(0);
      isSideTabOpen.addListener((observable, oldValue, newValue) -> {
         if (newValue) {
            divider.setPosition(0.5D);
            this.sideTabButton.getTooltip().setText("Close side tab");
            ((SVGPath)this.sideTabButton.getGraphic()).setContent(CLOSE_SIDE_ICON);
            this.sideTabButton.setOnAction((event) -> this.isSideTabOpen.set(false));
         } else {
            divider.setPosition(0.0D);
            this.sideTabButton.getTooltip().setText("Open side tab");
            ((SVGPath)this.sideTabButton.getGraphic()).setContent(OPEN_SIDE_ICON);
            this.sideTabButton.setOnAction((event) -> this.isSideTabOpen.set(true));
         }
      });
      isSideTabOpen.set(true);

      setUpDisplayDescription();
      setUpDisplayEditor();
      contentTabPane.getTabs().addListener((ListChangeListener.Change<? extends Tab> c) -> {
         descriptionTabContent = (DisplayWord) descriptionTabs.get(0).getContent();
         synonymTabContent = (DisplayThesaurus) descriptionTabs.get(1).getContent();
         antonymTabContent = (DisplayThesaurus) descriptionTabs.get(2).getContent();
      });
      changeModeButton.setOnAction(e -> {
         switch (appFunction.get()) {
            case SEARCH: {
               appFunction.set(AppFunction.FIX);
               break;
            }
            case ADD:
            case FIX:
            case DELETE: {
               if (isModified()) {
                  GraphicalDictionary.alert("Save changes", "Do you want to save changes?",
                          Alert.AlertType.CONFIRMATION,
                          () -> { save(); appFunction.set(AppFunction.SEARCH); },
                          () -> appFunction.set(AppFunction.SEARCH));
               } else {
                  appFunction.set(AppFunction.SEARCH);
               }
               break;
            }
         }
      });
      appFunction.addListener((observable, oldValue, newValue) -> {
         switch (newValue) {
            case SEARCH: {
               contentTabPane.getTabs().setAll(descriptionTabs);
               changeModeButton.setDisable(false);
               changeModeButton.setText("Edit");
               SVGPath svgPath = (SVGPath) changeModeButton.getGraphic();
               svgPath.setContent(EDIT_ICON);
               break;
            }
            case ADD: {
               changeModeButton.setDisable(true);
               contentTabPane.getTabs().setAll(editorTabs);
               break;
            }
            case FIX:
            case DELETE: {
               contentTabPane.getTabs().setAll(editorTabs);
               changeModeButton.setDisable(false);
               changeModeButton.setText("Display");
               SVGPath svgPath = (SVGPath) changeModeButton.getGraphic();
               svgPath.setContent(DISPLAY_ICON);
               break;
            }
         }
         replay(wordFlow.peek());
      });
      appFunction.addListener((observable, oldValue, newValue) -> {
         switch (newValue) {
            case SEARCH: {
               saveButton.setDisable(true);
               break;
            }
            case ADD:
            case FIX:
            case DELETE: {
               saveButton.setDisable(false);
               break;
            }
         }
      });
      appFunction.set(AppFunction.SEARCH);
      deleteButton.setOnAction(e -> GraphicalDictionary.alert("Delete", "Are you sure you want to delete this word? \nThis can't be undone.", Alert.AlertType.WARNING,
              () -> {
                 Word word = wordFlow.peek();
                 if (word != null) {
                    try {
                       changes.remove(word);
                    } catch (EditWordException ignored) {}
                    GraphicalDictionary.setAppFunction(AppFunction.SEARCH, wordFlow.peek());
                 }
              }, () -> {}));
      saveButton.setOnAction(e -> save());
   }

   private boolean isModified() {
      for (Tab tab : contentTabPane.getTabs()) {
         if (tab.getContent() instanceof EditWord) {
            if (((EditWord) tab.getContent()).isModified()) {
               return true;
            }
         }
      }
      return false;
   }

   private void setUpDisplayDescription() {
      Description desTabContent = new Description();
      desTabContent.setDictionary(dictionary);
      Tab desTab = new Tab("Description");
      descriptionTabs.add(desTab);
      desTab.setContent(desTabContent);

      Tab synTab = new Tab("Synonyms");
      descriptionTabs.add(synTab);
      synTab.setContent(new Thesaurus());

      Tab antTab = new Tab("Antonyms");
      descriptionTabs.add(antTab);
      antTab.setContent(new Thesaurus());

      descriptionTabs.forEach(tab -> {
         tab.setClosable(false);
         tab.setDisable(true);
         tab.getContent().disabledProperty().addListener((observable, oldValue, newValue) -> tab.setDisable(newValue));
      });
   }

   private void setUpDisplayEditor() {
      EditWord desTabContent = new EditWord();
      desTabContent.setDictionary(dictionary);
      Tab desTab = new Tab("Description");
      editorTabs.add(desTab);
      desTab.setContent(desTabContent);

      Tab synTab = new Tab("Synonyms");
      editorTabs.add(synTab);
      synTab.setContent(new EditThesaurus(ThesaurusType.SYNONYM, dictionary));

      Tab antTab = new Tab("Antonyms");
      editorTabs.add(antTab);
      antTab.setContent(new EditThesaurus(ThesaurusType.SYNONYM, dictionary));

      editorTabs.forEach(tab -> tab.setClosable(false));
   }

   public void displaySearch(AppFunction function, Word word) {
      if (word != null) {
         if (function != AppFunction.ADD) {
            wordFlow.push(word);
         } else {
            wordFlow.clear();
         }
         appFunction.set(function);
      }
      replay(word);
   }

   private void replay(Word word) {
      contentTabPane.getSelectionModel().selectFirst();
      if (word == null) {
         clear();
         return;
      }

      // Set description
      descriptionTabContent = (DisplayWord) contentTabPane.getTabs().get(0).getContent();
      descriptionTabContent.display(word, appFunction.get());

      // Set thesaurus
      ObservableList<data.Thesaurus> synonyms = database.getThesaurus(word, ThesaurusType.SYNONYM, dictionary);
      synonymTabContent = (DisplayThesaurus) contentTabPane.getTabs().get(1).getContent();
      synonymTabContent.display(word, synonyms);

      ObservableList<data.Thesaurus> antonyms = database.getThesaurus(word, ThesaurusType.ANTONYM, dictionary);
      antonymTabContent = (DisplayThesaurus) contentTabPane.getTabs().get(2).getContent();
      antonymTabContent.display(word, antonyms);
   }

   public void back() {
      appFunction.set(AppFunction.SEARCH);
      replay(wordFlow.goBack());
   }

   public void forward() {
      appFunction.set(AppFunction.SEARCH);
      replay(wordFlow.goForward());
   }

   public void clear() {
      contentTabPane.getSelectionModel().selectFirst();
      descriptionTabContent.clear();
      synonymTabContent.clear();
      antonymTabContent.clear();
   }

   private void save() {
      for (Tab tab : contentTabPane.getTabs()) {
         if (tab.getContent() instanceof WordEditor) {
            if (!((WordEditor) tab.getContent()).canSave()) {
               contentTabPane.getSelectionModel().select(tab);
               return;
            }
         }
      }
      contentTabPane.getTabs().forEach(tab -> {
         if (tab.getContent() instanceof WordEditor) {
            ((WordEditor) tab.getContent()).save();
         }
      });
   }

   private static class UniqueWordStack extends AutoDeleteWordList {
      private final SimpleIntegerProperty current = new SimpleIntegerProperty(-1);

      public Word peek() {
         current.set(Math.min(current.get(), size() - 1));
         if (current.get() < 0) return null;
         return get(current.get());
      }

      public void push(Word e) {
         try {
            remove(current.get() + 1, size());
         } catch (Exception ignored) {}

         remove(e);
         add(e);
         current.set(size() - 1);
         e.deletedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
               remove(e);
            }
         });
      }

      public void remove(Word e) {
         super.remove(e);
         current.set(Math.min(current.get(), size() - 1));
      }

      public Word goBack() {
         current.set(Math.max(current.get() - 1, 0));
         return peek();
      }

      public Word goForward() {
         current.set(Math.min(current.get() + 1, size() - 1));
         return peek();
      }

      public SimpleIntegerProperty currentProperty() {
         return current;
      }

   }
}
