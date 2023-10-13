package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLiteDatabase {
    private Connection conn;

    /**
     * Constructor
     *
     * @param dbName the name of the database file
     */
    public SQLiteDatabase(String dbName) {
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
        String url = "jdbc:sqlite:data/database/" + dbName;
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
     * Update data in the table specified by the id
     * 
     * @param tableName the name of the table
     * @param id the id of the word
     * @param word the word
     */
    public void update(String tableName, int id, Word word) {
        createNewTable(tableName);
        String sql = "UPDATE " + tableName + " SET word_target = ?, word_explain = ?, ipa_us = ?, ipa_uk = ? WHERE id = " + id;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
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
    public ArrayList<Word> importFromDatabase(String tableName){
        String sql = "SELECT id, word_target, word_explain, ipa_us, ipa_uk FROM " + tableName;
        
        ArrayList<Word> words = new ArrayList<Word>();
        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                Word word = new Word();
                word.setWordTarget(rs.getString("word_target"));
                word.setWordExplain(rs.getString("word_explain"));
                word.setUsPron(rs.getString("ipa_us"));
                word.setUkPron(rs.getString("ipa_uk"));
                words.add(word);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return words;
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
}
