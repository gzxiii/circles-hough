package it.gzxiii.circleshough.utils;

import it.gzxiii.circleshough.transformations.CircleHough;

public class Circle implements Comparable<Circle>{
    public static int x;
    public static int y;
    public static int r;
    public static double magnitude;

    public Circle(int x, int y, int r, double magnitude) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.magnitude = magnitude;
    }

    public static double getMagnitude() {
        return magnitude;
    }


    @Override
    public int compareTo(Circle o) {
        return Double.compare(getMagnitude(), o.getMagnitude());
    }
}
