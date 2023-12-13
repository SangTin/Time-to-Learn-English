package game.crossword;

import game.GameBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.UnaryOperator;

@SuppressWarnings("ALL")
public class CrossWord extends GameBase {
    @FXML
    private GridPane table;
    @FXML
    private Button Ok;
    @FXML
    private Label Across;
    @FXML
    private Label Down;
    @FXML
    private Label Border;
    @FXML
    private Label Color_Across;
    @FXML
    private Label Color_Down;
    @FXML
    private Label Type;
    @FXML
    private Label Notification;
    @FXML
    private AnchorPane Anchor;
    @FXML
    private Button Next;
    @FXML
    private Button End;

    public static int currentGame = 1;
    Font customFont;
    private double HeightOfCellInGridPane;
    private double WidthOfCellInGridPane;
    private String[][] dataId = new String[8][8];
    private String[][] hint = new String[8][8];
    public static Stage primaryStage;

    private static final String CONTENT_FXML = "/crossword/fxml/crossword.fxml";

    @SuppressWarnings("CallToPrintStackTrace")
    public CrossWord() {
        super();
        setBackgroundMusic("src/main/resources/crossword/sound/sound.wav");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTENT_FXML));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức để tạo TextField giới hạn chỉ nhập một kí tự
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
        int row = GridPane.getRowIndex(currentTextField);
        int col = GridPane.getColumnIndex(currentTextField);
        switch (keyCode) {
            case UP:
                if (row > 0) {
                    row--;
                }
                break;
            case DOWN:
                if (row < 8) {
                row++;
            }
            break;
            case LEFT:
                if (col > 0) {
                    col--;
                }
                break;
            case RIGHT:
                if (col < 8) {
                col++;
            }
            break;
            case F2:
                currentTextField.setText(hint[row][col]);
                break;
            default:
                return;
        }

        String newId = "textField_" + row + "_" + col;
        TextField nextTextField = (TextField) table.lookup("#" + newId);
        if (nextTextField != null) {
            nextTextField.requestFocus();
        }
    }

    public String[][] ReadFileToArray(String path) {
        String[][] array = new String[8][8];
        try{
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (scanner.hasNext()) {
                        array[row][col] = scanner.next();
                    } else {
                        System.out.println("Không đủ dữ liệu trong file." + path);
                        break;
                    }
                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Không tìm thấy file." + path);
            e.printStackTrace();
        }

        return array;
    }

    public String ReadFileToString(String path) {
        StringBuilder answer = new StringBuilder();
        try{
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                String x = scanner.nextLine();
                if(x.length() > 40) {
                    for(int i = 40; i >= 0; --i) {
                        if(x.charAt(i) == ' ') {
                            x = x.substring(0, i) + '\n' + x.substring(i + 1);
                            break;
                        }
                    }
                }
                answer.append(x).append("\n\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Không tìm thấy file." + path);
            e.printStackTrace();
        }
        return answer.toString();
    }

    public void SetUpGridPane() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                if(dataId[row][col].equals("-1")) {
                    Button button = new Button();
                    button.setPrefSize(WidthOfCellInGridPane, HeightOfCellInGridPane);
                    table.add(button, col, row);
                    button.setStyle("-fx-background-color: pink; -fx-background-radius: 0;");
                } else {
                    TextField text = createSingleCharTextField();
                    text.setPrefSize(WidthOfCellInGridPane, HeightOfCellInGridPane);
                    text.setStyle("-fx-background-color: white; -fx-background-radius: 0; -fx-border-color: pink; -fx-border-width: 0.5; -fx-border-height: 0.5;");
                    text.setId("textField_" + row + "_" + col);
                    text.setFont(Font.font(customFont.getFamily(), 15));
                    text.setAlignment(Pos.CENTER);
                    text.setOnKeyPressed(event -> handleArrowKeys(text, event.getCode()));
                    table.add(text, col, row);
                    if(!dataId[row][col].equals("0")) {
                        Label id = new Label(dataId[row][col]);
                        id.setFont(Font.font(customFont.getFamily(), 13));
                        table.add(id, col, row);
                        id.setTranslateX(2);
                    }

                }
            }
        }
    }

    public void EndGame(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End game");
        alert.setHeaderText("Your result won't be saved, if you quit");
        ButtonType yes = new ButtonType("Yes", ButtonData.YES);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, cancel);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/css/alert.css"))).toExternalForm());

        Optional<ButtonType> choice = alert.showAndWait();
        if(choice.get() == yes) {
            endGame();
        }
    }
    public void Compare(ActionEvent event) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(!dataId[row][col].equals("-1")) {
                    String newId = "textField_" + row + "_" + col;
                    TextField text = (TextField) table.lookup("#" + newId);
                    String answer = text.getText();
                    answer = answer.toLowerCase();
                    if(!hint[row][col].equals(answer)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Wrong answer");
                        alert.show();
                        return;
                    }
                }
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Correct");
        alert.show();
        if(currentGame < 10) {
            Next.setVisible(true);
            Next.setManaged(true);
        } else {
            endGame();
        }
    }

    public void Next(ActionEvent event) {
        ++currentGame;
        loadData();
    }

    public void loadData() {
        table.getChildren().clear();
        Next.setVisible(false);
        Next.setManaged(false);
        int type = (currentGame + 1) / 2;
        Type.setFont(Font.font(customFont.getFamily(), 25));
        Type.setAlignment(Pos.TOP_CENTER);
        if(type == 1) {
            Type.setText("Animals Crosswords");
        } else if(type == 2) {
            Type.setText("Sports Crosswords");
        } else if(type == 3) {
            Type.setText("Jobs and Work Crosswords");
        } else if(type == 4) {
            Type.setText("Science and Technology\nCrosswords");
        } else if(type == 5) {
            Type.setText("Health, Food and Fitness\nCrosswords");
        }
        Type.setTextFill(Color.PURPLE);
        Font other_customFont = Font.font(customFont.getFamily(), FontWeight.BOLD, customFont.getSize());
        String pathDataId = "src/main/resources/crossword/dataId/dataId" + currentGame +".txt";
        dataId = ReadFileToArray(pathDataId);
        String pathHint = "src/main/resources/crossword/hint/hint" + currentGame +".txt";
        hint = ReadFileToArray(pathHint);
        SetUpGridPane();
        String pathAcross = "src/main/resources/crossword/Across/Across" + currentGame +".txt";
        Across.setText(ReadFileToString(pathAcross));
        Across.setFont(customFont);
        Across.setAlignment(Pos.TOP_LEFT);
        String pathDown = "src/main/resources/crossword/Down/Down" + currentGame +".txt";
        Down.setText(ReadFileToString(pathDown));
        Down.setFont(other_customFont);
        Down.setAlignment(Pos.TOP_LEFT);
        Color_Across.setFont(other_customFont);
        Color_Across.setStyle("-fx-background-color: pink;");
        Color_Across.setText("    Across");
        Color_Across.setTextFill(Color.PURPLE);
        Color_Down.setFont(other_customFont);
        Color_Down.setStyle("-fx-background-color: pink;");
        Color_Down.setText("    Down");
        Color_Down.setTextFill(Color.PURPLE);
        Ok.setFont(Font.font(customFont.getFamily(), 18));
        Ok.setTextFill(Color.PURPLE);
        End.setFont(Font.font(customFont.getFamily(), 12));
        End.setTextFill(Color.PURPLE);
    }

    public void initialize() {
        table.getChildren().clear();
        customFont = Font.loadFont(getClass().getResourceAsStream("/crossword/font/lazy.ttf"), 17);
        HeightOfCellInGridPane = table.getPrefHeight() / 8;
        WidthOfCellInGridPane = table.getPrefWidth() / 8;
        Anchor.setStyle("-fx-background-color: white;");
        table.setStyle("-fx-border-color: pink; -fx-border-width: 0.5;");
        Border.setStyle("-fx-border-color: pink; -fx-border-width: 3;");
        Notification.setFont(Font.font(customFont.getFamily(), 11));
        Notification.setText("With each other cell\nPress \"F2\" to see hint");
    }

    @Override
    public void startGame() {
        super.startGame();
        table.getChildren().clear();
        backgroundMusic.play();
        loadData();
    }
}

