package data;

public class Changes {
    private Dictionary dictionary;
    private SQLiteDatabase database;

    public Changes(Dictionary dictionary, String dbName) {
        this.dictionary = dictionary;
        this.database = new SQLiteDatabase(dbName);
    }

    public boolean insert(Word newWord) {
        if(!dictionary.insert(newWord)) return false;
        database.insertWord(newWord);
        return true;
    }

    public boolean remove(Word newWord) {
        if(!dictionary.remove(newWord.getWordTarget())) return false;
        database.deleteWord(newWord.getId());
        return true;
    }

    public boolean update(Word newWord) {
        if(!dictionary.fix(newWord)) return false;
        database.updateWord(newWord.getId(), newWord);
        return true;
    }
}
