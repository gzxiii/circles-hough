package it.gzxiii.circleshough.transformations;

public class Sobel {
    private static final int[][] MASK_X = { {-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1} };
    private static final int[][] MASK_Y = { {-1, -2, -1}, {0, 0, 0}, {1, 2, 1} };

    /**
     * Convolution with Sobel Mask horizontally
     * @since 0.0.2
     * @author gzxiii
     * @param img source image binary
     * @return int[][]
     */
    public static int[][] Convolve_X(int[][] img) {
        int[][] out = null;
        int height = img.length;
        int width = img[0].length;

        if (height > 2 && width > 2) {
            out = new int[height - 2][width - 2];

            for (int r = 1; r < height - 1; r++) {
                for (int c = 1; c < width - 1; c++) {
                    int sum = 0;

                    for (int kr = -1; kr < 2; kr++) {
                        for (int kc = -1; kc < 2; kc++) {
                            sum += (MASK_X[kr + 1][kc + 1] * img[r + kr][c + kc]);
                        }
                    }

                    out[r - 1][c - 1] = sum;
                }
            }
        }

        return out;
    }

    /**
     * Convolution with Sobel Mask vertically
     * @since 0.0.2
     * @author gzxiii
     * @param img source image binary
     * @return int[][]
     */
    public static int[][] Convolve_Y(int[][] img) {
        int[][] out = null;
        int height = img.length;
        int width = img[0].length;

        if (height > 2 || width > 2) {
            out = new int[height - 2][width - 2];

            for (int r = 1; r < height - 1; r++) {
                for (int c = 1; c < width - 1; c++) {
                    int sum = 0;

                    for (int kr = -1; kr < 2; kr++) {
                        for (int kc = -1; kc < 2; kc++) {
                            sum += (MASK_Y[kr + 1][kc + 1] * img[r + kr][c + kc]);
                        }
                    }

                    out[r - 1][c - 1] = sum;
                }
            }
        }

        return out;
    }
}
