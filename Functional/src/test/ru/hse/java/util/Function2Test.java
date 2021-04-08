package ru.hse.java.util;
import org.junit.jupiter.api.*;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Function2Test {
    @Test
    public void testCompose() {
        Function2<String, String, Integer>  f = (x, y) -> Integer.parseInt(x + y);
        Function2<String, Integer, Integer> h = (x, y) -> Integer.parseInt(x) + y;
        Function1<Integer, String>          g = Object::toString;

        Assertions.assertEquals(f.compose(g).apply("12", "345"), "12345");
        Assertions.assertEquals(h.compose(g).apply("12", 1), "13");

        BiFunction<String, String, Integer>  f1 = (x, y) -> Integer.parseInt(x + y);
        BiFunction<String, Integer, Integer> h1 = (x, y) -> Integer.parseInt(x) + y;
        Function<Integer, String>            g1 = Object::toString;

        Assertions.assertEquals(f.compose(g).apply("2001", "2021"), f1.andThen(g1).apply("2001", "2021"));
        Assertions.assertEquals(h.compose(g).apply("18695", 255), h1.andThen(g1).apply("18695", 255));
    }

    @Test
    public void testBind1() {
        Function2<String, Integer, Integer> f = (x, y) -> Integer.parseInt(x) + y;
        Function1<Integer, Integer> expected  = y -> Integer.parseInt("123") + y;

        Assertions.assertEquals(f.bind1("123").apply(1), expected.apply(1));
        Assertions.assertEquals(f.bind1("123").apply(2), f.apply("123", 2));
        Assertions.assertNotEquals(f.bind1("123").apply(4), f.bind1("12").apply(34));
    }

    @Test
    public void testBind2() {
        Function2<String, Integer, Integer> f = (x, y) -> Integer.parseInt(x) + y;
        Function1<String, Integer> expected  = x -> Integer.parseInt(x) + 5;

        Assertions.assertEquals(f.bind2(5).apply("123"), expected.apply("123"));
        Assertions.assertEquals(f.bind2(5).apply("123"), f.apply("123", 5));
        Assertions.assertNotEquals(f.bind2(5).apply("123"), f.bind2(35).apply("12"));
    }

    @Test
    public void testCurry() {
        Function2<String, String, Integer>  f = (x, y) -> Integer.parseInt(x + y);
        Function2<String, Integer, Integer> g = (x, y) -> Integer.parseInt(x) + y;
        Function2<String, Integer, Double>  h = (x, y) -> (double) (y / 2);

        Assertions.assertEquals(f.curry().apply("1").apply("2"), f.apply("1", "2"));
        Assertions.assertEquals(g.curry().apply("1").apply(2), g.apply("1", 2));
        Assertions.assertEquals(h.curry().apply("1").apply(2), h.apply("1", 2));
    }
}
