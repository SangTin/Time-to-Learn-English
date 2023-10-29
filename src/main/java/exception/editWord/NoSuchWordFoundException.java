package exception.editWord;

public class NoSuchWordFoundException extends EditWordException {
    public NoSuchWordFoundException() {
        super("No such word found");
    }

    public NoSuchWordFoundException(String wordTarget) {
        super(String.format("No such word found with target: %s", wordTarget));
    }

    public NoSuchWordFoundException(int wordID) {
        super(String.format("No such word found with id: %d", wordID));
    }
}
