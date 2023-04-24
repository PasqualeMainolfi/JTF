package jtf;

import java.util.Arrays;

public class SpectrumDomain {

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

    public static Complex[][] stft(Complex[] x, double[] win, int hopSize) {

        int winSize = win.length;
        int row = winSize;
        int col = (int) Math.ceil((double) (x.length - winSize)/hopSize) + 1;

        Complex[][] y = new Complex[row][col];

        int hop = 0;
        for (int i = 0; i < col; i++) {
            Complex[] temp = Arrays.copyOfRange(x, hop, hop + winSize);
            Complex[] fftTemp = fft(temp);
            for (int j = 0; j < row; j++) {
                double scaleValue = win[j];
                y[j][i] = Complex.scale(fftTemp[j], scaleValue);
            }
            hop += hopSize;
        }

        return y;

    }

    public static Complex[] istft(Complex[][] x, double[] win, int hopSize) {

        int row = x.length;
        int col = x[0].length;

        int n = (int) ((col - 1) * hopSize + row);
        Complex[] y = Complex.zeros(n);
        double[] winSum = new double[n];

        int hop = 0;
        for (int i = 0; i < col; i++) {
            Complex[] temp = new Complex[row];
            for (int j = 0; j < row; j++) {
                temp[j] = x[j][i];
                winSum[hop + j] += win[j];
            }
            
            temp = ifft(temp, true);
            for (int k = 0; k < row; k++) {
                y[hop + k] = Complex.add(y[hop + k], temp[k]);
            }

            hop += hopSize;
        }

        for (int k = 0; k < n; k++) {
            y[k] = Complex.scale(y[k], (double) 1/winSum[k]);
        }

        return y;

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

    public static double[] centroid(Complex[][] xstft, int sampleRate) {

        double[][] x = Complex.ComplexToDoubleArray(xstft);
        int sr = (int) sampleRate/2;
        int row = x.length; 
        int col = x[0].length;

        double[] cent = new double[col];

        for (int i = 0; i < col; i++) {
            double num = 0.0;
            double den = 0.0;
            for (int j = 0; j < row; j++) {
                double f = (double) (sr * j/row);
                num += Math.abs(x[j][i]) * f;
                den += Math.abs(x[j][i]);
            }
            cent[i] = num/den;
        }

        return cent;

    }

    public static double[] spread(Complex[][] xstft, double[] spectralCentroid, int sampleRate) {

        double[][] x = Complex.ComplexToDoubleArray(xstft);
        int sr = (int) sampleRate/2;
        int row = x.length; 
        int col = x[0].length;

        double[] sprd = new double[col];

        for (int i = 0; i < col; i++) {
            double num = 0.0;
            double den = 0.0;
            for (int j = 0; j < row; j++) {
                double f = (double) (sr * j/row);
                num += Math.pow(f - spectralCentroid[i], 2) * Math.pow(Math.abs(x[j][i]), 2);
                den += Math.pow(Math.abs(x[j][i]), 2);
            }
            sprd[i] = num/den;
        }

        return sprd;

    }

    public static double[] skewness(Complex[][] xstft, double[] spectralCentroid, double[] sprectralSpread, int sampleRate) {

        double[][] x = Complex.ComplexToDoubleArray(xstft);
        int sr = (int) sampleRate/2;
        int row = x.length; 
        int col = x[0].length;

        double[] skw = new double[col];

        for (int i = 0; i < col; i++) {
            double num = 0.0;
            double den = 0.0;
            for (int j = 0; j < row; j++) {
                double f = (double) (sr * j/row);
                num += Math.pow(f - spectralCentroid[i], 3) * Math.abs(x[j][i]);
                den += Math.abs(x[j][i]);
            }
            skw[i] = num/(Math.pow(sprectralSpread[i], 3) * den);
        }

        return skw;

    }

    public static double[] kurtosis(Complex[][] xstft, double[] spectralCentroid, double[] sprectralSpread, int sampleRate) {

        double[][] x = Complex.ComplexToDoubleArray(xstft);
        int sr = (int) sampleRate/2;
        int row = x.length; 
        int col = x[0].length;

        double[] kurt = new double[col];

        for (int i = 0; i < col; i++) {
            double num = 0.0;
            double den = 0.0;
            for (int j = 0; j < row; j++) {
                double f = (double) (sr * j/row);
                num += Math.pow(f - spectralCentroid[i], 4) * Math.abs(x[j][i]);
                den += Math.abs(x[j][i]);
            }
            kurt[i] = num/(Math.pow(sprectralSpread[i], 4) * den);
        }

        return kurt;

    }

    public static double[] entropy(Complex[][] xstft, int hopSize, int sampleRate) {

        double[][] x = Complex.ComplexToDoubleArray(xstft);
        int sr = (int) sampleRate/2;
        int row = x.length; 
        int col = x[0].length;

        double[] spe = new double[col];

        for (int i = 0; i < col; i++) {
            double num = 0.0;
            for (int j = 0; j < row; j++) {
                double f = (double) (sr * j/row);
                double sk = Math.abs(x[j][i]);
                num += f * Math.log(sk);
            }
            double b1 = hopSize * i;
            double b2 = b1 + row;
            spe[i] = -num/Math.log(b2 - b1);
        }

        return spe;

    }





}
