package it.gzxiii.circleshough.transformations;

public class Kernel {

    protected double[] data;
    protected double norm;

    public Kernel() { }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public double getNorm() {
        return norm;
    }

    public void setNorm(double norm) {
        this.norm = norm;
    }
}