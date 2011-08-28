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
package org.geotools.data.aggregate.sort;

import java.util.Comparator;

import org.opengis.feature.simple.SimpleFeature;

/**
 * Compares two feature based on an attribute value
 * 
 * @author Andrea Aime - GeoSolutions
 */
class PropertyComparator implements Comparator<SimpleFeature> {

    String propertyName;

    boolean ascending;

    /**
     * Builds a new comparator
     * 
     * @param propertyName The property name to be used
     * @param inverse If true the comparator will force an ascending order (descending otherwise)
     */
    public PropertyComparator(String propertyName, boolean ascending) {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }

    @Override
    public int compare(SimpleFeature f1, SimpleFeature f2) {
        int result = compareAscending(f1, f2);
        if (ascending) {
            return result;
        } else {
            return result * -1;
        }
    }

    private int compareAscending(SimpleFeature f1, SimpleFeature f2) {
        Comparable o1 = (Comparable) f1.getAttribute(propertyName);
        Comparable o2 = (Comparable) f2.getAttribute(propertyName);

        if (o1 == null) {
            if (o2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (o2 == null) {
            return 1;
        } else {
            return o1.compareTo(o2);
        }
    }

}
