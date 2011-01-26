/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;


/**
 * A hashtable-based {@link Map} implementation with <em>weak values</em>. An entry in a
 * {@code WeakValueHashMap} will automatically be removed when its value is no longer
 * in ordinary use. This class is similar to the standard {@link java.util.WeakHashMap}
 * class provided in J2SE, except that weak references are hold on values instead of keys.
 * <p>
 * The {@code WeakValueHashMap} class is thread-safe.
 *
 * @param <K> The class of key elements.
 * @param <V> The class of value elements.
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see java.util.WeakHashMap
 * @see WeakHashSet
 */
public class WeakValueHashMap<K,V> extends AbstractMap<K,V> {
    /**
     * Minimal capacity for {@link #table}.
     */
    private static final int MIN_CAPACITY = 7;

    /**
     * Load factor. Control the moment
     * where {@link #table} must be rebuild.
     */
    private static final float LOAD_FACTOR = 0.75f;

    /**
     * An entry in the {@link WeakValueHashMap}. This is a weak reference
     * to a value together with a strong reference to a key.
     */
    private final class Entry extends WeakReference<V> implements Map.Entry<K,V> {
        /**
         * The key.
         */
        K key;

        /**
         * The next entry, or {@code null} if there is none.
         */
        Entry next;

        /**
         * Index for this element in {@link #table}. This index
         * must be updated at every {@link #rehash} call.
         */
        int index;

        /**
         * Constructs a new weak reference.
         */
        Entry(final K key, final V value, final Entry next, final int index) {
            super(value, WeakCollectionCleaner.DEFAULT.referenceQueue);
            this.key   = key;
            this.next  = next;
            this.index = index;
        }

        /**
         * Returns the key corresponding to this entry.
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value corresponding to this entry.
         */
        public V getValue() {
            return get();
        }

        /**
         * Replaces the value corresponding to this entry with the specified value.
         */
        public V setValue(final V value) {
            if (value != null) {
                throw new UnsupportedOperationException();
            }
            V old = get();
            clear();
            return old;
        }

        /**
         * Clear the reference. The {@link WeakCollectionCleaner} requires that this method is
         * overridden in order to remove this entry from the enclosing hash map.
         */
        @Override
        public void clear() {
            super.clear();
            removeEntry(this);
            key = null;
        }

        /**
         * Compares the specified object with this entry for equality.
         */
        @Override
        public boolean equals(final Object other) {
            if (other instanceof Map.Entry) {
                final Map.Entry that = (Map.Entry) other;
                return Utilities.equals(this.getKey(),   that.getKey()) &&
                       Utilities.equals(this.getValue(), that.getValue());
            }
            return false;
        }

        /**
         * Returns the hash code value for this map entry.
         */
        @Override
        public int hashCode() {
            final Object val = get();
            return (key==null ? 0 : key.hashCode()) ^
                   (val==null ? 0 : val.hashCode());
        }
    }

    /**
     * Table of weak references.
     */
    private Entry[] table;

    /**
     * Number of non-nul elements in {@link #table}.
     */
    private int count;

    /**
     * The next size value at which to resize. This value should
     * be <code>{@link #table}.length*{@link #loadFactor}</code>.
     */
    private int threshold;

    /**
     * The timestamp when {@link #table} was last rehashed. This information
     * is used to avoid too early table reduction. When the garbage collector
     * collected a lot of elements, we will wait at least 20 seconds before
     * rehashing {@link #table}. Too early table reduction leads to many cycles
     * like "reduce", "expand", "reduce", "expand", etc.
     */
    private long lastRehashTime;

    /**
     * Number of millisecond to wait before to rehash
     * the table for reducing its size.
     */
    private static final long HOLD_TIME = 20*1000L;

    /**
     * Creates a {@code WeakValueHashMap}.
     */
    public WeakValueHashMap() {
        this(MIN_CAPACITY);
    }

