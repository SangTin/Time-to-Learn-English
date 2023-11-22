package data;

import exception.editWord.EditWordException;
import exception.editWord.ExistingWordException;
import exception.editWord.NoSuchWordFoundException;

import java.util.ArrayList;


public class Dictionary {
      TrieWord trieWord;

      public Dictionary() {
            trieWord = new TrieWord();
      }

      public Dictionary(ArrayList<Word> words) {
            trieWord = new TrieWord();
            setWords(words);
      }

      public void setWords(ArrayList<Word> words) {
            for (Word word : words) {
                  try {
                        trieWord.insert(word);
                  } catch (ExistingWordException e) {
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
      }

      public void remove(String target) throws EditWordException {
            if (target == null || target.isEmpty()) {
                  throw new NoSuchWordFoundException("Word is null");
            }
            target = normalizeWord(target);
            trieWord.remove(target);
      }

      public void fix(Word newWord) throws EditWordException {
            if (newWord == null) {
                  throw new NoSuchWordFoundException("Word is null");
            }

            trieWord.fix(newWord);
      }

      public Word[] search(String target) {
            if (target == null || target.isEmpty()) {
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
            target = normalizeWord(target);
            try {
                  trieWord.searchExactly(target);
                  return true;
            } catch (NoSuchWordFoundException e) {
                  return false;
            }
      }
}
