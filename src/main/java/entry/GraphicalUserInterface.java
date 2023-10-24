package entry;

import gui.LinkedStage;
import gui.Menu;

public class GraphicalUserInterface {
    private LinkedStage primaryStage;

    public GraphicalUserInterface() {
        primaryStage = new Menu();
        primaryStage.show();
    }
}
