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
 * <p>Values are held in a WealValueHashSet, the garbage collector may reclaim them at any time.
 * After the LIMIT is reached additional values are ignored by the cache.
 *
 * @since 2.5
 * @version $Id$
 * @author Jody Garnett (Refractions Research)
 */
final class FixedSizeObjectCache<K, V> implements ObjectCache<K, V> {

    private final int LIMIT;

    /** The cached values for each key. */
    private final Map<K, V> cache;

    /** The locks for keys under construction. */
    private final Map<K, ReentrantLock> locks;

    /** Creates a new cache. */
    public FixedSizeObjectCache() {
        this(50);
    }

    /** Creates a new cache using the indicated initialSize. */
    public FixedSizeObjectCache(final int initialSize) {
        LIMIT = initialSize;
        cache = Collections.synchronizedMap(new WeakValueHashMap<>(initialSize));
        locks = new HashMap<>(initialSize);
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
    public boolean containsKey(final Object key) {
        return cache.containsKey(key);
    }

    /**
     * Returns the indicated object from the cache, or null if not found.
     *
     * @param key The authority code.
     */
    @Override
    public V get(final Object key) {
        return cache.get(key);
    }

    @Override
    public V peek(final K key) {
        return cache.get(key);
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
        // Must be outside the above synchronized section, since this call may block.
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
            if (lock.getHoldCount() == 0) {
                locks.remove(key);
            }
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
        if (cache.size() < LIMIT) {
            writeLock(key);
            cache.put(key, object);
            writeUnLock(key);
        }
    }

    /** @return the keys of the object currently in the set */
    @Override
    public Set<K> getKeys() {
        return new HashSet<>(cache.keySet());
    }

    /** Removes the given key from the cache. */
    @Override
    public void remove(K key) {
        // ensure nobody else is writing to this key as we remove it
        synchronized (locks) {
            locks.remove(key);
            cache.remove(key);
        }
    }
}
