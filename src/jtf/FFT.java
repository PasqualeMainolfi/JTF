package jtf;

public class FFT {

    public static Complex[] fft(Complex[] x) {

        Complex[] z = trasform(x, false);
        return z;

    }

    public static Complex[] ifft(Complex[] x, boolean normalize) {

        int n = x.length;
        double scalar = (double) 1/n;
        Complex[] z = trasform(x, true);
        if (normalize) {
            for (int i = 0; i < x.length; i++) {
                z[i].scale(scalar);
            }
        }

        return z;

    }

    public static Complex[] circularConvolution(Complex[] z1, Complex[] z2) {

        
        int n1 = z1.length;
        int n2 = z2.length;
        
        if (n1 != n2) {
            throw new IllegalArgumentException("[ERROR] dimensions must be equal and powers of 2!");
        }
        
        int n = n1;

        Complex[] a = fft(z1);
        Complex[] b = fft(z2);

        Complex[] y = new Complex[n];
        for (int i = 0; i < n; i++) {
            y[i] = Complex.mult(a[i], b[i]);
        }

        return ifft(y, true);

    }

    public static Complex[] linearConvolution(Complex[] z1, Complex[] z2) {

        Complex zero = new Complex();

        Complex[] a = new Complex[2 * z1.length];
        Complex[] b = new Complex[2 * z2.length];

        for (int i = 0; i < z1.length; i++) {
            a[i] = z1[i];
        }

        for (int i = z1.length; i < a.length; i++) {
            a[i] = zero;
        }

        for (int i = 0; i < z2.length; i++) {
            b[i] = z2[i];
        }
        
        for (int i = z1.length; i < b.length; i++) {
            b[i] = zero;
        }

        return circularConvolution(a, b);

    }

    private static Complex[] trasform(Complex[] x, boolean inverse) {

        int inversion = -1;
        if (inverse) {
            inversion = 1;
        }

        int n = x.length;
        
        if (n == 1) {
            return new Complex[] {x[0]};
        }

        if (n % 2 != 0) {
            throw new IllegalArgumentException("[ERROR] n must be power of 2!");
        }

        Complex[] even = new Complex[n/2];
        Complex[] odd = new Complex[n/2];
        for (int i = 0; i < n/2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

       Complex[] evenFFT = trasform(even, inverse);
       Complex[] oddFFT = trasform(odd, inverse);

        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double phi = (inversion * 2) * k * Math.PI/n;
            Complex wk = new Complex(Math.cos(phi), Math.sin(phi));
            Complex xe = Complex.mult(wk, oddFFT[k]);
            y[k] = Complex.add(evenFFT[k], xe);
            y[k + n/2] = Complex.sub(evenFFT[k], xe);
        }

        return y;

    }

    public static Complex[] zeroPad(Complex[] x) {

        int pot = (int) Math.ceil(Math.log(x.length)/Math.log(2));
        int n = (int) Math.pow(2, pot);

        Complex zero = new Complex();

        Complex[] y = new Complex[n];
        for (int i = 0; i < x.length; i++) {
            y[i] = x[i];
        }

        for (int i = x.length; i < n; i++) {
            y[i] = zero;
        }

        return y;

    }


}
