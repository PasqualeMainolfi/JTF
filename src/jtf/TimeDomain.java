package jtf;


public class TimeDomain {
    
    public static double[] amplitudeEnvelope(double[] x, int winSize, int hopSize) {

        int n = (int) Math.ceil((double) (x.length - winSize)/hopSize) + 1;
        double[] peek = new double[n];

        int hop = 0;
        for (int i = 0; i < n; i++) {
            double maxValue = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < winSize; j++) {
                maxValue = Math.max(maxValue, x[hop + j]);
            }
            peek[i] = maxValue;
            hop += hopSize;
        }

        return peek;

    }

    public static double[] zeroCrossingRate(double[] x, int winSize, int hopSize) {

        int n = (int) Math.ceil((double) (x.length - winSize)/hopSize) + 1;
        double[] zcr = new double[n];

        int hop = 0;
        for (int i = 0; i < n; i++) {
            double z = 0.0;
            for (int j = 0; j < winSize - 1; j++) {
                z += Math.abs(sign(x[j + hop + 1]) - sign(x[j + hop]));
            }
            zcr[i] = z/2 * winSize;
            hop += hopSize;
        }

        return zcr;

    }

    public static double[] getEnergy(double[] x, int winSize, int hopSize) {

        int n = (int) Math.ceil((double) (x.length - winSize)/hopSize) + 1;
        double[] rms = new double[n];

        int hop = 0;
        for (int i = 0; i < n; i++) {
            double r = 0.0;
            for (int j = 0; j < winSize; j++) {
                r += Math.pow(x[j + hop], 2);
            }
            rms[i] = Math.sqrt(r/winSize);
            hop += hopSize;
        }

        return rms;

        
    }

    private static double sign(double value) {
        
        if (value >= 0) {
            return 1.0;
        } 

        return -1.0;

    }

}
