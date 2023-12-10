package gui.style;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class MenuStyle {
   private static final DropShadow buttonShadow = null;

   public static DropShadow getButtonShadowEffect() {
      if (MenuStyle.buttonShadow != null) {
         return MenuStyle.buttonShadow;
      } else {
         DropShadow buttonShadow = new DropShadow(BlurType.GAUSSIAN, Color.web("rgba(45, 35, 66, 0.4)"), 4.0D, 0.0D, 0.0D, 2.0D);
         DropShadow buttonShadow2 = new DropShadow(BlurType.GAUSSIAN, Color.web("rgba(45, 35, 66, 0.3)"), 10.0D, 0.0D, 0.0D, 7.0D);
         InnerShadow buttonShadow3 = new InnerShadow(BlurType.GAUSSIAN, Color.web("#0076a3"), 0.0D, 0.0D, 0.0D, -5.0D);
         buttonShadow2.setInput(buttonShadow3);
         buttonShadow.setInput(buttonShadow2);
         return buttonShadow;
      }
   }
}
