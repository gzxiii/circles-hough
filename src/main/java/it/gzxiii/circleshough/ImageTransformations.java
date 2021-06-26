package it.gzxiii.circleshough;

import java.awt.image.BufferedImage;

public interface ImageTransformations {
    BufferedImage convert2Gray();
    BufferedImage gaussianBlur(int destImageType, double sigma);
}
