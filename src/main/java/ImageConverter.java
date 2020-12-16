package mystok;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageConverter {

  public static void imageConverter(String input_path, String output_path) {

    try {

      File input = new File(input_path);
      File output = new File(output_path);

      BufferedImage image = ImageIO.read(input);
      BufferedImage result = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);

      result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
      ImageIO.write(result, "jpg", output);

    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
