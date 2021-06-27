package it.gzxiii.circleshough;

import it.gzxiii.circleshough.transformations.GaussianBlur;
import it.gzxiii.circleshough.transformations.Kernel;
import it.gzxiii.circleshough.utils.Circle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param num_circles number of desired circles
     * @author gzxiii
     */
    public void drawCircles(int num_circles){
        BufferedImage out = new BufferedImage(edgesImg.getWidth(), edgesImg.getHeight(), BufferedImage.TYPE_INT_RGB);

        RescaleOp op = new RescaleOp(0.5f, 0, null);
        BufferedImage totalTmp = scaleTotal(this);
        BufferedImage total = op.filter(totalTmp, totalTmp);

        circles.sort(Collections.reverseOrder());

        out.getGraphics().drawImage(total, 0, 0, null);
        Graphics2D g = out.createGraphics();
        g.setColor(Color.GREEN);

        for(int c = 0; c < num_circles; c++){
            Circle circle = circles.get(c);
            double a =  circle.x - circle.r * Math.cos(0 * Math.PI / 180);
            double b =  circle.y - circle.r * Math.sin(90 * Math.PI / 180);
            g.drawOval((int)a,(int)b,2* circle.r,2* circle.r);
        }
        img = out;
    }

    /**
     * Scale image
     * @since 0.0.4
     * @author gzxiii
     */
    public static BufferedImage scaleTotal(IMG image){
        BufferedImage total = new BufferedImage(image.edgesImg.getWidth(), image.edgesImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        double max = 0;
        for(int i = 0; i< image.edgesImg.getWidth(); i++){
            for(int j = 0; j<image.edgesImg.getHeight(); j++){
                if(image.edges[i][j]>max){
                    max = image.edges[i][j];
                }
            }
        }
        for(int i = 0; i< image.edgesImg.getWidth(); i++){
            for(int j = 0; j<image.edgesImg.getHeight(); j++){
                //maps every pixel to a grayscale value between 0 and 255 from between 0 and the max value in sobelTotal
                double ratio = 500/max;
                int rgb = new Color((int)ratio * image.edges[i][j],
                        (int)ratio * image.edges[i][j],
                        (int)ratio * image.edges[i][j]).getRGB();
                total.setRGB(i,j,rgb);
            }
        }

        RescaleOp op = new RescaleOp(20.0f, 0, null);
        return op.filter(total, total);
    }
}