package data;

import java.util.ArrayList;

public class Trie {
    public class Node {
        ArrayList<String> recommendWord;
        Node[] child;
        Node dad;
        public Node() {}
        public Node(Node dad) {
            this.dad = dad;
            child = new Node[26];
            recommendWord = new ArrayList<>();
        }
    }
    Node root = new Node(null);
    public void addWord(String newWord) {
        Node current = root;
        for(int i = 0; i < newWord.length(); ++i) {
            int id = newWord.charAt(i) - 'a';
            if(current.child[id] == null) {
                current.child[id] = new Node(current);
            }
            current = current.child[id];
        }

        while(current != root) {
              if(current.recommendWord.size() < 10) current.recommendWord.add(newWord);
              current = current.dad;
        }
    }
    public void removeWord(String newWord) {
        Node current = root;
        for(int i = 0; i < newWord.length(); ++i) {
            int id = newWord.charAt(i) - 'a';
            current = current.child[id];
            if(current == null) break;
        }
        if(current != null) {
            while(current != root) {
              current.recommendWord.remove(newWord);
              current = current.dad;
            }
        }
    }
    public ArrayList<String> searchNode(String newWord) {
        Node current = root;
        for(int i = 0; i < newWord.length(); ++i) {
            int id = newWord.charAt(i) - 'a';
            current = current.child[id];
            if(current == null) break;
        }
        if(current != null) return current.recommendWord;
        return null;
    }
}
