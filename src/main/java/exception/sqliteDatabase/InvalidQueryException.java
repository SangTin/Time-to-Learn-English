package exception.sqliteDatabase;

public class InvalidQueryException extends SQLiteException {
   public InvalidQueryException() {
      super("Invalid query");
   }

   public InvalidQueryException(String message) {
      super(message);
   }
}
