package data;
import java.util.ArrayList;

public class Dictionary {
    ArrayList<Word> words = new ArrayList<>();
    Trie wordTrie = new Trie();
    public void buildTrie(ArrayList<Word> database) {
         for(int i = 0; i < database.size(); ++i) {
            wordTrie.addWord(database.get(i).getWordTarget());
         }
    }
    public void updateTrie(String type, String newWord) {
        if(type == "add") wordTrie.addWord(newWord);
        else wordTrie.removeWord(newWord);
    }
    public ArrayList<String> search(String newWord) {
        return wordTrie.searchNode(newWord);
    }
}
