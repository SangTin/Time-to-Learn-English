package entry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import data.Dictionary;
import data.SQLiteDatabase;
import data.Word;

public class CommandLine {
    static Scanner sc = new Scanner(System.in);
    private Dictionary words = new Dictionary();

    public void Init() {
    }

    public void showMenu() {
        System.out.print("Welcome to My Application!\n"
                + "[0] Exit\n"
                + "[1] Add\n"
                + "[2] Remove\n"
                + "[3] Update\n"
                + "[4] Display\n"
                + "[5] Lookup\n"
                + "[6] Search\n"
                + "[7] Game\n"
                + "[8] Import from file\n"
                + "[9] Export to file\n"
                + "Your action:");
    }

    public void Add() {
        System.out.print("Number of words you want to enter: ");
        int n = sc.nextInt();
        System.out.println();
        for (int i = 1; i <= n; ++i) {
            System.out.print("Word target: ");
            String wordTarget = sc.next();
            System.out.print("Word explain: ");
            String wordExplain = sc.nextLine();
            wordExplain = sc.nextLine();
            int index = words.wordTarget.getLowerBound(wordTarget);
            if (index < 0) {
                words.insert(new Word(wordTarget, wordExplain, null, null));
            } else {
                System.out.print(
                        "This word was added with another meaning\nDo you want overwrite this word?\nYes or No\nYour choice: ");
                String answer = sc.next();
                answer = answer.toLowerCase();
                if (answer == "yes")
                    words.words.get(index).setWordExplain(wordExplain);
            }
            System.out.println();
        }
    }

    public void Remove() {
        System.out.print("Word you want to remove: ");
        String wordString = sc.next();
        int index = words.wordTarget.getLowerBound(wordString);
        if (index < 0) {
            System.out.println("There is no that word in the dictionary!");
        } else {
            words.remove(wordString);
            System.out.println("This word is removed!");
        }
        System.out.println();
    }

    public void Update() {
        System.out.print("Word target: ");
        String wordTarget = sc.next();
        System.out.print("Word explain: ");
        String wordExplain = sc.nextLine();
        wordExplain = sc.nextLine();
        System.out.println();
        words.fix(new Word(wordTarget, wordExplain, null, null));
    }

    public void Display() {
        int n = words.words.size();
        System.out.println("No    |    English    |    Vietnamese");
        for (int i = 0; i < n; ++i) {
            System.out.printf("%-6s", Integer.toString(i + 1));
            System.out.printf("%-5s", "|");
            System.out.printf("%-11s", words.words.get(i).getWordTarget());
            System.out.printf("%-5s", "|");
            System.out.println(words.words.get(i).getWordExplain());
        }
        System.out.println();
    }

    public void Lookup() {
        System.out.print("Word you want to look up: ");
        String wordString = sc.next();
        int index = words.wordTarget.getLowerBound(wordString);
        if (index < 0) {
            System.out.println("There is no that word in the dictionary!");
        } else {
            Word word = words.words.get(index);
            System.out.println("Word target: " + word.getWordTarget());
            System.out.println("Word explain: " + word.getWordExplain());
        }
        System.out.println();
    }

    public void Search() {
        System.out.print("Word you want to search: ");
        String wordString = sc.next();
        System.out.println();
        String[] result = words.search(wordString);
        System.out.print("Search result : ");
        for (int i = 0; i < result.length; ++i) {
            if (i < result.length - 1)
                System.out.print(result[i] + ", ");
            else
                System.out.print(result[i] + ".");
        }
        System.out.println();
    }

    public void game() {
    }

    public void importFromFile() {
        System.out.print("Name of file you want to import from: ");
        String fileName = sc.next();
        try (BufferedReader br = new BufferedReader(new FileReader("data/CLI/" + fileName))) {
            String line;
            ArrayList<Word> wordDuplicate = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                // Tách từ tiếng Anh và giải thích tiếng Việt bằng dấu tab
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String wordTarget = parts[0];
                    String wordExplain = parts[1];
                    Word newWord = new Word(wordTarget, wordExplain, null, null);

                    int index = words.wordTarget.getLowerBound(wordTarget);
                    if (index < 0) {
                        words.insert(newWord);
                    } else {
                        wordDuplicate.add(newWord);
                    }
                }
            }
            if (wordDuplicate.size() > 0) {
                System.out.println(
                        "Some words have multiple meanings, you can only choose one meaning, please enter its meaning!");
                for (int i = 0; i < wordDuplicate.size(); ++i) {
                    if (i == 0 || (i > 0 && !wordDuplicate.get(i).getWordTarget()
                            .equals(wordDuplicate.get(i - 1).getWordTarget()))) {
                        System.out.println();
                        System.out.println("* Word target: " + wordDuplicate.get(i).getWordTarget());
                        int index = words.wordTarget.getLowerBound(wordDuplicate.get(i).getWordTarget());
                        System.out.print("  Word explain: " + words.words.get(index).getWordExplain());
                        System.out.print(", " + wordDuplicate.get(i).getWordExplain());
                    } else {
                        System.out.print(", " + wordDuplicate.get(i).getWordExplain());
                    }
                    if (i == wordDuplicate.size() - 1 || (i != wordDuplicate.size() - 1 && !wordDuplicate.get(i)
                            .getWordTarget().equals(wordDuplicate.get(i + 1).getWordTarget()))) {
                        System.out.println(".");
                        System.out.print("Your choice: ");
                        String answer = sc.nextLine();
                        if(i == 0) answer = sc.nextLine();
                        int index = words.wordTarget.getLowerBound(wordDuplicate.get(i).getWordTarget());
                        words.words.get(index).setWordExplain(answer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public void exportToFile() {
        System.out.print("Name of file you want to export to: ");
        String fileName = sc.next();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/CLI/" + fileName))) {
            for (Word word : words.words) {
                // Ghi từ tiếng Anh, sau đó dấu tab, và cuối cùng là giải thích tiếng Việt
                bw.write(word.getWordTarget() + "\t" + word.getWordExplain());
                bw.newLine(); // Thêm dòng mới sau mỗi cặp từ và giải thích
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        CommandLine x = new CommandLine();
        boolean OK = true;
        while (OK) {
            x.showMenu();
            int type = sc.nextInt();
            switch (type) {
                case 0:
                    OK = false;
                    break;
                case 1:
                    x.Add();
                    break;
                case 2:
                    x.Remove();
                    break;
                case 3:
                    x.Update();
                    break;
                case 4:
                    x.Display();
                    break;
                case 5:
                    x.Lookup();
                    break;
                case 6:
                    x.Search();
                    break;
                case 7:
                    x.game();
                    break;
                case 8:
                    x.importFromFile();
                    break;
                case 9:
                    x.exportToFile();
                    break;
                default:
                    OK = false;
                    break;
            }
        }
    }
}
