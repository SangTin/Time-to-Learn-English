package gui.style;

import data.GifDecoder;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;

import java.awt.image.BufferedImage;

/**
 * This class is used to display animated gif in JavaFX which can used as an Animation
 * (with animation's functions such as play, pause, stop, etc.) <br>
 * This is an answer to the question:
 * <a href="https://stackoverflow.com/a/28185996">How I can stop an animated GIF in JavaFX?</a>
 * used by <a href="https://stackoverflow.com/users/1844265/roland">Roland</a>
 *
 * @see Animation
 * @see GifDecoder
 * @author <a href="https://stackoverflow.com/users/1844265/roland">Roland</a>
 */
public class AnimatedGif extends Animation {
   public AnimatedGif(String filename, double durationMs, double width, double height) {
      GifDecoder d = new GifDecoder();
      d.read(this.getClass().getResourceAsStream(filename));
      Image[] sequence = new Image[d.getFrameCount()];

      for(int i = 0; i < d.getFrameCount(); ++i) {
         WritableImage wimg = null;
         BufferedImage bimg = d.getFrame(i);
         sequence[i] = SwingFXUtils.toFXImage(bimg, null);
      }

      super.init(sequence, durationMs, width, height);
   }
}

class Animation extends Transition {
   private ImageView imageView;
   private int count;
   private int pauseFrame;
   private int lastIndex;
   private Image[] sequence;

   protected Animation() {
   }

   protected void init(Image[] sequence, double durationMs, double width, double height) {
      this.imageView = new ImageView(sequence[0]);
      this.imageView.setFitWidth(width);
      this.imageView.setFitHeight(height);
      this.sequence = sequence;
      this.count = sequence.length;
      this.pauseFrame = -1;
      this.setCycleCount(1);
      this.setCycleDuration(Duration.millis(durationMs));
      this.setInterpolator(Interpolator.LINEAR);
   }

   protected void interpolate(double k) {
      int index = Math.min((int)Math.floor(k * (double)this.count), this.count - 1);
      if (this.pauseFrame != -1 && index > this.pauseFrame) {
         this.pause();
      } else {
         if (index != this.lastIndex) {
            this.imageView.setImage(this.sequence[index]);
            this.lastIndex = index;
         }

      }
   }

   public ImageView getView() {
      return this.imageView;
   }

   public void stop() {
      super.stop();
      this.imageView.setImage(this.sequence[0]);
   }

   public void playToFrame(int frame) {
      if (frame >= 0 && frame < this.count) {
         this.pauseFrame = frame;
         this.playFromStart();
      } else {
         throw new IllegalArgumentException("Invalid frame index");
      }
   }

   public void playFromFrame(int frame) {
      if (frame >= 0 && frame < this.count) {
         this.pauseFrame = -1;
         this.imageView.setImage(this.sequence[frame]);
         this.playFrom(Duration.millis((double)frame * this.getCycleDuration().toMillis() / (double)this.count));
      } else {
         throw new IllegalArgumentException("Invalid frame index");
      }
   }
}
