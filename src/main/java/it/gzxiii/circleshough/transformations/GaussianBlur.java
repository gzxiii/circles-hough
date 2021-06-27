package it.gzxiii.circleshough.transformations;



public class GaussianBlur {

    public final static int GAUSS_RADIUS =  15;

    /**
     * Creates the Gaussian Kernel
     * @since 0.0.2
     * @author gzxiii
     * @param sigma gauss sigma
     */
    public static void GaussianKernel(double sigma, Kernel kernel){
        double norm = 0.;
        double[] data = new double[2 * GAUSS_RADIUS + 1];
        double coefficient = 1 / (Math.sqrt(2 * Math.PI) * sigma);

        for (int x = -GAUSS_RADIUS; x < GAUSS_RADIUS + 1; x++) {
            double exp = Math.exp(-((x * x) / Math.pow(sigma, 2)));
            data[x + GAUSS_RADIUS] = coefficient * exp;
            norm += data[x + GAUSS_RADIUS];
        }

        kernel.data = data;
        kernel.norm = norm;

    }


    /**
     * Convolution kernel * image
     * @since 0.0.2
     * @author gzxiii
     * @param kernel, image, norm
     * @return int[][]
     */
    public static int[][] GaussConvolution(double[] kernel, double[][] image, double norm){

        int imgHeight = image.length;
        int imgWidth = image[0].length;
        int[][] result = new int[imgHeight - 2 * GaussianBlur.GAUSS_RADIUS][imgWidth - 2 * GaussianBlur.GAUSS_RADIUS];

        //Convolve image with kernel horizontally
        for (int r = GaussianBlur.GAUSS_RADIUS; r < imgHeight - GaussianBlur.GAUSS_RADIUS; r++) {
            for (int c = GaussianBlur.GAUSS_RADIUS; c < imgWidth - GaussianBlur.GAUSS_RADIUS; c++) {
                double sum = 0.;

                for (int mr = -GaussianBlur.GAUSS_RADIUS; mr < GaussianBlur.GAUSS_RADIUS + 1; mr++) {
                    sum += (kernel[mr + GaussianBlur.GAUSS_RADIUS] * image[r][c + mr]);
                }

                //Normalize channel after blur
                sum /= norm;
                result[r - GaussianBlur.GAUSS_RADIUS][c - GaussianBlur.GAUSS_RADIUS] = (int) Math.round(sum);
            }
        }

        //Convolve image with kernel vertically
        for (int r = GaussianBlur.GAUSS_RADIUS; r < imgHeight - GaussianBlur.GAUSS_RADIUS; r++) {
            for (int c = GaussianBlur.GAUSS_RADIUS; c < imgWidth - GaussianBlur.GAUSS_RADIUS; c++) {
                double sum = 0.;

                for(int mr = -GaussianBlur.GAUSS_RADIUS; mr < GaussianBlur.GAUSS_RADIUS + 1; mr++) {
                    sum += (kernel[mr + GaussianBlur.GAUSS_RADIUS] * image[r + mr][c]);
                }

                //Normalize channel after blur
                sum /= norm;
                result[r - GaussianBlur.GAUSS_RADIUS][c - GaussianBlur.GAUSS_RADIUS] = (int) Math.round(sum);
            }
        }

        return result;
    }
}
