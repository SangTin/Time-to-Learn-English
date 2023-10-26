package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import gui.LoadingProgressBar;
import javafx.concurrent.Task;

public class SQLiteDatabase {
    private static final String PATH = "data/database/";
    private Connection conn;

    /**
     * Normalize the database name
     * 
     * @param dbName the name of the database file
     */
    private static void normalizeDbName(String dbName) {
        if (!dbName.endsWith(".db")) {
            dbName += ".db";
        }
    }

    /**
     * Constructor
     *
     * @param dbName the name of the database file
     */
    public SQLiteDatabase(String dbName) {
        normalizeDbName(dbName);
        conn = connect(dbName);
        createTables();
    }

    /**
     * Connect to the database
     *
     * @param dbName the name of the database file
     * @return the Connection object
     */
    private Connection connect(String dbName) {
        if (conn != null) {
            return conn;
        }

        // SQLite connection string
        String url = "jdbc:sqlite:" + PATH + dbName;
        // Enable foreign key constraint
        String sql = "PRAGMA foreign_keys = ON";
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void createTables() {
        String wordsTable = """
            CREATE TABLE IF NOT EXISTS words(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                word_target TEXT
            );
        """;
        String meaningsTable = """
            CREATE TABLE IF NOT EXISTS meanings(
                id INTEGER PRIMARY KEY,
                word_explain TEXT,
                ipa_uk TEXT(110),
                ipa_us TEXT(110),
                
                FOREIGN KEY (id) REFERENCES words(id) ON DELETE CASCADE
            );     
        """;
        String synMeaningTable = """
            CREATE TABLE IF NOT EXISTS syn_meanings(
                meaning_id INTEGER PRIMARY KEY AUTOINCREMENT,
                word_id INTEGER NOT NULL,
                meaning TEXT,

                FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
            );    
        """;
        String synonymTable = """                
            CREATE TABLE IF NOT EXISTS synonyms(
                priority INTEGER NOT NULL DEFAULT 1,
                relation_id INTEGER NOT NULL,
                word_id INTEGER NOT NULL,
                synonym_id INTEGER NOT NULL,
                
                FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE,
                FOREIGN KEY (synonym_id) REFERENCES words(id) ON DELETE CASCADE,
                FOREIGN KEY (relation_id) REFERENCES syn_meanings(meaning_id) ON DELETE CASCADE
            );
        """;
        try (Statement stmt = conn.createStatement();) {
            stmt.addBatch(wordsTable);
            stmt.addBatch(meaningsTable);
            stmt.addBatch(synMeaningTable);
            stmt.addBatch(synonymTable);
            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete all rows in the table
     * 
     * @param tableName the name of the table
     */
    public void deleteAll(String tableName) {
        String sql = "DELETE FROM " + tableName;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * 1 data in the table specified by the id
     * 
     * @param tableName the name of the table
     * @param id the id of the word
     * @param word the word
     */
    public void updateWord(int id, Word word) {
        String sql = """
            INSERT INTO words(id, word_target) VALUES(?,?) 
            ON CONFLICT(id) DO UPDATE SET word_target = excluded.word_target;
        """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, word.getWordTarget());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = """
            INSERT INTO meanings(id, word_explain, ipa_uk, ipa_us) VALUES(?,?,?,?) 
            ON CONFLICT(id) 
            DO UPDATE SET word_explain = excluded.word_explain, ipa_uk = excluded.ipa_uk, ipa_us = excluded.ipa_us;
        """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, word.getWordExplain());
            pstmt.setString(3, word.getUkPron());
            pstmt.setString(4, word.getUsPron());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a word specified by the id
     * 
     * @param tableName the name of the table
     * @param id the id of the word
     */
    public void deleteWord(int id) {
        String sql = "DELETE FROM words WHERE id = " + id;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the table
     * New row will replace the old row if the id is duplicated
     * 
     * @param tableName
     * @param word
     */
    public void insertWord(Word word) {
        String sql = "INSERT INTO words (id, word_target) VALUES(?,?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, word.getId());
            pstmt.setString(2, word.getWordTarget());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "INSERT INTO meanings (id, word_explain, ipa_uk, ipa_us) VALUES(?,?,?,?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, word.getId());
            pstmt.setString(2, word.getWordExplain());
            pstmt.setString(3, word.getUkPron());
            pstmt.setString(4, word.getUsPron());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertSynonym() {
        
    }

    /**
     * Import data from the database
     * 
     * @param tableName the name of the table
     */
    public ArrayList<Word> importFromDatabase() {
        if (conn == null) {
            return new ArrayList<Word>();
        }
        
        ArrayList<Word> words = new ArrayList<Word>();

        Task<Void> loadData = new Task<>() {
            @Override public Void call() {
                String sqlSelect = """
                    SELECT wt.id, wt.word_target, we.word_explain, we.ipa_uk, we.ipa_us
                    FROM words wt
                    JOIN meanings we ON wt.id = we.id;
                """;
                String sqlCount = "SELECT COUNT(*) FROM words";
                try (Statement stmt = conn.createStatement()){
                    ResultSet rs = stmt.executeQuery(sqlCount);
                    int count = rs.getInt(1);
                    LoadingProgressBar.addTotalProgress(count);

                    rs = stmt.executeQuery(sqlSelect);
                    // loop through the result set
                    while (rs.next()) {
                        Word word = new Word();
                        word.setId(rs.getInt("id"));
                        word.setWordTarget(rs.getString("word_target"));
                        word.setWordExplain(rs.getString("word_explain"));
                        word.setUkPron(rs.getString("ipa_uk"));
                        word.setUsPron(rs.getString("ipa_us"));
                        words.add(word);

                        LoadingProgressBar.addCurrentProgress(1);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
                Collections.sort(words, (Word a, Word b) -> {
                    return a.getWordTarget().compareToIgnoreCase(b.getWordTarget());
                });
                return null;
            }
        };
        new Thread(loadData).start();

        return words;
    }

    /**
     * Export data to the database
     * 
     * @param words the list of words
     * @param tableName the name of the table
     */
    public static SQLiteDatabase exportToDatabase(String dbName, ArrayList<Word> words) {
        SQLiteDatabase database = new SQLiteDatabase(dbName);
        database.deleteAll("words");
        for (Word word : words) {
            database.insertWord(word);
        }
        return database;
    }

    /**
     * Backup the database. The new database will be named "backup_" + oldDbName.
     * This method will return the new database object connected to the new database.
     * 
     * @param oldDbName the name of the old database
     * @return the new database object
     */
    public static SQLiteDatabase backupDatabase(String oldDbName) {
        normalizeDbName(oldDbName);
        final String newDbName = "backup_" + oldDbName;
        try {
            Path oldDbPath = Paths.get(PATH + oldDbName);
            Path newDbPath = Paths.get(PATH + newDbName);
            Files.copy(oldDbPath, newDbPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLiteDatabase newDatabase = new SQLiteDatabase(newDbName);
        return newDatabase;
    }

    public static void main(String[] args) throws InterruptedException {
        SQLiteDatabase database1 = new SQLiteDatabase("test.db");
        database1.deleteAll("words");
        // SQLiteDatabase database2 = new SQLiteDatabase("test.db");
        // Word word1 = new Word("Hello", "Xin chao", "IPA", "IPA");
        // Word word2 = new Word("Goodbye", "Tam biet", "IPA", "IPA");
        // database1.insertWord(word1);
        // database2.insertWord(word2);
        // List<Word> words = database1.importFromDatabase();
        // Thread.sleep(1000);
        // System.out.println(words);
        // System.out.println(database2.importFromDatabase());
    }
}
