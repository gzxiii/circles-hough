package it.gzxiii.circleshough;

import it.gzxiii.circleshough.transformations.GaussianBlur;
import it.gzxiii.circleshough.transformations.Kernel;

import java.awt.image.BufferedImage;
import static it.gzxiii.circleshough.transformations.Common.*;

/**
 * Image Class
 * @author gzxiii
 * @since  0.0.1
 */
public class Image implements ImageTransformations {

    protected String uri;
    protected int width;
    protected int height;
    protected int type;
    protected BufferedImage img;
    protected int[][] imgArray;
    protected int[][] gx;
    protected int[][] gy;
    protected int[][] edges;
    protected double[][] mag;
    protected int[][] dir;

    public Image(String filePath) {
        this.uri = filePath;
    }

    /**
     * Converts to grayscale.
     * @since 0.0.1
     * @author gzxiii
     * @return BufferedImage
     */
    public BufferedImage convert2Gray(){
        BufferedImage res = new BufferedImage(this.img.getWidth(), this.img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        res.getGraphics().drawImage(this.img, 0 , 0, null);
        return res;
    }

    /**
     * Apply gaussian blur on the image. The image must be in Gray Scale!!!
     * @since 0.0.1
     * @author gzxiii
     * @param sigma gauss blur sigma
     * @return BufferedImage
     */
    public BufferedImage gaussianBlur(double sigma){
        Kernel kernel = new Kernel();
        GaussianBlur.GaussianKernel(sigma, kernel);
        this.imgArray = GaussianBlur.GaussConvolution(kernel.getData(), BuffImg2Matrix(this.img), kernel.getNorm());

        return intMatrix2BufferedImage(this.imgArray, BufferedImage.TYPE_BYTE_GRAY);
    }
}
