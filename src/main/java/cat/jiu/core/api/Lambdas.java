package cat.jiu.core.api;

public class Lambdas {
    public interface Function2<T1, T2, R> {
        R apply(T1 t1, T2 t2);
    }
    public interface Function3<T1, T2, T3, R> {
        R apply(T1 t1, T2 t2, T3 t3);
    }
    public interface Function4<T1, T2, T3, T4, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4);
    }
    public interface Function5<T1, T2, T3, T4, T5, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
    }
    public interface Function_WithException<T, R, E extends Throwable> {
        R apply(T t) throws E;
    }
    public interface Function2_WithException<T1, T2, R, E extends Throwable> {
        R apply(T1 t1, T2 t2) throws E;
    }
    public interface Function3_WithException<T1, T2, T3, R, E extends Throwable> {
        R apply(T1 t1, T2 t2, T3 t3) throws E;
    }
    public interface Function4_WithException<T1, T2, T3, T4, R, E extends Throwable> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws E;
    }
    public interface Function5_WithException<T1, T2, T3, T4, T5, R, E extends Throwable> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) throws E;
    }

    public interface Consumer2<T1, T2> {
        void accept(T1 t1, T2 t2);
    }
    public interface Consumer3<T1, T2, T3> {
        void accept(T1 t1, T2 t2, T3 t3);
    }
    public interface Consumer4<T1, T2, T3, T4> {
        void accept(T1 t1, T2 t2, T3 t3, T4 t4);
    }
    public interface Consumer5<T1, T2, T3, T4, T5> {
        void accept(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
    }
    public interface Consumer_WithException<T, E extends Throwable> {
        void accept(T t) throws E;
    }
    public interface Consumer2_WithException<T1, T2, E extends Throwable> {
        void accept(T1 t1, T2 t2) throws E;
    }
    public interface Consumer3_WithException<T1, T2, T3, E extends Throwable> {
        void accept(T1 t1, T2 t2, T3 t3) throws E;
    }
    public interface Consumer4_WithException<T1, T2, T3, T4, E extends Throwable> {
        void accept(T1 t1, T2 t2, T3 t3, T4 t4) throws E;
    }
    public interface Consumer5_WithException<T1, T2, T3, T4, T5, E extends Throwable> {
        void accept(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) throws E;
    }

    public interface Supplier_WithException<R, E extends Throwable> {
        R get() throws E;
    }
}
