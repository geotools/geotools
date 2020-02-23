/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * Sort key used to find the next feature to draw in a cross-layer z-ordering setup
 *
 * @author Andrea Aime - GeoSolutions
 */
class SortKey {

    private static java.util.Comparator<Comparable> FORWARD_COMPARATOR =
            new java.util.Comparator<Comparable>() {

                @Override
                public int compare(Comparable o1, Comparable o2) {
                    return o1.compareTo(o2);
                }
            };

    private static java.util.Comparator<Comparable> REVERSE_COMPARATOR =
            new java.util.Comparator<Comparable>() {

                @Override
                public int compare(Comparable o1, Comparable o2) {
                    return -FORWARD_COMPARATOR.compare(o1, o2);
                }
            };

    Object[] components;

    public SortKey(int length) {
        this.components = new Object[length];
    }

    public SortKey(SortKey other) {
        this(other.components.length);
        copy(other);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(components);
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SortKey other = (SortKey) obj;
        if (!Arrays.equals(components, other.components)) return false;
        return true;
    }

    static class Comparator implements java.util.Comparator<SortKey> {

        java.util.Comparator<Object>[] comparators;

        public Comparator(java.util.Comparator<Object>[] componentComparators) {
            this.comparators = componentComparators;
        }

        @Override
        public int compare(SortKey sk1, SortKey sk2) {
            for (int i = 0; i < comparators.length; i++) {
                int compare = comparators[i].compare(sk1.components[i], sk2.components[i]);
                if (compare != 0) {
                    return compare;
                }
            }

            return 0;
        }
    }

    /** Copies from another SortKey */
    public void copy(SortKey reference) {
        for (int i = 0; i < components.length; i++) {
            components[i] = reference.components[i];
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SortKey [components=" + Arrays.toString(components) + "]";
    }

    /** Builds a SortKey Comparator from a SortBy array */
    static Comparator buildComparator(SortBy[] sortBy) {
        // sanity check
        if (sortBy == SortBy.UNSORTED || sortBy == null) {
            throw new IllegalArgumentException("Expected to get a sort, but found none");
        }

        // build a list of comparators
        List<java.util.Comparator<?>> comparators = new ArrayList<java.util.Comparator<?>>();
        for (SortBy sb : sortBy) {
            if (sb.getSortOrder() == SortOrder.ASCENDING) {
                comparators.add(FORWARD_COMPARATOR);
            } else {
                comparators.add(REVERSE_COMPARATOR);
            }
        }

        return new Comparator(comparators.toArray(new java.util.Comparator[comparators.size()]));
    }
}
