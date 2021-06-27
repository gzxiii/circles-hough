package it.gzxiii.circleshough.transformations;

import it.gzxiii.circleshough.IMG;
import it.gzxiii.circleshough.utils.Circle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CircleHough {

    private static int threshold = 150;

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
        BufferedImage image = sourceIMG.img;
        imgWidth = image.getWidth();
        imgHeight = image.getHeight();
        minimumRadius = minR;
        maximumRadius = maxR;
        radius = maximumRadius == 0 ? Integer.min(imgHeight, imgWidth) : maximumRadius;
        sourceIMG.circles = new ArrayList<Circle>();
        double d = 0;
        int[][][] houghSpaceValues = new int[imgWidth][imgHeight][radius];

        double[][] imgArray = Common.BuffImg2Matrix(image);

        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                if (imgArray[x][y] > threshold) {
                    for (int r = 1; r < radius; r++) {
                        for (int t = 0; t <= 360; t++) {
                            int rcos = (int) Math.floor(x - r * Math.cos(t * Math.PI / 180));
                            int rsin = (int) Math.floor(y - r * Math.sin(t * Math.PI / 180));

                            if (!((rcos < 0 || rcos > imgWidth - 1) || (rsin < 0 || rsin > imgHeight - 1))) {
                                houghSpaceValues[rcos][rsin][r] += 1;
                                if (houghSpaceValues[rcos][rsin][r] > d) {
                                    d = houghSpaceValues[rcos][rsin][r];
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
                    sourceIMG.circles.add(new Circle(x,y,r, houghSpaceValues[x][y][r]));
                    houghSpace[x][y] = Math.floor((houghSpaceValues[x][y][r] * 255) / d);
                }
            }

        }
        return houghSpace;
    }
}
