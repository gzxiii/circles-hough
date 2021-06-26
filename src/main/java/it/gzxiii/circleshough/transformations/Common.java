package it.gzxiii.circleshough.transformations;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Common {
    /**
     * Converts 2D double Matrix to BufferedImage
     * @since 0.0.2
     * @author gzxiii
     * @param arr, type
     * @return BufferedImage
     */
    public static BufferedImage doubleMatrix2BufferedImage(double[][] arr, int type) {
        int xLenght = arr.length;
        int yLength = arr[0].length;
        BufferedImage b = new BufferedImage(xLenght, yLength, type);

        for (int x = 0; x < xLenght; x++) {
            for (int y = 0; y < yLength; y++) {
                int rgb = (int) arr[x][y] << 16 | (int) arr[x][y] << 8 | (int) arr[x][y];
                b.setRGB(x, y, rgb);
            }
        }
        return b;
    }

    /**
     * Converts 2D int Matrix to BufferedImage
     * @since 0.0.2
     * @author gzxiii
     * @param arr, type
     * @return BufferedImage
     */
    public static BufferedImage intMatrix2BufferedImage(int[][] arr, int type) {
        int xLenght = arr.length;
        int yLength = arr[0].length;
        BufferedImage b = new BufferedImage(xLenght, yLength, type);

        for (int x = 0; x < xLenght; x++) {
            for (int y = 0; y < yLength; y++) {
                int rgb = arr[x][y] << 16 | arr[x][y] << 8 | arr[x][y];
                b.setRGB(x, y, rgb);
            }
        }

        return b;
    }
    /**
     * Converts BufferedImage to 2D Matrix
     * @since 0.0.1
     * @author gzxiii
     * @param img source image
     * @return double[][]
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

