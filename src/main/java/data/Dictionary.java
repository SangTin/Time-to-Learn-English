package data;

import java.util.ArrayList;

import org.javatuples.Pair;

public class Dictionary {
      public ArrayList<Word> words = new ArrayList<>();
      public WordTargetList wordTarget = new WordTargetList();
      
      public void insert(Word newWord) {
            wordTarget.addWordTarget(newWord.getWordTarget());
            int index = wordTarget.getLowerBound(newWord.getWordTarget());
            if (index < 0)
                index = -(index + 1);
            words.add(index, newWord);
      }

      public void remove(Word newWord) {
            words.remove(newWord);
            wordTarget.removeWordTarget(newWord.getWordTarget());
      }
      
      public String[] search(String newString) {
            Pair<Integer, Integer> range = wordTarget.searchRange(newString);
            int start = range.getValue0();
            int end = range.getValue1();
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
