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
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.geotools.resources.XArray;
import org.geotools.util.logging.Logging;


/**
 * A set of objects hold by weak references. An entry in a {@code WeakHashSet}
 * will automatically be removed when it is no longer in ordinary use. More precisely,
 * the presence of an entry will not prevent the entry from being discarded by the
 * garbage collector, that is, made finalizable, finalized, and then reclaimed.
 * When an entry has been discarded it is effectively removed from the set, so
 * this class behaves somewhat differently than other {@link java.util.Set} implementations.
 * <p>
 * If you would like to use {@code WeakHashSet} as inside a factory to prevent creating
 * duplicate immutable objects, please look at the {@link CanonicalSet} subclass.
 * <p>
 * The {@code WeakHashSet} class is thread-safe.
 *
 * @param <E> The type of elements in the set.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see java.util.WeakHashMap
 */
public class WeakHashSet<E> extends AbstractSet<E> implements CheckedCollection<E> {
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
     * A weak reference to an element.
     */
    private final class Entry extends WeakReference<E> {
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
        Entry(final E obj, final Entry next, final int index) {
            super(obj, WeakCollectionCleaner.DEFAULT.referenceQueue);
            this.next  = next;
            this.index = index;
        }

        /**
         * Clear the reference.
         */
        @Override
        public void clear() {
            super.clear();
            removeEntry(this);
        }
    }

    /**
     * Table of weak references.
     */
    private Entry[] table;

    /**
     * The type of the elements in this set.
     */
    private final Class<E> type;

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
     * Constructs a {@code WeakHashSet}.
     *
     * @deprecated Use {@link WeakHashSet(Class)}.
     */
    @SuppressWarnings("unchecked")
    public WeakHashSet() {
        this((Class) Object.class);
    }

