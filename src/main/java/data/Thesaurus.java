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
   private static int id = 0;
   private int meaningId = ++id;
   private ThesaurusType type;
   private PartOfSpeech partOfSpeech;
   private Dictionary dictionary;
   private String meaning;
   private final ObservableSet<Word> mostUsed = FXCollections.observableSet(new AutoDeleteWordSet());
   private final ObservableSet<Word> lessUsed = FXCollections.observableSet(new AutoDeleteWordSet());

   public Thesaurus() {
   }

   public Thesaurus(int meaningId, String meaning, Dictionary dictionary, PartOfSpeech partOfSpeech, ThesaurusType type) {
      id = Math.max(id, meaningId);
      this.meaningId = meaningId;
      this.meaning = meaning;
      this.dictionary = dictionary;
      this.partOfSpeech = partOfSpeech;
      this.type = type;
   }

   public Thesaurus(Thesaurus thesaurus) {
      this(thesaurus.meaningId, thesaurus.meaning, thesaurus.dictionary, thesaurus.partOfSpeech, thesaurus.type);
      this.mostUsed.addAll(thesaurus.mostUsed);
      this.lessUsed.addAll(thesaurus.lessUsed);
   }

   public Word addMostUsedByWord(String word) throws NoSuchWordFoundException {
      return this.addByWord(word, this.mostUsed);
   }

   public Word addLessUsedByWord(String word) throws NoSuchWordFoundException {
      return this.addByWord(word, this.lessUsed);
   }

   private Word addByWord(String word, Set<Word> set) throws NoSuchWordFoundException {
      Word result = this.dictionary.searchExactly(word);
      set.add(result);
      return result;
   }

   public void addMostUsedByWord(Word word) {
      this.mostUsed.add(word);
   }

   public void deleteMostUsedByWord(Word word) {
      this.mostUsed.remove(word);
   }

   public void deleteLessUsedByWord(Word word) {
      this.lessUsed.remove(word);
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

   public void setType(ThesaurusType type) {
      this.type = type;
   }

   public int getMeaningId() {
      return this.meaningId;
   }

   public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
      this.partOfSpeech = partOfSpeech;
   }

   public void setPartOfSpeech(String partOfSpeech) {
      this.partOfSpeech = PartOfSpeech.fromString(partOfSpeech);
   }

   public void setDictionary(Dictionary dictionary) {
      this.dictionary = dictionary;
   }

   public Dictionary getDictionary() {
      return this.dictionary;
   }

   public void setMeaning(String meaning) {
      this.meaning = meaning;
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
