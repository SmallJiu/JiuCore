package cat.jiu.core.util;

import java.util.function.Function;

public class ArrayUtils {
    public static <T, R> R[] create(Function<Integer, R[]> arrayCreate, Function<T, R> function, T... other) {
        R[] array = arrayCreate.apply(other.length);
        for (int i = 0; i < array.length; i++) {
            array[i] = function.apply(other[i]);
        }
        return array;
    }
}
