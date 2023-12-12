package game.guessword;


import game.GameBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class GuessWord extends GameBase {
    public static Stage primaryStage;
    @FXML private GridPane answer;
    @FXML private ImageView data;
    @FXML private Button Ok;
    @FXML private Button Next;
    @FXML private Label Notification;
    @FXML private Label Level;
    @FXML private Label Title;
    @FXML private Button End;
    @FXML private Label x;
    public int current = 1;
    public int start = 0;
    public String hint;
    Font customFont;
    int size;

    private static final String CONTENT_FXML = "/guessword/fxml/game.fxml";
//    private static final String CONTENT_FXML = "/fxml/dictionary/Content.fxml";


    public GuessWord() {
        super();
        setBackgroundMusic("src/main/resources/guessword/sound/sound.wav");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTENT_FXML));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextField createSingleCharTextField() {
        TextField textField = new TextField();

        // Sử dụng TextFormatter để giới hạn chỉ nhập một kí tự
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 1) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);

        return textField;
    }

    // // Phương thức để xử lý di chuyển focus dựa trên nút mũi tên
    private void handleArrowKeys(TextField currentTextField, KeyCode keyCode) {
        int col = GridPane.getColumnIndex(currentTextField);
        switch (keyCode) {
            case LEFT:
                while (col >= start) {
                    col--;
                    if(col == start - 1)
                        col = start + hint.length() - 1;
                    String newId = "textField_" + col;
                    TextField nextTextField = (TextField) answer.lookup("#" + newId);
                    if (nextTextField != null) {
                        nextTextField.requestFocus();
                        break;
                    }
                }
                break;
            case RIGHT:
                while (col <= start + hint.length() - 1) {
                    col++;
                    if(col == start + hint.length())
                        col = start;
                    String newId = "textField_" + col;
                    TextField nextTextField = (TextField) answer.lookup("#" + newId);
                    if (nextTextField != null) {
                        nextTextField.requestFocus();
                        break;
                    }
                }
                break;
            default:
                return;
        }
    }

    private void handleKeyTyped(KeyEvent event) {
        TextField textField = (TextField) event.getSource();
        String typedCharacter = event.getCharacter();
        int col = GridPane.getColumnIndex(textField);

        if ("$".equals(typedCharacter)) {
            // Nếu người dùng nhập ký tự "$", thay đổi giá trị của TextField thành ký tự mong muốn
            textField.setText(String.valueOf(hint.charAt(col-start)));  // Thay đổi thành ký tự bạn muốn
        }
    }

    public void EndGame(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End game");
        alert.setHeaderText("Your result won't be saved, if you quit");
        ButtonType yes = new ButtonType("Yes", ButtonData.YES);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, cancel);

        Optional<ButtonType> choice = alert.showAndWait();
        if(choice.get() == yes) {
            endGame();
        }
    }

    public void Compare(ActionEvent event) {
        String s = "";
        for(int i = start; i <= start + size -1; ++i) {
            if(hint.charAt(i - start) != ' ') {
                String newId = "textField_" + i;
                TextField text= (TextField) answer.lookup("#" + newId);
                String c = text.getText();
                c = c.toLowerCase();
                s += c;
            } else {
                s += " ";
            }
        }
        if(!s.equals(hint)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Wrong answer");
            alert.show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Correct");
        alert.show();
        if(current < 20) {
            Next.setVisible(true);
            Next.setManaged(true);
        } else {
            endGame();
        }
    }
    public void setUpAnswer() {
        hint = Answer.level[current];
        hint = hint.toLowerCase();
        size = hint.length();
        start = 6 - size/2 + 1;
        for(int i = start; i <= start + size -1; ++i) {
            if(hint.charAt(i - start) != ' ') {
                TextField text = createSingleCharTextField();
                text.setPrefSize(28, 44);
                text.setStyle("-fx-background-color: transparent; -fx-border-radius: 10; -fx-border-color: #E3B448; -fx-border-width: 3; -fx-border-height: 3;");
                text.setId("textField_" + i);
                text.setAlignment(Pos.CENTER);
                text.setOnKeyPressed(event -> {
                    handleArrowKeys(text, event.getCode());
                });
                text.setOnKeyTyped(event -> handleKeyTyped(event));
                answer.add(text, i, 0);
            }
        }
    }
    public void NEXT(ActionEvent event) {
        ++current;
        loadData();
    }
    public void loadData() {
        answer.getChildren().clear();
        Next.setVisible(false);
        Next.setManaged(false);
        String imagePath = "src/main/resources/guessword/image/level/" + current + ".png";
        Image image = new Image("file:" + imagePath);
        data.setImage(image);
        Notification.setText("With each other cell\nPress \"$\" to see hint");
        Level.setText("Level: " + current + "/20");
        Level.setAlignment(Pos.CENTER);
        setUpAnswer();
    }

    public void initialize() {
        customFont = Font.loadFont(getClass().getResourceAsStream("/guessword/font/times.ttf"), 14);
        Next.setTextFill(Color.WHITE);
        Notification.setFont(Font.font(customFont.getFamily(), 12));
        Notification.setAlignment(Pos.CENTER);
        Ok.setFont(Font.font(customFont.getFamily(), 16));
        Ok.setTextFill(Color.BROWN);
        Notification.setTextFill(Color.web("#926d16"));
        Level.setFont(Font.font(customFont.getFamily(), 15));
        Level.setTextFill(Color.web("#722620"));
        Title.setFont(Font.font(customFont.getFamily(), 16));
        Title.setAlignment(Pos.CENTER);
        End.setFont(Font.font(customFont.getFamily(), 13));
    }

    public void startGame() {
        backgroundMusic.play();
        loadData();
    }
}