    /**
     * Creates a {@code WeakValueHashMap} of the requested size and default load factor.
     *
     * @param initialSize The initial size.
     */
    public WeakValueHashMap(final int initialSize) {
        newEntryTable(initialSize);
        threshold = Math.round(table.length * LOAD_FACTOR);
        lastRehashTime = System.currentTimeMillis();
    }

    /**
     * Sets the {@link #table} array to the specified size. The content of the old array is lost.
     *
     * @todo Use the commented line instead if a future Java version supports generic arrays.
     */
    private void newEntryTable(final int size) {
//      table = new Entry[size];
        table = (Entry[]) Array.newInstance(Entry.class, size);
    }

    /**
     * Creates a new {@code WeakValueHashMap} populated with the contents of the provied map.
     *
     * @param map Initial contents of the {@code WeakValueHashMap}.
     */
    public WeakValueHashMap(final Map<K,V> map) {
        this(Math.round(map.size() / LOAD_FACTOR) + 1);
        putAll(map);
    }

    /**
     * Invoked by {@link Entry} when an element has been collected
     * by the garbage collector. This method will remove the weak reference
     * from {@link #table}.
     */
    private synchronized void removeEntry(final Entry toRemove) {
        assert valid() : count;
        final int i = toRemove.index;
        // Index 'i' may not be valid if the reference 'toRemove'
        // has been already removed in a previous rehash.
        if (i < table.length) {
            Entry prev = null;
            Entry e = table[i];
            while (e != null) {
                if (e == toRemove) {
                    if (prev != null) {
                        prev.next = e.next;
                    } else {
                        table[i] = e.next;
                    }
                    count--;
                    assert valid();

                    // If the number of elements has dimunished
                    // significatively, rehash the table.
                    if (count <= threshold/4) {
                        rehash(false);
                    }
                    // We must not continue the loop, since
                    // variable 'e' is no longer valid.
                    return;
                }
                prev = e;
                e = e.next;
            }
        }
        assert valid();
        /*
         * If we reach this point, its mean that reference 'toRemove' has not
         * been found. This situation may occurs if 'toRemove' has already been
         * removed in a previous run of {@link #rehash}.
         */
    }

    /**
     * Rehashs {@link #table}.
     *
     * @param augmentation {@code true} if this method is invoked
     *        for augmenting {@link #table}, or {@code false} if
     *        it is invoked for making the table smaller.
     */
    private void rehash(final boolean augmentation) {
        assert Thread.holdsLock(this);
        assert valid();
        final long currentTime = System.currentTimeMillis();
        final int capacity = Math.max(Math.round(count/(LOAD_FACTOR/2)), count+MIN_CAPACITY);
        if (augmentation ? (capacity<=table.length) :
                           (capacity>=table.length || currentTime-lastRehashTime<HOLD_TIME))
        {
            return;
        }
        lastRehashTime = currentTime;
        final Entry[] oldTable = table;
        newEntryTable(capacity);
        threshold = Math.round(capacity*LOAD_FACTOR);
        for (int i=0; i<oldTable.length; i++) {
            for (Entry old=oldTable[i]; old!=null;) {
                final Entry e=old;
                old = old.next; // On retient 'next' tout de suite car sa valeur va changer...
                final Object key = e.key;
                if (key != null) {
                    final int index=(key.hashCode() & 0x7FFFFFFF) % table.length;
                    e.index = index;
                    e.next  = table[index];
                    table[index] = e;
                } else {
                    count--;
                }
            }
        }
        final Logger logger = Logging.getLogger("org.geotools.util");
        final Level   level = Level.FINEST;
        if (logger.isLoggable(level)) {
            final LogRecord record = new LogRecord(level,
                    "Rehash from " + oldTable.length + " to " + table.length);
            record.setSourceMethodName(augmentation ? "unique" : "remove");
            record.setSourceClassName(WeakValueHashMap.class.getName());
            record.setLoggerName(logger.getName());
            logger.log(record);
        }
        assert valid();
    }

