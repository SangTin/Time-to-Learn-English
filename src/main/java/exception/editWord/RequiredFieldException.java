package exception.editWord;

public class RequiredFieldException extends EditWordException {
   public RequiredFieldException() {
      super("Required field(s) is empty");
   }

   public RequiredFieldException(String message) {
      super(message);
   }
}
