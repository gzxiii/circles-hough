package it.gzxiii.circleshough.utils;

public class Circle implements Comparable<Circle>{
    public static short x;
    public static short y;
    public static short r;
    public static short magnitude;

    public Circle(int x, int y, int r){
        Circle.x = (short) x;
        Circle.y = (short) y;
        Circle.r = (short) r;
        Circle.magnitude = 1;
    }
    public Circle(int x, int y, int r, int magnitude) {
        Circle.x = (short) x;
        Circle.y = (short) y;
        Circle.r = (short) r;
        Circle.magnitude = (short) magnitude;
    }

    public double getMagnitude() {
        return magnitude;
    }


    @Override
    public int compareTo(Circle o) {
        return Double.compare(getMagnitude(), o.getMagnitude());
    }
}
