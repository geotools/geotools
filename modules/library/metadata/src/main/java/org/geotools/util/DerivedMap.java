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
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * A map whose keys are derived from an other map. The keys are derived only when
 * requested, which make it possible to backup potentially large maps. Implementations
 * need only to overrides {@link #baseToDerived} and {@link #derivedToBase} methods.
 * This set do not supports {@code null} key, since {@code null} is used
 * when no mapping from {@linkplain #base} to {@code this} exists.
 * This class is serializable if the underlying {@linkplain #base} set is serializable
 * too.
 * <p>
 * This class is <strong>not</strong> thread-safe. Synchronizations (if wanted) are user's
 * reponsability.
 *
 * @param <BK> The type of keys in the backing map.
 * @param <K>  The type of keys in this map.
 * @param <V>  The type of values in both this map and the underlying map.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class DerivedMap<BK,K,V> extends AbstractMap<K,V> implements Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -6994867383669885934L;

    /**
     * The base map whose keys are derived from.
     *
     * @see #baseToDerived
     * @see #derivedToBase
     */
    protected final Map<BK,V> base;

    /**
     * Key set. Will be constructed only when first needed.
     *
     * @see #keySet
     */
    private transient Set<K> keySet;

    /**
     * Entry set. Will be constructed only when first needed.
     *
     * @see #entrySet
     */
    private transient Set<Map.Entry<K,V>> entrySet;

    /**
     * The derived key type.
     */
    private final Class<K> keyType;

    /**
     * Creates a new derived map from the specified base map.
     *
     * @param base The base map.
     *
     * @deprecated Use {@link #DerivedMap(Map, Class} instead.
     */
    @SuppressWarnings("unchecked")
    public DerivedMap(final Map<BK,V> base) {
        this(base, (Class) Object.class);
    }

    /**
     * Creates a new derived map from the specified base map.
     *
     * @param base The base map.
     * @param keyType the type of keys in the derived map.
     *
     * @since 2.5
     */
    public DerivedMap(final Map<BK,V> base, final Class<K> keyType) {
        this.base    = base;
        this.keyType = keyType;
    }

    /**
     * Transforms a key from the {@linkplain #base} map to a key in this map.
     * If there is no key in the derived map for the specified base key,
     * then this method returns {@code null}.
     *
     * @param  key A ley from the {@linkplain #base} map.
     * @return The key that this view should contains instead of {@code key},
     *         or {@code null}.
     */
    protected abstract K baseToDerived(final BK key);

    /**
     * Transforms a key from this derived map to a key in the {@linkplain #base} map.
     *
     * @param  key A key in this map.
     * @return The key stored in the {@linkplain #base} map.
     */
    protected abstract BK derivedToBase(final K key);

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map.
     */
    @Override
    public int size() {
	return super.size();
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings.
     */
    @Override
    public boolean isEmpty() {
	return base.isEmpty() || super.isEmpty();
    }

    /**
     * Returns {@code true} if this map maps one or more keys to this value.
     * The default implementation invokes
     * <code>{@linkplain #base}.containsValue(value)</code>.
     *
     * @return {@code true} if this map maps one or more keys to this value.
     */
    @Override
    public boolean containsValue(final Object value) {
        return base.containsValue(value);
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     * The default implementation invokes
     * <code>{@linkplain #base}.containsKey({@linkplain #derivedToBase derivedToBase}(key))</code>.
     *
     * @param  key key whose presence in this map is to be tested.
     * @return {@code true} if this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(final Object key) {
        if (keyType.isInstance(key)) {
            return base.containsKey(derivedToBase(keyType.cast(key)));
        } else {
            return false;
        }
    }

    /**
     * Returns the value to which this map maps the specified key.
     * The default implementation invokes
     * <code>{@linkplain #base}.get({@linkplain #derivedToBase derivedToBase}(key))</code>.
     *
     * @param  key key whose associated value is to be returned.
     * @return the value to which this map maps the specified key.
     */
    @Override
    public V get(final Object key) {
        if (keyType.isInstance(key)) {
            return base.get(derivedToBase(keyType.cast(key)));
        } else {
            return null;
        }
    }

    /**
     * Associates the specified value with the specified key in this map.
     * The default implementation invokes
     * <code>{@linkplain #base}.put({@linkplain #derivedToBase derivedToBase}(key), value)</code>.
     *
     * @param  key key with which the specified value is to be associated.
     * @param  value value to be associated with the specified key.
     * @return previous value associated with specified key, or {@code null}
     *	       if there was no mapping for key.
     * @throws UnsupportedOperationException if the {@linkplain #base} map doesn't
     *         supports the {@code put} operation.
     */
    @Override
    public V put(final K key, final V value) throws UnsupportedOperationException {
        return base.put(derivedToBase(key), value);
    }

    /**
     * Removes the mapping for this key from this map if present.
     * The default implementation invokes
     * <code>{@linkplain #base}.remove({@linkplain #derivedToBase derivedToBase}(key))</code>.
     *
     * @param  key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or {@code null}
     *	       if there was no entry for key.
     * @throws UnsupportedOperationException if the {@linkplain #base} map doesn't
     *         supports the {@code remove} operation.
     */
    @Override
    public V remove(final Object key) throws UnsupportedOperationException {
        if (keyType.isInstance(key)) {
            return base.remove(derivedToBase(keyType.cast(key)));
        } else {
            return null;
        }
    }

    /**
     * Returns a set view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map.
     */
    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new KeySet(base.keySet());
        }
        return keySet;
    }

    /**
     * Returns a collection view of the values contained in this map.
     *
     * @return a collection view of the values contained in this map.
     */
    @Override
    public Collection<V> values() {
        return base.values();
    }

    /**
     * Returns a set view of the mappings contained in this map.
     *
     * @return a set view of the mappings contained in this map.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<Map.Entry<K,V>> entrySet() {
        if (entrySet == null) {
            entrySet = (Set) new EntrySet(base.entrySet());
        }
        return entrySet;
    }

    /**
     * The key set.
     */
    private final class KeySet extends DerivedSet<BK,K> {
        private static final long serialVersionUID = -2931806200277420177L;

        public KeySet(final Set<BK> base) {
            super(base, keyType);
        }

        protected K baseToDerived(final BK element) {
            return DerivedMap.this.baseToDerived(element);
        }

        protected BK derivedToBase(final K element) {
            return DerivedMap.this.derivedToBase(element);
        }
    }

    /**
     * The entry set.
     */
    private final class EntrySet extends DerivedSet<Map.Entry<BK,V>, Entry<BK,K,V>> {
        private static final long serialVersionUID = -2931806200277420177L;

        @SuppressWarnings("unchecked")
        public EntrySet(final Set<Map.Entry<BK,V>> base) {
            super(base, (Class) Entry.class);
        }

        protected Entry<BK,K,V> baseToDerived(final Map.Entry<BK,V> entry) {
            final K derived = DerivedMap.this.baseToDerived(entry.getKey());
            return derived!=null ? new Entry<BK,K,V>(entry, derived) : null;
        }

        protected Map.Entry<BK,V> derivedToBase(final Entry<BK,K,V> element) {
            return element.entry;
        }
    }

    /**
     * The entry element.
     */
    private static final class Entry<BK,K,V> implements Map.Entry<K,V> {
        public final Map.Entry<BK,V> entry;
        private final K derived;

        public Entry(final Map.Entry<BK,V> entry, final K derived) {
            this.entry   = entry;
            this.derived = derived;
        }

        public K getKey() {
            return derived;
        }

        public V getValue() {
            return entry.getValue();
        }

        public V setValue(V value) {
            return entry.setValue(value);
        }
    }
}
