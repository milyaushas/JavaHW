package ru.hse.java.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DictionaryImpl<K, V> extends AbstractMap<K, V> implements Dictionary<K, V>{
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOADFACTOR = 0.75F;

    private int capacity;
    private final float loadFactor;
    private int size;
    private List<List<DictEntry>> data;
    private DictEntry lastEntry;

    private class DictEntry extends SimpleEntry<K, V> {
        private DictEntry prev;
        private DictEntry next;

        public DictEntry(K key, V value) {
            super(key, value);
        }
    }

    public DictionaryImpl() {
        this(DEFAULT_LOADFACTOR, DEFAULT_CAPACITY);
    }

    public DictionaryImpl(float loadFactor) {
        this(loadFactor, DEFAULT_CAPACITY);
    }

    public DictionaryImpl(float customLoadFactor, int customCapacity) {
        capacity = customCapacity;
        loadFactor = customLoadFactor;
        size = 0;
        data = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            data.add(new ArrayList<>());
        }
        lastEntry = null;
    }

    private int getHashCode(Object key, int capacity) {
        int hashCode = key.hashCode() % capacity;
        if (hashCode < 0) {
            hashCode += capacity;
        }
        return hashCode;
    }


    private void rebuild(int newCapacity) {
        DictionaryImpl<K, V> newDictionary = new DictionaryImpl<>(loadFactor, newCapacity);
        for (List<DictEntry> bucket : data) {
            for (DictEntry element : bucket) {
                int hashCode = getHashCode(element.getKey(), newCapacity);
                newDictionary.data.get(hashCode).add(element);
            }
        }
        capacity = newDictionary.capacity;
        data = newDictionary.data;
    }

    @Override
    public int size(){
        return size;
    }

    private DictEntry getEntry(Object key) {
        if (key == null) {
            return null;
        }
        int hashCode = getHashCode(key, capacity);
        for (DictEntry element : data.get(hashCode)) {
            if (element.getKey().equals(key)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    @Override
    public V get(Object key) {
        SimpleEntry<K, V> element = getEntry(key);
        return (element == null) ? null : element.getValue();
    }

    private void addEntry(DictEntry entry) {
        if (lastEntry == null) {
            lastEntry = entry;
            return;
        }
        lastEntry.next = entry;
        entry.prev = lastEntry;
        lastEntry = entry;
    }


    @Override
    public V put(K key, V value) {
        if (key == null) {
            return null;
        }
        int hashCode = getHashCode(key, capacity);
        for (DictEntry element : data.get(hashCode)) {
            if (element.getKey().equals(key)) {
                V prevValue = element.getValue();
                element.setValue(value);
                return prevValue;
            }
        }
        DictEntry entry = new DictEntry(key, value);
        addEntry(entry);
        data.get(hashCode).add(entry);
        size++;
        if (size >= capacity * loadFactor) {
            rebuild(2 * capacity);
        }
        return null;
    }

    private void changeLinks(DictEntry element) {
        if ((element.prev == null) && (element.next == null)) {
            return;
        }
        if (element.prev == null) {
            element.next.prev = null;
            return;
        }
        if (element.next == null) {
            element.prev.next = null;
            lastEntry = element.prev;
            return;
        }
        element.prev.next = element.next;
        element.next.prev = element.prev;
    }

    @Override
    public V remove(Object key) {
        int hashCode = getHashCode(key, capacity);
        for (DictEntry element : data.get(hashCode)) {
            if (element.getKey().equals(key)) {
                V removedValue = element.getValue();
                changeLinks(element);
                data.get(hashCode).remove(element);
                size--;
                if (size < loadFactor * capacity * 0.5) {
                    rebuild((int) (capacity * 0.5));
                }
                return removedValue;
            }
        }
        return null;
    }

    @Override
    public void clear(){
        DictionaryImpl<K, V> emptyDictionary = new DictionaryImpl<>(loadFactor);
        capacity = emptyDictionary.capacity;
        size = 0;
        data = emptyDictionary.data;
        lastEntry = null;
    }

    private class DictIterator implements Iterator<DictEntry> {
        private DictEntry currentNext;
        private DictEntry currentPrev;

        public DictIterator() {
            currentNext = lastEntry;
            currentPrev = null;
        }

        @Override
        public boolean hasNext() {
            return currentNext != null;
        }

        @Override
        public DictEntry next() {
            if (currentNext == null) {
                throw new NoSuchElementException();
            }
            DictEntry element = currentNext;
            currentNext = element.prev;
            currentPrev = element;
            return element;
        }

        @Override
        public void remove() {
            if (currentPrev == null) {
                throw new IllegalStateException();
            }
            DictionaryImpl.this.remove(currentPrev.getKey());
            currentPrev = null;
        }

    }

    @Override
    @NotNull
    public Set<K> keySet() {
        return new KeySet();
    }

    private class KeySet extends AbstractSet<K> {
        KeyIterator keyIterator;
        DictIterator iterator;

        public KeySet() {
            keyIterator = new KeyIterator();
        }

        private class KeyIterator implements Iterator<K> {

            public KeyIterator() {
                iterator = new DictIterator();
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                DictEntry element = iterator.next();
                return (element == null) ? null : element.getKey();
            }

            @Override
            public void remove() {
                iterator.remove();
            }

        }

        @Override
        public @NotNull Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return DictionaryImpl.this.size();
        }
    }

    @Override
    @NotNull
    public Collection<V> values(){
        return new Values();
    }

    private class Values extends AbstractCollection<V> {
        ValuesIterator valuesIterator;
        DictIterator iterator;

        public Values() {
            valuesIterator = new ValuesIterator();
        }

        private class ValuesIterator implements Iterator<V> {

            public ValuesIterator() {
                iterator = new DictIterator();
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public V next() {
                DictEntry element = iterator.next();
                return (element == null) ? null : element.getValue();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        }

        @Override
        public @NotNull Iterator<V> iterator() {
            return new ValuesIterator();
        }

        @Override
        public int size() {
            return DictionaryImpl.this.size();
        }
    }


    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    private class EntrySet extends AbstractSet<Entry<K, V>> {
        EntryIterator entryIterator;
        DictIterator iterator;

        public EntrySet() {
            entryIterator = new EntryIterator();
        }

        private class EntryIterator implements Iterator<Entry<K, V>> {

            public EntryIterator() {
                iterator = new DictIterator();
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry<K, V> next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        }

        @Override
        public @NotNull Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return DictionaryImpl.this.size();
        }
    }

}
