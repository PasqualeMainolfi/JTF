package jtf;

public class WindowFunction {
    
    private double PI = Math.PI;
    private int size;
    private double[] window;

    public WindowFunction(int size) {
        this.size = size;
        this.window = new double[size]; 
    }

    public double[] rect() {
        for (int i = 0; i < this.size; i++) {
            this.window[i] = 1;
        }
        return this.window;
    }

    public double[] hann() {
        for (int i = 0; i < this.size; i++) {
            this.window[i] = 0.5 * (1 - Math.cos(2 * PI * i/this.size));
        }
        return this.window;
    }

    public double[] hamming() {
        double a0 = 0.5;
        double a1 = 0.46;
        for (int i = 0; i < this.size; i++) {
            this.window[i] = a0 - a1 * Math.cos(2 * PI * i/this.size);
        }
        return this.window;
    }

    public double[] sine() {
        for (int i = 0; i < this.size; i++) {
            this.window[i] = Math.cos((PI * i/this.size) - (PI/2));
        }
        return this.window;
    }

    public double[] triangular() {
        double den = this.size/2;
        for (int i = 0; i < this.size; i++) {
            double num = i - this.size/2;
            this.window[i] = 1 - Math.abs(num/den);
        }
        return this.window;
    }

    public double[] blackmanHarris() {
        double a0 = 0.35875;
        double a1 = 0.48829;
        double a2 = 0.14128;
        double a3 = 0.01168;

        for (int i = 0; i < this.size; i++) {
            double n = i/this.size;
            this.window[i] = a0 - a1 * Math.cos(2 * PI * n) + a2 * Math.cos(4 * PI * n) - a3 * Math.cos(6 * PI * n);
        }
        return this.window;

    }

    public double[] gaussian(double sigma) {
        for (int i = 0; i < this.size; i++) {
            double arg = (i - this.size/2)/(sigma * this.size/2);
            this.window[i] = Math.exp(-0.5 * Math.pow(arg, 2));
        }
        return this.window;

    }

    
}
