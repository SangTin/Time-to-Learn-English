package data;

import java.util.ArrayList;
import java.util.Collections;

import org.javatuples.Pair;

public class WordTargetList {
    private ArrayList<String> List = new ArrayList<>();
    public String getIndex(int i) {
        return List.get(i);
    }
    public int getLowerBound(String newString) {
        newString = newString.toLowerCase();
        int index = Collections.binarySearch(List, newString);
        return index;
    }
    public int addWordTarget(String newString) {
        newString = newString.toLowerCase();
        int index = getLowerBound(newString);
        if (index < 0)
            index = -(index + 1);
        List.add(index, newString);
        return index;
    }

    public void removeWordTarget(String oldString) {
        oldString = oldString.toLowerCase();
        List.remove(oldString);
    }

    public int searchExactly(String newString) {
        newString = newString.toLowerCase();
        int index = Collections.binarySearch(List, newString);
        if (index >= 0)
            return index;
        return -1;
    }

    public Pair<Integer, Integer> searchRange(String newString) {
        newString = newString.toLowerCase();
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
