package gui.style;

import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public class SVGImage {
   private final SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
   private final BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

   public SVGImage() {}

   public SVGImage(String svgFile) {
      this.load(svgFile);
   }

   public void load(String svgFile) {
      try {
         InputStream file = this.getClass().getResourceAsStream(svgFile);
         TranscoderInput transIn = new TranscoderInput(file);
         transcoder.transcode(transIn, null);
         imageProperty.set(SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public SimpleObjectProperty<Image> imageProperty() {
      return this.imageProperty;
   }

   private static class BufferedImageTranscoder extends ImageTranscoder {
      private BufferedImage img = null;

      @Override
      public BufferedImage createImage(int width, int height) {
         return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      }

      @Override
      public void writeImage(BufferedImage img, TranscoderOutput to) throws TranscoderException {
         this.img = img;
      }

      public BufferedImage getBufferedImage() {
         return img;
      }
   }
}
