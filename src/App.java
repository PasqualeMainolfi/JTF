import java.util.Arrays;

import jtf.Complex;
import jtf.SpectrumDomain;
import jtf.WindowFunction;

public class App {
    public static void main(String[] args) {

        double[] x = new double[8];

        for (int i = 0; i < 8; i++) {
            x[i] = Math.sin(2 * Math.PI * i/8);
        }

        Complex[] vec = Complex.doubleToComplexArray(x);

        Complex[] xfft = SpectrumDomain.fft(vec);
        Complex[] xifft = SpectrumDomain.ifft(xfft, true);

        int winSize = 4;
        int hopSize = 2;
        
        WindowFunction win = new WindowFunction(winSize);
        double[] winFunc = win.blackmanHarris();

        Complex[][] xstft = SpectrumDomain.stft(vec, winFunc, hopSize);
        Complex[] xistft = SpectrumDomain.istft(xstft, winFunc, hopSize);


        double[] res1 = Complex.ComplexToDoubleArray(xifft);
        double[] res2 = Complex.ComplexToDoubleArray(xistft);

        double[] mag = Complex.getMag(xfft);
        System.out.println(Arrays.toString(mag));

        // System.out.println(Arrays.toString(vec));
        // System.out.println(Arrays.toString(res1));
        // System.out.println(Arrays.toString(xifft));
        // System.out.println(Arrays.toString(res2));
        // System.out.println(Arrays.toString(xistft));
        // System.out.println(xistft.length);
       
    }
}
