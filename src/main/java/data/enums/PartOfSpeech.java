package data.enums;

public enum PartOfSpeech {
    NOUN("danh từ"), VERB("động từ"), 
    ADJECTIVE("tính từ"), ADVERB("phó từ"), 
    PREPOSITION("giới từ"), CONJUNCTION("liên từ"), 
    INTERJECTION("thán từ"), ARTICLE("mạo từ");

    private final String text;

    private PartOfSpeech(String text) {
        this.text = text;
    }

    public static String fromString(String text) {
        text = text.toLowerCase();
        for (PartOfSpeech pos : PartOfSpeech.values()) {
            if (text.contains(pos.text)) return pos.text;
        }
        for (PartOfSpeech pos : PartOfSpeech.values()) {
            String posName = pos.name().toLowerCase();
            if (text.contains(posName)) return posName;
        }
        return null;
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
