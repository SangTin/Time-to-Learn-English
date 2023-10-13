module EasyEnglish {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.jsoup;
    requires java.sql;

    opens entry to javafx.fxml;
    opens gui.dictionaryScene to javafx.fxml;
    
    exports entry to javafx.graphics;
}