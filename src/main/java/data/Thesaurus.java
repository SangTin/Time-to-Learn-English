package data;

import data.dictionary.Word;
import data.enums.PartOfSpeech;
import data.enums.ThesaurusType;
import exception.editWord.NoSuchWordFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.HashSet;
import java.util.Set;

public class Thesaurus {
   private final ThesaurusType type;
   private final PartOfSpeech partOfSpeech;
   private final Dictionary dictionary;
   private final String meaning;
   private final ObservableSet<Word> mostUsed;
   private final ObservableSet<Word> lessUsed;

   public Thesaurus(String meaning, Dictionary dictionary, PartOfSpeech partOfSpeech, ThesaurusType type) {
      this.mostUsed = FXCollections.observableSet(new AutoDeleteWordSet());
      this.lessUsed = FXCollections.observableSet(new AutoDeleteWordSet());
      this.meaning = meaning;
      this.dictionary = dictionary;
      this.partOfSpeech = partOfSpeech;
      this.type = type;
   }

   public void addMostUsedByWord(String word) throws NoSuchWordFoundException {
      this.addByWord(word, this.mostUsed);
   }

   public void addLessUsedByWord(String word) throws NoSuchWordFoundException {
      this.addByWord(word, this.lessUsed);
   }

   private void addByWord(String word, Set<Word> set) throws NoSuchWordFoundException {
      set.add(this.dictionary.searchExactly(word));
   }

   public void addMostUsedByWord(Word word) {
      this.mostUsed.add(word);
   }

    public void addLessUsedByWord(Word word) {
        this.lessUsed.add(word);
    }

   public ObservableSet<Word> getMostUsedWords() {
      return this.mostUsed;
   }

   public ObservableSet<Word> getLessUsedWords() {
      return this.lessUsed;
   }

   public PartOfSpeech getPartOfSpeech() {
      return this.partOfSpeech;
   }

   public String getMeaning() {
      return this.meaning;
   }

   public ThesaurusType getType() {
      return this.type;
   }

   private static class AutoDeleteWordSet extends HashSet<Word> {
      @Override
      public boolean add(Word e) {
         e.deletedProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue) {
               this.remove(e);
             }
         });
         return super.add(e);
      }
   }
}
