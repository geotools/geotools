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

import java.util.List;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Collection;
import java.util.Comparator;
import java.io.Serializable;


/**
 * General purpose comparators.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class Comparators {
    /**
     * Do not allows instantiation of this class.
     */
    private Comparators() {
    }

    /**
     * The comparator to be returned by {@link #forLists} and {@linkplain #forSet}. Can not be
     * public because of parameterized types - we need a method for casting to the expected type.
     * This is the same trick than {@link java.util.Collections#emptySet} for example.
     */
    private static final Comparator<Collection<Comparable>> COLLECTIONS = new Collections();

    /**
     * The {@link #COLLECTIONS} classes explicitly named (rather than anonymous) for avoiding
     * serialization issues.
     */
    private static final class Collections implements Comparator<Collection<Comparable>>, Serializable {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -8926770873102046405L;

        /**
         * Compares to collections of comparable objects.
         */
        public int compare(final Collection<Comparable> c1, final Collection<Comparable> c2) {
            final Iterator<Comparable> i1 = c1.iterator();
            final Iterator<Comparable> i2 = c2.iterator();
            int c;
            do {
                final boolean h1 = i1.hasNext();
                final boolean h2 = i2.hasNext();
                if (!h1) return h2 ? -1 : 0;
                if (!h2) return +1;
                final Comparable e1 = i1.next();
                final Comparable e2 = i2.next();
                @SuppressWarnings("unchecked")
                final int cmp = e1.compareTo(e2);
                c = cmp;
            } while (c == 0);
            return c;
        }
    };

    /**
     * Returns a comparator for lists of comparable elements. The first element of each list
     * are {@linkplain Comparable#compareTo compared}. If one is <cite>greater than</cite> or
     * <cite>less than</cite> the other, the result of that comparaison is returned. Otherwise
     * the second element are compared, and so on until either non-equal elements are found,
     * or end-of-list are reached. In the later case, the shortest list is considered
     * <cite>less than</cite> the longuest one.
     * <p>
     * If both lists have the same length and equal elements in the sense of
     * {@link Comparable#compareTo}, then the comparator returns 0.
     *
     * @param <T> The type of elements in both lists.
     * @return The ordering between two lists.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Comparator<List<T>> forLists() {
        return (Comparator) COLLECTIONS;
    }

    /**
     * Returns a comparator for sorted sets of comparable elements. The elements are compared in
     * iteration order as {@linkplain #forLists for lists}.
     *
     * @param <T> The type of elements in both sets.
     * @return The ordering between two sets.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Comparator<SortedSet<T>> forSortedSets() {
        return (Comparator) COLLECTIONS;
    }

    /**
     * Returns a comparator for arbitrary collections of comparable elements. The elements are
     * compared in iteration order as {@linkplain #forLists for lists}. <strong>This comparator
     * make sense only for collections having determinist order</strong> like
     * {@link java.util.TreeSet}, {@link java.util.LinkedHashSet} or queues.
     * Do <strong>not</strong> use it with {@link java.util.HashSet}.
     *
     * @param <T> The type of elements in both collections.
     * @return The ordering between two collections.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Comparator<Collection<T>> forCollections() {
        return (Comparator) COLLECTIONS;
    }
}