    /**
     * Checks if this {@code WeakValueHashMap} is valid. This method counts the
     * number of elements and compare it to {@link #count}. If the check fails,
     * the number of elements is corrected (if we didn't, an {@link AssertionError}
     * would be thrown for every operations after the first error,  which make
     * debugging more difficult). The set is otherwise unchanged, which should
     * help to get similar behaviour as if assertions hasn't been turned on.
     */
    private boolean valid() {
        int n=0;
        for (int i=0; i<table.length; i++) {
            for (Entry e=table[i]; e!=null; e=e.next) {
                n++;
            }
        }
        if (n!=count) {
            count = n;
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public synchronized int size() {
        assert valid();
        return count;
    }

    /**
     * Returns {@code true} if this map maps one or more keys to this value.
     *
     * @param value value whose presence in this map is to be tested.
     * @return {@code true} if this map maps one or more keys to this value.
     */
    @Override
    public synchronized boolean containsValue(final Object value) {
        return super.containsValue(value);
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested.
     * @return {@code true} if this map contains a mapping for the specified key.
     * @throws NullPointerException If key is {@code null}.
     */
    @Override
    public boolean containsKey(final Object key) {
        return get(key) != null;
    }

    /**
     * Returns the value to which this map maps the specified key. Returns
     * {@code null} if the map contains no mapping for this key.
     *
     * @param  key Key whose associated value is to be returned.
     * @return The value to which this map maps the specified key.
     * @throws NullPointerException if the key is {@code null}.
     */
    @Override
    public synchronized V get(final Object key) {
        assert WeakCollectionCleaner.DEFAULT.isAlive();
        assert valid() : count;
        final int index = (key.hashCode() & 0x7FFFFFFF) % table.length;
        for (Entry e=table[index]; e!=null; e=e.next) {
            if (key.equals(e.key)) {
                return e.get();
            }
        }
        return null;
    }

    /**
     * Implementation of {@link #put} and {@link #remove} operations.
     */
    private synchronized V intern(final K key, final V value) {
        assert WeakCollectionCleaner.DEFAULT.isAlive();
        assert valid() : count;
        /*
         * Check if {@code obj} is already contained in this
         * {@code WeakValueHashMap}. If yes, clear it.
         */
        V oldValue = null;
        final int hash = key.hashCode() & 0x7FFFFFFF;
        int index = hash % table.length;
        for (Entry e=table[index]; e!=null; e=e.next) {
            if (key.equals(e.key)) {
                oldValue = e.get();
                e.clear();
            }
        }
        if (value != null) {
            if (count >= threshold) {
                rehash(true);
                index = hash % table.length;
            }
            table[index] = new Entry(key, value, table[index], index);
            count++;
        }
        assert valid();
        return oldValue;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * The value is associated using a {@link WeakReference}.
     *
     * @param  key key with which the specified value is to be associated.
     * @param  value value to be associated with the specified key.
     * @return previous value associated with specified key, or {@code null}
     *	       if there was no mapping for key.
     *
     * @throws NullPointerException if the key or the value is {@code null}.
     */
    @Override
    public V put(final K key, final V value) {
        if (value == null) {
            throw new NullPointerException("Null value not allowed");
            // TODO: localize this message.
        }
        return intern(key, value);
    }

    /**
     * Removes the mapping for this key from this map if present.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or {@code null}
     *	       if there was no entry for key.
     */
    @Override
    @SuppressWarnings("unchecked")
    public V remove(final Object key) {
        return intern((K) key, null);
    }

    /**
     * Removes all of the elements from this map.
     */
    @Override
    public synchronized void clear() {
        Arrays.fill(table, null);
        count = 0;
    }

    /**
     * Returns a set view of the mappings contained in this map.
     * Each element in this set is a {@link java.util.Map.Entry}.
     * The current implementation thrown {@link UnsupportedOperationException}.
     *
     * @return a set view of the mappings contained in this map.
     */
    @Override
    public Set<Map.Entry<K,V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
