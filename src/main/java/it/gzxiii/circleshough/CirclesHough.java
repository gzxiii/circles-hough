package it.gzxiii.circleshough;


import it.gzxiii.circleshough.constants.ErrorCodes;
import it.gzxiii.circleshough.utils.Utils;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.gzxiii.circleshough.utils.Utils.imageRead;

public class CirclesHough{

    public static final Logger logger = Logger.getLogger(CirclesHough.class.getName());
    private static final String rootDir = System.getProperty("user.dir") + "/";

    /**
     * Process Hough Transformation for circular figures.
     * Arguments: [filepath, gauss sigma, sobel threshold]
     * @since 0.0.1
     * @author gzxiii
     * @param args command line arguments
     */
    public static void main(String[] args){
        logger.log(Level.INFO, "Circle Hough Detector starting...");

        if (args.length < 2) {
            logger.log(Level.SEVERE, ErrorCodes.NOT_ENOUGH_ARGS_MSG);
            System.exit(ErrorCodes.NOT_ENOUGH_ARGS);
        }

        if(!(args[0].length() > 3)){
            logger.log(Level.SEVERE, ErrorCodes.ARG_NOT_VALID_MSG, args[0]);
            System.exit(ErrorCodes.ARG_NOT_VALID);
        }

        Image sourceImage = new Image(rootDir + args[0]);

        //Acquire image
        getImageDetails(sourceImage);
        logger.log(Level.INFO,
                String.format("Acquired %d x %d px image from %s",
                        sourceImage.width, sourceImage.height, args[0]));

        //Convert it to gray scale
        logger.log(Level.INFO, "Converting image in grayscale");
        sourceImage.img = sourceImage.convert2Gray();
        saveImageToFile("res_gray", sourceImage);


        //Apply Gaussian Blur
        logger.log(Level.INFO, "Applying gaussian blur");
        double sigma = Double.parseDouble(args[1]);
        sourceImage.img =  sourceImage.gaussianBlur(BufferedImage.TYPE_BYTE_GRAY, sigma);
        saveImageToFile("res_blur", sourceImage);



    }

    /**
     * Read Image from the file path and extract all the details.
     * @since 0.0.1
     * @author gzxiii
     * @param sourceImage source image entire class
     */
    private static void getImageDetails(Image sourceImage){
        try {
            logger.log(Level.INFO, String.format("Processing %s", sourceImage.uri));
            sourceImage.img = imageRead(sourceImage.uri);
            sourceImage.width = sourceImage.img.getWidth();
            sourceImage.height = sourceImage.img.getHeight();

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, ErrorCodes.IMAGE_NOT_READABLE_MSG);
            System.exit(ErrorCodes.IMAGE_NOT_READABLE);
        }
    }

    /**
     * Save Image to disk.
     * @since 0.0.1
     * @author gzxiii
     * @param filename, sourceImage
     */
    private static void saveImageToFile(String filename, Image sourceImage) {
        try {
            logger.log(Level.INFO, String.format("Saving output to %s.jpg", filename));
            Utils.imageSave(filename, sourceImage.img);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, ErrorCodes.UNABLE_TO_SAVE_IMAGE_MSG);
            System.exit(ErrorCodes.UNABLE_TO_SAVE_IMAGE);
        }

    }

}
