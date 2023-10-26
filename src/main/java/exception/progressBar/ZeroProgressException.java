package exception.progressBar;

public class ZeroProgressException extends ProgressBarException {
    public ZeroProgressException() {
        super("Total progress cannot be 0 when loading");
    }

    public ZeroProgressException(String message) {
        super(message);
    }
}
