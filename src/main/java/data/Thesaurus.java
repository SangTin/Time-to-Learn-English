package data;

import data.enums.PartOfSpeech;
import data.enums.ThesaurusType;
import exception.editWord.NoSuchWordFoundException;

import java.util.HashSet;
import java.util.Set;

public class Thesaurus {
    private final ThesaurusType type;
    private final PartOfSpeech partOfSpeech;
    private final Dictionary dictionary;
    private final String meaning;
    private final Set<String> mostUsed;
    private final Set<String> lessUsed;

    /**
     * Create a new thesaurus
     * 
     * @param meaning the meaning of the thesaurus
     * @param dictionary the dictionary that the thesaurus' words belong to
     * @param partOfSpeech the part of speech of the thesaurus' words
     * @param type the type of the thesaurus (synonym or antonym)
     * 
     * @see data.enums.PartOfSpeech
     * @see data.enums.ThesaurusType
     */
    public Thesaurus(String meaning, Dictionary dictionary, PartOfSpeech partOfSpeech, ThesaurusType type) {
        mostUsed = new HashSet<>();
        lessUsed = new HashSet<>();
        this.meaning = meaning;
        this.dictionary = dictionary;
        this.partOfSpeech = partOfSpeech;
        this.type = type;
    }

    /**
     * Create a new thesaurus
     * 
     * @param meaning the meaning of the thesaurus
     * @param description the description of the thesaurus
     * @param dictionary the dictionary that the thesaurus' words belong to
     * @param partOfSpeech the part of speech of the thesaurus' words
     * @param type the type of the thesaurus (synonym or antonym)
     * 
     * @see data.enums.PartOfSpeech
     * @see data.enums.ThesaurusType
     */
    public Thesaurus(String meaning, String description, Dictionary dictionary, PartOfSpeech partOfSpeech, ThesaurusType type) {
        this(meaning, dictionary, partOfSpeech, type);
    }

    /** 
     * Add a word to mostUsed set by word
     * This method is recommended
     * 
     * @param word the word to add
     */
    public void addMostUsedByWord(String word) throws NoSuchWordFoundException {
        dictionary.searchExactly(word);
        mostUsed.add(word);
    }

    /**
     * Add a word to lessUsed set
     * This method is slower than using id
     * 
     * @param word the word to add
     */
    public void addLessUsedByWord(String word) throws NoSuchWordFoundException {
        dictionary.searchExactly(word);
        lessUsed.add(word);
    }

    /**
     * Get a set of most used words' id
     */
    public Set<Integer> getMostUsedIds() {
        return getIds(mostUsed);
    }

    /**
     * Get a set of less used words' id
     */
    public Set<Integer> getLessUsedIds() {
        return getIds(lessUsed);
    }

    private Set<Integer> getIds(Set<String> words) {
        Set<Integer> ids = new HashSet<>();
        for (String word : lessUsed) {
            Word w;
            try {
                w = dictionary.searchExactly(word);
            } catch (NoSuchWordFoundException e) {
                continue;
            }
            ids.add(w.getId());
        }
        return ids;
    }

    /**
     * Get a set of most used words
     * This set contains words' target
     *
     * @return a set of most used words
     */
    public Set<String> getMostUsedWords() {
        return mostUsed;
    }

    /**
     * Get a set of less used words
     * This set contains words' target
     *
     * @return a set of less used words
     */
    public Set<String> getLessUsedWords() {
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
     * Get the meaning of the thesaurus
     * 
     * @return the meaning of the thesaurus
     */
    public String getMeaning() {
        return meaning;
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
