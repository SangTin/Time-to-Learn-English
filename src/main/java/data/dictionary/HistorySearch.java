package data.dictionary;

import data.Dictionary;
import data.SQLiteDatabase;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;

public class HistorySearch {
   private final UniqueWordStack words = new UniqueWordStack();
   protected final SQLiteDatabase database;
   protected final Dictionary dictionary;
   private boolean isLoaded = false;

   public HistorySearch(SQLiteDatabase database, Dictionary dictionary) {
      this.database = database;
      this.dictionary = dictionary;
   }

   public void load() {
      this.isLoaded = true;
      List<String> words = this.database.getHistorySearch();

      for (String word : words) {
         try {
            this.words.add(this.dictionary.searchExactly(word));
         } catch (Exception ignored) {
         }
      }

   }

   protected void reload() {
      if (!this.isLoaded) {
         this.load();
      }

   }

   public void add(Word word) {
      this.reload();
      this.words.add(word);
      this.database.insertHistorySearch(word.getId());
   }

   public void remove(Word word) {
      this.reload();
      this.words.remove(word);
      this.database.deleteHistorySearch(word.getId());
   }

   public void clear() {
      this.reload();
      this.words.clear();
      this.database.deleteAllHistorySearch();
   }

   public ObservableList<Word> getWords() {
      this.reload();
      return this.words;
   }

   private static class UniqueWordStack extends AutoDeleteWordList {
      protected void onProposedChange(List<Word> toBeAdded, int... indexes) {
         super.onProposedChange(toBeAdded, indexes);
         for (Word e : toBeAdded) {
            this.remove(e);
         }
      }

      public boolean add(Word e) {
         onProposedChange(Collections.singletonList(e), size(), size());
         addFirst(e);
         return true;
      }
   }
}
