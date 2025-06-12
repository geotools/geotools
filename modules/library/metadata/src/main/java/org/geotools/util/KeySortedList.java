/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.util;

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * List of elements sorted by a key which is not the element itself.
 *
 * <p>This class is <strong>not</strong> thread-safe. Synchronizations (if wanted) are user's reponsability.
 *
 * @param <K> The type of keys in the sorted list, to be used for sorting.
 * @param <V> The type of elements in the list.
 * @since 2.2
 * @version $Id$
 * @author Simone Giannecchini
 * @author Martin Desruisseaux
 */
public class KeySortedList<K extends Comparable<K>, V> extends AbstractSequentialList<V> implements Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 6969483179756527012L;

    /** The sorted map of <var>key</var>-<var>list of values</var> pairs. */
    private final SortedMap<K, List<V>> map;

    /** Creates a new, initially empty list. */
    public KeySortedList() {
        map = new TreeMap<>();
    }

    /** Creates a list using the specified map of <var>key</var>-<var>list of values</var> pairs. */
    private KeySortedList(final SortedMap<K, List<V>> map) {
        this.map = map;
    }

    /** Removes all of the elements from this list. */
    @Override
    public void clear() {
        map.clear();
    }

    /** Returns the number of elements in this list. */
    @Override
    public int size() {
        int count = 0;
        for (final List<V> list : map.values()) {
            count += list.size();
        }
        return count;
    }

    /**
     * Inserts the specified element at a position determined by the specified key. If some elements were already
     * inserted for the specified key, then this method do not replaces the old value (like what a {@link Map} would
     * do), but instead add the new element with the same key.
     *
     * @param key Key to be used to find the right location.
     * @param element Object to be inserted.
     */
    public void add(final K key, final V element) {
        List<V> values = map.get(key);
        if (values == null) {
            values = new ArrayList<>();
            map.put(key, values);
        }
        values.add(element);
    }

    /**
     * Removes all values that were {@linkplain #add(Comparable,Object) added} with the specified key.
     *
     * @param key The key of values to remove.
     * @return The number of elements removed.
     */
    public int removeAll(final K key) {
        final List<V> values = map.remove(key);
        return (values != null) ? values.size() : 0;
    }

    /**
     * Returns the number of elements {@linkplain #add(Comparable,Object) added} with the specified key.
     *
     * @param key The key of elements to count.
     * @return The number of elements inserted with the given key.
     */
    public int count(final K key) {
        final List<V> values = map.get(key);
        return (values != null) ? values.size() : 0;
    }

    /**
     * Returns {@code true} if the list contains an element {@linkplain #add(Comparable,Object) added} with the
     * specified key. This is equivalent to testing <code>
     * {@linkplain #count count}(key) != 0</code>.
     */
    public boolean containsKey(final K key) {
        return map.containsKey(key);
    }

    /**
     * Returns the first element {@linkplain #add(Comparable,Object) added} with the specified key.
     *
     * @param key The key for the element to search for.
     * @return The first element added with the specified key.
     * @throws NoSuchElementException if there is no element for the specified key.
     */
    public V first(final K key) throws NoSuchElementException {
        final List<V> values = map.get(key);
        if (values == null || values.isEmpty()) {
            throw new NoSuchElementException();
        }
        return values.get(0);
    }

    /**
     * Returns the last element {@linkplain #add(Comparable,Object) added} with the specified key.
     *
     * @param key The key for the element to search for.
     * @return The last element added with the specified key.
     * @throws NoSuchElementException if there is no element for the specified key.
     */
    public V last(final K key) throws NoSuchElementException {
        final List<V> values = map.get(key);
        if (values == null || values.isEmpty()) {
            throw new NoSuchElementException();
        }
        return values.get(values.size() - 1);
    }

    /**
     * Returns a list iterator of the elements in this list (in proper sequence), starting at the elements
     * {@linkplain #add(Comparable,Object) added} with the specified key.
     *
     * @param fromKey The key of the first element to returns.
     * @return A list iterator of the elements in this list (in proper sequence).
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public ListIterator<V> listIterator(final K fromKey) {
        return new Iter(fromKey);
    }

    /**
     * Returns a list iterator of the elements in this list (in proper sequence), starting at the specified position.
     * The specified index indicates the first element that would be returned by an initial call to the
     * {@link ListIterator#next next()} method.
     *
     * @param index Index of first element to be returned from the list iterator.
     * @return A list iterator of the elements in this list (in proper sequence).
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    @Override
    public ListIterator<V> listIterator(final int index) {
        return new Iter(index);
    }

    /** The list iterator required for {@link AbstractSequentialList} implementation. */
    private final class Iter implements ListIterator<V> {
        /** The iterator over <var>key</var>-<var>list of values</var> pairs. */
        private Iterator<Map.Entry<K, List<V>>> entriesIter;

        /** The current key, or {@code null} if we are past the last entry. */
        private K key;

        /** The values list for the current key. */
        private List<V> values;

        /** The iterator over the current values list. */
        private ListIterator<V> valuesIter;

        /** The base index for the current values list. */
        private int base;

        /** Creates an iterator initialy positioned to the first value of the specified key. */
        public Iter(final K fromKey) {
            entriesIter = map.entrySet().iterator();
            while (entriesIter.hasNext()) {
                final Map.Entry<K, List<V>> entry = entriesIter.next();
                key = entry.getKey();
                values = entry.getValue();
                if (fromKey.compareTo(key) <= 0) {
                    valuesIter = values.listIterator();
                    assert equals(new Iter(base));
                    return;
                }
                base += values.size();
            }
            key = null;
            values = Collections.emptyList();
            valuesIter = values.listIterator();
        }

        /** Creates an iterator initialy positioned to the specified index. */
        public Iter(int index) {
            entriesIter = map.entrySet().iterator();
            while (entriesIter.hasNext()) {
                final Map.Entry<K, List<V>> entry = entriesIter.next();
                key = entry.getKey();
                values = entry.getValue();
                final int size = values.size();
                if (index < size) {
                    valuesIter = values.listIterator(index);
                    return;
                }
                index -= size;
                base += size;
            }
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            key = null;
            values = Collections.emptyList();
            valuesIter = values.listIterator();
        }

        /**
         * Returns {@code true} if this list iterator has more elements when traversing the list in the forward
         * direction.
         */
        @Override
        public boolean hasNext() {
            return valuesIter.hasNext() || entriesIter.hasNext();
        }

        /** Returns the next element in the list. */
        @Override
        public V next() {
            while (!valuesIter.hasNext()) {
                if (entriesIter.hasNext()) {
                    final Map.Entry<K, List<V>> entry = entriesIter.next();
                    base += values.size(); // Must be before 'values' new assignement.
                    key = entry.getKey();
                    values = entry.getValue();
                    valuesIter = values.listIterator();
                } else {
                    key = null;
                    values = Collections.emptyList();
                    valuesIter = values.listIterator();
                    break;
                }
            }
            return valuesIter.next();
        }

        /**
         * Returns {@code true} if this list iterator has more elements when traversing the list in the reverse
         * direction.
         */
        @Override
        public boolean hasPrevious() {
            return valuesIter.hasPrevious() || base != 0;
        }

        /** Returns the previous element in the list. */
        @Override
        public V previous() {
            while (!valuesIter.hasPrevious() && base != 0) {
                /*
                 * Gets the key from the previous entry, and recreates a new entries iterator
                 * starting from this key (the assert statement ensure that). It should be the
                 * only place where this iterator needs to be recreated. Hopefully it should not
                 * happen often.
                 */
                key = map.headMap(key).lastKey();
                entriesIter = map.tailMap(key).entrySet().iterator();
                final Map.Entry<K, List<V>> entry = entriesIter.next();
                assert key == entry.getKey() : key;
                /*
                 * Updates the values list, iterator and base index. It should now reflect the
                 * content of the list in the previous entry.
                 */
                values = entry.getValue();
                final int size = values.size();
                valuesIter = values.listIterator(Math.max(size - 1, 0));
                base -= size; // Must be after 'values' new assignement.
                assert base >= 0 : base;
            }
            return valuesIter.previous();
        }

        /** Returns the index of the element that would be returned by a subsequent call to {@link #next}. */
        @Override
        public int nextIndex() {
            return base + valuesIter.nextIndex();
        }

        /** Returns the index of the element that would be returned by a subsequent call to {@link #previous}. */
        @Override
        public int previousIndex() {
            return base + valuesIter.previousIndex();
        }

        /** Removes from the list the last element that was returned by {@link #next} or {@link #previous} */
        @Override
        public void remove() {
            valuesIter.remove();
        }

        /** Replaces the last element returned by {@link #next} or {@link #previous} with the specified element. */
        @Override
        public void set(final V o) {
            valuesIter.set(o);
        }

        /**
         * Inserts the specified element into the list. The element will have the same key than the one from the
         * previous call to {@link #next} or {@link #previous}.
         */
        @Override
        public void add(final V o) {
            valuesIter.add(o);
        }

        /**
         * Compares two iterators for equality, assuming that they are iterator for the same {@link KeySortedList} (this
         * is not verified). This method is used for assertions only.
         */
        @SuppressWarnings("NonOverridingEquals") // Private method for assertions, not overriding Object.equals
        private boolean equals(final Iter that) {
            return this.key == that.key
                    && this.values == that.values
                    && this.base == that.base
                    && this.valuesIter.nextIndex() == that.valuesIter.nextIndex();
        }
    }

    /**
     * Returns a view of the portion of this list whose keys are strictly less than {@code toKey}. The returned list is
     * backed by this list, so changes in the returned list are reflected in this list, and vice-versa.
     *
     * @param toKey high endpoint (exclusive) of the sub list.
     * @return A view of the specified initial range of this list.
     */
    public KeySortedList<K, V> headList(final K toKey) {
        return new KeySortedList<>(map.headMap(toKey));
    }

    /**
     * Returns a view of the portion of this list whose keys are greater than or equal to {@code fromKey}. The returned
     * list is backed by this list, so changes in the returned list are reflected in this list, and vice-versa.
     *
     * @param fromKey low endpoint (inclusive) of the sub list.
     * @return A view of the specified final range of this list.
     */
    public KeySortedList<K, V> tailList(final K fromKey) {
        return new KeySortedList<>(map.tailMap(fromKey));
    }
}
