package data;

import exception.editWord.ExistingWordException;
import exception.editWord.NoSuchWordFoundException;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

public class TrieWord {
   private final TrieNode root = new TrieNode();

   private TrieNode searchNode(String word) throws NoSuchWordFoundException {
      String target = word.trim();
      TrieNode current = this.root;

      for(int i = 0; i < target.length(); ++i) {
         char c = target.charAt(i);
         TrieNode node = current.children.get(c);
         if (node == null) {
            throw new NoSuchWordFoundException(word);
         }

         current = node;
      }

      return current;
   }

   private void insert(TrieNode current, Word word, String target, int index) throws ExistingWordException {
      if (index >= target.length()) {
         if (current.word != null) {
            throw new ExistingWordException(current.word);
         } else {
            current.word = word;
            ++current.count;
         }
      } else {
         char c = target.charAt(index);
         TrieNode node = current.children.get(c);
         if (node == null) {
            node = new TrieNode();
            current.children.put(c, node);
         }

         this.insert(node, word, target, index + 1);
         ++current.count;
      }
   }

   public void insert(Word word) throws ExistingWordException {
      this.insert(this.root, word, word.getWordTarget(), 0);
   }

   private Word remove(TrieNode current, String word, int index) throws NoSuchWordFoundException {
      if (index >= word.length()) {
         if (current.word == null) {
            throw new NoSuchWordFoundException(word);
         } else {
            --current.count;
            Word result = current.word;
            current.word = null;
            return result;
         }
      } else {
         char c = word.charAt(index);
         TrieNode node = current.children.get(c);
         if (node == null) {
            throw new NoSuchWordFoundException(word);
         } else {
            Word result = this.remove(node, word, index + 1);
            --current.count;
            if (node.count == 0) {
               current.children.remove(c);
            }
            return result;
         }
      }
   }

   public Word remove(String word) throws NoSuchWordFoundException {
      return this.remove(this.root, word.trim(), 0);
   }

   public Word searchExactly(String word) throws NoSuchWordFoundException {
      TrieNode node = this.searchNode(word);
      if (node.word == null) {
         throw new NoSuchWordFoundException(word);
      } else {
         return node.word;
      }
   }

   public void fix(Word word) throws NoSuchWordFoundException {
      TrieNode node = this.searchNode(word.getWordTarget());
      if (node.word == null) {
         throw new NoSuchWordFoundException(word.getWordTarget());
      } else {
         node.word.set(word);
      }
   }

   public Word[] search(String word) {
      return this.searchLimit(word, -1);
   }

   public Word[] searchLimit(String word, int limit) {
      TrieNode node;
      try {
         node = this.searchNode(word);
      } catch (NoSuchWordFoundException var6) {
         return new Word[0];
      }

      Stack<TrieNode> stack = new Stack<>();
      ArrayList<Word> list = new ArrayList<>();
      stack.add(node);
      while (!stack.isEmpty()) {
         TrieNode current = stack.pop();
         if (current.word != null) {
            list.add(current.word);
            if (limit >= 0 && list.size() == limit) {
               break;
            }
         }
         stack.addAll(current.children.values().stream().toList().reversed());
      }

      return list.toArray(new Word[0]);
   }

   public Word[] showALl() {
      return this.search("");
   }

   private static class TrieNode {

      private final TreeMap<Character, TrieNode> children = new TreeMap<>();
      private Word word = null;
      private int count = 0;

      public TrieNode() {
      }
   }
}
