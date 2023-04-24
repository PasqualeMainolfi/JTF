## JTF

JTF allow you to work with signals in the frequency and time domain, in java.  

In frequency domain (class `SpectrumDomain`): 

- fft, ifft
- stft, istft
- linear and circular convolution
- spectral centroid
- spectral spread
- spectral skewness
- spectral kurtosis
- spectral entropy

In time domain (class `TimeDomain`):  

- amplitude envelope
- zero crossing rate
- rms

It also contain a class for working with complex numbers `Complex` and a class for window functions `WindowFunction`

here is an example...

```java

import java.util.Arrays;
import jtf.Complex;
import jtf.SpectrumDomain;
import jtf.TimeDomain;
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

        double[] cent = SpectrumDomain.centroid(xstft, 8);
        System.out.println(Arrays.toString(cent));
        
        double[] spread = SpectrumDomain.spread(xstft, cent, 8);
        System.out.println(Arrays.toString(spread));

        double[] skw = SpectrumDomain.skewness(xstft, cent, spread, 8);
        System.out.println(Arrays.toString(skw));

        double[] kurt = SpectrumDomain.kurtosis(xstft, cent, spread, 8);
        System.out.println(Arrays.toString(kurt));

        double[] ent = SpectrumDomain.entropy(xstft, hopSize, 8);
        System.out.println(Arrays.toString(ent));

        double[] env = TimeDomain.getEnergy(x, winSize, hopSize);
        System.out.println(Arrays.toString(env));

        System.out.println(Arrays.toString(vec));
        System.out.println(Arrays.toString(res1));
        System.out.println(Arrays.toString(xifft));
        System.out.println(Arrays.toString(res2));
        System.out.println(Arrays.toString(xistft));
        System.out.println(xistft.length);
       
    }
}

```



