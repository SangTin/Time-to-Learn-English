package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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

      public int getLowerBound(Object o) {
            return Collections.binarySearch(words, o, wordTargetCompartor);
      }

      public void insert(Word newWord) {
            int index = getLowerBound(newWord);
            if (index < 0)
                  index = -(index + 1);
            words.add(index, newWord);
      }

      public void remove(String newWord) {
            int index = getLowerBound(newWord);
            if(index >= 0) {
                  words.remove(index);
            }
      }

      public Word[] search(String newString) {
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

      public void fix(Word newWord) {
            int index = getLowerBound(newWord.getWordTarget());
            if(index < 0) {
                  insert(newWord);
            } else {
                  words.set(index, newWord);
            }
      }

      public Word searchExactly(String newString) {
            int index = getLowerBound(newString);
            if(index >= 0) {
                  return words.get(index);
            }
            return null;
      }
}
