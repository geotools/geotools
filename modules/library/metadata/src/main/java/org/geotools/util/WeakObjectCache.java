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

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Caching implementation for {@link ObjectCache}. This instance is used when caching is desired,
 * and memory use is an issue.
 *
 * <p>Values are held in a WeakReference, the garbage collector may reclaim them at any time.
 *
 * @since 2.5
 * @author Cory Horner (Refractions Research)
 * @author Jody Garnett (Refractions Research)
 * @author Martin Desruisseaux (Geomatys)
 */
final class WeakObjectCache<K, V> implements ObjectCache<K, V> {

    /** The cached values for each key. */
    private final Map<K, WeakReference<V>> cache;

    /** The locks for keys under construction. */
    private final Map<K, ReentrantLock> locks;

    /** Creates a new cache. */
    public WeakObjectCache() {
        this(50);
    }

    /** Creates a new cache using the indicated initialSize. */
    public WeakObjectCache(final int initialSize) {
        cache = Collections.synchronizedMap(new HashMap<>(initialSize));
        locks = new HashMap<>();
    }

    /** Removes all entries from this map. */
    @Override
    public void clear() {
        synchronized (locks) {
            locks.clear();
            cache.clear();
        }
    }

    /**
     * Check if an entry exists in the cache.
     *
     * @return boolean
     */
    public boolean containsKey(final K key) {
        if (cache.containsKey(key)) {
            WeakReference<V> reference = cache.get(key);
            if (reference == null) return false;
            return reference.get() != null;
        }
        return false;
    }

    /**
     * Returns the indicated object from the cache, or null if not found.
     *
     * @param key The authority code.
     */
    @Override
    public V get(final K key) {
        WeakReference<V> reference = cache.get(key);
        if (reference == null) return null;
        V value = reference.get();
        if (value == null) {
            cache.remove(key);
        }
        return value;
    }

    @Override
    public V peek(final K key) {
        WeakReference<V> reference = cache.get(key);
        if (reference == null) return null;
        return reference.get();
    }

    @Override
    public void writeLock(final K key) {
        ReentrantLock lock;
        synchronized (locks) {
            lock = locks.get(key);
            if (lock == null) {
                lock = new ReentrantLock();
                locks.put(key, lock);
            }
        }
        // Must be outside the above synchronized section, since this call may
        // block.
        lock.lock();
    }

    @Override
    public void writeUnLock(final K key) {
        synchronized (locks) {
            final ReentrantLock lock = locks.get(key);
            if (lock == null) {
                throw new IllegalMonitorStateException("Cannot unlock prior to locking");
            }
            if (lock.getHoldCount() == 0) {
                throw new IllegalMonitorStateException("Cannot unlock prior to locking");
            }
            lock.unlock();
            // TODO: stop lock from being removed when another worker is trying
            // to acquire it
            // TODO: review w/ J2SE 5.0
            // if (lock.holds() == 0) {
            // locks.remove(key);
            // }
        }
    }

    boolean holdsLock(final K key) {
        synchronized (locks) {
            final ReentrantLock lock = locks.get(key);
            if (lock != null) {
                return lock.getHoldCount() != 0;
            }
        }
        return false;
    }

    /** Stores a value */
    @Override
    public void put(final K key, final V object) {
        writeLock(key);
        WeakReference<V> reference = new WeakReference<>(object);
        cache.put(key, reference);
        writeUnLock(key);
    }

    /** @return the keys of the object currently in the set */
    @Override
    public Set<K> getKeys() {
        return new HashSet<>(cache.keySet());
    }

    /** Removes the given key from the cache. */
    @Override
    public void remove(K key) {
        synchronized (locks) {
            locks.remove(key);
            cache.remove(key);
        }
    }
}
