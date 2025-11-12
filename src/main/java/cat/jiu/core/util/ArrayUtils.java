package cat.jiu.core.util;

import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

public class ArrayUtils {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final Byte[] EMPTY_BYTE_ARRAY_ = new Byte[0];

    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final Short[] EMPTY_SHORT_ARRAY_ = new Short[0];

    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final Integer[] EMPTY_INT_ARRAY_ = new Integer[0];

    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Long[] EMPTY_LONG_ARRAY_ = new Long[0];

    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final Float[] EMPTY_FLOAT_ARRAY_ = new Float[0];

    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final Double[] EMPTY_DOUBLE_ARRAY_ = new Double[0];

    public static final Number[] EMPTY_NUMBER_ARRAY = new Number[0];

    public static final BigInteger[] EMPTY_BIG_INTEGER_ARRAY = new BigInteger[0];
    public static final BigDecimal[] EMPTY_BIG_DECIMAL_ARRAY = new BigDecimal[0];

    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final Boolean[] EMPTY_BOOLEAN_ARRAY_ = new Boolean[0];

    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    
    @SafeVarargs
    public static <T, R> R[] cast(Function<Integer, R[]> arrayCreate, Function<T, R> objCast, T... other) {
        R[] array = arrayCreate.apply(other.length);
        for (int i = 0; i < array.length; i++) {
            array[i] = objCast.apply(other[i]);
        }
        return array;
    }

    public static <R, E> R[] asArray(Iterable<E> list, Function<Integer, R[]> newArray, Function<E, R> elementGetter, R[] empty) {
        if (Iterables.isEmpty(list)) {
            return empty;
        }
        R[] result = newArray.apply(Iterables.size(list));
        int index = 0;
        for (E e : list) {
            result[index] = elementGetter.apply(e);
            index++;
        }
        return result;
    }
    
    public static byte[] toArray(Byte[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static Byte[] toArray(byte[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_BYTE_ARRAY_;
        }
        Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static short[] toArray(Short[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static Short[] toArray(short[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_SHORT_ARRAY_;
        }
        Short[] result = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static int[] toArray(Integer[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static Integer[] toArray(int[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_INT_ARRAY_;
        }
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static long[] toArray(Long[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static Long[] toArray(long[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_LONG_ARRAY_;
        }
        Long[] result = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static float[] toArray(Float[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static Float[] toArray(float[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_FLOAT_ARRAY_;
        }
        Float[] result = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static double[] toArray(Double[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static Double[] toArray(double[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_DOUBLE_ARRAY_;
        }
        Double[] result = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static boolean[] toArray(Boolean[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
    public static Boolean[] toArray(boolean[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_BOOLEAN_ARRAY_;
        }
        Boolean[] result = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static String[] toStringArray(JsonArray array) {
        String[] result = new String[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsString();
        }
        return result;
    }
    public static JsonArray toStringJsonArray(String[] array) {
        JsonArray result = new JsonArray();
        for (String s : array) {
            result.add(s);
        }
        return result;
    }
    public static boolean[] toBooleanArray(JsonArray array) {
        boolean[] result = new boolean[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsBoolean();
        }
        return result;
    }
    public static JsonArray toBooleanJsonArray(boolean[] array) {
        JsonArray result = new JsonArray();
        for (boolean b : array) {
            result.add(b);
        }
        return result;
    }
    public static byte[] toByteArray(JsonArray array) {
        byte[] result = new byte[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsByte();
        }
        return result;
    }
    public static JsonArray toByteJsonArray(byte[] array) {
        JsonArray result = new JsonArray();
        for (Number num : array) {
            result.add(num);
        }
        return result;
    }
    public static short[] toShortArray(JsonArray array) {
        short[] result = new short[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsShort();
        }
        return result;
    }
    public static JsonArray toShortJsonArray(short[] array) {
        JsonArray result = new JsonArray();
        for (Number num : array) {
            result.add(num);
        }
        return result;
    }
    public static int[] toIntArray(JsonArray array) {
        int[] result = new int[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsInt();
        }
        return result;
    }
    public static JsonArray toIntJsonArray(int[] array) {
        JsonArray result = new JsonArray();
        for (Number num : array) {
            result.add(num);
        }
        return result;
    }
    public static long[] toLongArray(JsonArray array) {
        long[] result = new long[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsLong();
        }
        return result;
    }
    public static JsonArray toLongJsonArray(long[] array) {
        JsonArray result = new JsonArray();
        for (Number num : array) {
            result.add(num);
        }
        return result;
    }
    public static float[] toFloatArray(JsonArray array) {
        float[] result = new float[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsFloat();
        }
        return result;
    }
    public static JsonArray toFloatJsonArray(float[] array) {
        JsonArray result = new JsonArray();
        for (Number num : array) {
            result.add(num);
        }
        return result;
    }
    public static double[] toDoubleArray(JsonArray array) {
        double[] result = new double[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsDouble();
        }
        return result;
    }
    public static JsonArray toDoubleJsonArray(double[] array) {
        JsonArray result = new JsonArray();
        for (Number num : array) {
            result.add(num);
        }
        return result;
    }
    public static BigInteger[] toBigIntegerArray(JsonArray array) {
        BigInteger[] result = new BigInteger[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsBigInteger();
        }
        return result;
    }
    public static JsonArray toBigIntegerJsonArray(BigInteger[] array) {
        JsonArray result = new JsonArray();
        for (BigInteger num : array) {
            result.add(num.toString());
        }
        return result;
    }
    public static BigDecimal[] toBigDecimalArray(JsonArray array) {
        BigDecimal[] result = new BigDecimal[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsBigDecimal();
        }
        return result;
    }
    public static JsonArray toBigDecimalJsonArray(BigDecimal[] array) {
        JsonArray result = new JsonArray();
        for (BigDecimal num : array) {
            result.add(num.toString());
        }
        return result;
    }
    public static Number[] toNumberArray(JsonArray array) {
        Number[] result = new Number[array.size()];
        for(int i = 0; i < array.size(); i++) {
            result[i] = array.get(i).getAsNumber();
        }
        return result;
    }
    public static JsonArray toNumberJsonArray(Number[] array) {
        JsonArray result = new JsonArray();
        for (Number num : array) {
            result.add(num);
        }
        return result;
    }
}
