module EasyEnglish {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.jsoup;
    requires java.sql;
    requires javafx.media;

    requires org.apache.commons.text;

    opens entry to javafx.fxml;
    opens gui.dictionary to javafx.fxml;
    opens gui.dictionary.content to javafx.fxml;
    
    exports entry to javafx.graphics;
}