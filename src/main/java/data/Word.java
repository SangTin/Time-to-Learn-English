package data;

public class Word {
    private String wordTarget;
    private String wordExplain;
    private String usPron;
    private String ukPron;

    public Word() {

    }
    
    public Word(Word word) {
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

    public void setWord(Word word) {
        this.wordTarget = word.getWordTarget();
        this.wordExplain = word.getWordExplain();
        this.usPron = word.getUsPron();
        this.ukPron = word.getUkPron();
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
}
