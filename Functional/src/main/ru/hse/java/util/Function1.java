package ru.hse.java.util;

@FunctionalInterface
public interface Function1 <X, F> {
    F apply (X x);

    default <G> Function1<X, G> compose(Function1<? super F, ? extends G> g) {
        return x -> g.apply(apply(x));
    }
}


