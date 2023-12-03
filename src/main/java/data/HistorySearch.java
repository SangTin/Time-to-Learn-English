package data;

import com.sun.javafx.collections.VetoableListDecorator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayDeque;
import java.util.List;

public class HistorySearch {
   private final UniqueStack<Word> words = new UniqueStack<>();
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

   private static class UniqueStack<E> extends VetoableListDecorator<E> {
      public UniqueStack() {
            super(FXCollections.observableArrayList(new ArrayDeque<>()));
      }

      protected void onProposedChange(List<E> toBeAdded, int... indexes) {
         for (E e : toBeAdded) {
            this.remove(e);
         }
      }

      public boolean add(E e) {
         FXCollections.reverse(this);
         super.add(e);
         FXCollections.reverse(this);
         return true;
      }
   }
}
