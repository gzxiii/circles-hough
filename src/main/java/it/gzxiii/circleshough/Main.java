package it.gzxiii.circleshough;


import it.gzxiii.circleshough.constants.ErrorCodes;
import it.gzxiii.circleshough.transformations.*;
import it.gzxiii.circleshough.transformations.Sobel;
import it.gzxiii.circleshough.utils.Utils;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.gzxiii.circleshough.transformations.Common.BuffImg2Matrix;
import static it.gzxiii.circleshough.utils.Utils.imageRead;

public class Main {

    public static final Logger logger = Logger.getLogger(Main.class.getName());
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

        if (args.length < 5) {
            logger.log(Level.SEVERE, ErrorCodes.NOT_ENOUGH_ARGS_MSG);
            System.exit(ErrorCodes.NOT_ENOUGH_ARGS);
        }

        if(!(args[0].length() > 3)){
            logger.log(Level.SEVERE, ErrorCodes.ARG_NOT_VALID_MSG, args[0]);
            System.exit(ErrorCodes.ARG_NOT_VALID);
        }

        IMG sourceIMG = new IMG(rootDir + args[0]);

        /* Preliminar tasks: */
        /* Acquiring image */
        getImageDetails(sourceIMG);
        logger.log(Level.INFO,
                String.format("Acquired %d x %d px image from %s",
                        sourceIMG.width, sourceIMG.height, args[0]));

        /* Converting it to gray scale */
        logger.log(Level.INFO, "Converting image in grayscale");
        sourceIMG.img = sourceIMG.convert2Gray();
        saveImageToFile("res_gray", sourceIMG);


        /* Applying Gaussian Blur */
        logger.log(Level.INFO, "Applying gaussian blur");
        double sigma = Double.parseDouble(args[1]);
        sourceIMG.img =  sourceIMG.gaussianBlur(sigma);
        sourceIMG.blurred = sourceIMG.img;
        saveImageToFile("res_blur", sourceIMG);

        /* Canny Edge Detection: */
        logger.log(Level.INFO, "Detecting Edges with Canny operator");
        /* *****************************************************
         1) SOBEL:
            Convolve the blurred grayscale image with the
            Sobel kernel horizontally and vertically to find
            the magnitude and direction of each pixel
        ******************************************************* */
        sourceIMG.gx = Sobel.Convolve_X(sourceIMG.imgArray);
        sourceIMG.img = Common.intMatrix2BufferedImage(sourceIMG.gx, BufferedImage.TYPE_BYTE_GRAY);
        saveImageToFile("res_sobel_x", sourceIMG);
        sourceIMG.gy = Sobel.Convolve_Y(sourceIMG.imgArray);
        sourceIMG.img = Common.intMatrix2BufferedImage(sourceIMG.gy, BufferedImage.TYPE_BYTE_GRAY);
        saveImageToFile("res_sobel_y", sourceIMG);

        /* *****************************************************
         2) MAGNITUDE:
            magnitude of gradient at each pixel.
        ******************************************************* */
        sourceIMG.mag = CannyEdge.gradientMagnitude(sourceIMG.gx, sourceIMG.gy);
        sourceIMG.img = Common.doubleMatrix2BufferedImage(sourceIMG.mag, BufferedImage.TYPE_BYTE_GRAY);
        saveImageToFile("res_magnitude", sourceIMG);

        /* *****************************************************
         3) DIRECTION:
            direction of gradient at each pixel.
        ******************************************************* */
        sourceIMG.dir = CannyEdge.gradientDirection(sourceIMG.gx, sourceIMG.gy);
        sourceIMG.img = Common.intMatrix2BufferedImage(sourceIMG.dir, BufferedImage.TYPE_BYTE_GRAY);
        saveImageToFile("res_direction", sourceIMG);

         /* *****************************************************
         4) NON-MAXIMUM SUPPRESSION:
            marking points where the magnitude is biggest.
        ******************************************************* */
        sourceIMG.img = Common.doubleMatrix2BufferedImage(CannyEdge.nonMaximumSuppression(sourceIMG.mag, sourceIMG.dir)
                , BufferedImage.TYPE_BYTE_GRAY);
        saveImageToFile("res_magnitude_suppression", sourceIMG);

        /* *****************************************************
         5) HYSTERESIS:
            avoid all 'weak' edge pixels unless they are directly adjacent to a 'strong' edge pixel.
            Returning and saving the edges
        ******************************************************* */
        sourceIMG.edges = CannyEdge.hysteresis(sourceIMG.mag);
        sourceIMG.edgesImg = Common.intMatrix2BufferedImage(sourceIMG.edges, BufferedImage.TYPE_BYTE_GRAY);
        saveImageToFile("res_edge", sourceIMG);

        /* Circles Hough: */
        logger.log(Level.INFO, "Performing Hough transform");
        sourceIMG.houghSpaces = CircleHough.houghTransform(sourceIMG, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        sourceIMG.img = Common.doubleMatrix2BufferedImage(sourceIMG.houghSpaces, BufferedImage.TYPE_BYTE_GRAY);
        saveImageToFile("res_hough_spaces", sourceIMG);

        /* TODO - missing detection on the original image */
        /* The circles detected will be displayed in red directly to the source image */
        logger.log(Level.INFO, "Drawing Circles transform");
        sourceIMG.drawCircles(Integer.parseInt(args[4]));
        saveImageToFile("circles_detected", sourceIMG);


    }

    /**
     * Read Image from the file path and extract all the details.
     * @since 0.0.1
     * @author gzxiii
     * @param sourceIMG source image entire class
     */
    private static void getImageDetails(IMG sourceIMG){
        try {
            logger.log(Level.INFO, String.format("Processing %s", sourceIMG.uri));
            sourceIMG.img = imageRead(sourceIMG.uri);
            sourceIMG.width = sourceIMG.img.getWidth();
            sourceIMG.height = sourceIMG.img.getHeight();

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
    private static void saveImageToFile(String filename, IMG sourceIMG) {
        try {
            logger.log(Level.INFO, String.format("Saving output to %s.jpg", filename));
            Utils.imageSave(filename, sourceIMG.img);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, ErrorCodes.UNABLE_TO_SAVE_IMAGE_MSG);
            System.exit(ErrorCodes.UNABLE_TO_SAVE_IMAGE);
        }

    }

}
