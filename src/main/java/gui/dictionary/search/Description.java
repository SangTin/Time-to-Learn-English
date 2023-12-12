package gui.dictionary.search;

import data.Dictionary;
import data.dictionary.Word;
import data.enums.PartOfSpeech;
import gui.style.DisplayWord;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class Description extends DisplayWord {
   private static final String DESCRIPTION_FXML = "/fxml/dictionary/search/Description.fxml";
   private static final String DES_ICON = "M4 9h8v-3.586a1 1 0 0 1 1.707 -.707l6.586 6.586a1 1 0 0 1 0 1.414l-6.586 6.586a1 1 0 0 1 -1.707 -.707v-3.586h-8a1 1 0 0 1 -1 -1v-4a1 1 0 0 1 1 -1z";
   private static final double DESPANE_TOP_PADDING = 10;
   private static final double TOOLBAR_HEIGHT = 40;

   @FXML private ButtonBar posBar;
   @FXML private ScrollPane desView;
   @FXML private VBox desPane;
   @FXML private Label headWord;
   @FXML private HBox header;
   @FXML private HBox ukPron;
   @FXML private HBox usPron;
   @FXML private Button favouriteButton;

   private Dictionary dictionary;
   private final SimpleBooleanProperty isFavourite = new SimpleBooleanProperty(false);

   public Description() {
      super();
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource(DESCRIPTION_FXML));
         loader.setController(this);
         loader.setRoot(this);
         loader.load();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void initialize() {
      // Set up description view
      posBar.heightProperty().addListener((observable, oldVal, newVal) -> {
         AnchorPane.setTopAnchor(desView, newVal.doubleValue());
      });
      posBar.getButtons().addListener((ListChangeListener.Change<? extends Node> c) -> {
         if (posBar.getButtons().isEmpty()) {
            posBar.setPrefHeight(0);
         } else {
            posBar.setPrefHeight(TOOLBAR_HEIGHT);
         }
      });
      isFavourite.addListener((observable, oldValue, newValue) -> {
         favouriteButton.getStyleClass().remove("is-favourite");
         if (newValue) {
            favouriteButton.getStyleClass().add("is-favourite");
         }
      });
      clear();
   }

   public void clear() {
      setDisable(true);

      // Clear description
      desView.setVvalue(0);
      desPane.getChildren().clear();
      posBar.getButtons().clear();
   }

   public void display(Word word) {
      clear();

      // Set description
      if (word != null && word.getWordTarget() != null) {
         setDisable(false);
      } else {
         return;
      }

      // Set favourite button
      isFavourite.unbind();
      isFavourite.set(word.isFavorite());
      isFavourite.bind(word.favoriteProperty());
      favouriteButton.setOnAction(e -> {
         if (word.isFavorite()) {
            dictionary.removeFavourite(word);
         } else {
            dictionary.insertFavourite(word);
         }
      });

      // Set head word
      headWord.setText(word.getWordTarget());
      desPane.getChildren().add(header);

      // Set pronunciation
      Text ukIpa = (Text) ukPron.getChildren().get(2);
      ukIpa.setText(word.getUkPron());
      Text usIpa = (Text) usPron.getChildren().get(2);
      usIpa.setText(word.getUsPron());
      if (!ukIpa.getText().isEmpty()) {
         desPane.getChildren().add(ukPron);
      }
      if (!usIpa.getText().isEmpty()) {
         desPane.getChildren().add(usPron);
      }

      // Generate new description
      if (!word.getWordExplain().isEmpty()) {
         for (String line : word.getWordExplain().split("\\r?\\n|\\r")) {
            char op = line.charAt(0);
            String content = line.substring(1).trim();
            switch (op) {
               case '*': { // Part of speech
                  String pos = content;
                  if (pos.contains(",")) pos = pos.substring(0, pos.indexOf(","));
                  Text posText = new Text(pos);
                  posText.getStyleClass().add("pos-text");
                  desPane.getChildren().add(posText);

                  // Get part of speech
                  PartOfSpeech type = PartOfSpeech.fromString(pos);
                  if (type == null) break;
                  String partOfSpeech = type.toVietnamese();

                  // Check if pos button already exists
                  boolean create = true;
                  for (Node node : posBar.getButtons()) {
                     if (!(node instanceof Button)) continue;
                     if (((Button) node).getText().equalsIgnoreCase(partOfSpeech)) {
                        create = false;
                        break;
                     }
                  }
                  if (!create) break;

                  // Create new pos button
                  Button posButton = new Button(partOfSpeech);
                  posButton.setOnAction(e -> ensureVisible(desView, posText));
                  ButtonBar.setButtonData(posButton, ButtonBar.ButtonData.LEFT);
                  posBar.getButtons().add(posButton);

                  break;
               }
               case '-': { // Description
                  HBox desLine = new HBox();
                  desLine.getStyleClass().add("des-line");
                  desPane.getChildren().add(desLine);

                  SVGPath desIcon = new SVGPath();
                  desIcon.setContent(DES_ICON);
                  desIcon.getStyleClass().add("des-icon");
                  desLine.getChildren().add(desIcon);

                  content = content.replaceAll("_", " ");
                  Text desText = new Text(content);
                  desText.getStyleClass().add("des-text");
                  desLine.getChildren().add(desText);

                  break;
               }
               case '>': { // English example
                  HBox engLine = engExample(content);
                  desPane.getChildren().addAll(engLine);
                  break;
               }
               case '=': { // Vietnamese example
                  Text vie = vieExample(content);
                  desPane.getChildren().addAll(vie);
                  break;
               }
            }
         }
      }
   }

   public void setDictionary(Dictionary dictionary) {
      this.dictionary = dictionary;
   }

   private static void ensureVisible(ScrollPane scrollPane, Node node) {
      double h = scrollPane.getContent().getBoundsInLocal().getHeight();
      double y = node.getBoundsInParent().getMinY();
      double v = scrollPane.getViewportBounds().getHeight();
      scrollPane.setVvalue(scrollPane.getVmax() * ((y - DESPANE_TOP_PADDING) / (h - v)));
   }

   private static Text vieExample(String text) {
      Text vie = new Text(text);
      vie.getStyleClass().add("vie-text");
      VBox.setMargin(vie, new javafx.geometry.Insets(0, 0, 0, 15));
      return vie;
   }

   private HBox engExample(String engText) {
      HBox engLine = new HBox();
      engLine.getStyleClass().add("eng-line");
      VBox.setMargin(engLine, new javafx.geometry.Insets(0, 0, 0, 15));

      for (String text : engText.split(" ")) {
         Node engWord = TextToNode.textToLink(text, dictionary);
         engWord.getStyleClass().add("eng-text");
         engLine.getChildren().add(engWord);
      }

      return engLine;
   }
}