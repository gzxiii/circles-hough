package it.gzxiii.circleshough;

import java.awt.image.BufferedImage;

public interface ImageTransformations {
    BufferedImage convert2Gray();
    BufferedImage gaussianBlur(double sigma);
}
