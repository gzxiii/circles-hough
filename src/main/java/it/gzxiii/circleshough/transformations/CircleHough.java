package it.gzxiii.circleshough.transformations;

import it.gzxiii.circleshough.IMG;
import it.gzxiii.circleshough.Main;
import it.gzxiii.circleshough.utils.Circle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;

public class CircleHough {

    private static final int threshold = 150;

    public static int minimumRadius;
    public static int maximumRadius;
    public static int imgHeight;
    public static int imgWidth;
    public static int radius;



    /**
     * Performs Hough Transform
     * http://www.ai.sri.com/pubs/files/tn036-duda71.pdf
     * @since 0.0.3
     * @author gzxiii
     * @param sourceIMG, minR, maxR
     * @return double[][]
     */
    public static double[][] houghTransform(IMG sourceIMG, int minR, int maxR ) {
        BufferedImage image = sourceIMG.edgesImg;
        imgWidth = image.getWidth();
        imgHeight = image.getHeight();
        minimumRadius = minR;
        maximumRadius = maxR;
        radius = maximumRadius == 0 ? Integer.min(imgHeight, imgWidth) : maximumRadius;
        IMG.circles = new ArrayList<>();
        double d = 0;
        int[][][] accumulator = new int[imgWidth][imgHeight][radius];

        double[][] imgArray = Common.BuffImg2Matrix(image);

        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                if (imgArray[x][y] > threshold) {
                    for (int r = 1; r < radius; r++) {
                        for (int t = 0; t <= 360; t++) {
                            int a = (int) Math.floor(x - r * Math.cos(t * Math.PI / 180));
                            int b = (int) Math.floor(y - r * Math.sin(t * Math.PI / 180));

                            if (!(( 0 > a  || a > imgWidth - 1) || (0 > b  || b > imgHeight - 1))) {
                                accumulator[a][b][r] += 1;
                                if (accumulator[a][b][r] > d) {
                                    d = accumulator[a][b][r]; //to create the hough space
                                }
                            }
                        }
                    }
                }
            }
        }

        double[][] houghSpace = new double[imgWidth][imgHeight];
        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                for (int r = minR; r < radius; r++) {
                    IMG.circles.add(new Circle(x,y,r, accumulator[x][y][r]));
                    double ratio = 650/d;
                    houghSpace[x][y] = Math.floor(ratio * accumulator[x][y][r]);
                }
            }

        }
        return houghSpace;
    }
}
