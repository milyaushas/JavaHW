package ru.hse.java.util;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionsTest {
    private List<Integer> a;
    private List<Integer> expected;

    @BeforeEach
    public void init() {
        a = new ArrayList<>();
        expected = new ArrayList<>();
    }

    @Test
    public void testMap() {
        Function1<Integer, Integer> f = x -> x * x;
        for (int i = 1; i <= 10; i++) {
            a.add(i);
            expected.add(i * i);
        }
        Assertions.assertEquals(Collections.map(f, a), expected);
    }

    @Test
    public void testFilter() {
        Predicate<Integer> p = x -> x % 3 == 0;
        for (int i = 1; i <= 10; i++) {
            a.add(i);
            if (p.apply(i)) {
                expected.add(i);
            }
        }
        Assertions.assertEquals(Collections.filter(p, a), expected);
    }

    @Test
    public void testTakeWhile() {
        Predicate<Integer> p = x -> x < 7;
        boolean stop = false;
        for (int i = 1; i <= 10; i++) {
            a.add(i);
            if (!p.apply(i)) {
                stop = true;
            }
            if (!stop) {
                expected.add(i);
            }
        }
        Assertions.assertEquals(Collections.takeWhile(p, a), expected);
    }

    @Test
    public void testTakeUnless() {
        Predicate<Integer> p = x -> x >= 7;
        boolean stop = false;
        for (int i = 1; i <= 10; i++) {
            a.add(i);
            if (p.apply(i)) {
                stop = true;
            }
            if (!stop) {
                expected.add(i);
            }
        }
        Assertions.assertEquals(Collections.takeUnless(p, a), expected);
    }

    @Test
    public void testFoldl() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x - y;
        Integer ini = 100;
        a.add(1);
        a.add(2);
        a.add(3);
        Assertions.assertEquals(Collections.foldl(f, ini, a), 94);

        Function2<String, String, String> g = (x, y) -> x + y;
        List<String> str = new ArrayList<String>();
        str.add(" is ");
        str.add("passed");
        str.add("!");
        Assertions.assertEquals(Collections.foldl(g, "Test", str), "Test is passed!");
    }

    @Test
    public void testFoldr() {
        Function2<Integer, Integer, Integer> f = (x, y) -> x / y;
        a.add(8);
        a.add(12);
        a.add(24);
        a.add(4);
        Assertions.assertEquals(8, Collections.foldr(f, 2, a));

        Function2<String, String, String> g = (x, y) -> x + y;
        List<String> str = new ArrayList<String>();
        str.add("Test");
        str.add(" is ");
        str.add("passed");
        Assertions.assertEquals("Test is passed!", Collections.foldr(g, "!", str));
    }
}
