package jtf;

public class Complex {

    private double real;
    private double imag;

    public Complex(double re, double im) {
        this.real = re;
        this.imag = im;
    }
    
    public Complex(double re) {
        this.real = re;
        this.imag = 0.0;
    }

    public Complex() {
        this.real = 0.0;
        this.imag = 0.0;
    }


    public double getRealPart() {
        return this.real;
    }

    public double getImagPart() {
        return this.imag;
    }
    
    public double getMag() {
        return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.imag, 2));
    }

    public double getArg() {
        return Math.atan2(this.imag, this.real);
    }

    private void set(Complex z) {
       this.real = z.real;
       this.imag = z.imag;
    }
    
    public void add(Complex z) {
        this.set(Complex.add(this, z));
    }
    
    public void sub(Complex z) {
        this.set(Complex.sub(this, z));
    }
    
    public void mult(Complex z) {
        this.set(Complex.mult(this, z));
    }
    
    public void div(Complex z) {
        this.set(Complex.div(this, z));
    }

    public void normalize() {
        double modulus = this.getMag();
        if (modulus == 0) {
            modulus = 1;
        }
        this.set(new Complex(this.real/modulus, this.imag/modulus));
    }

    public void scale(double scalar) {
        this.set(new Complex(this.real * scalar, this.imag * scalar));
    }

    public Complex conjugate() {
        return new Complex(this.real, -this.imag);
    }

    public Complex square() {
        double a = this.real * this.real - this.imag * this.imag;
        double b = 2 * this.real * this.imag;
        return new Complex(a, b);
    }

    public static Complex add(Complex z1, Complex z2) {
        double a = z1.real + z2.real;
        double b = z1.imag + z2.imag;
        return new Complex(a, b);
    }
    
    public static Complex sub(Complex z1, Complex z2) {
        double a = z1.real - z2.real;
        double b = z1.imag - z2.imag;
        return new Complex(a, b);
    }
    
    public static Complex mult(Complex z1, Complex z2) {
        // (a + bi) * (c + di) = (ac - bd) + (ad + bc)i
        double a = z1.real * z2.real - z1.imag * z2.imag;
        double b = z1.real * z2.imag + z1.imag * z2.real;
        return new Complex(a, b);
    }
    
    public static Complex div(Complex z1, Complex z2) {
        // ((a + bi) * (c - di)) / ((c + di) * (c - di)) = ((ac + bd) + (bc - ad)i) / (c^2 + d^2) = z1 * conj(z2)/z2 * conj(z2)
        double den = z2.getMag();
        double a = (z1.real * z2.real + z1.imag * z2.imag)/den;
        double b = (z1.imag * z2.real - z1.real * z2.imag)/den;
        return new Complex(a, b);
    }

    public static Complex[] doubleToComplexArray(double[] x) {
        int n = x.length;
        Complex[] z = new Complex[n];
        for (int i = 0; i < n; i++) {
            z[i] = new Complex(x[i]);
        }
        return z;
    }

    public static double[] ComplexToDoubleArray(Complex[] x) {
        int n = x.length;
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            y[i] = x[i].getRealPart();
        }
        return y;
    }

    @Override
    public String toString() {
        if (this.imag < 0) {
            return this.real + "-" + (-this.imag) + "j";
        } else {
            return this.real + "+" + this.imag + "j";
        }
    }

}
