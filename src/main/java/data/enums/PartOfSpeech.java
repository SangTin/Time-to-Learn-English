package data.enums;

public enum PartOfSpeech {

   NOUN("danh từ"),
   VERB("động từ"),
   ADJECTIVE("tính từ"),
   ADVERB("phó từ"),
   PREPOSITION("giới từ"),
   CONJUNCTION("liên từ"),
   INTERJECTION("thán từ"),
   ARTICLE("mạo từ");

   private final String text;

   PartOfSpeech(String text) {
      this.text = text;
   }

   public static PartOfSpeech fromEnglish(String text) {
      text = text.toLowerCase();
      PartOfSpeech[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PartOfSpeech pos = var1[var3];
         if (text.contains(pos.name().toLowerCase())) {
            return pos;
         }
      }

      return null;
   }

   public static PartOfSpeech fromVietnamese(String text) {
      text = text.toLowerCase();
      PartOfSpeech[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PartOfSpeech pos = var1[var3];
         if (text.contains(pos.text.toLowerCase())) {
            return pos;
         }
      }

      return null;
   }

   public static PartOfSpeech fromString(String text) {
      PartOfSpeech pos = fromEnglish(text);
      if (pos == null) {
         pos = fromVietnamese(text);
      }

      return pos;
   }

   public String toEnglish() {
      return this.name().toLowerCase();
   }

   public String toVietnamese() {
      return this.text;
   }

   public String toString() {
      return this.text;
   }

   // $FF: synthetic method
   private static PartOfSpeech[] $values() {
      return new PartOfSpeech[]{NOUN, VERB, ADJECTIVE, ADVERB, PREPOSITION, CONJUNCTION, INTERJECTION, ARTICLE};
   }
}
