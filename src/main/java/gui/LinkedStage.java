package gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class LinkedStage extends Stage {
    protected Scene mainScene;

    public void backToStage() {
        setScene(mainScene);
    }
}
