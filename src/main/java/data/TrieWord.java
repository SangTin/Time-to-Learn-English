package data;

import exception.editWord.ExistingWordException;
import exception.editWord.NoSuchWordFoundException;

import java.util.ArrayList;
import java.util.Queue;
import java.util.TreeMap;

public class TrieWord {
    private static class TrieNode {
        private final TreeMap<Character, TrieNode> children;
        private Word word;
        private int count;

        public TrieNode() {
            children = new TreeMap<>();
            word = null;
            count = 0;
        }
    }

    private final TrieNode root;

    public TrieWord() {
        root = new TrieNode();
    }

    private void insert(TrieNode current, Word word, String target, int index) throws ExistingWordException {
        if (index >= target.length()) {
            if (current.word != null) {
                System.out.println(word.getWordTarget());
                System.out.println(current.word.getWordTarget());
                throw new ExistingWordException(word.getWordTarget());
            }
            current.word = word;
            ++current.count;
            return;
        }
        char c = target.charAt(index);
        TrieNode node = current.children.get(c);
        if (node == null) {
            node = new TrieNode();
            current.children.put(c, node);
        }
        insert(node, word, target, index + 1);
        ++current.count;
    }

    public void insert(Word word) throws ExistingWordException {
        insert(root, word, word.getWordTarget(), 0);
    }

    private void remove(TrieNode current, String word, int index) throws NoSuchWordFoundException {
        if (index >= word.length()) {
            if (current.word == null) {
                throw new NoSuchWordFoundException(word);
            }
            --current.count;
            current.word = null;
            return;
        }
        char c = word.charAt(index);
        TrieNode node = current.children.get(c);
        if (node == null) {
            throw new NoSuchWordFoundException(word);
        }
        remove(node, word, index + 1);
        --current.count;
        if (node.count == 0) {
            current.children.remove(c);
        }
    }

    public void remove(String word) throws NoSuchWordFoundException {
        remove(root, word.trim(), 0);
    }

    private TrieNode searchNode(String word) throws NoSuchWordFoundException {
        String target = word.trim();
        TrieNode current = root;
        for (int i = 0; i < target.length(); i++) {
            char c = target.charAt(i);
            TrieNode node = current.children.get(c);
            if (node == null) {
                throw new NoSuchWordFoundException(word);
            }
            current = node;
        }
        return current;
    }

    public Word searchExactly(String word) throws NoSuchWordFoundException {
        TrieNode node = searchNode(word);
        if (node.word == null) {
            throw new NoSuchWordFoundException(word);
        }
        return node.word;
    }

    public void fix(Word word) throws NoSuchWordFoundException {
        TrieNode node = searchNode(word.getWordTarget());
        if (node.word == null) {
            throw new NoSuchWordFoundException(word.getWordTarget());
        }
        node.word = word;
    }

    public Word[] search(String word) {
        TrieNode node;
        try {
            node = searchNode(word);
        } catch (NoSuchWordFoundException e) {
            return new Word[0];
        }
        Queue<TrieNode> queue = new java.util.LinkedList<>();
        ArrayList<Word> list = new ArrayList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            TrieNode current = queue.poll();
            if (current.word != null) {
                list.add(current.word);
            }
            queue.addAll(current.children.values());
        }
        return list.toArray(new Word[0]);
    }

    public Word[] showALl() {
        return search("");
    }
}
