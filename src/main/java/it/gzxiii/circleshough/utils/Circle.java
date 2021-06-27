package it.gzxiii.circleshough.utils;

public class Circle implements Comparable<Circle>{
    public short x;
    public  short y;
    public  short r;
    public  short magnitude;

    public Circle(int x, int y, int r){
        this.x = (short) x;
        this.y = (short) y;
        this.r = (short) r;
        this.magnitude = 1;
    }
    public Circle(int x, int y, int r, int magnitude) {
        this.x = (short) x;
        this.y = (short) y;
        this.r = (short) r;
        this.magnitude = (short) magnitude;
    }

    public double getMagnitude() {
        return magnitude;
    }


    @Override
    public int compareTo(Circle o) {
        return Double.compare(getMagnitude(), o.getMagnitude());
    }
}
