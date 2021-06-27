package it.gzxiii.circleshough.utils;

import it.gzxiii.circleshough.Main;
import it.gzxiii.circleshough.constants.ErrorCodes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Utils {

    private static final String baseDir = System.getProperty("user.dir") + "/results/";

    /**
     * Read Image as Buffered Image.
     * @since 0.0.1
     * @author gzxiii
     * @param uri image's URI (filepath)
     * @return BufferedImage
     */
    public static BufferedImage imageRead(String uri) throws Exception {
        return ImageIO.read(new File(uri));
    }

    /**
     * Save Image as jpg File.
     * @since 0.0.1
     * @author gzxiii
     * @param filename, img
     */
    public static void imageSave(String filename, BufferedImage img) throws Exception {

        File directory = new File(baseDir);
        if (! directory.exists()){
            boolean dir = directory.mkdir();
            if(!dir){
                Main.logger.log(Level.SEVERE, ErrorCodes.UNABLE_TO_SAVE_IMAGE_MSG);
                System.exit(ErrorCodes.UNABLE_TO_SAVE_IMAGE);
            }
        }

        File outputFile = new File(baseDir + filename + ".jpg");
        ImageIO.write(img, "jpg", outputFile);
    }
}
