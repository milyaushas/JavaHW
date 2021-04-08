package ru.hse.java.util;
import org.junit.jupiter.api.*;


public class PredicateTest {
    @Test
    public void testOr() {
        Predicate<Integer>p1 = x -> x % 2 == 0;
        Predicate<Integer>p2 = x -> x % 3 == 0;
        Predicate<Integer>pTrue = x -> x < 0;

        Assertions.assertTrue(p1.or(p2).apply(88));
        Assertions.assertTrue(p1.or(p2).apply(15));
        Assertions.assertTrue(p1.or(p2).apply(6));
        Assertions.assertFalse(p1.or(p2).apply(7));
        Assertions.assertEquals(p1.or(p2).apply(36456434), p2.or(p1).apply(36456434));
        Assertions.assertTrue(pTrue.ALWAYS_TRUE().or(p1).or(p2).apply(7));
    }

    @Test
    public void testAnd() {
        Predicate<Integer>p1 = x -> x % 2 == 0;
        Predicate<Integer>p2 = x -> x % 3 == 0;
        Predicate<Integer>pFalse = x -> x > 0;

        Assertions.assertTrue(p1.and(p2).apply(6));
        Assertions.assertFalse(p1.and(p2).apply(4));
        Assertions.assertFalse(p1.and(p2).apply(9));
        Assertions.assertFalse(p1.and(p2).apply(13));
        Assertions.assertEquals(p1.and(p2).apply(789), p2.and(p1).apply(789));
        Assertions.assertFalse(pFalse.ALWAYS_FALSE().and(p1).and(p2).apply(6));
    }

    @Test
    public void testNot() {
        Predicate<Integer>p = x -> x % 2 == 0;

        Assertions.assertTrue(p.not().apply(5));
        Assertions.assertFalse(p.not().apply(16));
        Assertions.assertEquals(p.not().apply(17), !p.apply(17));
        Assertions.assertNotEquals(p.not().apply(3), p.apply(3));
    }

    @Test
    public void testALWAYS_TRUE() {
        Predicate<String>p = x -> false;
        Assertions.assertTrue(p.ALWAYS_TRUE().apply("always"));
    }

    @Test
    public void testALWAYS_FALSE() {
        Predicate<Double>p = x -> true;
        Assertions.assertFalse(p.ALWAYS_FALSE().apply(0.1225));
    }
}
