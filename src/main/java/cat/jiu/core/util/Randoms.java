package cat.jiu.core.util;

import java.util.Random;
import java.util.function.DoubleFunction;
import java.util.stream.*;

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

    public static byte[] nextBytes(int size) {
        return nextBytes(new byte[size]);
    }
    public static byte[] nextBytes(byte[] bytes) {
        getRandom().nextBytes(bytes);
        return bytes;
    }

    public static double nextExponential(double mean, double stddev) {
        return getRandom().nextGaussian(mean, stddev);
    }
    public static double nextExponential() {
        return getRandom().nextExponential();
    }

    public static boolean nextBoolean() {
        return getRandom().nextBoolean();
    }
    public static double nexGaussian() {
        return getRandom().nextGaussian();
    }

    public static IntStream ints() {
        return getRandom().ints();
    }
    public static IntStream ints(long size) {
        return getRandom().ints(size);
    }
    public static IntStream ints(int origin, int bound) {
        return getRandom().ints(origin, bound);
    }
    public static IntStream ints(long size, int origin, int bound) {
        return getRandom().ints(size, origin, bound);
    }

    public static LongStream longs() {
        return getRandom().longs();
    }
    public static LongStream longs(long size) {
        return getRandom().longs(size);
    }
    public static LongStream longs(long origin, long bound) {
        return getRandom().longs(origin, bound);
    }
    public static LongStream longs(long size, long origin, long bound) {
        return getRandom().longs(size, origin, bound);
    }

    /**
     * @see Random#doubles()
     * @see DoubleStream#mapToObj(DoubleFunction)
     */
    public static Stream<Float> floats() {
        return getRandom().doubles().mapToObj(d->(float)d);
    }
    /**
     * @see Random#doubles(long)
     * @see DoubleStream#mapToObj(DoubleFunction)
     */
    public static Stream<Float> floats(long size) {
        return getRandom().doubles(size).mapToObj(d->(float)d);
    }
    /**
     * @see Random#doubles(double, double)
     * @see DoubleStream#mapToObj(DoubleFunction)
     */
    public static Stream<Float> floats(float origin, float bound) {
        return getRandom().doubles(origin, bound).mapToObj(d->(float)d);
    }
    /**
     * @see Random#doubles(long, double, double)
     * @see DoubleStream#mapToObj(DoubleFunction)
     */
    public static Stream<Float> floats(long size, float origin, float bound) {
        return getRandom().doubles(size, origin, bound).mapToObj(d->(float)d);
    }

    public static DoubleStream doubles() {
        return getRandom().doubles();
    }
    public static DoubleStream doubles(long size) {
        return getRandom().doubles(size);
    }
    public static DoubleStream doubles(double origin, double bound) {
        return getRandom().doubles(origin, bound);
    }
    public static DoubleStream doubles(long size, double origin, double bound) {
        return getRandom().doubles(size, origin, bound);
    }

}
