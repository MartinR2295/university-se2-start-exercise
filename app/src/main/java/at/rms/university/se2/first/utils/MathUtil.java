package at.rms.university.se2.first.utils;

/**
 * Util Class for Math Operations
 */
public class MathUtil {

    /**
     * use the euclid, to calculate the greatest common divisor
     * @param a
     * @param b
     * @return greatest common divisor
     */
    public static int euclid(int a, int b) {
        if (b == 0) return a;
        return MathUtil.euclid(b, a % b);
    }
}
