package data;

public class Word {
    private String wordTarget;
    private String wordExplain;
    private String pronunciation;

    public Word() {

    }

    public Word(String wordTarget, String wordExplain, String pronunciation) {
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
        this.pronunciation = pronunciation;
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

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }
}
