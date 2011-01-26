/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.Serializable;
import java.util.Map;

import org.geotools.resources.Classes;


/**
 * A default implementation of {@link java.util.Map.Entry} which map an arbitrary
 * key-value pairs. This entry is immutable by default.
 *
 * @param <K> The class of key elements.
 * @param <V> The class of value elements.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @todo This class will be removed when we will be allowed to compile for JSE 1.6, since a
 *       default map entry implementation is provided there.
 */
public class MapEntry<K,V> implements Map.Entry<K,V>, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 8627698052283756776L;

    /**
     * The key.
     */
    private final K key;

    /**
     * The value.
     */
    private final V value;

    /**
     * Creates a new map entry with the specified key-value pair.
     *
     * @param key The key.
     * @param value The value.
     */
    public MapEntry(final K key, final V value) {
        this.key   = key;
        this.value = value;
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
        return value;
    }

    /**
     * Replaces the value corresponding to this entry with the specified
     * value (optional operation). The default implementation throws an
     * {@link UnsupportedOperationException}.
     */
    public V setValue(final V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Compares the specified object with this entry for equality.
     *
     * @param object The object to compare with this entry for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Map.Entry) {
            final Map.Entry that = (Map.Entry) object;
            return Utilities.equals(this.getKey(),   that.getKey()) &&
                   Utilities.equals(this.getValue(), that.getValue());
        }
        return false;
    }

    /**
     * Returns the hash code value for this map entry
     */
    @Override
    public int hashCode() {
        int code = 0;
        if (key   != null) code  =   key.hashCode();
        if (value != null) code ^= value.hashCode();
        return code;
    }

    /**
     * Returns a string representation of this entry.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) + "[key=" + key + ", value=" + value + ']';
    }
}
