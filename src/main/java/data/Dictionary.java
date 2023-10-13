package data;

import java.util.ArrayList;
import java.util.Collections;

import javafx.util.Pair;


public class Dictionary {
      private ArrayList<Word> words;
      private WordTargetList wordTarget;
      
      public Dictionary() {
            words = new ArrayList<>();
            wordTarget = new WordTargetList();
      }

      public Dictionary(ArrayList<Word> words) {
            this.words = words;
            Collections.sort(this.words, (Word a, Word b) -> {
                  return a.getWordTarget().compareToIgnoreCase(b.getWordTarget());
            });
            wordTarget = new WordTargetList(words);
      }

      public ArrayList<Word> getWords() {
            return words;
      }

      public void setWords(ArrayList<Word> words) {
            this.words = words;
            wordTarget = new WordTargetList(words);
      }

      public WordTargetList getWordTarget() {
            return wordTarget;
      }

      public void insert(Word newWord) {
            int index = wordTarget.addWordTarget(newWord.getWordTarget());
            if (index < 0)
                index = -(index + 1);
            words.add(index, newWord);
      }

      public void remove(String newWord) {
            int index = wordTarget.getLowerBound(newWord);
            if(index >= 0) {
                  words.remove(index);
                  wordTarget.removeWordTarget(newWord);
            }
      }
      
      public String[] search(String newString) {
            Pair<Integer, Integer> range = wordTarget.searchRange(newString);
            int start = range.getKey();
            int end = range.getValue();
            int size = 0;
            String[] answer = new String[end-start+1];
            for(int i = start; i <= end; ++i) {
                answer[size++] = wordTarget.getIndex(i);
            }
            return answer;
      }
      
      public void fix(Word newWord) {
            int index = wordTarget.searchExactly(newWord.getWordTarget());
            if(index == -1) {
                  insert(newWord);
            } else {
                  words.set(index, newWord);
            }

      }
}
