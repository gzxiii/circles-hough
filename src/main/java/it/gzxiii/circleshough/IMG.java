package it.gzxiii.circleshough;

import it.gzxiii.circleshough.transformations.GaussianBlur;
import it.gzxiii.circleshough.transformations.Kernel;
import it.gzxiii.circleshough.utils.Circle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import static it.gzxiii.circleshough.transformations.Common.*;

/**
 * Image Class
 * @author gzxiii
 * @since  0.0.1
 */
public class IMG implements ImageTransformations {

    protected String uri;
    protected int width;
    protected int height;
    protected int type;
    public BufferedImage img;
    public BufferedImage edgesImg;
    public BufferedImage blurred;
    protected int[][] imgArray;
    protected int[][] gx;
    protected int[][] gy;
    protected int[][] edges;
    protected double[][] mag;
    protected int[][] dir;
    protected double[][] houghSpaces;
    public static ArrayList<Circle> circles;

    public IMG(String filePath) {
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

    /**
     * Draw circles found after hough transform
     * @since 0.0.3
     * @author gzxiii
     */
    public void drawCircles(){
        BufferedImage out = new BufferedImage(edgesImg.getWidth(), edgesImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        Collections.sort(circles, Collections.reverseOrder());
        out.getGraphics().drawImage(blurred, 0, 0, null);
        Graphics2D g = out.createGraphics();
        g.setColor(Color.RED);
        for(int c = 0; c < 1; c++){
            Circle circle = circles.get(c);
            double a =  circle.x - circle.r * Math.cos(0 * Math.PI / 180);
            double b =  circle.y - circle.r * Math.sin(90 * Math.PI / 180);
            g.drawOval((int)a,(int)b,2*circle.r,2*circle.r);
        }
        img = out;
    }
}