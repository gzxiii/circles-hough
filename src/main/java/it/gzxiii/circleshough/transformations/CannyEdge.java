package it.gzxiii.circleshough.transformations;

public class CannyEdge {
    private static final double NUMBER_DEVIATION = 1.4;
    private static final double FRAC_THRESHOLD = 0.5; //fraction of the high threshold
    public static double sum = 0, variance = 0, mean = 0, stdDev = 0, low_thr = 0, high_trh = 0;

    /**
     * Find Magnitude of gradient at each pixel passing the Gx and Gy results from Sobel mask convolution
     * @since 0.0.2
     * @author gzxiii
     * @param gx, gy
     * @return int[][]
     */
    public static double[][] gradientMagnitude(int[][] gx, int[][] gy) {

        int height = gx.length;
        int width = gx[0].length;
        double total = height * width;
        double[][] res = new double[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                res[r][c] = Math.sqrt(gx[r][c] * gx[r][c] + gy[r][c] * gy[r][c]);

                sum += res[r][c];
            }
        }
        mean = (int) Math.round(sum / total);

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                double diff = res[r][c] - mean;

                variance += (diff * diff);
            }
        }
        stdDev = (int) Math.sqrt(variance / total);
        return res;
    }

    /**
     * Find Direction of gradient at each pixel passing the Gx and Gy results from Sobel mask convolution
     * @since 0.0.2
     * @author gzxiii
     * @param gx, gy
     * @return int[][]
     */
    public static int[][] gradientDirection(int[][] gx, int[][] gy) {
        int height = gx.length;
        int width = gx[0].length;
        double piRad = 180 / Math.PI;
        int[][] res = new int[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                double angle = Math.atan2(gy[r][c], gx[r][c]) * piRad;

                if (angle < 0) {
                    angle += 360.;
                }

                //Each pixels ACTUAL angle is examined and placed in 1 of four groups (for the four searched 45-degree neighbors)
                //Reorder this for optimization
                if (angle <= 22.5 || (angle >= 157.5 && angle <= 202.5) || angle >= 337.5) {
                    res[r][c] = 0;      //Check left and right neighbors
                } else if ((angle >= 22.5 && angle <= 67.5) || (angle >= 202.5 && angle <= 247.5)) {
                    res[r][c] = 45;     //Check diagonal (upper right and lower left) neighbors
                } else if ((angle >= 67.5 && angle <= 112.5) || (angle >= 247.5 && angle <= 292.5)) {
                    res[r][c] = 90;     //Check top and bottom neighbors
                } else {
                    res[r][c] = 135;    //Check diagonal (upper left and lower right) neighbors
                }
            }
        }

        return res;
    }


    /**
     * It finds where the magnitude is biggest, suppressing non maximum points.
     * @since 0.0.2
     * @author gzxiii
     * @param magnitude, direction
     * @return int[][]
     */
    public static double[][] nonMaximumSuppression(double[][] magnitude, int[][] direction) {
        int height = magnitude.length - 1;
        int width = magnitude[0].length - 1;

        for (int r = 1; r < height; r++) {
            for (int c = 1; c < width; c++) {
                double magCompare = magnitude[r][c];

                switch (direction[r][c]) {
                    case 0 :
                        if (magCompare < magnitude[r][c - 1] && magCompare < magnitude[r][c + 1]) {
                            magnitude [r - 1][c - 1] = 0;
                        }
                        break;
                    case 45 :
                        if (magCompare < magnitude[r - 1][c + 1] && magCompare < magnitude[r + 1][c - 1]) {
                            magnitude [r - 1][c - 1] = 0;
                        }
                        break;
                    case 90 :
                        if (magCompare < magnitude[r - 1][c] && magCompare < magnitude[r + 1][c]) {
                            magnitude [r - 1][c - 1] = 0;
                        }
                        break;
                    case 135 :
                        if (magCompare < magnitude[r - 1][c - 1] && magCompare < magnitude[r + 1][c + 1]) {
                            magnitude [r - 1][c - 1] = 0;
                        }
                        break;
                }
            }
        }

        return magnitude;
    }

    /**
     * avoid all 'weak' edge pixels unless they are directly adjacent to a 'strong' edge pixel
     * @since 0.0.2
     * @author gzxiii
     * @param magnitude, direction
     * @return int[][]
     */
    public static int[][] hysteresis(double[][] magnitude) {
        int height = magnitude.length - 1;
        int width = magnitude[0].length - 1;
        int[][] bin = new int[height - 1][width - 1];

        high_trh = mean + (NUMBER_DEVIATION * stdDev);    //Magnitude greater than or equal to high threshold is an edge pixel
        low_thr = high_trh * FRAC_THRESHOLD;               //Magnitude less than low threshold not an edge, equal or greater possible edge

        for (int r = 1; r < height; r++) {
            for (int c = 1; c < width; c++) {
                double magCompare = magnitude[r][c];

                if (magCompare >= 240) {
                    bin[r - 1][c - 1] = 255;
                } else if (magCompare < 0.3) {
                    bin[r - 1][c - 1] = 0;
                } else {    //This could be separate method or lambda
                    boolean connected = false;

                    for (int nr = -1; nr < 2; nr++) {
                        for (int nc = -1; nc < 2; nc++) {
                            if (magnitude[r + nr][c + nc] >= high_trh) {
                                connected = true;
                            }
                        }
                    }

                    bin[r - 1][c - 1] = (connected) ? 255 : 0;
                }
            }
        }

        return bin;
    }
}

