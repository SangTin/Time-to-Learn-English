package data.enums;

public enum SearchResultType {
   HISTORY, FAVOURITE, SUGGESTION, CREATE;

   public String toString() {
      return super.toString().toLowerCase();
   }
}
