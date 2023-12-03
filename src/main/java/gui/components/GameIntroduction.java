package gui.components;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameIntroduction extends VBox {
    @FXML private ImageView logo;
    @FXML private Label title;

    private final Text description = new Text();
    private String descriptionText;

    public GameIntroduction() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/components/GameIntroduction.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in GameIntroduction.java");
        }
    }

    public void initialize() {
        Service<Void> service = new Service<>() {
            @Override
            protected javafx.concurrent.Task<Void> createTask() {

                return new javafx.concurrent.Task<>() {
                    @Override
                    protected Void call() {
                        for (int i = 0; i < descriptionText.length() && !isCancelled(); i++) {
                            description.setText(descriptionText.substring(0, i + 1));
                            try {
                                Thread.sleep(5);
                            } catch (Exception ignored) {
                            }
                        }
                        return null;
                    }
                    @Override
                    protected void cancelled() {
                        super.cancelled();
                        System.out.println("Cancelled");
                    }
                };
            }
        };
        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                getChildren().add(description);
                description.setText(descriptionText);
                service.restart();
            } else {
                getChildren().remove(description);
                description.setText("");
                service.cancel();
            }
        });
    }

    public void setDescription(String description) {
        this.descriptionText = description;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}
