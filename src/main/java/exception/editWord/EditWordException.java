package exception.editWord;

public class EditWordException extends Exception {
   public EditWordException() {
      super("Error while editing word");
   }

   public EditWordException(String message) {
      super(message);
   }
}
