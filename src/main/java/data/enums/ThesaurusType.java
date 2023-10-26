package data.enums;

public enum ThesaurusType {
    SYNONYM("synonym"),
    ANTONYM("antonym");

    private final String text;

    private ThesaurusType(String text) {
        this.text = text;
    }

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
