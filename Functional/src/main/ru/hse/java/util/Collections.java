package ru.hse.java.util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collections {
    public static  <A, F> List<F> map(Function1<? super A, ? extends F> f, Iterable<A> a) {
        List<F>result = new ArrayList<>();
        a.forEach((element) -> result.add(f.apply(element)));
        return result;
    }

    public static  <A> List<A> filter(Predicate<? super A>p, Iterable<A> a) {
        List<A>result = new ArrayList<>();
        a.forEach((element) -> {if (p.apply(element)) result.add(element);});
        return result;
    }

    public static  <A> List<A> takeWhile(Predicate<? super A> p,Iterable<A> a) {
        List<A> result = new ArrayList<>();
        for (A element : a) {
            if (!p.apply(element)) {
                break;
            }
            result.add(element);
        }
        return result;
    }

    public static  <A> List<A> takeUnless(Predicate<? super A> p,Iterable<A> a) {
        return takeWhile(p.not(), a);
    }

    public static <A, B> A foldl(Function2<? super A, ? super B, ? extends A> f, A ini, Iterable<? extends B> a) {
        for (B element : a) {
            ini = f.apply(ini, element);
        }
        return ini;
    }

    public static <A, B> B foldr(Function2<? super A, ? super B, ? extends B> f, B ini, Iterable<? extends A> a) {
        ArrayList<A>elements = new ArrayList<>();
        for (A element : a) {
            elements.add(element);
        }
        for (int i = elements.size() - 1; i >= 0; i--) {
            ini = f.apply(elements.get(i), ini);
        }
        return ini;
    }
}

