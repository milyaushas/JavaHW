package ru.hse.java.util;

@FunctionalInterface
public interface Function2 <X, Y, F> {
    F apply (X x, Y y);

    default <G> Function2<X, Y, G> compose(Function1<? super F, ? extends G> g) {
        return (x, y) -> g.apply(apply(x, y));
    }

    default Function1<Y, F> bind1(X bindedX) {
        return y -> apply(bindedX, y);
    }

    default Function1<X, F> bind2(Y bindedY) {
        return x -> apply(x, bindedY);
    }

    default Function1<X, Function1<Y, F>> curry() {
        return Function2.this::bind1;
    }
}
