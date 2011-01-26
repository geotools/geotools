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
package org.geotools.metadata;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.geotools.util.Utilities;


/**
 * A view of a metadata object as a map. Keys are property names and values
 * are the value returned by the {@code getFoo()} method using reflection.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 *
 * @see MetadataStandard#asMap
 */
final class PropertyMap extends AbstractMap<String,Object> {
    /**
     * The metadata object to wrap.
     */
    private final Object metadata;

    /**
     * The accessor to use for the metadata.
     */
    private final PropertyAccessor accessor;

    /**
     * A view of the mappings contained in this map.
     */
    private final Set<Map.Entry<String,Object>> entrySet;

    /**
     * Creates a property map for the specified metadata and accessor.
     */
    public PropertyMap(final Object metadata, final PropertyAccessor accessor) {
        this.metadata = metadata;
        this.accessor = accessor;
        entrySet = new Entries();
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     */
    @Override
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(final Object key) {
        return get(key) != null;
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key.
     */
    @Override
    public Object get(final Object key) {
        if (key instanceof String) {
            final Object value = accessor.get(accessor.indexOf((String) key), metadata);
            if (!PropertyAccessor.isEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @throws IllegalArgumentException if the specified property can't be set.
     * @throws ClassCastException if the given value is not of the expected type.
     */
    @Override
   public Object put(final String key, final Object value)
            throws IllegalArgumentException, ClassCastException
    {
        return accessor.set(accessor.requiredIndexOf(key), metadata, value);
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     */
    @Override
    public Object remove(final Object key) {
        if (key instanceof String) {
            return put((String) key, null);
        } else {
            return null;
        }
    }

    /**
     * Returns a view of the mappings contained in this map.
     */
    @Override
    public Set<Map.Entry<String,Object>> entrySet() {
        return entrySet;
    }




    /**
     * A map entry for a given property.
     *
     * @author Martin Desruisseaux
     */
    private final class Property implements Map.Entry<String,Object> {
        /**
         * The property index.
         */
        final int index;

        /**
         * Creates an entry for the given property.
         */
        Property(final int index) {
            this.index = index;
        }

        /**
         * Creates an entry for the given property.
         */
        Property(final String property) {
            index = accessor.indexOf(property);
        }

        /**
         * Returns the key corresponding to this entry.
         */
        public String getKey() {
            return accessor.name(index);
        }

        /**
         * Returns the value corresponding to this entry.
         */
        public Object getValue() {
            final Object value = accessor.get(index, metadata);
            return PropertyAccessor.isEmpty(value) ? null : value;
        }

        /**
         * Replaces the value corresponding to this entry with the specified value.
         *
         * @throws ClassCastException if the given value is not of the expected type.
         */
        public Object setValue(Object value) throws ClassCastException {
            return accessor.set(index, metadata, value);
        }

        /**
         * Compares the specified entry with this one for equality.
         */
        public boolean equals(final Map.Entry<?,?> entry) {
            return Utilities.equals(getKey(),   entry.getKey()) &&
                   Utilities.equals(getValue(), entry.getValue());
        }

        /**
         * Compares the specified object with this entry for equality.
         * Criterions are specified by the {@link Map.Entry} contract.
         */
        @Override
        public boolean equals(final Object object) {
            return (object instanceof Map.Entry) && equals((Map.Entry) object);
        }

        /**
         * Returns the hash code value for this map entry. The
         * formula is specified by the {@link Map.Entry} contract.
         */
        @Override
        public int hashCode() {
            Object x = getKey();
            int code = (x != null) ? x.hashCode() : 0;
            x = getValue();
            if (x != null) {
                code ^= x.hashCode();
            }
            return code;
        }
    }




    /**
     * The iterator over the {@link Property} elements contained in a {@link Entries} set.
     *
     * @author Martin Desruisseaux
     */
    private final class Iter implements Iterator<Map.Entry<String,Object>> {
        /**
         * The current and the next property, or {@code null} if the iteration is over.
         */
        private Property current, next;

        /**
         * Creates en iterator.
         */
        Iter() {
            move(0);
        }

        /**
         * Move {@link #next} to the first property with a valid value,
         * starting at the specified index.
         */
        private void move(int index) {
            final int count = accessor.count();
            while (index < count) {
                if (!PropertyAccessor.isEmpty(accessor.get(index, metadata))) {
                    next = new Property(index);
                    return;
                }
                index++;
            }
            next = null;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         */
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next element in the iteration.
         */
        public Map.Entry<String,Object> next() {
            if (next != null) {
                current = next;
                move(next.index + 1);
                return current;
            } else {
                throw new NoSuchElementException();
            }
        }

        /**
         * Removes from the underlying collection the last element returned by the iterator.
         */
        public void remove() {
            if (current != null) {
                current.setValue(null);
                current = null;
            } else {
                throw new IllegalStateException();
            }
        }
    }




    /**
     * View of the mapping contained in the map.
     *
     * @author Martin Desruisseaux
     */
    private final class Entries extends AbstractSet<Map.Entry<String,Object>> {
        /**
         * Creates an entry set.
         */
        Entries() {
        }

        /**
         * Returns an iterator over the elements contained in this collection.
         */
        @Override
        public Iterator<Map.Entry<String,Object>> iterator() {
            return new Iter();
        }

        /**
         * Returns the number of elements in this collection.
         */
        public int size() {
            return accessor.count(metadata, Integer.MAX_VALUE);
        }

        /**
         * Returns true if this collection contains no elements.
         */
        @Override
        public boolean isEmpty() {
            return accessor.count(metadata, 1) == 0;
        }

        /**
         * Returns {@code true} if this collection contains the specified element.
         */
        @Override
        public boolean contains(final Object object) {
            if (object instanceof Map.Entry) {
                final Map.Entry entry = (Map.Entry) object;
                final Object key = entry.getKey();
                if (key instanceof String) {
                    return new Property((String) key).equals(entry);
                }
            }
            return false;
        }
    }
}
