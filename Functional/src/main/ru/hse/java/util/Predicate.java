package ru.hse.java.util;

@FunctionalInterface
public interface Predicate<T> extends Function1<T, Boolean>{

    default Predicate<T> or(Predicate<? super T> other) {
        return t -> apply(t) || other.apply(t);
    }

    default Predicate<T> and(Predicate<? super T> other) {
        return t -> apply(t) && other.apply(t);
    }

    default Predicate<T> not() {
        return t -> !apply(t);
    }

    default Predicate<T> ALWAYS_TRUE() {
        return t -> true;
    }

    default Predicate<T> ALWAYS_FALSE() {
        return t -> false;
    }

}
