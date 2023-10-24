package style;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class MenuStyle {
    private static DropShadow buttonShadow = null;

    //Shadow effects
    public static DropShadow getButtonShadowEffect() {
        if (buttonShadow != null) {
            return buttonShadow;
        }
        DropShadow buttonShadow = new DropShadow(BlurType.GAUSSIAN, Color.web("rgba(45, 35, 66, 0.4)"), 4, 0, 0, 2);
        DropShadow buttonShadow2 = new DropShadow(BlurType.GAUSSIAN, Color.web("rgba(45, 35, 66, 0.3)"), 10, 0, 0, 7);
        InnerShadow buttonShadow3 = new InnerShadow(BlurType.GAUSSIAN, Color.web("#0076a3"), 0, 0, 0, -5);
        buttonShadow2.setInput(buttonShadow3);
        buttonShadow.setInput(buttonShadow2);
        return buttonShadow;
    }
}
