package data.enums;

public enum PartOfSpeech {
    NOUN("danh từ"), VERB("động từ"), 
    ADJECTIVE("tính từ"), ADVERB("phó từ"), 
    PREPOSITION("giới từ"), CONJUNCTION("liên từ"), 
    INTERJECTION("thán từ"), ARTICLE("mạo từ");

    private final String text;

    PartOfSpeech(String text) {
        this.text = text;
    }

    /**
     * Get the part of speech from the English string <p>
     * Create if the input text has the keyword of the part of speech <p>
     * Part of speech: noun, verb, adjective, adverb, preposition, conjunction, interjection, article
     * 
     * @param text the English string
     * @return the part of speech
     */
    public static PartOfSpeech fromEnglish(String text) {
        text = text.toLowerCase();
        for (PartOfSpeech pos : PartOfSpeech.values()) {
            if (text.contains(pos.name().toLowerCase())) return pos;
        }
        return null;
    }

    /**
     * Get the part of speech from the Vietnamese string <p>
     * Create if the input text has the keyword of the part of speech <p>
     * Part of speech: danh từ, động từ, tính từ, phó từ, giới từ, liên từ, thán từ, mạo từ
     * 
     * @param text the Vietnamese string
     * @return the part of speech
     */
    public static PartOfSpeech fromVietnamese(String text) {
        text = text.toLowerCase();
        for (PartOfSpeech pos : PartOfSpeech.values()) {
            if (text.contains(pos.text.toLowerCase())) return pos;
        }
        return null;
    }

    /**
     * Get the part of speech from the string <p>
     * Create if the input text has the keyword of the part of speech <p>
     * English: noun, verb, adjective, adverb, preposition, conjunction, interjection, article <p>
     * Vietnamese: danh từ, động từ, tính từ, phó từ, giới từ, liên từ, thán từ, mạo từ
     * 
     * @param text the string
     * @return the part of speech
     */
    public static PartOfSpeech fromString(String text) {
        PartOfSpeech pos = fromEnglish(text);
        if (pos == null) {
            pos = fromVietnamese(text);
        }
        return pos;
    }

    /**
     * Convert to English
     * 
     * @return the English string
     */
    public String toEnglish() {
        return this.name().toLowerCase();
    }

    /**
     * Convert to Vietnamese
     * 
     * @return the Vietnamese string
     */
    public String toVietnamese() {
        return text;
    }

    /**
     * Convert to string. This method will return the Vietnamese string
     * 
     * @return the Vietnamese string
     */
    @Override
    public String toString() {
        return text;
    }
}
