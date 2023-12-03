package data;

import javafx.beans.property.SimpleBooleanProperty;

public class Word implements Comparable<Word> {
   private static int numWord = 0;
   private int id;
   private String wordTarget;
   private String wordExplain;
   private String usPron;
   private String ukPron;
   private SimpleBooleanProperty isFavorite;

   public Word() {
      this.id = ++numWord;
      this.isFavorite = new SimpleBooleanProperty(false);
   }

   public Word(String wordTarget, String wordExplain) {
      this.id = ++numWord;
      this.isFavorite = new SimpleBooleanProperty(false);
      this.wordTarget = wordTarget.trim();
      this.wordExplain = wordExplain;
   }

   public Word(Word word) {
      this.id = word.id;
      this.wordTarget = word.wordTarget;
      this.wordExplain = word.wordExplain;
      this.usPron = word.usPron;
      this.ukPron = word.ukPron;
      this.isFavorite = word.isFavorite;
   }

   public Word(String wordTarget, String wordExplain, String usPron, String ukPron) {
      this(wordTarget, wordExplain);
      this.usPron = usPron;
      this.ukPron = ukPron;
   }

   public Word(int id, String wordTarget, String wordExplain, String usPron, String ukPron) {
      this(wordTarget, wordExplain);
      this.id = id;
      if (id > numWord) {
         numWord = id;
      }

      this.usPron = usPron;
      this.ukPron = ukPron;
   }

   public int getId() {
      return this.id;
   }

   public void setId(Integer id) {
      if (id > numWord) {
         numWord = id;
      }

      this.id = id;
   }

   public void set(Word word) {
      this.id = word.id;
      this.wordTarget = word.wordTarget;
      this.wordExplain = word.wordExplain;
      this.usPron = word.usPron;
      this.ukPron = word.ukPron;
      this.isFavorite = word.isFavorite;
   }

   public String getWordTarget() {
      return this.wordTarget;
   }

   public void setWordTarget(String wordTarget) {
      this.wordTarget = wordTarget;
   }

   public String getWordExplain() {
      return this.wordExplain;
   }

   public void setWordExplain(String wordExplain) {
      this.wordExplain = wordExplain;
   }

   public String getUsPron() {
      return this.usPron;
   }

   public void setUsPron(String usPron) {
      this.usPron = usPron;
   }

   public String getUkPron() {
      return this.ukPron;
   }

   public void setUkPron(String ukPron) {
      this.ukPron = ukPron;
   }

   public boolean isFavorite() {
      return this.isFavorite.get();
   }

   public void setFavourite(boolean isFavorite) {
      this.isFavorite.set(isFavorite);
   }

   public SimpleBooleanProperty isFavoriteProperty() {
      return this.isFavorite;
   }

   public String toString() {
      return this.wordTarget;
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (!(obj instanceof Word word)) {
         return false;
      } else {
         return this.id == word.id
                 && this.wordTarget.equals(word.wordTarget);
      }
   }

   @Override
   public int compareTo(Word word) {
     return this.wordTarget.compareToIgnoreCase(word.wordTarget);
   }

   @Override
   public int hashCode() {
      return this.wordTarget.hashCode();
   }
}
