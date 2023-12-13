package data;

import data.dictionary.Word;
import exception.editWord.EditWordException;

public class Changes {
   private final Dictionary dictionary;
   private final SQLiteDatabase database;

   public Changes(Dictionary dictionary, String dbName) {
      this.dictionary = dictionary;
      this.database = new SQLiteDatabase(dbName);
   }

   public Changes(Dictionary dictionary, SQLiteDatabase database) {
      this.dictionary = dictionary;
      this.database = database;
   }

   public void insert(Word newWord) throws EditWordException {
      this.dictionary.insert(newWord);
      this.database.insertWord(newWord);
   }

   public void remove(Word newWord) throws EditWordException {
      this.dictionary.remove(newWord.getWordTarget());
      this.database.deleteWord(newWord.getId());
   }

   public void fix(Word newWord) throws EditWordException {
      this.dictionary.fix(newWord);
      this.database.updateWord(newWord.getId(), newWord);
   }
}
