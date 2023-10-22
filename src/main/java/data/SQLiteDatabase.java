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

import gui.GraphicalDictionary;
import javafx.scene.control.ProgressBar;

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
    }

    /**
     * Connect to the database
     *
     * @param dbName the name of the database file
     * @return the Connection object
     */
    private Connection connect(String dbName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + PATH + dbName;
        if (conn != null) {
            return conn;
        }
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Create a new table in the database
     *
     * @param tableName the name of the table
     */
    public void createNewTable(String tableName) {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + """
                (
                    id integer PRIMARY KEY,
                    word_target text NOT NULL,
                    word_explain text NOT NULL,
                    ipa_us text,
                    ipa_uk text
                );
                """;
        
        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
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
    public void update(String tableName, int id, Word word) {
        createNewTable(tableName);
        String sql = "INSERT INTO " + tableName + "(id, word_target, word_explain, ipa_us, ipa_uk) "
                + "VALUES(?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET "
                + "word_target = excluded.word_target, word_explain = excluded.word_explain, " 
                + "ipa_us = excluded.ipa_us, ipa_uk = excluded.ipa_uk";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, word.getWordTarget());
            pstmt.setString(3, word.getWordExplain());
            pstmt.setString(4, word.getUsPron());
            pstmt.setString(5, word.getUkPron());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a row specified by the id
     * 
     * @param tableName the name of the table
     * @param id the id of the word
     */
    public void delete(String tableName, int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = " + id;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the table
     * 
     * @param tableName
     * @param word
     */
    public void insert(String tableName, Word word) {
        createNewTable(tableName);
        String sql = "INSERT INTO " + tableName + "(word_target, word_explain, ipa_us, ipa_uk) VALUES(?,?,?,?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, word.getWordTarget());
            pstmt.setString(2, word.getWordExplain());
            pstmt.setString(3, word.getUsPron());
            pstmt.setString(4, word.getUkPron());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Import data from the database
     * 
     * @param tableName the name of the table
     */
    public ArrayList<Word> importFromDatabase(String tableName) {
        if (conn == null) {
            return new ArrayList<Word>();
        }
        
        ArrayList<Word> words = new ArrayList<Word>();
        Thread thread = new ImportDataThread(conn, tableName, words);
        try {
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    private static class ImportDataThread extends Thread {
        private Connection conn;
        private String tableName;
        private ArrayList<Word> words;

        public ImportDataThread(Connection conn, String tableName, ArrayList<Word> words) {
            this.conn = conn;
            this.tableName = tableName;
            this.words = words;
        }

        @Override
        public void run() {
            String sqlSelect = "SELECT * FROM " + tableName;
            String sqlCount = "SELECT COUNT(*) FROM " + tableName;
            try (Statement stmt = conn.createStatement()){
                ResultSet rs = stmt.executeQuery(sqlCount);
                int count = rs.getInt(1);
                ProgressBar loadingProgressBar = (ProgressBar) GraphicalDictionary.getLoadingProgressBar();
                loadingProgressBar.setProgress(0);
                loadingProgressBar.setVisible(true);

                rs = stmt.executeQuery(sqlSelect);
                // loop through the result set
                while (rs.next()) {
                    Word word = new Word();
                    word.setWordTarget(rs.getString("word_target"));
                    word.setWordExplain(rs.getString("word_explain"));
                    word.setUsPron(rs.getString("ipa_us"));
                    word.setUkPron(rs.getString("ipa_uk"));
                    words.add(word);

                    loadingProgressBar.setProgress((double) rs.getRow() / count);
                }

                loadingProgressBar.setVisible(false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
            Collections.sort(this.words, (Word a, Word b) -> {
                  return a.getWordTarget().compareToIgnoreCase(b.getWordTarget());
            });
        }
    }

    /**
     * Export data to the database
     * 
     * @param words the list of words
     * @param tableName the name of the table
     */
    public void exportToDatabase(ArrayList<Word> words, String tableName) {
        createNewTable(tableName);
        deleteAll(tableName);
        String sql = "INSERT INTO " + tableName + "(word_target, word_explain, ipa_us, ipa_uk) VALUES(?,?,?,?)";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Word word : words) {
                pstmt.setString(1, word.getWordTarget());
                pstmt.setString(2, word.getWordExplain());
                pstmt.setString(3, word.getUsPron());
                pstmt.setString(4, word.getUkPron());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
}
