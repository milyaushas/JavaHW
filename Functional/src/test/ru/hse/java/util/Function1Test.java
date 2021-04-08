package ru.hse.java.util;
import org.junit.jupiter.api.*;

import java.util.function.Function;

public class Function1Test {
    private Function1<Integer, Integer> f = x -> x + 7;
    private Function1<Integer, Integer> g = x -> x * x;
    private Function1<Integer, String>  h = Object::toString;
    private Function1<String, Integer>  p = Integer::parseInt;

    @Test
    public void testComposeFG() {
        Assertions.assertEquals(g.compose(f).apply(13), 176);
    }

    @Test
    public void testComposeGF() {
        Assertions.assertEquals(f.compose(g).apply(13), 400);
    }

    @Test
    public void testComposeHF() {
        Assertions.assertEquals(f.compose(h).apply(13), "20");
    }

    @Test
    public void testComposeGP() {
        Assertions.assertEquals(p.compose(g).apply("13"), 169);
    }

    @Test
    public void testComposeHP() {
        Assertions.assertEquals(p.compose(h).apply("13"), "13");
    }

    @Test
    public void testComposePHGF() {
        Assertions.assertEquals(f.compose(g).compose(h).compose(p).apply(13), 400);
    }

    @Test
    public void testCompareToFunction() {
        Function<Integer, Integer> f1 = x -> x + 7;
        Function<Integer, Integer> g1 = x -> x * x;
        Function<Integer, String>  h1 = Object::toString;
        Function<String, Integer>  p1 = Integer::parseInt;

        Assertions.assertEquals(f.compose(g).apply(875), f1.andThen(g1).apply(875));
        Assertions.assertEquals(f.compose(h).apply(23131), f1.andThen(h1).apply(23131));
        Assertions.assertEquals(h.compose(p).apply(666), h1.andThen(p1).apply(666));
        Assertions.assertEquals(p.compose(g).apply("789"), p1.andThen(g1).apply("789"));
        Assertions.assertEquals(f.compose(g).compose(h).compose(p).apply(256),
                                f1.andThen(g1).andThen(h1).andThen(p1).apply(256));
    }

}
