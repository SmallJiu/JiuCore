package cat.jiu.core.api;

public interface FailBack<T1, T2, R> {
    R apply(T1 a, T2 b);
}
