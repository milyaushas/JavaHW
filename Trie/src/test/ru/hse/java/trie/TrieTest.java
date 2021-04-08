package ru.hse.java.trie;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.TreeSet;

public class TrieTest {
    private Trie trie;

    @BeforeEach
    public void init() {
        trie = new TrieImpl();
    }

    @Test
    public void testAdd() {
        Assertions.assertEquals(0, trie.size());
        Assertions.assertFalse(trie.contains(""));
        trie.add("");
        Assertions.assertEquals(1, trie.size());
        Assertions.assertTrue(trie.contains(""));
        Assertions.assertFalse(trie.add(""));

        trie.add("abc");
        Assertions.assertTrue(trie.contains("abc"));
        Assertions.assertFalse(trie.contains("ab"));
        Assertions.assertFalse(trie.add("abc"));
        Assertions.assertEquals(2, trie.size());

        Assertions.assertTrue(trie.add("Abc"));
        Assertions.assertTrue(trie.add("aaaaaa"));
        Assertions.assertTrue(trie.add("aaaa"));
        Assertions.assertFalse(trie.add("aaaa"));
    }

    @Test
    public void testContains() {
        trie.add("");
        trie.add("ab");
        trie.add("abcd");
        Assertions.assertTrue(trie.contains(""));
        Assertions.assertTrue(trie.contains("ab"));
        Assertions.assertFalse(trie.contains("abc"));
    }

    @Test
    public void testRemove() {
        Assertions.assertFalse(trie.remove(""));
        Assertions.assertFalse(trie.remove("a"));
        trie.add("a");
        trie.add("abc");
        trie.add("");
        Assertions.assertTrue(trie.remove("a"));
        Assertions.assertFalse(trie.remove("a"));
    }

    @Test
    public void testSize() {
        Assertions.assertEquals(0, trie.size());
        trie.add("a");
        trie.add("b");
        trie.add("c");
        Assertions.assertEquals(3, trie.size());
        trie.add("a");
        Assertions.assertEquals(3, trie.size());
        trie.remove("b");
        Assertions.assertEquals(2, trie.size());
    }

    @Test
    public void testHowManyStartsWithPrefix() {
        trie.add("a");
        trie.add("b");
        trie.add("c");
        trie.add("abc");
        trie.add("bcd");
        trie.add("Abc");
        trie.add("aBc");
        Assertions.assertEquals(trie.size(), trie.howManyStartsWithPrefix(""));
        Assertions.assertEquals(3, trie.howManyStartsWithPrefix("a"));
        Assertions.assertEquals(0, trie.howManyStartsWithPrefix("abcd"));
        Assertions.assertEquals(0, trie.howManyStartsWithPrefix("d"));
    }

    @Test
    public void testNextString() {
        trie.add("a");
        trie.add("b");
        trie.add("c");
        trie.add("abc");
        trie.add("bcd");
        trie.add("Abc");
        trie.add("aBc");
        Assertions.assertNull(trie.nextString("", 0));
        Assertions.assertEquals("aBc", trie.nextString("a", 1));
        Assertions.assertEquals("a", trie.nextString("a", 0));
        Assertions.assertEquals("c", trie.nextString("bb", 2));
        Assertions.assertEquals("c", trie.nextString("", trie.size()));
        Assertions.assertNull(trie.nextString("", trie.size() + 1));
    }

    @Test
    public void testIllegalArgumentExceptions() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> trie.add("ПлохаяCтрока"));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> trie.remove("BadString "));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> trie.contains("Bad$tring"));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> trie.howManyStartsWithPrefix(" "));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> trie.nextString("", -1));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> trie.nextString("bAdStRiNg#", 1));

        String message1 = Assertions.assertThrows(IllegalArgumentException.class,
                () -> trie.nextString("badString\n", -1)).getMessage();

        String message2 = Assertions.assertThrows(IllegalArgumentException.class,
                () -> trie.nextString("goodString", -2)).getMessage();

        Assertions.assertEquals(message1, message2);
    }

    @Test
    public void stressTest() {
        for (int maxLength = 1; maxLength <= 100; maxLength++) {
            trie = new TrieImpl();
            TreeSet<String> treeSet = new TreeSet<>();
            latinLetters = generateAlphabet();

            for (int i = 0; i < 10_000; i++) {
                String element = generateString(maxLength);
                Assertions.assertEquals(trie.add(element), treeSet.add(element));
            }

            Assertions.assertEquals(trie.add(""), treeSet.add(""));
            Assertions.assertEquals(trie.size(), treeSet.size());

            for (int j = 0; j < 10_000; j++) {
                String element = generateString(maxLength);
                Assertions.assertEquals(trie.remove(element), treeSet.remove(element));
            }

            Assertions.assertEquals(trie.size(), treeSet.size());

            for (String element : treeSet.descendingSet()) {
                Assertions.assertTrue(trie.contains(element));
            }

            for (int j = 0; j < 10_000; j++) {
                String element = generateString(maxLength);
                Assertions.assertEquals(trie.contains(element), treeSet.contains(element));
            }

            Assertions.assertEquals(trie.size(), treeSet.size());

            for (int i = 0; i < 10_000; i++) {
                String element = generateString(maxLength);
                int command = (int) (Math.random() * 3);
                if (command == 0) {
                    Assertions.assertEquals(trie.add(element), treeSet.add(element));
                }
                if (command == 1) {
                    Assertions.assertEquals(trie.contains(element), treeSet.contains(element));
                }
                if (command == 2) {
                    Assertions.assertEquals(trie.remove(element), treeSet.remove(element));
                }
            }

            Assertions.assertEquals(trie.size(), treeSet.size());
        }
    }

    private ArrayList<Character>latinLetters;

    private ArrayList<Character> generateAlphabet() {
        ArrayList<Character> alphabet = new ArrayList<>();

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
