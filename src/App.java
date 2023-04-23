import java.util.Arrays;

import jtf.Complex;
import jtf.FFT;

public class App {
    public static void main(String[] args) {

        double[] x = new double[] {1, 1, 1, 1, -1, -1, -1, -1};
        Complex[] vec = Complex.doubleToComplexArray(x);

        Complex[] xfft = FFT.fft(vec);
        Complex[] xifft = FFT.ifft(xfft, true);

        double[] res = Complex.ComplexToDoubleArray(xifft);

        System.out.println(Arrays.toString(res));
        System.out.println(Arrays.toString(xfft));
       
    }
}
