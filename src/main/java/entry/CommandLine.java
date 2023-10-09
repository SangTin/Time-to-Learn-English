package entry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import data.Dictionary;
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
        System.out.println("Number of words you want to enter: ");
        int n = sc.nextInt();
        for (int i = 1; i <= n; ++i) {
            System.out.println("Word target: ");
            String wordTarget = sc.next();
            System.out.println("Word explain: ");
            String wordExplain = sc.nextLine();
            wordExplain = sc.nextLine();
            words.insert(new Word(wordTarget, wordExplain, null, null));
        }
    }

    public void Remove() {
        System.out.println("Word you want to remove: ");
        String wordString = sc.next();
        int index = words.wordTarget.getLowerBound(wordString);
        if (index < 0) {
            System.out.println("There is no that word in the dictionary!");
        } else {
            words.remove(words.words.get(index));
            System.out.println("This word is removed!");
        }
    }

    public void Update() {
        System.out.println("Word target: ");
        String wordTarget = sc.next();
        System.out.println("Word explain: ");
        String wordExplain = sc.nextLine();
        wordExplain = sc.nextLine();
        words.fix(new Word(wordTarget, wordExplain, null, null));
    }

    public void Display() {
        int n = words.words.size();
        System.out.println("No    |    English    |    Vietnamese");
        for(int i = 0; i < n; ++i) {
           System.out.printf("%-6s", Integer.toString(i+1));
           System.out.printf("%-5s", "|");
           System.out.printf("%-11s", words.words.get(i).getWordTarget());
           System.out.printf("%-5s", "|");
           System.out.println(words.words.get(i).getWordExplain());
        }
    }

    public void Lookup() {
        System.out.println("Word you want to look up: ");
        String wordString = sc.next();
        int index = words.wordTarget.getLowerBound(wordString);
        if (index < 0) {
            System.out.println("There is no that word in the dictionary!");
        } else {
            Word word = words.words.get(index);
            System.out.println("Word target: " + word.getWordTarget());
            System.out.println("Word explain: " + word.getWordExplain());
        }
    }

    public void Search() {
        System.out.println("Word you want to search: ");
        String wordString = sc.next();
        String[] result = words.search(wordString);
        System.out.print("Search result : ");
        for (int i = 0; i < result.length; ++i) {
            if (i < result.length - 1)
                System.out.print(result[i] + ", ");
        }
        System.out.println("...");
    }

    public void game() {}

    public void importFromFile() {
        // System.out.println("Name of file you want to import from: ");
        // String fileName = sc.next();
        // try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        //     String line;
        //     while ((line = br.readLine()) != null) {
        //         // Tách từ tiếng Anh và giải thích tiếng Việt bằng dấu tab
        //         String[] parts = line.split("\t");
        //         if (parts.length == 2) {
        //             String wordTarget = parts[0];
        //             String wordExplain = parts[1];
        //             words.insert(new Word(wordTarget, wordExplain, null, null));
        //         }
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    public void exportToFile() {
        // System.out.println("Name of file you want to export to: ");
        // String fileName = sc.next();
        // try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
        //     for (Word word : words.words) {
        //         // Ghi từ tiếng Anh, sau đó dấu tab, và cuối cùng là giải thích tiếng Việt
        //         bw.write(word.getWordTarget() + " " + word.getWordExplain());
        //         bw.newLine(); // Thêm dòng mới sau mỗi cặp từ và giải thích
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
    public static void main(String[] args) {
        CommandLine x = new CommandLine();
        boolean OK = true;
        while(OK) {
             x.showMenu();
             int type = sc.nextInt();
             switch(type) {
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
