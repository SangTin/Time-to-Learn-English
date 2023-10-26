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
                        } else if (o2 instanceof Integer) {
                              return ((Word) o1).compareTo((Integer) o2);
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

      public boolean insert(Word newWord) {
            if (newWord == null) {
                  return false;
            }
            
            int index = getLowerBound(newWord);
            if (index < 0) {
                  index = -(index + 1);
                  words.add(index, newWord);
                  return true;
            }
            return false;
      }

      public boolean remove(String newWord) {
            if (newWord == null || newWord.isEmpty()) {
                  return false;
            }

            normalizeWord(newWord);
            int index = getLowerBound(newWord);
            if(index >= 0) {
                  words.remove(index);
                  return true;
            }
            return false;
      }

      public boolean remove(Integer id) {
            if (id == null) {
                  return false;
            }

            int index = getLowerBound(id);
            if(index >= 0) {
                  words.remove(index);
                  return true;
            }
            return false;
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

      public boolean fix(Word newWord) {
            if (newWord == null) {
                  return false;
            }
            
            int index = getLowerBound(newWord.getWordTarget());
            if(index >= 0) {
                  words.set(index, newWord);
                  return true;
            }
            return false;
      }

      public Word searchExactly(String newString) {
            if (newString == null || newString.isEmpty()) {
                  return null;
            }

            normalizeWord(newString);
            int index = getLowerBound(newString);
            if(index >= 0) {
                  return words.get(index);
            }
            return null;
      }

      public Word searchExactly(Integer id) {
            if (id == null) {
                  return null;
            }

            int index = getLowerBound(id);
            if(index >= 0) {
                  return words.get(index);
            }
            return null;
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
