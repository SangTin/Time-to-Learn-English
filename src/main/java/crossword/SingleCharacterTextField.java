package crossword;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.function.UnaryOperator;

public class SingleCharacterTextField extends TextField {
    public SingleCharacterTextField() {
        super();
        setup();
    }

    private void setup() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 1) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        this.setTextFormatter(textFormatter);
    }

    public void getHint(KeyEvent event, String hint) {
        TextField textField = (TextField) event.getSource();
        String typedCharacter = event.getCharacter();
        int row = GridPane.getRowIndex(textField);
        int col = GridPane.getColumnIndex(textField);

        if ("$".equals(typedCharacter)) {
            // Nếu người dùng nhập ký tự "$", thay đổi giá trị của TextField thành ký tự mong muốn
            textField.setText(hint);  // Thay đổi thành ký tự bạn muốn
        }
    }
}
