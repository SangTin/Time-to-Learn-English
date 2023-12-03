package exception.progressBar;

public class InvalidProgressException extends ProgressBarException {
   public InvalidProgressException() {
      super("Invalid progress number");
   }

   public InvalidProgressException(String message) {
      super(message);
   }
}
