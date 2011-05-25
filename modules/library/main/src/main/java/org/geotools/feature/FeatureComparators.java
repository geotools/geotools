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
package org.geotools.feature;

import org.opengis.feature.simple.SimpleFeature;

/**
 * A utility class for creating simple Comparators for Features.
 *
 * @author Ian Schneider
 *
 * @source $URL$
 */
public final class FeatureComparators {
    /** A utility comparator for comparison by id. */
    public static final java.util.Comparator BY_ID = new java.util.Comparator() {
            public int compare(Object o1, Object o2) {
                SimpleFeature f1 = (SimpleFeature) o1;
                SimpleFeature f2 = (SimpleFeature) o2;

                return f1.getID().compareTo(f2.getID());
            }
        };

    /**
     * Private constructor so default constructor is not available for this
     * utility class.
     */
    private FeatureComparators() {
    }

    /**
     * Create a Comparator which compares Features by the attribute at the
     * given index. The attribute at the index MUST be Comparable. This will
     * probably not work for heterogenous collections, UNLESS the classes at
     * the given index are the same.
     *
     * @param idx The index to look up attributes at.
     *
     * @return A new Comparator.
     */
    public static java.util.Comparator byAttributeIndex(final int idx) {
        return new Index(idx);
    }

    /**
     * Create a Comparator which compares Features by the attribute found at
     * the given path. The attribute found MUST be Comparable. This will
     * probably not work for heterogenous collections, UNLESS the attributes
     * found are the same class.
     *
     * @param name The xpath to use while comparing.
     *
     * @return A new Comparator.
     */
    public static java.util.Comparator byAttributeName(final String name) {
        return new Name(name);
    }

    /**
     * A Comparator which performs the comparison on attributes at a given
     * index.
     */
    public static class Index implements java.util.Comparator {
        /** the index of the attribute to compare against. */
        private final int idx;

        /**
         * Create a new Comparator based on the given index.
         *
         * @param i The index.
         */
        public Index(int i) {
            idx = i;
        }

        /**
         * Implementation of Comparator. Calls compareAtts to perform the
         * actual comparison.
         *
         * @param o1 The first Feature.
         * @param o2 The second Feature
         *
         * @return A value indicating less than, equal, or greater than.
         */
        public int compare(Object o1, Object o2) {
            SimpleFeature f1 = (SimpleFeature) o1;
            SimpleFeature f2 = (SimpleFeature) o2;

            return compareAtts(f1.getAttribute(idx), f2.getAttribute(idx));
        }

        /**
         * Compares the two attributes.
         *
         * @param att1 The first attribute to compare.
         * @param att2 The second attribute to compare.
         *
         * @return A value indicating less than, equal, or greater than.
         */
        protected int compareAtts(Object att1, Object att2) {
            return ((Comparable) att1).compareTo((Comparable) att2);
        }
    }

    /**
     * A Comparator which performs the comparison on attributes with a given
     * name.
     */
    public static class Name implements java.util.Comparator {
        /** The name to compare on */
        private final String name;

        /**
         * Create a new Comparator based on the given index.
         *
         * @param name The attribute name.
         */
        public Name(String name) {
            this.name = name;
        }

        /**
         * Implementation of Comparator. Calls compareAtts to perform the
         * actual comparison.
         *
         * @param o1 The first Feature.
         * @param o2 The second Feature
         *
         * @return A value indicating less than, equal, or greater than.
         */
        public int compare(Object o1, Object o2) {
            SimpleFeature f1 = (SimpleFeature) o1;
            SimpleFeature f2 = (SimpleFeature) o2;

            return compareAtts(f1.getAttribute(name), f2.getAttribute(name));
        }

        /**
         * Compares the two attributes.
         *
         * @param att1 The first attribute to compare.
         * @param att2 The second attribute to compare.
         *
         * @return A value indicating less than, equal, or greater than.
         */
        protected int compareAtts(Object att1, Object att2) {
            if ((att1 == null) && (att2 == null)) {
                return 0;
            }
            
            if (att1 == null) {
                return -1;
            }
            
            if (att2 == null) {
                return 1;
            }                        

            return ((Comparable) att1).compareTo((Comparable) att2);
        }
    }
}
