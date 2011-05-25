/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Map;
import java.util.LinkedHashMap;
import org.opengis.util.Cloneable;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A {@linkplain Collections#checkedMap checked} and {@linkplain Collections#synchronizedMap
 * synchronized} {@link java.util.Map}. Type checks are performed at run-time in addition of
 * compile-time checks. The synchronization lock can be modified at runtime by overriding the
 * {@link #getLock} method.
 * <p>
 * This class is similar to using the wrappers provided in {@link Collections}, minus the cost
 * of indirection levels and with the addition of overrideable methods.
 *
 * @todo Current implementation do not synchronize the {@linkplain #entrySet entry set},
 *       {@linkplain #keySet key set} and {@linkplain #values values} collection.
 *
 * @param <K> The type of keys in the map.
 * @param <V> The type of values in the map.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett (Refractions Research)
 * @author Martin Desruisseaux (IRD)
 *
 * @see Collections#checkedMap
 * @see Collections#synchronizedMap
 */
public class CheckedHashMap<K,V> extends LinkedHashMap<K,V> implements Cloneable {
    /**
     * Serial version UID for compatibility with different versions.
     */
    private static final long serialVersionUID = -7777695267921872849L;

    /**
     * The class type for keys.
     */
    private final Class<K> keyType;

    /**
     * The class type for values.
     */
    private final Class<V> valueType;

    /**
     * Constructs a map of the specified type.
     *
     * @param keyType   The key type (should not be null).
     * @param valueType The value type (should not be null).
     */
    public CheckedHashMap(final Class<K> keyType, final Class<V> valueType) {
        this.keyType   = keyType;
        this.valueType = valueType;
        ensureNonNull(  keyType,   "keyType");
        ensureNonNull(valueType, "valueType");
    }

    /**
     * Ensure that the given argument is non-null.
     */
    private static void ensureNonNull(final Class<?> type, final String name) {
        if (type == null) {
            throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }

    /**
     * Checks the type of the specified object. The default implementation ensure
     * that the object is assignable to the type specified at construction time.
     *
     * @param  element the object to check, or {@code null}.
     * @throws IllegalArgumentException if the specified element is not of the expected type.
     */
    private static <E> void ensureValidType(final E element, final Class<E> type)
            throws IllegalArgumentException
    {
        if (element!=null && !type.isInstance(element)) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.ILLEGAL_CLASS_$2, element.getClass(), type));
        }
    }

    /**
     * Checks if changes in this collection are allowed. This method is automatically invoked
     * after this collection got the {@linkplain #getLock lock} and before any operation that
     * may change the content. The default implementation does nothing (i.e. this collection
     * is modifiable). Subclasses should override this method if they want to control write
     * access.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     *
     * @since 2.5
     */
    protected void checkWritePermission() throws UnsupportedOperationException {
        assert Thread.holdsLock(getLock());
    }

    /**
     * Returns the synchronization lock. The default implementation returns {@code this}.
     * Subclasses that override this method should be careful to update the lock reference
     * when this set is {@linkplain #clone cloned}.
     *
     * @return The synchronization lock.
     *
     * @since 2.5
     */
    protected Object getLock() {
        return this;
    }

    /**
     * Returns the number of elements in this map.
     */
    @Override
    public int size() {
	synchronized (getLock()) {
            return super.size();
        }
    }

    /**
     * Returns {@code true} if this map contains no elements.
     */
    @Override
    public boolean isEmpty() {
	synchronized (getLock()) {
            return super.isEmpty();
        }
    }

    /**
     * Returns {@code true} if this map contains the specified key.
     */
    @Override
    public boolean containsKey(final Object key) {
	synchronized (getLock()) {
            return super.containsKey(key);
        }
    }

    /**
     * Returns {@code true} if this map contains the specified value.
     */
    @Override
    public boolean containsValue(final Object value) {
	synchronized (getLock()) {
            return super.containsValue(value);
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if none.
     */
    @Override
    public V get(Object key) {
	synchronized (getLock()) {
            return super.get(key);
        }
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for this key, the old
     * value is replaced.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return previous value associated with specified key, or {@code null}.
     * @throws IllegalArgumentException if the key or the value is not of the expected type.
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public V put(final K key, final V value)
            throws IllegalArgumentException, UnsupportedOperationException
    {
        ensureValidType(key,     keyType);
        ensureValidType(value, valueType);
	synchronized (getLock()) {
            checkWritePermission();
            return super.put(key, value);
        }
    }

    /**
     * Copies all of the mappings from the specified map to this map.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) throws UnsupportedOperationException {
        for (final Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            ensureValidType(entry.getKey(),     keyType);
            ensureValidType(entry.getValue(), valueType);
        }
	synchronized (getLock()) {
            checkWritePermission();
            super.putAll(m);
        }
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public V remove(Object key) throws UnsupportedOperationException {
        synchronized (getLock()) {
            checkWritePermission();
            return super.remove(key);
        }
    }

    /**
     * Removes all of the elements from this map.
     *
     * @throws UnsupportedOperationException if this collection is unmodifiable.
     */
    @Override
    public void clear() throws UnsupportedOperationException {
        synchronized (getLock()) {
            checkWritePermission();
            super.clear();
        }
    }

    /**
     * Returns a string representation of this map.
     */
    @Override
    public String toString() {
        synchronized (getLock()) {
            return super.toString();
        }
    }

    /**
     * Compares the specified object with this map for equality.
     */
    @Override
    public boolean equals(Object o) {
        synchronized (getLock()) {
            return super.equals(o);
        }
    }

    /**
     * Returns the hash code value for this map.
     */
    @Override
    public int hashCode() {
        synchronized (getLock()) {
            return super.hashCode();
        }
    }

    /**
     * Returns a shallow copy of this map.
     *
     * @return A shallow copy of this map.
     */
    @Override
    @SuppressWarnings("unchecked")
    public CheckedHashMap<K,V> clone() {
        synchronized (getLock()) {
            return (CheckedHashMap) super.clone();
        }
    }
}
