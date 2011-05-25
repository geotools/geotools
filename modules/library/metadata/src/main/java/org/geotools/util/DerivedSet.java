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
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A set whose values are derived from an other set. The values are derived only when
 * requested, which make it possible to backup potentially large sets. Implementations
 * need only to overrides {@link #baseToDerived} and {@link #derivedToBase} methods.
 * This set do not supports {@code null} value, since {@code null} is used
 * when no mapping from {@linkplain #base} to {@code this} exists.
 * This class is serializable if the underlying {@linkplain #base} set is serializable
 * too.
 * <p>
 * This class is <strong>not</strong> thread-safe. Synchronizations (if wanted) are user's
 * reponsability.
 *
 * @param <B> The type of elements in the backing set.
 * @param <E> The type of elements in this set.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class DerivedSet<B,E> extends AbstractSet<E>
        implements CheckedCollection<E>, Serializable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -4662336508586424581L;

    /**
     * The base set whose values are derived from.
     *
     * @see #baseToDerived
     * @see #derivedToBase
     */
    protected final Set<B> base;

    /**
     * The derived type.
     */
    private final Class<E> derivedType;

    /**
     * Creates a new derived set from the specified base set.
     *
     * @param base The base set.
     *
     * @deprecated Use {@link #DerivedSet(Set,Class)} instead.
     */
    @SuppressWarnings("unchecked")
    public DerivedSet(final Set<B> base) {
        this(base, (Class) Object.class);
    }

    /**
     * Creates a new derived set from the specified base set.
     *
     * @param base The base set.
     * @param derivedType The type of elements in this derived set.
     *
     * @since 2.5
     */
    public DerivedSet(final Set<B> base, final Class<E> derivedType) {
        this.base        = base;
        this.derivedType = derivedType;
    }

    /**
     * Returns the derived element type.
     *
     * @since 2.5
     */
    public Class<E> getElementType() {
        return derivedType;
    }

    /**
     * Transforms a value in the {@linkplain #base} set to a value in this set.
     * If there is no mapping in the derived set for the specified element,
     * then this method returns {@code null}.
     *
     * @param  element A value in the {@linkplain #base} set.
     * @return The value that this view should contains instead of {@code element},
     *         or {@code null}.
     */
    protected abstract E baseToDerived(final B element);

    /**
     * Transforms a value in this set to a value in the {@linkplain #base} set.
     *
     * @param  element A value in this set.
     * @return The value stored in the {@linkplain #base} set.
     */
    protected abstract B derivedToBase(final E element);

    /**
     * Returns an iterator over the elements contained in this set.
     * The iterator will invokes {@link #baseToDerived} for each element.
     *
     * @return an iterator over the elements contained in this set.
     */
    public Iterator<E> iterator() {
        return new Iter(base.iterator());
    }

    /**
     * Returns the number of elements in this set. The default implementation counts
     * the number of elements returned by the {@link #iterator iterator}.
     *
     * @return the number of elements in this set.
     */
    public int size() {
        int count = 0;
        for (final Iterator it=iterator(); it.hasNext();) {
            it.next();
            count++;
        }
        return count;
    }

    /**
     * Returns {@code true} if this set contains no elements.
     *
     * @return {@code true} if this set contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return base.isEmpty() || super.isEmpty();
    }

    /**
     * Returns {@code true} if this set contains the specified element.
     * The default implementation invokes
     * <code>{@linkplain #base}.contains({@linkplain #derivedToBase derivedToBase}(element))</code>.
     *
     * @param  element object to be checked for containment in this set.
     * @return {@code true} if this set contains the specified element.
     */
    @Override
    public boolean contains(final Object element) {
        if (derivedType.isInstance(element)) {
            return base.contains(derivedToBase(derivedType.cast(element)));
        } else {
            return false;
        }
    }

    /**
     * Ensures that this set contains the specified element.
     * The default implementation invokes
     * <code>{@linkplain #base}.add({@linkplain #derivedToBase derivedToBase}(element))</code>.
     *
     * @param  element element whose presence in this set is to be ensured.
     * @return {@code true} if the set changed as a result of the call.
     * @throws UnsupportedOperationException if the {@linkplain #base} set doesn't
     *         supports the {@code add} operation.
     */
    @Override
    public boolean add(final E element) throws UnsupportedOperationException {
        return base.add(derivedToBase(element));
    }

    /**
     * Removes a single instance of the specified element from this set.
     * The default implementation invokes
     * <code>{@linkplain #base}.remove({@linkplain #derivedToBase derivedToBase}(element))</code>.
     *
     * @param  element element to be removed from this set, if present.
     * @return {@code true} if the set contained the specified element.
     * @throws UnsupportedOperationException if the {@linkplain #base} set doesn't
     *         supports the {@code remove} operation.
     */
    @Override
    public boolean remove(final Object element) throws UnsupportedOperationException {
        if (derivedType.isInstance(element)) {
            return base.remove(derivedToBase(derivedType.cast(element)));
        } else {
            return false;
        }
    }

    /**
     * Iterates through the elements in the set.
     */
    private final class Iter implements Iterator<E> {
        /**
         * The iterator from the {@linkplain #base} set.
         */
        private final Iterator<B> iterator;

        /**
         * The next element to be returned, or {@code null}.
         */
        private transient E next;

        /**
         * The iterator from the {@linkplain #base} set.
         */
        public Iter(final Iterator<B> iterator) {
            this.iterator = iterator;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         */
        public boolean hasNext() {
            while (next == null) {
                if (!iterator.hasNext()) {
                    return false;
                }
                next = baseToDerived(iterator.next());
            }
            return true;
        }

        /**
         * Returns the next element in the iteration.
         */
        public E next() {
            while (next == null) {
                next = baseToDerived(iterator.next());
            }
            final E value = next;
            next = null;
            return value;
        }

        /**
         * Removes from the underlying set the last element returned by the iterator.
         *
         * @throws UnsupportedOperationException if the {@linkplain #base} set doesn't
         *         supports the {@code remove} operation.
         */
        public void remove() {
            iterator.remove();
        }
    }
}
