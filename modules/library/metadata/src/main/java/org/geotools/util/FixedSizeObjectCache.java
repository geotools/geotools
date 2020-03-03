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
final class FixedSizeObjectCache implements ObjectCache {

    private final int LIMIT;

    /** The cached values for each key. */
    private final Map cache;

    /** The locks for keys under construction. */
    private final Map /*<K,ReentrantLock>*/ locks;

    /** Creates a new cache. */
    public FixedSizeObjectCache() {
        this(50);
    }

    /** Creates a new cache using the indicated initialSize. */
    public FixedSizeObjectCache(final int initialSize) {
        LIMIT = initialSize;
        cache = Collections.synchronizedMap(new WeakValueHashMap(initialSize));
        locks = new HashMap(initialSize);
    }

    /** Removes all entries from this map. */
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
    public Object get(final Object key) {
        return cache.get(key);
    }

    public Object peek(final Object key) {
        return cache.get(key);
    }

    public void writeLock(final Object key) {
        ReentrantLock lock;
        synchronized (locks) {
            lock = (ReentrantLock) locks.get(key);
            if (lock == null) {
                lock = new ReentrantLock();
                locks.put(key, lock);
            }
        }
        // Must be outside the above synchronized section, since this call may block.
        lock.lock();
    }

    public void writeUnLock(final Object key) {
        synchronized (locks) {
            final ReentrantLock lock = (ReentrantLock) locks.get(key);
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

    boolean holdsLock(final Object key) {
        synchronized (locks) {
            final ReentrantLock lock = (ReentrantLock) locks.get(key);
            if (lock != null) {
                return lock.getHoldCount() != 0;
            }
        }
        return false;
    }
    /** Stores a value */
    public void put(final Object key, final Object object) {
        if (cache.size() < LIMIT) {
            writeLock(key);
            cache.put(key, object);
            writeUnLock(key);
        }
    }

    /** @return the keys of the object currently in the set */
    public Set<Object> getKeys() {
        Set<Object> keys = null;
        keys = new HashSet<Object>(cache.keySet());
        return keys;
    }

    /** Removes the given key from the cache. */
    public void remove(Object key) {
        // ensure nobody else is writing to this key as we remove it
        synchronized (locks) {
            locks.remove(key);
            cache.remove(key);
        }
    }
}
