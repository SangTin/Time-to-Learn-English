package data;

import data.dictionary.Word;
import data.enums.PartOfSpeech;
import data.enums.ThesaurusType;
import exception.editWord.NoSuchWordFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabase {
   private static final String PATH = "data/database/";
   private Connection conn;

   private static void normalizeDbName(String dbName) {
      if (!dbName.endsWith(".db")) {
         dbName = dbName + ".db";
      }

   }

   public SQLiteDatabase(String dbName) {
      normalizeDbName(dbName);
      this.conn = this.connect(dbName);
      this.createTables();
   }

   private Connection connect(String dbName) {
      if (this.conn != null) {
         return this.conn;
      } else {
         String url = "jdbc:sqlite:data/database/" + dbName;
         String sql = "PRAGMA foreign_keys = ON";

         try {
            this.conn = DriverManager.getConnection(url);
            Statement stmt = this.conn.createStatement();
            stmt.execute(sql);
         } catch (SQLException var5) {
            System.out.println(var5.getMessage());
         }

         return this.conn;
      }
   }

   private void createTables() {
      String wordsTable = """
         CREATE TABLE IF NOT EXISTS words(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            word_target TEXT
         );
      """;
      String meaningsTable = """
         CREATE TABLE IF NOT EXISTS "meanings" (
           	"id"	INTEGER,
           	"word_explain"	TEXT NOT NULL,
           	"ipa_uk"	TEXT(110),
           	"ipa_us"	TEXT(110),
           	"is_favourite"	REAL DEFAULT 0,
           	PRIMARY KEY("id"),
           	FOREIGN KEY("id") REFERENCES "words"("id") ON DELETE CASCADE
         );
      """;
      String thesaurusMeaningTable = """
         CREATE TABLE IF NOT EXISTS %1$s_meanings(
            meaning_id INTEGER PRIMARY KEY AUTOINCREMENT,
            word_id INTEGER NOT NULL,
            part_of_speech TEXT(20),
            meaning TEXT,
            
            FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
         );
      """;
      String thesaurusTable = """
         CREATE TABLE IF NOT EXISTS %1$ss(
            priority INTEGER NOT NULL DEFAULT 1,
            relation_id INTEGER NOT NULL,
            word_id INTEGER NOT NULL,
            
            FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE,
            FOREIGN KEY (relation_id) REFERENCES %1$s_meanings(meaning_id) ON DELETE CASCADE
         );
      """;
      String historySearchTable = """
         CREATE TABLE IF NOT EXISTS history_search(
            word_id INTEGER PRIMARY KEY,
            time real NOT NULL DEFAULT (julianday('now')),
            FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
         );
      """;

      try (Statement stmt = this.conn.createStatement()) {
         stmt.addBatch(wordsTable);
         stmt.addBatch(meaningsTable);
         for (ThesaurusType type : ThesaurusType.values()) {
            stmt.addBatch(String.format(thesaurusMeaningTable, type));
            stmt.addBatch(String.format(thesaurusTable, type));
         }
         stmt.addBatch(historySearchTable);
         stmt.executeBatch();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void deleteAll(String tableName) {
      String sql = "DELETE FROM " + tableName;

      try {
         Statement stmt = this.conn.createStatement();

         try {
            stmt.execute(sql);
         } catch (Throwable var7) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

          stmt.close();
      } catch (SQLException var8) {
         System.out.println(var8.getMessage());
      }

   }

   public void updateWord(int id, Word word) {
      String sql = "    UPDATE words SET word_target = ? WHERE id = ?;\n";

      PreparedStatement pstmt;
      try {
         pstmt = this.conn.prepareStatement(sql);

         try {
            pstmt.setString(1, word.getWordTarget());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
         } catch (Throwable var11) {
            if (pstmt != null) {
               try {
                  pstmt.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

          pstmt.close();
      } catch (SQLException var12) {
         System.out.println(var12.getMessage());
      }

      String meaningSql = """
         UPDATE meanings
         SET word_explain = ?, ipa_uk = ?, ipa_us = ?,
            is_favourite =
            CASE
               WHEN ? = 1 THEN julianday('now')
               ELSE 0
            END
         WHERE id = ?;
      """;

      try {
         pstmt = this.conn.prepareStatement(meaningSql);

         try {
            pstmt.setString(1, word.getWordExplain());
            pstmt.setString(2, word.getUkPron());
            pstmt.setString(3, word.getUsPron());
            pstmt.setInt(4, word.isFavorite() ? 1 : 0);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
         } catch (Throwable var9) {
            if (pstmt != null) {
               try {
                  pstmt.close();
               } catch (Throwable var7) {
                  var9.addSuppressed(var7);
               }
            }

            throw var9;
         }

          pstmt.close();
      } catch (SQLException var10) {
         System.out.println(var10.getMessage());
      }

   }

   public void deleteWord(int id) {
      String sql = "DELETE FROM words WHERE id = " + id;

      try {
         Statement stmt = this.conn.createStatement();

         try {
            stmt.execute(sql);
         } catch (Throwable var7) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

          stmt.close();
      } catch (SQLException var8) {
         System.out.println(var8.getMessage());
      }

   }

   public void insertWord(Word word) {
      String sql = """
         INSERT INTO words (id, word_target) VALUES(?,?)
      """;

      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setInt(1, word.getId());
         pstmt.setString(2, word.getWordTarget());
         pstmt.executeUpdate();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

      sql = """
         INSERT INTO meanings (id, word_explain, ipa_uk, ipa_us, is_favourite)
         VALUES(?,?,?,?,
            CASE
               WHEN ? = 1 THEN julianday('now')
               ELSE 0
            END)
      """;
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setInt(1, word.getId());
         pstmt.setString(2, word.getWordExplain());
         pstmt.setString(3, word.getUkPron());
         pstmt.setString(4, word.getUsPron());
         pstmt.setInt(5, word.isFavorite() ? 1 : 0);
         pstmt.executeUpdate();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

   }

   public void setFavourite(Word word) {
      String sql = """
          UPDATE meanings
          SET is_favourite = CASE
              WHEN ? = 1 THEN julianday('now')
              ELSE 0
          END
          WHERE id = ?;
      """;

      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setDouble(1, word.isFavorite() ? 1 : 0);
         pstmt.setInt(2, word.getId());
         pstmt.executeUpdate();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void insertHistorySearch(int wordId) {
      String sql = """
         INSERT OR REPLACE INTO history_search (word_id, time)
         VALUES(?, julianday('now'))
      """;

      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setInt(1, wordId);
         pstmt.executeUpdate();
         pstmt.executeUpdate();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public void deleteHistorySearch(int wordId) {
      String sql = String.format("""
         DELETE FROM history_search
         WHERE word_id = %d
      """, wordId);

      try {
         Statement stmt = this.conn.createStatement();

         try {
            stmt.execute(sql);
         } catch (Throwable var8) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         stmt.close();
      } catch (SQLException var9) {
         System.out.println(var9.getMessage());
      }

   }

   public void deleteAllHistorySearch() {
      String sql = """
         DELETE FROM history_search
      """;

      try (Statement stmt = conn.createStatement()) {
         stmt.execute(sql);
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public ArrayList<String> getHistorySearch() {
      String sql = """
         SELECT w.word_target
         FROM history_search s
         JOIN words w ON s.word_id = w.id
         ORDER BY s.time DESC;
      """;
      ArrayList<String> words = new ArrayList<>();

      try {
         Statement stmt = this.conn.createStatement();

         try {
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
               words.add(rs.getString("word_target"));
            }
         } catch (Throwable var8) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

          stmt.close();
      } catch (SQLException var9) {
         System.out.println(var9.getMessage());
      }

      return words;
   }

   public void addThesaurus(Word word, Thesaurus thesaurus) {
        String sql = String.format("""
            INSERT INTO %1$s_meanings (word_id, part_of_speech, meaning)
            VALUES(?,?,?)
        """, thesaurus.getType());
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, word.getId());
            pstmt.setString(2, thesaurus.getPartOfSpeech().toEnglish());
            pstmt.setString(3, thesaurus.getMeaning());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = String.format("""
            SELECT meaning_id FROM %1$s_meanings
            WHERE word_id = ? AND part_of_speech = ? AND meaning = ?
        """, thesaurus.getType());
        int meaningId = -1;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, word.getId());
            pstmt.setString(2, thesaurus.getPartOfSpeech().toEnglish());
            pstmt.setString(3, thesaurus.getMeaning());
            ResultSet rs = pstmt.executeQuery();
            meaningId = rs.getInt("meaning_id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (meaningId == -1) {
            return;
        }

        sql = String.format("""
            INSERT INTO %1$ss (priority, relation_id, word_id)
            VALUES(?,?,?)
        """, thesaurus.getType());
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Word word1 : thesaurus.getMostUsedWords()) {
                pstmt.setInt(1, 1);
                pstmt.setInt(2, meaningId);
                pstmt.setInt(3, word1.getId());
                pstmt.executeUpdate();
            }
            for (Word word1 : thesaurus.getLessUsedWords()) {
                pstmt.setInt(1, 2);
                pstmt.setInt(2, meaningId);
                pstmt.setInt(3, word1.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
   }

   public List<Thesaurus> getThesaurus(Word word, ThesaurusType type, Dictionary dictionary) {
      List<Thesaurus> thesauruses = new ArrayList<>();
      String sql = String.format("""
         SELECT meaning_id, meaning, part_of_speech
         FROM %1$s_meanings
         WHERE word_id = ?;
      """, type);
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setInt(1, word.getId());

         ResultSet rs = stmt.executeQuery();
         while (rs.next()) {
            int meaningId = rs.getInt("meaning_id");
            String meaning = rs.getString("meaning");
            PartOfSpeech partOfSpeech = PartOfSpeech.fromString(rs.getString("part_of_speech"));
            Thesaurus thesaurus = new Thesaurus(meaning, dictionary, partOfSpeech, type);

            Statement most = conn.createStatement();
            String mostSql = String.format("""
               SELECT word_target FROM
                  (SELECT word_id FROM %1$ss
                  WHERE relation_id = %2$d AND priority = 1) as ids
               INNER JOIN words ON ids.word_id = words.id;
            """, type, meaningId);
            ResultSet mostRs = most.executeQuery(mostSql);
            while (mostRs.next()) {
               String wordTarget = mostRs.getString("word_target");
               try {
                  thesaurus.addMostUsedByWord(dictionary.searchExactly(wordTarget));
               } catch (NoSuchWordFoundException ignored) {
               }
            }

            String lessSql = String.format("""
               SELECT word_target FROM
                  (SELECT word_id FROM %1$ss
                  WHERE relation_id = %2$d AND priority = 2) as ids
               INNER JOIN words ON ids.word_id = words.id;
            """, type, meaningId);
            ResultSet lessRs = most.executeQuery(lessSql);
            while (lessRs.next()) {
               String wordTarget = lessRs.getString("word_target");
               try {
                  thesaurus.addLessUsedByWord(dictionary.searchExactly(wordTarget));
               } catch (NoSuchWordFoundException ignored) {
               }
            }
            thesauruses.add(thesaurus);
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return thesauruses;
   }

   public ArrayList<Word> importFromDatabase() {
      if (this.conn == null) {
         return new ArrayList<>();
      } else {
         ArrayList<Word> words = new ArrayList<>();
         SQLiteDatabase.ImportThread importThread = new SQLiteDatabase.ImportThread(this.conn, words);
         importThread.start();
         return words;
      }
   }

   public void importToDictionary(Dictionary dictionary) {
      new Thread(() -> {
         ArrayList<Word> words = new ArrayList<>();
         SQLiteDatabase.ImportThread importThread = new SQLiteDatabase.ImportThread(this.conn, words);
         importThread.start();

         try {
            importThread.join();
            System.out.println("Imported " + words.size() + " words");
            dictionary.setWords(words);
            dictionary.getHistorySearch().load();
         } catch (InterruptedException var5) {
            System.out.println(var5.getMessage());
         }

      }).start();
   }

   public static SQLiteDatabase exportToDatabase(String dbName, ArrayList<Word> words) {
      SQLiteDatabase database = new SQLiteDatabase(dbName);
      database.deleteAll("words");

      for (Word word : words) {
        database.insertWord(word);
      }

      return database;
   }

   public static SQLiteDatabase backupDatabase(String oldDbName) {
      normalizeDbName(oldDbName);
      String newDbName = "backup_" + oldDbName;

      try {
         Path oldDbPath = Paths.get("data/database/" + oldDbName);
         Path newDbPath = Paths.get("data/database/" + newDbName);
         Files.copy(oldDbPath, newDbPath, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException var4) {
         System.out.println(var4.getMessage());
      }

      return new SQLiteDatabase(newDbName);
   }

   private static class ImportThread extends Thread {
      private final Connection conn;
      private final ArrayList<Word> words;

      public ImportThread(Connection conn, ArrayList<Word> words) {
         this.conn = conn;
         this.words = words;
      }

      public void run() {
         String sql = """
            SELECT w.id, w.word_target, m.word_explain, m.ipa_uk, m.ipa_us, m.is_favourite
            FROM words w
            INNER JOIN meanings m ON w.id = m.id
            WHERE m.word_explain IS NOT NULL
            ORDER BY m.is_favourite DESC;
         """;

         try {
            Statement stmt = this.conn.createStatement();

            try {
               ResultSet rs = stmt.executeQuery(sql);

               while(rs.next()) {
                  Word word = new Word();
                  word.setId(rs.getInt("id"));
                  word.setWordTarget(rs.getString("word_target"));
                  word.setWordExplain(rs.getString("word_explain"));
                  word.setUkPron(rs.getString("ipa_uk"));
                  word.setUsPron(rs.getString("ipa_us"));
                  word.setFavourite(rs.getDouble("is_favourite") > 0);
                  this.words.add(word);
               }
            } catch (Throwable var6) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

             stmt.close();
         } catch (SQLException var7) {
            System.out.println(var7.getMessage());
         }

      }
   }
}
