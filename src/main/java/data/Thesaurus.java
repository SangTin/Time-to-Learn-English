package data;

import data.enums.PartOfSpeech;
import data.enums.ThesaurusType;
import exception.editWord.NoSuchWordFoundException;

import java.util.HashSet;
import java.util.Set;

public class Thesaurus {
   public static int count = 0;
   private final ThesaurusType type;
   private final PartOfSpeech partOfSpeech;
   private final Dictionary dictionary;
   private final String meaning;
   private final Set<Word> mostUsed;
   private final Set<Word> lessUsed;

   Changes changes;
   public Thesaurus(String meaning, Dictionary dictionary, PartOfSpeech partOfSpeech, ThesaurusType type) {
      this.mostUsed = new HashSet<>();
      this.lessUsed = new HashSet<>();
      this.meaning = meaning;
      this.dictionary = dictionary;
      this.partOfSpeech = partOfSpeech;
      this.type = type;
      this.changes = new Changes(dictionary, "dictionary.db");
   }

   public Thesaurus(String meaning, String description, Dictionary dictionary, PartOfSpeech partOfSpeech, ThesaurusType type) {
      this(meaning, dictionary, partOfSpeech, type);
   }

   public void addMostUsedByWord(String word) throws NoSuchWordFoundException {
      this.addByWord(word, this.mostUsed);
   }

   public void addLessUsedByWord(String word) throws NoSuchWordFoundException {
      this.addByWord(word, this.lessUsed);
   }

   private void addByWord(String word, Set<Word> set) throws NoSuchWordFoundException {
      try {
         set.add(this.dictionary.searchExactly(word));
      } catch (NoSuchWordFoundException ignored) {}
   }

   public void addMostUsedByWord(Word word) {
      this.mostUsed.add(word);
   }

    public void addLessUsedByWord(Word word) {
        this.lessUsed.add(word);
    }

   public Set<Integer> getMostUsedIds() {
      return this.getIds(this.mostUsed);
   }

   public Set<Integer> getLessUsedIds() {
      return this.getIds(this.lessUsed);
   }

   private Set<Integer> getIds(Set<Word> words) {
      Set<Integer> ids = new HashSet();
      for (Word word : words) {
         ids.add(word.getId());
      }

      return ids;
   }

   public Set<Word> getMostUsedWords() {
      return this.mostUsed;
   }

   public Set<Word> getLessUsedWords() {
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
}
