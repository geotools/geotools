/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// import java.util.concurrent.locks.ReentrantWriterPreferenceReadWriteLock;

/**
 * Caching implementation for {@link ObjectCache}. This instance is used when actual caching is
 * desired.
 *
 * @since 2.5
 * @version $Id$
 * @source $URL$
 * @author Cory Horner (Refractions Research)
 */
final class DefaultObjectCache implements ObjectCache {
    /** The cached values for each key. */
    private final Map /*<Object,ObjectCacheEntry>*/ cache;

    /**
     * An entry in the {@link DefaultObjectCache}.
     *
     * <p>To use as a reader:
     *
     * <blockquote>
     *
     * <pre>
     * entry.get();
     * </pre>
     *
     * </blockquote>
     *
     * To use as a writer:
     *
     * <blockquote>
     *
     * <pre>
     * try {
     *    entry.writeLock();
     *    entry.set(value);
     * } finally {
     *    entry.writeUnLock();
     * }
     * </pre>
     *
     * </blockquote>
     *
     * Tip: The use of try/finally is more than just a good idea - it is the law.
     *
     * @todo change from edu.oswego to java.concurrent
     */
    static final class ObjectCacheEntry {
        /**
         * Value of this cache entry, managed by the {@linkplain #lock}.
         *
         * @todo According {@link java.util.concurrent.locks.ReentrantReadWriteLock} documentation,
         *     we don't need to declare this field as volatile. Revisit when we will be allowed to
         *     compile for J2SE 1.5.
         */
        private volatile Object value;

        /** The lock used to manage the {@linkplain #value}. */
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        // formally ReentrantReadWriteLock

        /** Creates an entry with no initial value. */
        public ObjectCacheEntry() {}

        /** Creates an entry with the specified initial value. */
        public ObjectCacheEntry(final Object value) {
            this.value = value;
        }

        /**
         * Acquires a write lock, obtains the value, unlocks, and returns the value. If another
         * thread already has the read or write lock, this method will block.
         *
         * <blockquote>
         *
         * <pre>
         * try {
         *    entry.writeLock();
         *    value = entry.peek();
         * }
         * finally {
         *    entry.writeUnLock();
         * }
         * </pre>
         *
         * </blockquote>
         */
        public Object peek() {
            try {
                lock.writeLock().lock();
                return value;
            }
            //            catch (RuntimeException e) {
            //                return null;
            //            }
            finally {
                lock.writeLock().unlock();
            }
        }

        /**
         * Acquires a read lock, obtains the value, unlocks, and returns the value. If another
         * thread already has the write lock, this method will block.
         *
         * @return cached value or null if empty
         */
        public Object getValue() {
            try {
                lock.readLock().lock();
                return value;
            }
            //            catch (InterruptedException e) {
            //                //TODO: add logger, or is this too performance constrained?
            //                return null;
            //            }
            finally {
                lock.readLock().unlock();
            }
        }

        /**
         * Stores the value in the entry, using the write lock. It is common to use this method
         * while already holding the writeLock (since writeLock is re-entrant).
         *
         * @param value
         */
        public void setValue(Object value) {
            try {
                lock.writeLock().lock();
                this.value = value;
            }
            //            catch (InterruptedException e) {
            //            }
            finally {
                lock.writeLock().unlock();
            }
        }

        /**
         * Acquires a write lock. This will block other readers and writers (on this entry only),
         * and other readers and writers will need to be cleared before the write lock can be
         * acquired, unless it is the same thread attempting to read or write.
         */
        public boolean writeLock() {
            lock.writeLock().lock();
            return true;
        }

        /** Releases a write lock. */
        public void writeUnLock() {
            lock.writeLock().unlock();
        }
    }

    /** Creates a new cache. */
    public DefaultObjectCache() {
        cache = new HashMap();
    }

    /** Creates a new cache using the indicated initialSize. */
    public DefaultObjectCache(final int initialSize) {
        cache = new HashMap(initialSize);
    }

    /** Removes all entries from this map. */
    public void clear() {
        synchronized (cache) {
            cache.clear();
        }
    }

    /**
     * Check if an entry exists in the cache.
     *
     * @param key
     * @return boolean
     */
    public boolean containsKey(final Object key) {
        return cache.containsKey(key);
    }

    /**
     * Returns the object from the cache.
     *
     * <p>Please note that a read lock is maintained on the cache contents; you may be stuck waiting
     * for a writer to produce the result over the course of calling this method. The contents (of
     * course) may be null.
     *
     * @param key The authority code.
     * @todo Consider logging a message here to the finer or finest level.
     */
    public Object get(final Object key) {
        return getEntry(key).getValue();
    }

    public Object peek(final Object key) {
        synchronized (cache) {
            if (!cache.containsKey(key)) {
                // no entry for this key - so no value
                return null;
            }
            return getEntry(key).peek();
        }
    }

    public void writeLock(final Object key) {
        getEntry(key).writeLock();
    }

    public void writeUnLock(final Object key) {
        synchronized (cache) {
            if (!cache.containsKey(key)) {
                throw new IllegalStateException("Cannot unlock prior to locking");
            }
            getEntry(key).writeUnLock();
        }
    }

    /** Stores a value */
    public void put(final Object key, final Object object) {
        getEntry(key).setValue(object);
    }

    /**
     * Retrieve cache entry, will create one if needed.
     *
     * @param key
     * @return ObjectCacheEntry
     */
    private ObjectCacheEntry getEntry(Object key) {
        synchronized (cache) {
            ObjectCacheEntry entry = (ObjectCacheEntry) cache.get(key);
            if (entry == null) {
                entry = new ObjectCacheEntry();
                cache.put(key, entry);
            }
            return entry;
        }
    }

    /**
     * Retrieves all keys currently in the cache.
     *
     * @return Set of keys
     */
    public Set<Object> getKeys() {
        Set<Object> ret = null;
        synchronized (cache) {
            ret = new HashSet<Object>(cache.keySet());
        }
        return ret;
    }

    /** Removes this item from the object cache. */
    public void remove(Object key) {
        synchronized (cache) {
            cache.remove(key);
        }
    }
}
