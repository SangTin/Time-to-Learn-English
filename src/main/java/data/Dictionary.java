package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import exception.editWord.EditWordException;
import exception.editWord.ExistingWordException;
import exception.editWord.NoSuchWordFoundException;


public class Dictionary {
      private ArrayList<Word> words;
      private static final WordStringComparator wordTargetCompartor = new WordStringComparator();

      public Dictionary() {
            words = new ArrayList<>();
      }

      public Dictionary(ArrayList<Word> words) {
            this.words = words;
            Collections.sort(this.words, (Word a, Word b) -> {
                  return a.getWordTarget().compareToIgnoreCase(b.getWordTarget());
            });
      }

      public ArrayList<Word> getWords() {
            return words;
      }

      public void setWords(ArrayList<Word> words) {
            this.words = words;
      }

      private static class WordStringComparator implements Comparator<Object> {
            @Override
            public int compare(Object o1, Object o2) {
                  if (o1 instanceof Word) {
                        if (o2 instanceof Word) {
                              return ((Word) o1).compareTo((Word) o2);
                        } else if (o2 instanceof String) {
                              return ((Word) o1).compareTo((String) o2);
                        }
                  }
                  throw new IllegalArgumentException("Cannot compare " + 
                        o1.getClass().getName() + " with " + o2.getClass().getName());
            }
      }

      private String normalizeWord(String word) {
            return word.trim().replaceAll("_", " ");
      }

      public int getLowerBound(Object o) {
            return Collections.binarySearch(words, o, wordTargetCompartor);
      }

      public void insert(Word newWord) throws EditWordException {
            if (newWord == null) {
                  throw new NoSuchWordFoundException("Word is null");
            }
            
            int index = getLowerBound(newWord);
            if (index >= 0) {
                  throw new ExistingWordException(words.get(index));
            }

            index = -(index + 1);
            words.add(index, newWord);
      }

      public void remove(String newWord) throws EditWordException {
            if (newWord == null || newWord.isEmpty()) {
                  throw new NoSuchWordFoundException("Word is null");
            }
      
            normalizeWord(newWord);
            int index = getLowerBound(newWord);
            if (index < 0) {
                  throw new NoSuchWordFoundException(newWord);
            }

            words.remove(index);
      }

      public void fix(Word newWord) throws EditWordException {
            if (newWord == null) {
                  throw new NoSuchWordFoundException("Word is null");
            }
            
            int index = getLowerBound(newWord.getWordTarget());
            if (index < 0) {
                  throw new NoSuchWordFoundException(newWord.getWordTarget());
            }

            words.set(index, newWord);
      }

      public Word[] search(String newString) {
            if (newString == null || newString.isEmpty()) {
                  return null;
            }

            normalizeWord(newString);
            int start = getLowerBound(newString);
            if (start < 0)
                start = -(start + 1);
            int end = getLowerBound(newString + Character.MAX_VALUE);
            if (end < 0)
                end = -(end + 1);
            int size = 0;
            Word[] answer = new Word[end-start];
            for(int i = start; i < end; ++i) {
                  answer[size++] = words.get(i);
            }
            return answer;
      }

      public Word searchExactly(String newString) throws NoSuchWordFoundException {
            if (newString == null || newString.isEmpty()) {
                  throw new NoSuchWordFoundException("Word is null");
            }

            normalizeWord(newString);
            int index = getLowerBound(newString);
            if (index < 0) {
                  throw new NoSuchWordFoundException(newString);
            }

            return words.get(index);
      }

      public boolean have(String newString) {
            if (newString == null || newString.isEmpty()) { 
                  return false;
            }

            normalizeWord(newString);
            int index = getLowerBound(newString);
            return index >= 0;
      }
}
