/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A {@link Map} with a fixed maximum size which removes the <cite>least recently used</cite> (LRU)
 * entry if an entry is added when full. This class implements a simple technique for LRU pooling
 * of objects.
 * <p>
 * This class is <strong>not</strong> thread-safe. Synchronizations (if wanted) are user's
 * reponsability.
 *
 * @param <K> The type of keys in the map.
 * @param <V> The type of values in the map.
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 * @author Martin Desruisseaux
 * @since 2.3
 */
public final class LRULinkedHashMap<K,V> extends LinkedHashMap<K,V> {
    /**
     * Serial number for cross-version compatibility.
     */
    private static final long serialVersionUID = -6668885347230182669L;

    /**
     * The default load factor copied from {@link HashMap} package-privated field.
     */
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    /**
     * Default maximum size (not to be confused with capacity).
     */
    private static final int DEFAULT_MAXIMUM_SIZE = 100;

    /**
     * Maximum number of entries for this LRU map.
     */
    private int maximumSize;

    /**
     * Constructs a {@code LRULinkedHashMap} with default initial capacity, maximum size
     * and load factor.
     */
    public LRULinkedHashMap() {
        super();
        maximumSize = DEFAULT_MAXIMUM_SIZE;
    }

    /**
     * Constructs a {@code LRULinkedHashMap} with default maximum size and load factor.
     *
     * @param initialCapacity The initial capacity.
     */
    public LRULinkedHashMap(final int initialCapacity) {
        super(initialCapacity);
        maximumSize = DEFAULT_MAXIMUM_SIZE;
    }

