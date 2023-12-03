package entry;

import data.Dictionary;
import data.Word;
import exception.editWord.EditWordException;
import exception.editWord.NoSuchWordFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CommandLine {
    static Scanner sc = new Scanner(System.in);
    private final Dictionary words = new Dictionary();

    public void Init() {
    }

    public void showMenu() {
        System.out.print("\033\143");
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
                + "Your action: ");

        int type = sc.nextInt();
        switch (type) {
            case 0:
                break;
            case 1:
                Add();
                break;
            case 2:
                Remove();
                break;
            case 3:
                Update();
                break;
            case 4:
                Display();
                break;
            case 5:
                Lookup();
                break;
            case 6:
                Search();
                break;
            case 7:
                game();
                break;
            case 8:
                importFromFile();
                break;
            case 9:
                exportToFile();
                break;
            default:
                break;
        }
    }

    public void Add() {
        System.out.print("\033\143");
        System.out.print("Number of words you want to add: ");
        int n = sc.nextInt();
        System.out.println();
        ArrayList<String> summary = new ArrayList<>();
        for (int i = 1; i <= n; ++i) {
            System.out.print("Word target: ");
            String wordTarget = sc.next();
            System.out.print("Word explain: ");
            String wordExplain = sc.nextLine();
            wordExplain = sc.nextLine();
            Word newWord = new Word(wordTarget, wordExplain);

            try {
                words.insert(newWord);
                summary.add(wordTarget);
            } catch (EditWordException e) {
                System.out.println(e.getMessage());
                System.out.print("Do you want to overwrite this word?\nYes or No\nYour choice: ");
                String answer = sc.next();
                answer = answer.toLowerCase();
                if (answer.equals("yes")) {
                    try {
                        words.fix(newWord);
                    } catch (EditWordException ee) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            System.out.println();
        }

        System.out.println("Sum up the words you added or changed!");
        System.out.println();
        System.out.println("No    |    English    |    Vietnamese");
        for (int i = 0; i < summary.size(); ++i) {
            try {
            Word word = words.searchExactly(summary.get(i));
            System.out.printf("%-6s", i + 1);
            System.out.printf("%-5s", "|");
            System.out.printf("%-11s", word.getWordTarget());
            System.out.printf("%-5s", "|");
            System.out.println(word.getWordExplain());
            } catch (NoSuchWordFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println();

        System.out.print("Press:\n[0] Exit\n[1] Show Menu\nYour choice: ");
        int type = sc.nextInt();
        if (type == 1) {
            showMenu();
        }
    }

    public void Remove() {
        System.out.print("\033\143");
        System.out.print("Word you want to remove: ");
        String wordString = sc.next();
        try {
            words.remove(wordString);
            System.out.println("This word is removed!");
        } catch (EditWordException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.print("Press:\n[0] Exit\n[1] Show Menu\nYour choice: ");
        int type = sc.nextInt();
        if (type == 1) {
            showMenu();
        }
    }

    public void Update() {
        System.out.print("\033\143");
        System.out.print("Word target: ");
        String wordTarget = sc.next();
        System.out.print("Word explain: ");
        String wordExplain = sc.nextLine();
        wordExplain = sc.nextLine();
        System.out.println();
        Word newWord = new Word(wordTarget, wordExplain);
        Word copy = new Word();
        try {
            copy = words.searchExactly(wordTarget);
            words.fix(newWord);
            System.out.println("You changed word explain!");
            System.out.println(copy.getWordExplain() + " ---------> " + wordExplain);
        } catch (NoSuchWordFoundException e) {
            System.out.println(e.getMessage());
            try {
                words.insert(newWord);
                System.out.println("You added 1 word!");
                System.out.println("Word target: " + wordTarget);
                System.out.println("Word explain: " + wordExplain);
            } catch (EditWordException e1) {
                e1.printStackTrace();
            }
        } catch (EditWordException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println();
        System.out.print("Press:\n[0] Exit\n[1] Show Menu\nYour choice: ");
        int type = sc.nextInt();
        if (type == 1) {
            showMenu();
        }
    }

    public void Display() {
        System.out.print("\033\143");
        Word[] allWords = words.showALl();
        int n = allWords.length;
        System.out.println("No    |    English    |    Vietnamese");
        for (int i = 0; i < n; ++i) {
            System.out.printf("%-6s", i + 1);
            System.out.printf("%-5s", "|");
            System.out.printf("%-11s", allWords[i].getWordTarget());
            System.out.printf("%-5s", "|");
            System.out.println(allWords[i].getWordExplain());
        }
        System.out.println();
        System.out.print("Press:\n[0] Exit\n[1] Show Menu\nYour choice: ");
        int type = sc.nextInt();
        if (type == 1) {
            showMenu();
        }
    }

    public void Lookup() {
        System.out.print("\033\143");
        System.out.print("Word you want to look up: ");
        String wordString = sc.next();
        try {
            Word word = words.searchExactly(wordString);
            System.out.println("Word target: " + word.getWordTarget());
            System.out.println("Word explain: " + word.getWordExplain());
        } catch (NoSuchWordFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.print("Press:\n[0] Exit\n[1] Show Menu\n[2] Continue to Look Up\nYour choice: ");
        int type = sc.nextInt();
        if (type == 1) {
            showMenu();
        } else if (type == 2) {
            Lookup();
        } else if (type == 2) {
            Lookup();
        }
    }

    public void Search() {
        System.out.print("\033\143");
        System.out.print("Word you want to search: ");
        String wordString = sc.next();
        Word[] result = words.search(wordString);
        System.out.print("Search result : ");
        for (int i = 0; i < result.length; ++i) {
            if (i < result.length - 1)
                System.out.print(result[i].getWordTarget() + ", ");
            else
                System.out.println(result[i].getWordTarget() + ".");
        }
        System.out.println();
        System.out.print("Press:\n[0] Exit\n[1] Show Menu\n[2] Continue to Search\n[3] Look up\nYour choice: ");
        int type = sc.nextInt();
        if (type == 1) {
            showMenu();
        } else if (type == 2) {
            Search();
        } else if (type == 3) {
            Lookup();
        } else if (type == 2) {
            Search();
        } else if (type == 3) {
            Lookup();
        }
    }

    public void game() {
    }

    public void importFromFile() {
        System.out.print("\033\143");
        System.out.println("Please add * before a word you want to overwrite");
        System.out.println();
        System.out.print("Name of file you want to import from: ");
        String fileName = sc.next();
        ArrayList<String> summary = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/CLI/" + fileName))) {
            String line;
            ArrayList<Word> wordDuplicate = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                // Tách từ tiếng Anh và giải thích tiếng Việt bằng dấu tab
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String wordTarget = parts[0];
                    String wordExplain = parts[1];
                    boolean overwrite = false;
                    if (wordTarget.charAt(0) == '*') {
                        wordTarget = wordTarget.substring(1);
                        overwrite = true;
                    }
                    Word newWord = new Word(wordTarget, wordExplain);
                    try {
                        words.insert(newWord);
                        summary.add(wordTarget);
                    } catch (EditWordException e) {
                        if (!overwrite) {
                        wordDuplicate.add(newWord);
                        } else {
                        try {
                            words.fix(newWord);
                        } catch (EditWordException ee) {
                            System.out.println(e.getMessage());
                        }
                        }
                    }
                    }
            }
            if (wordDuplicate.size() > 0) {
                System.out.println();
                System.out.println(
                        "Some words have multiple meanings, you can only choose one meaning, please enter its meaning!");
                for (int i = 0; i < wordDuplicate.size(); ++i) {
                    if (i == 0 || (i > 0 && !wordDuplicate.get(i).getWordTarget()
                            .equals(wordDuplicate.get(i - 1).getWordTarget()))) {
                        System.out.println();
                        System.out.println("* Word target: " + wordDuplicate.get(i).getWordTarget());
                        Word eWord;
                        try {
                            eWord = words.searchExactly(wordDuplicate.get(i).getWordTarget());
                            System.out.print("  Word explain: " + eWord.getWordExplain());
                            System.out.print(", " + wordDuplicate.get(i).getWordExplain());
                        } catch (NoSuchWordFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.print(", " + wordDuplicate.get(i).getWordExplain());
                    }
                    if (i == wordDuplicate.size() - 1 || (i != wordDuplicate.size() - 1 && !wordDuplicate.get(i)
                            .getWordTarget().equals(wordDuplicate.get(i + 1).getWordTarget()))) {
                        System.out.println(".");
                        System.out.print("Your choice: ");
                        String answer = sc.nextLine();
                        if (i == 0)
                            answer = sc.nextLine();
                            Word eWord = new Word(wordDuplicate.get(i).getWordTarget(), answer);
                            try {
                                words.fix(eWord);
                            } catch (EditWordException e) {
                                System.out.println(e.getMessage());
                            }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("Sum up the words you added!");
        System.out.println();
        System.out.println("No    |    English    |    Vietnamese");
        for (int i = 0; i < summary.size(); ++i) {
            try {
            Word word = words.searchExactly(summary.get(i));
            System.out.printf("%-6s", i + 1);
            System.out.printf("%-5s", "|");
            System.out.printf("%-11s", word.getWordTarget());
            System.out.printf("%-5s", "|");
            System.out.println(word.getWordExplain());
            } catch (NoSuchWordFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println();

        System.out.print("Press:\n[0] Exit\n[1] Show Menu\nYour choice: ");
        int type = sc.nextInt();
        if (type == 1) {
            showMenu();
        }
    }

    public void exportToFile() {
        System.out.print("\033\143");
        System.out.print("Name of file you want to export to: ");
        String fileName = sc.next();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/CLI/" + fileName))) {
            for (Word word : words.showALl()) {
                // Ghi từ tiếng Anh, sau đó dấu tab, và cuối cùng là giải thích tiếng Việt
                bw.write(word.getWordTarget() + "\t" + word.getWordExplain());
                bw.newLine(); // Thêm dòng mới sau mỗi cặp từ và giải thích
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.print("Press:\n[0] Exit\n[1] Show Menu\nYour choice: ");
        int type = sc.nextInt();
        if (type == 1) {
            showMenu();
        }
    }

    public static void main(String[] args) {
        CommandLine x = new CommandLine();
        x.showMenu();
    }
}

