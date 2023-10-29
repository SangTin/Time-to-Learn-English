package data;

import exception.editWord.EditWordException;

public class Changes {
    private Dictionary dictionary;
    private SQLiteDatabase database;

    public Changes(Dictionary dictionary, String dbName) {
        this.dictionary = dictionary;
        this.database = new SQLiteDatabase(dbName);
    }

    public void insert(Word newWord) throws EditWordException {
        dictionary.insert(newWord);
        database.insertWord(newWord);
    }

    public void remove(Word newWord) throws EditWordException {
        dictionary.remove(newWord.getWordTarget());
        database.deleteWord(newWord.getId());
    }

    public void update(Word newWord) throws EditWordException {
        dictionary.fix(newWord);
        database.updateWord(newWord.getId(), newWord);
    }
}
