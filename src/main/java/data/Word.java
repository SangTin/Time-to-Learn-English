package data;

public class Word implements Comparable<Word> {
    private static int numWord = 0;

    private Integer id = ++numWord;
    private String wordTarget;
    private String wordExplain;
    private String usPron;
    private String ukPron;

    public Word() {}
    
    public Word(String wordTarget, String wordExplain) {
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
    }

    public Word(Word word) {
        this.id = word.getId();
        this.wordTarget = word.getWordTarget();
        this.wordExplain = word.getWordExplain();
        this.usPron = word.getUsPron();
        this.ukPron = word.getUkPron();
    }

    public Word(String wordTarget, String wordExplain, String usPron, String ukPron) {
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
        this.usPron = usPron;
        this.ukPron = ukPron;
    }

    public Word(int id, String wordTarget, String wordExplain, String usPron, String ukPron) {
        this.id = id;
        if (id > numWord) numWord = id;
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
        this.usPron = usPron;
        this.ukPron = ukPron;
    }

    public void setWord(Word word) {
        this.id = word.getId();
        this.wordTarget = word.getWordTarget();
        this.wordExplain = word.getWordExplain();
        this.usPron = word.getUsPron();
        this.ukPron = word.getUkPron();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > numWord) numWord = id;
        this.id = id;
    }

    public String getWordTarget() {
        return wordTarget;
    }

    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    public String getWordExplain() {
        return wordExplain;
    }

    public void setWordExplain(String wordExplain) {
        this.wordExplain = wordExplain;
    }

    public String getUsPron() {
        return usPron;
    }

    public void setUsPron(String usPron) {
        this.usPron = usPron;
    }

    public String getUkPron() {
        return ukPron;
    }

    public void setUkPron(String ukPron) {
        this.ukPron = ukPron;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(wordTarget + "\n");
        if (usPron != null) sb.append("US: " + usPron + "\n");
        if (ukPron != null) sb.append("UK: " + ukPron + "\n");
        sb.append(wordExplain);
        
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Word)) {
            return false;
        }
        Word word = (Word) obj;
        return id == word.id
            && wordTarget.equals(word.wordTarget);
    }

    @Override
    public int compareTo(Word word) {
        return wordTarget.compareToIgnoreCase(word.getWordTarget());
    }

    public int compareTo(String word) {
        return wordTarget.compareToIgnoreCase(word);
    }

    public int compareTo(Integer wordId) {
        return id.compareTo(wordId);
    }
}
