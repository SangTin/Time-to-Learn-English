package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.util.Pair;

public class WordTargetList {
    private ArrayList<String> List = new ArrayList<>();

    public WordTargetList() {}

    public WordTargetList(List<Word> words) {
        for (Word word : words) {
            List.add(word.getWordTarget());
        }
    }

    public String getIndex(int i) {
        return List.get(i);
    }
    public int getLowerBound(String newString) {
        int index = Collections.binarySearch(List, newString, String.CASE_INSENSITIVE_ORDER);
        return index;
    }
    public int addWordTarget(String newString) {
        int index = getLowerBound(newString);
        if (index < 0)
            index = -(index + 1);
        List.add(index, newString);
        return index;
    }

    public void removeWordTarget(String oldString) {
        int idx = searchExactly(oldString);
        if (idx >= 0) List.remove(oldString);
    }

    public int searchExactly(String newString) {
        int index = Collections.binarySearch(List, newString, String.CASE_INSENSITIVE_ORDER);
        if (index >= 0)
            return index;
        return -1;
    }

    public Pair<Integer, Integer> searchRange(String newString) {
        int start = getLowerBound(newString);
        if (start < 0)
            start = -(start + 1);
        newString += "~";
        int end = getLowerBound(newString);
        if (end < 0)
            end = -(end + 1);
        end -= 1;
        return new Pair<>(start, end);
    }

}