    /**
     * Constructs a {@code LRULinkedHashMap} with default maximum size.
     *
     * @param initialCapacity The initial capacity.
     * @param loadFactor      The load factor.
     */
    public LRULinkedHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        maximumSize = DEFAULT_MAXIMUM_SIZE;
    }

    /**
     * Constructs a {@code LRULinkedHashMap} with default maximum size.
     *
     * @param initialCapacity The initial capacity.
     * @param loadFactor      The load factor.
     * @param accessOrder     The ordering mode: {@code true} for access-order,
     *                        {@code false} for insertion-order.
     */
    public LRULinkedHashMap(final int initialCapacity, final float loadFactor,
                            final boolean accessOrder)
    {
        super(initialCapacity, loadFactor, accessOrder);
        maximumSize = DEFAULT_MAXIMUM_SIZE;
    }

    /**
     * Constructs a {@code LRULinkedHashMap} with the specified maximum size.
     *
     * @param initialCapacity The initial capacity.
     * @param loadFactor      The load factor.
     * @param accessOrder     The ordering mode: {@code true} for access-order,
     *                        {@code false} for insertion-order.
     * @param maximumSize     Maximum number of entries for this LRU map.
     */
    public LRULinkedHashMap(final int initialCapacity, final float loadFactor,
                            final boolean accessOrder, final int maximumSize)
    {
        super(initialCapacity, loadFactor, accessOrder);
        this.maximumSize = maximumSize;
        checkMaximumSize(maximumSize);
    }

    /**
     * For private usage by static convenience constructors only. The {@code maximumSize} must
     * be checked before this construcor is invoked, because it is used in the calculation of
     * the value given to {@code super} constructor call.
     */
    private LRULinkedHashMap(final boolean accessOrder, final int maximumSize) {
        super((int) Math.ceil(maximumSize / DEFAULT_LOAD_FACTOR),
              (float)DEFAULT_LOAD_FACTOR, accessOrder);
        this.maximumSize = maximumSize;
    }

    /**
     * Constructs a {@code LRULinkedHashMap} with all entries from the specified map.
     * The {@linkplain #getMaximumSize maximum size} is set to the given
     * {@linkplain Map#size map size}.
     *
     * @param map The map whose mappings are to be placed in this map.
     *
     * @since 2.5
     */
    public LRULinkedHashMap(final Map<K,V> map) {
        super(map);
        maximumSize = map.size();
    }

    /**
     * Constructs a {@code LRULinkedHashMap} with all entries from the specified map
     * and maximum number of entries.
     *
     * @param map The map whose mappings are to be placed in this map.
     * @param maximumSize Maximum number of entries for this LRU map.
     *
     * @since 2.5
     */
    public LRULinkedHashMap(final Map<K,V> map, final int maximumSize) {
        super(map);
        this.maximumSize = maximumSize;
        checkMaximumSize(maximumSize);
        removeExtraEntries();
    }

    /**
     * Ensures that the given size is strictly positive.
     */
    private static void checkMaximumSize(final int maximumSize) throws IllegalArgumentException {
        if (maximumSize <= 0) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.NOT_GREATER_THAN_ZERO_$1, maximumSize));
        }
    }

    /**
     * If there is more entries than the maximum amount, remove extra entries.
     * <p>
     * <b>Note:</b> Invoking {@code removeExtraEntries()} after adding all entries in the
     * {@link #LRULinkedHashMap(Map,int)} constructor is less efficient than just iterating over
     * the {@link #maximumSize} first entries at construction time,  but super-class constructor
     * is more efficient for maps with less than {@code maximumSize}. We assume that this is the
     * most typical case.
     */
    private void removeExtraEntries() {
        if (size() > maximumSize) {
            final Iterator<Map.Entry<K,V>> it = entrySet().iterator();
            for (int c=0; c<maximumSize; c++) {
                it.next();
            }
            while (it.hasNext()) {
                it.remove();
            }
        }
    }

    /**
     * Creates a map for the most recently accessed entries. This convenience constructor
     * uses an initial capacity big enough for holding the given maximum number of entries,
     * so it is not appropriate if {@code maximumSize} is very large.
     *
     * @param <K> The type of keys in the map.
     * @param <V> The type of values in the map.
     *
     * @param  maximumSize Maximum number of entries for this LRU map.
     * @return A map holding only the {@code maximumSize} most recently accessed entries.
     *
     * @since 2.5
     */
    public static <K,V> LRULinkedHashMap<K,V> createForRecentAccess(final int maximumSize) {
        checkMaximumSize(maximumSize);
        return new LRULinkedHashMap<K,V>(true, maximumSize);
    }

    /**
     * Creates a map for the most recently inserted entries. This convenience constructor
     * uses an initial capacity big enough for holding the given maximum number of entries,
     * so it is not appropriate if {@code maximumSize} is very large.
     *
     * @param <K> The type of keys in the map.
     * @param <V> The type of values in the map.
     *
     * @param  maximumSize Maximum number of entries for this LRU map.
     * @return A map holding only the {@code maximumSize} most recently inserted entries.
     *
     * @since 2.5
     */
    public static <K,V> LRULinkedHashMap<K,V> createForRecentInserts(final int maximumSize) {
        checkMaximumSize(maximumSize);
        return new LRULinkedHashMap<K,V>(false, maximumSize);
    }

    /**
     * Returns the maximal {@linkplain #size size} allowed for this map.
     * This is the value given to constructors.
     *
     * @return The maximal size allowed for this map.
     *
     * @since 2.5
     */
    public int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Sets the maximal {@linkplain #size size} allowed for this map. If the given size
     * is smaller than the previous one, eldest entries will be immediately removed.
     *
     * @param max The new maximal size.
     * @throws IllegalArgumentException if the given size is negative.
     *
     * @since 2.5
     */
    public void setMaximumSize(final int max) {
        checkMaximumSize(max);
        maximumSize = max;
        removeExtraEntries();
    }

    /**
     * Returns {@code true} if this map should remove its eldest entry. The default implementation
     * returns {@code true} if the {@linkplain #size number of entries} in this map has reached the
     * maximum number of entries specified at construction time.
     *
     * @param eldest The least recently inserted entry in the map, or if this is an access-ordered
     *        map, the least recently accessed entry. This is the entry that will be removed it this
     *        method returns {@code true}.
     * @return {@code true} if the eldest entry should be removed from the map;
     *         {@code false} if it should be retained.
     */
    @Override
    protected boolean removeEldestEntry(final Map.Entry<K,V> eldest) {
        return size() > maximumSize;
    }
}
