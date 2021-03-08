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
import java.util.Set;

/**
 * Null implementation for the {@link ObjectCache}. Used for cases where caching is
 * <strong>not</strong> desired.
 *
 * @since 2.5
 * @version $Id$
 * @author Cory Horner (Refractions Research)
 */
final class NullObjectCache<K, V> implements ObjectCache<K, V> {
    /** The singleton instance. */
    public static final NullObjectCache<Object, Object> INSTANCE = new NullObjectCache<>();

    /** Do not allow instantiation of this class, since a singleton is enough. */
    private NullObjectCache() {}

    /** Do nothing since this map is already empty. */
    @Override
    public void clear() {}

    /** Returns {@code null} since this map is empty. */
    @Override
    public V get(K key) {
        return null;
    }

    /** Returns {@code null} since this map is empty. */
    @Override
    public V peek(K key) {
        return null;
    }

    /** Do nothing since this map does not cache anything. */
    @Override
    public void put(K key, V object) {}

    /** There is no cache, therefore a cache miss is a safe assumption. */
    public boolean containsKey(K key) {
        return false;
    }

    /** Do nothing since there is no write lock. */
    @Override
    public void writeLock(K key) {}

    /** Do nothing since there is no write lock. */
    @Override
    public void writeUnLock(K key) {}

    /** Return an empty set. */
    @Override
    public Set<K> getKeys() {
        return Collections.emptySet();
    }

    /** Do nothing since there is nothing to remove. */
    @Override
    public void remove(K key) {}
}
