package data;

import java.util.HashSet;
import java.util.Set;

import data.enums.PartOfSpeech;
import data.enums.ThesaurusType;

public class Thesaurus {
    private final ThesaurusType type;
    private final PartOfSpeech partOfSpeech;
    private final Dictionary dictionary;
    private final String description;

    private Set<Integer> mostUsed;
    private Set<Integer> lessUsed;

    /**
     * Create a new thesaurus
     * 
     * @param description the description of the thesaurus
     * @param dictionary the dictionary that the thesaurus' words belong to
     * @param partOfSpeech the part of speech of the thesaurus' words
     * @param type the type of the thesaurus (synonym or antonym)
     * 
     * @see data.enums.PartOfSpeech
     * @see data.enums.ThesaurusType
     */
    public Thesaurus(String description, Dictionary dictionary, PartOfSpeech partOfSpeech, ThesaurusType type) {
        mostUsed = new HashSet<>();
        lessUsed = new HashSet<>();
        this.description = description;
        this.dictionary = dictionary;
        this.partOfSpeech = partOfSpeech;
        this.type = type;
    }

    /**
     * Add a word to mostUsed set by id
     * This method is recommended
     * 
     * @param id the id of the word to add
     */
    public void addMostUsedById(int id) {
        mostUsed.add(id);
    }

    /** 
     * Add a word to mostUsed set by word
     * This method is slower than using id
     * 
     * @param word the word to add
     * @see addMostUsedById
     */
    public void addMostUsedByWord(String word) {
        Word w = dictionary.searchExactly(word);
        mostUsed.add(w.getId());
    }

    /**
     * Add a word to lessUsed set by id
     * This method is recommended
     * 
     * @param id the id of the word to add
     */
    public void addLessUsedById(int id) {
        lessUsed.add(id);        
    }

    /**
     * Add a word to lessUsed set
     * This method is slower than using id
     * 
     * @param word the word to add
     * @see addLessUsedById
     */
    public void addLessUsedByWord(String word) {
        Word w = dictionary.searchExactly(word);
        lessUsed.add(w.getId());
    }

    /**
     * Get a set of most used words
     * This set contains words' target
     * 
     * @return a set of most used words
     */
    public Set<String> getMostUsedWords() {
        Set<String> words = new HashSet<>();
        for (Integer id : mostUsed) {
            words.add(dictionary.searchExactly(id).getWordTarget());
        }
        return words;
    }

    /**
     * Get a set of less used words
     * This set contains words' target
     * 
     * @return a set of less used words
     */
    public Set<String> getLessUsedWords() {
        Set<String> words = new HashSet<>();
        for (Integer id : lessUsed) {
            words.add(dictionary.searchExactly(id).getWordTarget());
        }
        return words;
    }

    /**
     * Get a set of most used words
     * This set contains words' id
     * 
     * @return a set of most used words
     */
    public Set<Integer> getMostUsedIds() {
        return mostUsed;
    }

    /**
     * Get a set of less used words
     * This set contains words' id
     * 
     * @return a set of less used words
     */
    public Set<Integer> getLessUsedIds() {
        return lessUsed;
    }

    /**
     * Get the part of speech of the thesaurus' words
     * 
     * @return the part of speech of the thesaurus' words
     * @see data.enums.PartOfSpeech
     */
    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    /**
     * Get the description of the thesaurus
     * 
     * @return the description of the thesaurus
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the type of the thesaurus
     * 
     * @return the type of the thesaurus (synonym or antonym)
     * @see data.enums.ThesaurusType
     */
    public ThesaurusType getType() {
        return type;
    }
}
