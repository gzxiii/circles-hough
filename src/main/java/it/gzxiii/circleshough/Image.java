package it.gzxiii.circleshough;

import it.gzxiii.circleshough.transformations.Common;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

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

    public Image(String filePath) {
        uri = filePath;
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
     * Apply gaussian blur on the image.
     * @since 0.0.1
     * @author gzxiii
     * @param destImageType, float
     * @return BufferedImage
     */
    public BufferedImage gaussianBlur(int destImageType, double sigma){
        int KERNEL_WIDTH = 3;
        int KERNEL_HEIGHT = 3;

        double coeff = 1/(2*Math.PI* Math.pow(sigma, 2));
        double[][] imgArray = Common.BuffImg2Matrix(this.img);

        BufferedImage res =  new BufferedImage(this.img.getWidth(), this.img.getHeight(), type);

        float[] matrix = {
                1/16f, 1/8f, 1/16f,
                1/8f, 1/4f, 1/8f,
                1/16f, 1/8f, 1/16f,
        };

        BufferedImageOp op = new ConvolveOp(new Kernel(KERNEL_WIDTH, KERNEL_HEIGHT, matrix));
        op.filter(this.img,res);

        return res;
    }
}
