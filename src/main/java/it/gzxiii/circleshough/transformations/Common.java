package it.gzxiii.circleshough.transformations;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Common {

    /**
     * Converts BufferedImage to 2D Matrix
     * @since 0.0.1
     * @author gzxiii
     * @param img source image
     * @return BufferedImage
     */
    public static double[][] BuffImg2Matrix(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] res = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                res[x][y] = new Color(img.getRGB(x, y)).getRed();
            }
        }
        return res;
    }
}

