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
package org.geotools.referencing.factory;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Caching implementation for ReferencingObjectCache. This instance is used when
 * actual caching is desired. This is a temporary class.
 * 
 * @since 2.4
 * @version $Id: DefaultReferencingObjectCache.java 25972 2007-06-21 13:38:35Z
 *          desruisseaux $
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/library/referencing/src/main/java/org/geotools/referencing/factory/DefaultReferencingObjectCache.java $
 * @author Cory Horner (Refractions Research)
 */
final class OldReferencingObjectCache {
    /**
     * The pool of cached objects.
     * <p>
     * The following may be seen for a key (String key?):
     * <ul>
     * <li>Object (ie a strong reference) usually a referencing object like CoordinateReferenceSystem or Datum</li>
     * <li>WeakReference used to hold a referencing object (may be cleaned up at any time</li>
     * </ul>
     */
    private final LinkedHashMap pool = new LinkedHashMap(32, 0.75f, true);

    /**
     * The maximum number of objects to keep by strong reference. If a greater amount of
     * objects are created, then the strong references for the oldest ones are replaced by
     * weak references.
     */
    private final int maxStrongReferences;

    /**
     * Creates a new cache which will hold the specified amount of object by strong references.
     * Any additional object will be help by weak references.
     */
    public OldReferencingObjectCache(final int maxStrongReferences) {
        this.maxStrongReferences = maxStrongReferences;
    }

    /**
     * Removes all entries from this map.
     */
    public synchronized void clear() {
        if (pool != null) {
            pool.clear();
        }
    }

    /**
     * Returns an object from the pool for the specified code. If the object was retained as a
     * {@linkplain Reference weak reference}, the {@link Reference#get referent} is returned.
     *
     * @param key The authority code.
     *
     * @todo Consider logging a message here to the finer or finest level.
     */
    public Object get(final Object key) {
        //assert Thread.holdsLock(factory);
        Object object = pool.get(key);
        if (object instanceof Reference) {
            object = ((Reference) object).get();
        }
        return object;
    }

    /**
     * Put an element in the pool. This method is invoked everytime a {@code createFoo(...)}
     * method is invoked, even if an object was already in the pool for the given code, for
     * the following reasons: 1) Replaces weak reference by strong reference (if applicable)
     * and 2) Alters the linked hash set order, so that this object is declared as the last
     * one used.
     *
     * @param key the authority code.
     * @param object The referencing object to add in the pool.
     */
    public void put(final Object key, final Object object) {
        //assert Thread.holdsLock(factory);
        pool.put(key, object);
        int toReplace = pool.size() - maxStrongReferences;
        if (toReplace > 0) {
            for (final Iterator it=pool.entrySet().iterator(); it.hasNext();) {
                final Map.Entry entry = (Map.Entry) it.next();
                final Object value = entry.getValue();
                if (value instanceof Reference) {
                    if (((Reference) value).get() == null) {
                        it.remove();
                    }
                    continue;
                }
                entry.setValue(new WeakReference(value));
                if (--toReplace == 0) {
                    break;
                }
            }
        }
    }

    public void writeLock(Object key) {
    }

    public void writeUnLock(Object key) {
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public Object test( Object key ) {
        return null;
    }
}