    /**
     * Constructs a {@code WeakHashSet}.
     *
     * @param type The type of the element to be included in this set.
     *
     * @since 2.5
     */
    public WeakHashSet(final Class<E> type) {
        this.type = type;
        newEntryTable(MIN_CAPACITY);
        threshold = Math.round(table.length*LOAD_FACTOR);
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
     * Returns the element type.
     *
     * @since 2.5
     */
    public Class<E> getElementType() {
        return type;
    }

    /**
     * Invoked by {@link Entry} when an element has been collected by the garbage
     * collector. This method will remove the weak reference from {@link #table}.
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
     * Rehash {@link #table}.
     *
     * @param augmentation
     *          {@code true} if this method is invoked for augmenting {@link #table},
     *          or {@code false} if it is invoked for making the table smaller.
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
                old = old.next; // We keep 'next' right now because its value will change.
                final E obj_e = e.get();
                if (obj_e != null) {
                    final int index = (obj_e.hashCode() & 0x7FFFFFFF) % table.length;
                    e.index = index;
                    e.next  = table[index];
                    table[index]=e;
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
            record.setSourceClassName(WeakHashSet.class.getName());
            record.setLoggerName(logger.getName());
            logger.log(record);
        }
        assert valid();
    }

    /**
     * Checks if this {@code WeakHashSet} is valid. This method counts the
     * number of elements and compare it to {@link #count}. If the check fails,
     * the number of elements is corrected (if we didn't, an {@link AssertionError}
     * would be thrown for every operations after the first error,  which make
     * debugging more difficult). The set is otherwise unchanged, which should
     * help to get similar behaviour as if assertions hasn't been turned on.
     */
    private boolean valid() {
        int n = 0;
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
     * Returns the count of element in this set.
     */
    public synchronized int size() {
        assert valid();
        return count;
    }

    /**
     * Returns {@code true} if this set contains the specified element.
     *
     * @param  obj Object to be checked for containment in this set.
     * @return {@code true} if this set contains the specified element.
     */
    @Override
    public synchronized boolean contains(final Object obj) {
        return obj != null && intern(type.cast(obj), GET) != null;
    }

    /**
     * Removes a single instance of the specified element from this set, if it is present
     *
     * @param  obj element to be removed from this set, if present.
     * @return {@code true} if the set contained the specified element.
     */
    @Override
    public synchronized boolean remove(final Object obj) {
        return intern(type.cast(obj), REMOVE) != null;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * If this set already contains the specified element, the call leaves
     * this set unchanged and returns {@code false}.
     *
     * @param  obj Element to be added to this set.
     * @return {@code true} if this set did not already contain the specified element.
     */
    @Override
    public synchronized boolean add(final E obj) {
        return intern(obj, ADD) == null;
    }

    // Arguments for the {@link #intern} method.
    /** The "remove" operation.  */  static final int REMOVE = -1;
    /** The "get"    operation.  */  static final int GET    =  0;
    /** The "add"    operation.  */  static final int ADD    = +1;
    /** The "intern" operation.  */  static final int INTERN = +2;

    /**
     * Returns an object equals to {@code obj} if such an object already
     * exist in this {@code WeakHashSet}. Otherwise, add {@code obj}
     * to this {@code WeakHashSet}. This method is equivalents to the
     * following code:
     *
     * <blockquote><pre>
     * &nbsp;  if (object!=null) {
     * &nbsp;      final Object current = get(object);
     * &nbsp;      if (current != null) {
     * &nbsp;          return current;
     * &nbsp;      } else {
     * &nbsp;          add(object);
     * &nbsp;      }
     * &nbsp;  }
     * &nbsp;  return object;
     * </pre></blockquote>
     */
    final <T extends E> T intern(final T obj, final int operation) {
        assert Thread.holdsLock(this);
        assert WeakCollectionCleaner.DEFAULT.isAlive();
        assert valid() : count;
        if (obj != null) {
            assert obj.equals(obj) : obj;
            /*
             * Check if {@code obj} is already contained in this
             * {@code WeakHashSet}. If yes, returns the element.
             */
            final int hash = obj.hashCode() & 0x7FFFFFFF;
            int index = hash % table.length;
            for (Entry e=table[index]; e!=null; e=e.next) {
                final E candidate = e.get();
                if (candidate != null) {
                    if (candidate.equals(obj)) {
                        if (operation == REMOVE) {
                            e.clear();
                        }
                        assert candidate.getClass().equals(obj.getClass()) : candidate;
                        @SuppressWarnings("unchecked")
                        final T result = (T) candidate;
                        return result;
                    }
                }
                // Do not remove the null element; lets ReferenceQueue do its job
                // (it was a bug to remove element here as an "optimization")
            }
            if (operation >= ADD) {
                /*
                 * Check if the table need to be rehashed,
                 * and add {@code obj} to the table.
                 */
                if (count >= threshold) {
                    rehash(true);
                    index = hash % table.length;
                }
                table[index] = new Entry(obj, table[index], index);
                count++;
            }
        }
        assert valid();
        return (operation == INTERN) ? obj : null;
    }

    /**
     * Removes all of the elements from this set.
     */
    @Override
    public synchronized void clear() {
        Arrays.fill(table, null);
        count = 0;
    }

    /**
     * Returns a view of this set as an array. Elements will be in an arbitrary
     * order. Note that this array contains strong reference.  Consequently, no
     * object reclamation will occurs as long as a reference to this array is hold.
     */
    @Override
    public synchronized E[] toArray() {
        assert valid();
        @SuppressWarnings("unchecked")
        final E[] elements = (E[]) Array.newInstance(type, count);
        int index = 0;
        for (int i=0; i<table.length; i++) {
            for (Entry el=table[i]; el!=null; el=el.next) {
                if ((elements[index]=el.get()) != null) {
                    index++;
                }
            }
        }
        return XArray.resize(elements, index);
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     * No element from this set will be garbage collected as long as a
     * reference to the iterator is hold.
     */
    @Override
    public Iterator<E> iterator() {
        return Arrays.asList(toArray()).iterator();
    }
}
