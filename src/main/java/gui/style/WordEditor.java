package gui.style;

public interface WordEditor {
    boolean isModified();
    default boolean canSave() {
        return true;
    }
    void save();
}
