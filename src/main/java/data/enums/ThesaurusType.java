package data.enums;

public enum ThesaurusType {
    SYNONYM("synonym"),
    ANTONYM("antonym");

    private final String text;

    ThesaurusType(String text) {
        this.text = text;
    }

    /**
     * Get the thesaurus type from the string <p>
     * Create if the input text has the keyword of the thesaurus type <p>
     * English: synonym, antonym
     * 
     * @param text the string
     * @return the thesaurus type
     */
    public static String fromString(String text) {
        text = text.toLowerCase();
        for (ThesaurusType type : ThesaurusType.values()) {
            if (text.contains(type.text)) return type.text;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return text;
    }
}
