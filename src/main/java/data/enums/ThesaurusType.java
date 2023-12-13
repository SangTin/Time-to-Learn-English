package data.enums;

public enum ThesaurusType {
   SYNONYM("synonym"),
   ANTONYM("antonym");

   private final String text;

   ThesaurusType(String text) {
      this.text = text;
   }

   public static ThesaurusType fromString(String text) {
      text = text.toLowerCase();
      ThesaurusType[] var1 = values();
      int var2 = var1.length;

       for (ThesaurusType type : var1) {
           if (text.contains(type.text)) {
               return type;
           }
       }

      return null;
   }

   public String toString() {
      return this.text;
   }

   // $FF: synthetic method
   private static ThesaurusType[] $values() {
      return new ThesaurusType[]{SYNONYM, ANTONYM};
   }
}
