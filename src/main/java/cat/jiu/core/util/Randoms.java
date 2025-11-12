package cat.jiu.core.util;

public class Randoms {
    private static java.util.Random random;
    public static void setRandom(java.util.Random random) {
        Randoms.random = random;
    }
    public static java.util.Random getRandom() {
        if (random == null) {
            random = new java.util.Random();
        }
        return random;
    }
    private static net.minecraft.util.RandomSource randomSource;
    public static net.minecraft.util.RandomSource getRandomSource() {
        if (randomSource == null) {
            randomSource = net.minecraft.util.RandomSource.createNewThreadLocalInstance();
        }
        return randomSource;
    }
    public static void setRandomSource(net.minecraft.util.RandomSource randomSource) {
        Randoms.randomSource = randomSource;
    }
    public static int nextInt() {
        return getRandom().nextInt();
    }
    public static int nextInt(int bound) {
        return getRandom().nextInt(bound);
    }
    public static int nextInt(int origin, int bound) {
        return getRandom().nextInt(origin, bound);
    }
    public static long nextLong() {
        return getRandom().nextLong();
    }
    public static long nextLong(long bound) {
        return getRandom().nextLong(bound);
    }
    public static long nextLong(long origin, long bound) {
        return getRandom().nextLong(origin, bound);
    }
    public static double nextDouble() {
        return getRandom().nextDouble();
    }
    public static double nextDouble(double bound) {
        return getRandom().nextDouble(bound);
    }
    public static double nextDouble(double origin, double bound) {
        return getRandom().nextDouble(origin, bound);
    }
    public static float nextFloat() {
        return getRandom().nextFloat();
    }
    public static float nextFloat(float bound) {
        return getRandom().nextFloat(bound);
    }
    public static float nextFloat(float origin, float bound) {
        return getRandom().nextFloat(origin, bound);
    }
    public static byte[] nextBytes(byte[] bytes) {
        getRandom().nextBytes(bytes);
        return bytes;
    }
    public static boolean nextBoolean() {
        return getRandom().nextBoolean();
    }
    public static double nexGaussian() {
        return getRandom().nextGaussian();
    }
    public static double nextExponential(double mean, double stddev) {
        return getRandom().nextGaussian(mean, stddev);
    }
    public static double nextExponential() {
        return getRandom().nextExponential();
    }
}
