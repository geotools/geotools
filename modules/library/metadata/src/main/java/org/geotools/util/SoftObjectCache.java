package org.geotools.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Caching implementation for {@link ObjectCache}. This instance is used when
 * caching is desired, and memory use is an issue.
 * <p>
 * Values are held in a SoftReference, the garbage collector may reclaim them at
 * any time.
 * 
 * <p>From the soft reference javadocs:<br>
 * Soft reference objects, which are cleared at the discretion of the garbage collector in response to memory demand. Soft references are most often used to implement memory-sensitive caches.</p>
 * 
 * @since 2.6
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/trunk/modules/library/metadata/src/main/
 *         java/org/geotools/util/SoftObjectCache.java $
 * @author Emily Gouge (Refractions Research)
 */
final class SoftObjectCache implements ObjectCache {

    /**
     * The cached values for each key.
     */
    private final Map/*<Object,SoftReference<Object>>*/ cache;

    /**
     * The locks for keys under construction.
     */
    private final Map/*<Object,ReentrantLock>*/ locks;
    

	/**
	 * Creates a new cache.
	 */
    public SoftObjectCache(){
        this(50);
    }
    
	/**
	 * Creates a new cache using the indicated initialSize.
	 */
    public SoftObjectCache(final int initialSize){
        cache = Collections.synchronizedMap(new HashMap<Object, SoftReference<Object>>(initialSize));
        locks = new HashMap<Object, ReentrantLock>();
    }
    
	/**
	 * Removes all entries from this map.
	 */
    public void clear() {
        synchronized (locks) {
            locks.clear();
            cache.clear();

        }
    }

	/**
	 * Returns the indicated object from the cache, or null if not found.
	 * 
	 * @param key
	 *            The authority code.
	 */
    public Object get( final Object key ) {
        Object stored = cache.get(key);
        if (stored instanceof Reference) {
            Reference reference = (Reference) stored;
            Object value = reference.get();
            if (value == null) {
                cache.remove(key);
            }
            return value;
        }
        return stored;
    }

	/**
	 * @return a copy of the keys currently in the map
	 */
    public Set<Object> getKeys() {
        Set<Object> keys = null;
        keys = new HashSet<Object>(cache.keySet());
        return keys;
    }

    public Object peek( final Object key ) {
        Object stored = cache.get(key);
        if (stored instanceof Reference) {
            Reference reference = (Reference) stored;
            return reference.get();
        }
        return stored;
    }

	/**
	 * Stores a value
	 */
    public void put( final Object key, final Object object ) {
        writeLock(key);
        SoftReference reference = new SoftReference(object);
        cache.put(key, reference);
        writeUnLock(key);
    }

	/**
     * Removes the given key from the cache.
     */
    public void remove( final Object key ) {
        synchronized (locks) {
            locks.remove(key);
            cache.remove(key);
        }
    }

    public void writeLock( final Object key ) {
        ReentrantLock lock;
        synchronized (locks) {
            lock = (ReentrantLock) locks.get(key);
            if (lock == null) {
                lock = new ReentrantLock();
                locks.put(key, lock);
            }
        }
        // Must be outside the above synchronized section, since this call may
        // block.
        lock.lock();
    }

    public void writeUnLock( final Object key ) {
        synchronized (locks) {
            final ReentrantLock lock = (ReentrantLock) locks.get(key);
            if (lock == null) {
                throw new IllegalMonitorStateException(
                        "Cannot unlock prior to locking");
            }
            if (lock.getHoldCount() == 0) {
                throw new IllegalMonitorStateException(
                        "Cannot unlock prior to locking");
            }
            lock.unlock();
        }
    }

}