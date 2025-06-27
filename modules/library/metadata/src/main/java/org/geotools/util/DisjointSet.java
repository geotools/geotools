/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A set which is disjoint from others {@code DisjointSet}s. Two sets are disjoint (or <em>mutually exclusive</em>) if
 * their intersection is the empty set. Adding an element to a {@code DisjointSet} remove it from any other mutually
 * exclusive {@code DisjointSet}. Optionnaly, {@code DisjointSet}s may also have a trash set receiving removed elements.
 * The example below creates 3 mutually exclusive sets with a trash:
 *
 * <blockquote>
 *
 * <pre>
 * DisjointSet set0 = new DisjointSet(true); // Used as the trash set.
 * DisjointSet set1 = new DisjointSet(set0);
 * DisjointSet set2 = new DisjointSet(set0);
 * </pre>
 *
 * </blockquote>
 *
 * Disjoint sets are thread-safe.
 *
 * @param <E> The type of elements in this set.
 * @since 2.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DisjointSet<E> extends AbstractSet<E> implements Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -7933552571588598563L;

    /**
     * The underlying map. {@code add} and {@code remove} operations on this set are translated into {@link Map}
     * operations as below:
     *
     * <p><strong>Adding a new element to this {@link Set}:</strong>
     *
     * <ul>
     *   <li>Puts the corresponding key-value pair in the underlying {@link Map}, where:
     *       <ul>
     *         <li>the key is the element to add;
     *         <li>the value is this {@code DisjointSet}.
     *       </ul>
     *       If an other value was mapped to the key, the old value is discarted. This is equivalents to removing the
     *       element from an other {@code DisjointSet} prior to add it to this set (in other words, moving the element).
     * </ul>
     *
     * <p><strong>Removing an element from this {@link Set}:</strong>
     *
     * <ul>
     *   <li>If the element is not an existing key in the underlying map, nothing is done.
     *   <li>Otherwise, if the element is a key mapping a different value than this {@code DisjointSet}, then the
     *       element is assumed to belongs to an other {@code DisjointSet} and nothing is done.
     *   <li>Otherwise, puts the key-value pair with the {@code trash} value in the underlying {@link Map}. This is
     *       equivalent to moving the element from this set to the "trash" set. Note that if the operation is applied on
     *       the "trash" set itself or if this set doesn't have a trash ({@code trash==null}), then the element is
     *       effectively removed from the underlying map.
     * </ul>
     */
    private final Map<E, DisjointSet<E>> map;

    /** The set where to move removed elements, or {@code null} if there is none. */
    private final DisjointSet<E> trash;

    /**
     * Construct a initially empty set. There is initially no other set mutually exclusive with this one. Mutually
     * exclusive sets must be created using the {@code DisjointSet(DisjointSet)} constructor with this newly created set
     * as argument.
     *
     * <p>{@code DisjointSet}s constructed using this constructor has no trash. All remove operations on this set really
     * remove all references to the removed element, like a usual {@link Set}. This is opposed to moving the element to
     * a "trash" set, which is allowed by the {@code DisjointSet(true)} constructor.
     */
    public DisjointSet() {
        this(false);
    }

    /**
     * Construct a initially empty set with an optional trash set. There is initially no other set mutually exclusive
     * with this one. Mutually exclusive sets must be created using the {@code DisjointSet(DisjointSet)} constructor
     * with this newly created set as argument.
     *
     * @param hasTrash If {@code true}, all {@linkplain #remove remove} operations will add removed elements to a trash
     *     set (thus, really just moving the element to the trash). If {@code false}, there is no trash and this
     *     constructor behave like the no-argument constructor.
     * @see #getTrash
     */
    public DisjointSet(final boolean hasTrash) {
        map = new LinkedHashMap<>();
        trash = hasTrash ? new DisjointSet<>(map) : null;
    }

    /**
     * Construct a new set mutually exclusive with the specified set. All sets mutually exclusive with
     * {@code disjointSet} will also be mutually exclusive with the newly created set. If {@code disjointSet} has a
     * trash set, the newly created set will use the same trash (i.e. all {@code remove} operations will really move the
     * element to the trash set). Otherwise, the new {@code DisjointSet} have no trash.
     *
     * @param disjointSet The set to be disjoint from.
     */
    public DisjointSet(final DisjointSet<E> disjointSet) {
        map = disjointSet.map;
        trash = disjointSet.trash;
    }

    /** Construct a trash set. */
    private DisjointSet(final Map<E, DisjointSet<E>> map) {
        this.map = map;
        trash = null;
    }

    /**
     * Returns the trash set, or {@code null} if there is none. The trash set receive all elements removed from this
     * set.
     *
     * @return The trash set, or {@code null} if none.
     */
    public Set<E> getTrash() {
        return trash;
    }

    /**
     * Returns the number of elements in this set. The size of this set may change as a result of adding elements to a
     * mutually exclusive set.
     */
    @Override
    public int size() {
        synchronized (map) {
            int count = 0;
            for (DisjointSet<E> es : map.values()) {
                if (es == this) {
                    count++;
                }
            }
            return count;
        }
    }

    /**
     * Returns {@code true} if this set contains the specified element.
     *
     * @param element Object to be checked for containment in this set.
     * @return {@code true} if this set contains the specified element.
     */
    @Override
    public boolean contains(final Object element) {
        synchronized (map) {
            return map.get(element) == this;
        }
    }

    /**
     * Ensures that this collection contains the specified element. Adding an element to this set will remove it from
     * any mutually exclusive set.
     *
     * @param element Element whose presence in this set is to be ensured.
     * @return {@code true} if the set changed as a result of the call.
     */
    @Override
    public boolean add(final E element) {
        synchronized (map) {
            return map.put(element, this) != this;
        }
    }

    /**
     * Removes a single instance of the specified element from this set, if it is present. If this {@code DisjointSet}
     * has a trash set, the removed element will be added to the trash set.
     *
     * @param element Element to be removed from this set.
     * @return {@code true} if the set changed as a result of the call.
     */
    @Override
    public boolean remove(final Object element) {
        synchronized (map) {
            if (map.get(element) != this) {
                return false; // The element do not belongs to this set.
            } else if (trash != null) {
                // Do not remove. Move it to the "trash" set.
                @SuppressWarnings("unchecked")
                final DisjointSet<E> old = map.put((E) element, trash);
                return old != trash;
            } else {
                // Completly remove the element from the set.
                return map.remove(element) != null;
            }
        }
    }

    /**
     * Returns {@code true} if this set contains all of the elements in the specified collection.
     *
     * @param c collection to be checked for containment in this collection.
     * @return {@code true} if this set contains all of the elements in the specified collection.
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
        synchronized (map) {
            return super.containsAll(c);
        }
    }

    /**
     * Adds all of the elements in the specified collection to this set. All of the elements will be removed from
     * mutually exclusive sets.
     *
     * @param c collection whose elements are to be added to this set.
     * @return {@code true} if this set changed as a result of the call.
     */
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        synchronized (map) {
            return super.addAll(c);
        }
    }

    /**
     * Removes from this set all of its elements that are contained in the specified collection. If this
     * {@code DisjointSet} has a trash set, all removed elements will be added to the trash set.
     *
     * @param c elements to be removed from this set.
     * @return {@code true} if this set changed as a result of the call.
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        synchronized (map) {
            return super.removeAll(c);
        }
    }

    /**
     * Retains only the elements in this set that are contained in the specified collection. If this {@code DisjointSet}
     * has a trash set, all removed elements will be added to the trash set.
     *
     * @param c elements to be retained in this collection.
     * @return {@code true} if this collection changed as a result of the call.
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        synchronized (map) {
            return super.retainAll(c);
        }
    }

    /**
     * Removes all of the elements from this set. If this {@code DisjointSet} has a trash set, all removed elements will
     * be added to the trash set.
     */
    @Override
    public void clear() {
        synchronized (map) {
            super.clear();
        }
    }

    /** Returns an iterator over the elements in this collection. */
    @Override
    public Iterator<E> iterator() {
        synchronized (map) {
            return new Iter();
        }
    }

    /**
     * Returns an array containing all of the elements in this collection.
     *
     * @return an array containing all of the elements in this set.
     */
    @Override
    public Object[] toArray() {
        synchronized (map) {
            return super.toArray();
        }
    }

    /**
     * Returns an array containing all of the elements in this collection.
     *
     * @param <T> The type of elements in the array.
     * @param a The array into which the elements of the set are to be stored, if it is big enough; otherwise, a new
     *     array of the same runtime type is allocated for this purpose.
     * @return an array containing the elements of the set.
     */
    @Override
    public <T> T[] toArray(final T[] a) {
        synchronized (map) {
            return super.toArray(a);
        }
    }

    /** Returns a string representation of this set. */
    @Override
    public String toString() {
        synchronized (map) {
            return super.toString();
        }
    }

    /** Returns an hash value for this set. */
    @Override
    public int hashCode() {
        synchronized (map) {
            return super.hashCode();
        }
    }

    /**
     * Compare this set with the specified object for equality.
     *
     * @param set The object to compare with this set for equality.
     */
    @Override
    public boolean equals(final Object set) {
        synchronized (map) {
            return super.equals(set);
        }
    }

    /**
     * The iterator for {@link DisjointSet}.
     *
     * @version 1.0
     * @author Martin Desruisseaux (IRD)
     */
    private final class Iter implements Iterator<E> {
        /** The iterator over the entries of the underlying {@link Map} object. */
        private final Iterator<Map.Entry<E, DisjointSet<E>>> iterator;

        /** Entry for the next element to return, or {@code null} if there is no more element. */
        private Map.Entry<E, DisjointSet<E>> prefetch;

        /**
         * Entry to remove if the {@link #remove} operation is invoked, or {@code null} if this iterator is not in a
         * legal state for element removing. An element can be removed only after a {@link #next} call and only if this
         * call still synchronized with the {@link Iterator#next} call on the underlying map's iterator.
         */
        private Map.Entry<E, DisjointSet<E>> toRemove;

        /** Construct a new iterator. */
        private Iter() {
            iterator = map.entrySet().iterator();
        }

        /**
         * Returns the {@link #prefetch} entry. If this entry was {@code null}, fetch the next entry. If there is no
         * more entries, returns {@code null}.
         */
        private Map.Entry<E, DisjointSet<E>> prefetch() {
            toRemove = null;
            if (prefetch == null) {
                while (iterator.hasNext()) {
                    final Map.Entry<E, DisjointSet<E>> next = iterator.next();
                    if (next.getValue() == DisjointSet.this) {
                        prefetch = next;
                        break;
                    }
                }
            }
            return prefetch;
        }

        /** Returns {@code true} if the iteration has more elements. */
        @Override
        public boolean hasNext() {
            return prefetch() != null;
        }

        /** Returns the next element in the iteration. */
        @Override
        public E next() {
            toRemove = prefetch();
            prefetch = null;
            if (toRemove != null) {
                return toRemove.getKey();
            } else {
                throw new NoSuchElementException();
            }
        }

        /** Removes from the underlying set the last element returned by the iterator. */
        @Override
        public void remove() {
            if (toRemove != null) {
                if (trash != null) {
                    // Move to the trash set.
                    toRemove.setValue(trash);
                } else {
                    iterator.remove();
                }
                toRemove = null;
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
