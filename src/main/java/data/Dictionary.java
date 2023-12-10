package data;

import data.dictionary.AutoDeleteWordList;
import data.dictionary.HistorySearch;
import data.dictionary.TrieWord;
import data.dictionary.Word;
import exception.editWord.EditWordException;
import exception.editWord.NoSuchWordFoundException;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;


public class Dictionary {
      private final TrieWord trieWord;
      private SQLiteDatabase database;
      private HistorySearch historySearch;
      private final AutoDeleteWordList favouriteSearch;

      public Dictionary() {
            trieWord = new TrieWord();
            favouriteSearch = new AutoDeleteWordList();
            favouriteSearch.addListener((ListChangeListener<Word>) c -> {
                  while (c.next()) {
                        if (c.wasAdded()) {
                              for (Word word : c.getAddedSubList()) {
                                    word.setFavourite(true);
                                    database.setFavourite(word);
                              }
                        }
                        if (c.wasRemoved()) {
                              for (Word word : c.getRemoved()) {
                                    word.setFavourite(false);
                                    database.setFavourite(word);
                              }
                        }
                  }
            });
      }

      public Dictionary(SQLiteDatabase database) {
            this();
            setDatabase(database);
      }

      public Dictionary(ArrayList<Word> words) {
            this();
            setWords(words);
      }

      public void setDatabase(SQLiteDatabase database) {
            this.database = database;
            historySearch = new HistorySearch(database, this);
      }

      public void setWords(ArrayList<Word> words) {
            for (Word word : words) {
                  try {
                        insert(word);
                  } catch (EditWordException e) {
                        throw new RuntimeException(e);
                  }
            }
      }

      private String normalizeWord(String word) {
            return word.trim().replaceAll("_", " ");
      }

      public void insert(Word newWord) throws EditWordException {
            if (newWord == null) {
                  throw new NoSuchWordFoundException("Word is null");
            }

            trieWord.insert(newWord);
            if (newWord.isFavorite()) {
                  favouriteSearch.add(newWord);
            }
      }

      public void remove(String target) throws EditWordException {
            if (target == null || target.isEmpty()) {
                  throw new NoSuchWordFoundException("Word is null");
            }
            target = normalizeWord(target);
            Word word = trieWord.remove(target);
            historySearch.remove(word);
            favouriteSearch.remove(word);
      }

      public void fix(Word newWord) throws EditWordException {
            if (newWord == null) {
                  throw new NoSuchWordFoundException("Word is null");
            }
            trieWord.fix(newWord);
      }

      public Word[] search(String target, int limit) {
            if (target == null) {
                  return null;
            }
            target = normalizeWord(target);
            return trieWord.searchLimit(target, limit);
      }

      public Word[] search(String target) {
            if (target == null) {
                  return null;
            }
            target = normalizeWord(target);
            return trieWord.search(target);
      }

      public Word searchExactly(String target) throws NoSuchWordFoundException {
            target = normalizeWord(target);
            return trieWord.searchExactly(target);
      }

      public boolean have(String target) {
            if (target == null || target.isEmpty()) {
                  return false;
            }
            try {
                  trieWord.searchExactly(target);
                  return true;
            } catch (NoSuchWordFoundException e) {
                  return false;
            }
      }
      public Word[] showALl() {
            return trieWord.showALl();
      }

      public SQLiteDatabase getDatabase() {
            return database;
      }

      public HistorySearch getHistorySearch() {
            return historySearch;
      }

      public void insertHistory(Word word) {
            historySearch.add(word);
      }

      public void removeHistory(Word word) {
          historySearch.remove(word);
      }

      public void clearHistorySearch() {
            historySearch.clear();
      }

      public ObservableList<Word> getFavouriteSearch() {
            return favouriteSearch;
      }

      public void insertFavourite(Word word) {
          favouriteSearch.addFirst(word);
      }

      public void removeFavourite(Word word) {
          favouriteSearch.remove(word);
      }

      public void clearFavouriteSearch() {
            favouriteSearch.clear();
      }
}
