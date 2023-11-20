package data;

import java.util.ArrayList;

public class Word {
    private static int numWord = 0;

    private Integer id = ++numWord;
    private String wordTarget;
    private String wordExplain;
    private String usPron;
    private String ukPron;
    private ArrayList<Thesaurus> synonyms;
    private ArrayList<Thesaurus> antonyms;

    public Word() {}
    
    public Word(String wordTarget, String wordExplain) {
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
    }

    public Word(Word word) {
        this.id = word.getId();
        if (id > numWord) numWord = id;
        this.wordTarget = word.getWordTarget();
        this.wordExplain = word.getWordExplain();
        this.usPron = word.getUsPron();
        this.ukPron = word.getUkPron();
        this.synonyms = word.getSynonyms();
        this.antonyms = word.getAntonyms();
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

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id > numWord) numWord = id;
        this.id = id;
    }

    public ArrayList<Thesaurus> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<Thesaurus> synonyms) {
        this.synonyms = synonyms;
    }

    public ArrayList<Thesaurus> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(ArrayList<Thesaurus> antonyms) {
        this.antonyms = antonyms;
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
        if (!(obj instanceof Word word)) {
            return false;
        }
        return id.equals(word.id)
            && wordTarget.equals(word.wordTarget);
    }
}
