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

import java.lang.ref.Reference;  // For javadoc
import java.util.Set;


/**
 * A cache for arbitrary objects. Cache implementations are thread-safe and support concurrency.
 * A cache entry can be locked when an object is in process of being created, but the locking /
 * unlocking <strong>must</strong> be protected in a {@code try} ... {@code finally} block.
 * <p>
 * To use as a reader:
 *
 * <blockquote><pre>
 * key = &quot;EPSG:4326&quot;;
 * CoordinateReferenceSystem crs = cache.get(key);
 * </pre></blockquote>
 *
 * To overwrite:
 *
 * <blockquote><pre>
 * cache.put(key, crs);
 * </pre></blockquote>
 *
 * To reserve the entry while figuring out what to write:
 *
 * <blockquote><pre>
 * try {
 *     cache.writeLock(key); // may block if another writer is working on this code.
 *     value = cache.peek(key);
 *     if (value == null) {
 *        // another writer got here first
 *     } else { 
 *        value = figuringOutWhatToWrite(....);
 *        cache.put(key, value);
 *     }
 * } finally {
 *     cache.writeUnLock(key);
 * }
 * </pre></blockquote>
 *
 * To use as a proper cache:
 *
 * <blockquote><pre>
 * CylindricalCS cs = (CylindricalCS) cache.get(key);
 * if (cs == null) {
 *     try {
 *         cache.writeLock(key);
 *         cs = (CylindricalCS) cache.test(key);
 *         if (cs == null) {
 *             cs = csAuthority.createCylindricalCS(code);
 *             cache.put(key, cs);
 *         }
 *     } finally {
 *         cache.writeUnLock(key);
 *     }
 * }
 * return cs;
 * </pre></blockquote>
 *
 * @since 2.5
 * @version $Id$
 *
 * @source $URL$
 * @author Cory Horner (Refractions Research)
 *
 * @see https://jsr-107-interest.dev.java.net/javadoc/javax/cache/package-summary.html
 */
public interface ObjectCache {
    /**
     * Removes all entries from this cache.
     */
    void clear();

    /**
     * Returns an object from the pool for the specified code. If the object was retained as a
     * {@linkplain Reference weak reference}, the {@link Reference#get referent} is returned.
     * 
     * @param   key The key whose associated value is to be returned.
     * @returns The value to which the specified key is mapped, or {@code null} if this cache
     *          contains no mapping for the key.
     */
    Object get(Object key);

    /**
     * Use the write lock to test the value for the provided key.
     * <p>
     * This method is used by a writer to test if someone (ie another writer) has provided the value
     * for us (while we were blocked waiting for them).
     * 
     * @param key
     * @return The value, may be <code>null</code>
     */
    Object peek(Object key);

    /**
     * Puts an element into the cache.
     * <p>
     * You may simply use this method - it is threadsafe:
     * 
     * <pre></code>
     * cache.put(&quot;4326&quot;, crs);
     * </code></pre>
     * 
     * You may also consider reserving the entry while you work on the answer:
     * 
     * <pre></code>
     *  try {
     *     cache.writeLock( &quot;fred&quot; );
     *     ...find fred
     *     cache.put( &quot;fred&quot;, fred );
     *  }
     *  finally {
     *     cache.writeUnLock();
     *  }
     * </code></pre>
     * 
     * @param key the authority code.
     * @param object The referencing object to add in the pool.
     */
    void put(Object key, Object object);

    /**
     * Acquire a write lock on the indicated key.
     * 
     * @param key
     */
    void writeLock(Object key); // TODO: how to indicate lock was not acquired?

    /**
     * Release write lock on the indicated key.
     * 
     * @param key
     */
    void writeUnLock(Object key);
    
    /**
     * Returns a set of all the keys currently contained within the 
     * ObjectCache.
     * <p>
     * This is a static copy of the keys in the cache at the point 
     * in time when the function is called.
     * 
     * 
     * @return a set of keys currently contained within the cache.
     */
    Set<Object> getKeys();
    
    /**
     * Removes a given key from the cache.
     * @param key
     */
    void remove(Object key);
}
