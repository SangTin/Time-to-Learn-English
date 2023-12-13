package gui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameIntroduction extends VBox {
    private final ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<>() {
        @Override protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return GameIntroduction.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };

    @FXML private ImageView logo;
    @FXML private Label title;
    private final Text description = new Text();

    public GameIntroduction() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/components/GameIntroduction.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            System.out.println("Error in GameIntroduction.java");
        }
    }

    public void initialize() {
        this.setOnMouseClicked(event -> {
            if (getOnAction() != null) {
                getOnAction().handle(new ActionEvent(this, null));
            }
        });
    }

    public void setDescription(String description) {
        this.description.setText(description);
        this.description.setWrappingWidth(300);
        this.getChildren().add(this.description);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setLogo(String logo) {
        this.logo.setImage(new Image(getClass().getResource(logo).toString()));
    }

    public void setLogo(Image logo) {
        this.logo.setImage(logo);
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().get();
    }
}
