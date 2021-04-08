package ru.hse.java.util;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class DictionaryTest {
    Dictionary<String, Integer> dict;

    @BeforeEach
    public void init1() {
        dict = new DictionaryImpl<>();
    }

    @Test
    public void testPut() {
        assertNull(dict.put("a", 1));
        assertEquals(dict.put("a", 2), 1);
        assertNull(dict.put("b", 1));
    }

    @Test
    public void testSize() {
        assertEquals(dict.size(), 0);
        dict.put("a", 1);
        assertEquals(dict.size(), 1);
        dict.put("b", 2);
        dict.put("c", 2);
        assertEquals(dict.size(), 3);
        dict.put("b", 3);
        assertEquals(dict.size(), 3);
    }

    @Test
    public void testContainsKey() {
        assertFalse(dict.containsKey("a"));
        dict.put("a", 1);
        assertTrue(dict.containsKey("a"));
        dict.put("a", null);
        assertTrue(dict.containsKey("a"));
        assertFalse(dict.containsKey("a "));
        dict.remove("a");
        assertFalse(dict.containsKey("a"));
    }

    @Test
    public void testGet() {
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 3);
        dict.put("a ", 4);
        assertEquals(dict.get("a"), 1);
        assertEquals(dict.get("a "), 4);
        assertNull(dict.get("d"));
        dict.remove("a");
        assertNull(dict.get("a"));
    }

    @Test
    public void testRemove() {
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 2);
        assertEquals(dict.remove("b"), 2);
        assertNull(dict.remove("b"));
        assertEquals(dict.size(), 2);
        assertNull(dict.remove("d"));
    }

    @Test
    public void testClear() {
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 2);
        assertTrue(dict.containsKey("a"));
        dict.clear();
        assertEquals(dict.size(), 0);
        assertFalse(dict.containsKey("a"));
        assertFalse(dict.containsKey("b"));
        assertFalse(dict.containsKey("c"));
        assertFalse(dict.containsValue(2));
    }


    @Test
    public void testKeySet() {
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 3);
        dict.put("d", 4);
        dict.put("e", 5);
        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        expected.add("e");
        assertTrue(dict.keySet().containsAll(expected));
        assertFalse(dict.containsKey("a "));
        dict.remove("a");
        assertFalse(dict.containsKey("a"));
        Iterator<String> it = dict.keySet().iterator();
        String first = it.next();
        it.remove();
        assertFalse(dict.containsKey(first));
        assertEquals(dict.size(), 3);
    }

    @Test
    public void testValue() {
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 3);
        dict.put("d", 4);
        dict.put("e", 5);
        List<Integer> expected = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            expected.add(1);
        }
        assertTrue(dict.values().containsAll(expected));
        assertFalse(dict.containsValue(0));
        dict.remove("b");
        assertFalse(dict.containsValue(2));
        Iterator<Integer>it = dict.values().iterator();
        assertTrue(it.hasNext());
        Integer first = it.next();
        assertTrue(dict.containsValue(first));
        it.remove();
        assertFalse(dict.containsValue(first));
        assertEquals(dict.size(), 3);
    }

    @Test
    public void testEntrySet() {
        dict.put("a", 1);
        dict.put("b", 2);
        dict.put("c", 3);
        dict.put("d", 4);
        dict.put("e", 5);
        assertEquals(dict.size(), dict.entrySet().size());
        Iterator<Map.Entry<String, Integer>>it = dict.entrySet().iterator();
        Map.Entry<String, Integer>first = it.next();
        it.remove();
        assertFalse(dict.containsKey(first.getKey()));
        assertFalse(dict.containsValue(first.getValue()));
        it.next();
        it.remove();
        assertEquals(dict.size(), 3);

    }

    @Test
    public void stressTest() {
        Dictionary<String, String> d = new DictionaryImpl<>();
        HashMap<String, String> hm = new HashMap<>();
        latinLetters = generateAlphabet();
        for (int i = 0; i < 10000; i++) {
            String key = generateString(100);
            String value = generateString(100);
            assertEquals(d.put(key, value), hm.put(key, value));
            assertEquals(d.size(), hm.size());
        }
        assertEquals(d.keySet(), hm.keySet());
        assertEquals(d.entrySet(), hm.entrySet());
        for (String value : d.values()) {
            assertTrue(hm.containsValue(value));
        }
        for (String key : hm.keySet()) {
            assertTrue(hm.containsKey(key));
            assertEquals(d.get(key), hm.get(key));
        }
        for (int i = 0; i < 10000; i++) {
            String key = generateString(100);
            assertEquals(d.remove(key), hm.remove(key));
        }
        for (Map.Entry<String, String> entry : hm.entrySet()) {
            assertTrue(d.containsKey(entry.getKey()));
        }
        assertEquals(d.keySet(), hm.keySet());
        assertEquals(d.entrySet(), hm.entrySet());
        d.clear();
        hm.clear();
        assertEquals(d.size(), hm.size());
        for (int i = 0; i < 10000; i++) {
            String key = generateString(10);
            String value = generateString(10);
            assertEquals(d.put(key, value), hm.put(key, value));
            assertEquals(d.size(), hm.size());
        }
        for (String key : d.keySet()) {
            assertEquals(d.remove(key), hm.remove(key));
        }
        assertEquals(d.size(), hm.size());
    }

    @Test
    public void testRemoveIf() {
        latinLetters = generateAlphabet();
        Dictionary<String, String> d = new DictionaryImpl<>();
        HashMap<String, String> hm = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            String key = generateString(10);
            String value = generateString(10);
            assertEquals(d.put(key, value), hm.put(key, value));
        }
        d.values().removeIf(s -> s.length() > 5);
        hm.values().removeIf(s -> s.length() > 5);
        assertEquals(d.entrySet(), hm.entrySet());
        assertEquals(d.size(), hm.size());
        assertEquals(d.keySet(), d.keySet());
    }

    @Test
    public void testIteratorExceptions() {
        dict.put("a", 2);
        dict.put("b", 3);
        dict.put("c", 4);

        Iterator<String> it = dict.keySet().iterator();
        it.next();
        it.next();
        it.next();
        Assertions.assertThrows(NoSuchElementException.class, it::next);

        Iterator<Integer> iterator = dict.values().iterator();
        iterator.next();
        iterator.remove();
        Assertions.assertThrows(IllegalStateException.class, iterator::remove);
    }

    private List<Character>latinLetters;

    private List<Character> generateAlphabet() {
        List<Character> alphabet = new ArrayList<>();

        for (char letter = 'A'; letter <= 'Z'; letter++) {
            alphabet.add(letter);
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            alphabet.add(letter);
        }

        return alphabet;
    }

    private String generateString(int maxLength) {
        int length = 1 + (int) (Math.random() * (maxLength + 1));
        StringBuilder buffer = new StringBuilder();

        for (int j = 0; j < length; j++) {
            char symbol = latinLetters.get((int) (Math.random() * latinLetters.size()));
            buffer.append(symbol);
        }

        return buffer.toString();
    }

}
