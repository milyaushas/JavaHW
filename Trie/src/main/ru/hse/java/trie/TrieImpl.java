package ru.hse.java.trie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

public class TrieImpl implements Trie {

    private static class Node {
        private final TreeMap<Character, Node> nextNodes = new TreeMap<>();
        private static final char TERMINAL_SYMBOL = '#';

        private int subtreeTerminalNodesCounter = 0;

        private boolean hasVertexTo(char symbol) {
            return nextNodes.containsKey(symbol);
        }

        private void addVertexTo(char symbol) {
            nextNodes.put(symbol, new Node());
        }

        private void deleteVertexTo(char symbol) {
            nextNodes.remove(symbol);
        }

        private Node nextNode(char symbol) {
            return nextNodes.get(symbol);
        }
    }

    private final Node root = new Node();

    private String addTerminalSymbol(@NotNull String element) {
        return element + Node.TERMINAL_SYMBOL;
    }

    private static boolean isLatin(char symbol) {
        return (('A' <= symbol) && (symbol <= 'Z') || ('a' <= symbol) && (symbol <= 'z'));
    }

    private void checkElement(@NotNull String element) throws IllegalArgumentException {
        for (char symbol : element.toCharArray()) {
            if (!isLatin(symbol)) {
                throw new IllegalArgumentException("String contains invalid symbols");
            }
        }
    }

    @Override
    public boolean add(@NotNull String element) {
        if (contains(element)) {
            return false;
        }

        Node currentNode = root;
        String changedElement = addTerminalSymbol(element);

        for (char symbol : changedElement.toCharArray()) {
            currentNode.subtreeTerminalNodesCounter++;
            if (!currentNode.hasVertexTo(symbol)) {
                currentNode.addVertexTo(symbol);
            }
            currentNode = currentNode.nextNode(symbol);
        }

        currentNode.subtreeTerminalNodesCounter++;
        return true;
    }

    @Override
    public boolean contains(@NotNull String element) {
        checkElement(element);
        String changedElement = addTerminalSymbol(element);
        Node prefixEnd = findPrefixEnd(changedElement);

        return prefixEnd != null;
    }

    @Override
    public boolean remove(@NotNull String element) {
        if (!contains(element)) {
            return false;
        }

        Node currentNode = root;
        String  changedElement = addTerminalSymbol(element);

        for (char symbol : changedElement.toCharArray()) {
            Node nextNode = currentNode.nextNode(symbol);
            if (nextNode.subtreeTerminalNodesCounter == 1) {
                currentNode.deleteVertexTo(symbol);
            }
            currentNode.subtreeTerminalNodesCounter--;
            currentNode = nextNode;
        }

        return true;
    }

    @Override
    public int size() {
        return root.subtreeTerminalNodesCounter;
    }

    @Nullable
    private Node findPrefixEnd(@NotNull String element) {
        Node currentNode = root;

        for (char symbol : element.toCharArray()) {
            currentNode = currentNode.nextNode(symbol);
            if (currentNode == null) {
                return null;
            }
        }

        return currentNode;
    }

    @Override
    public int howManyStartsWithPrefix(@NotNull String prefix) {
        checkElement(prefix);
        Node prefixEnd = findPrefixEnd(prefix);

        return prefixEnd != null ? prefixEnd.subtreeTerminalNodesCounter : 0;
    }


    private int countElementsNumber(@NotNull String element) {
        int count = 0;
        Node currentNode = root;

        for (char symbol : element.toCharArray()) {

            for (char key : currentNode.nextNodes.keySet()) {
                if (key < symbol) {
                    count += currentNode.nextNodes.get(key).subtreeTerminalNodesCounter;
                }
                else {
                    break;
                }
            }

            currentNode = currentNode.nextNode(symbol);
            if (currentNode == null) {
                break;
            }
        }

        return count + 1;
    }

    @Nullable
    private String findKthString(int k) {
        if (k > size()) {
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        Node currentNode = root;
        char symbol = ' ';

        while (k > 0) {
            for (char key : currentNode.nextNodes.keySet()) {
                if (currentNode.nextNode(key).subtreeTerminalNodesCounter < k) {
                    k -= currentNode.nextNode(key).subtreeTerminalNodesCounter;
                }
                else {
                    symbol = key;
                    break;
                }
            }

            if (symbol == Node.TERMINAL_SYMBOL) {
                break;
            }

            buffer.append(symbol);
            currentNode = currentNode.nextNode(symbol);
        }

        return buffer.toString();
    }

    @Override
    @Nullable
    public String nextString(@NotNull String element, int k) throws IllegalArgumentException {
        if (k < 0) {
            throw new IllegalArgumentException("k must be non-negative");
        }

        if (k == 0) {
            return contains(element) ? element : null;
        }

        if (!contains(element)) {
            k--;
        }
        int number = countElementsNumber(element);

        return findKthString(number + k);
    }
}



