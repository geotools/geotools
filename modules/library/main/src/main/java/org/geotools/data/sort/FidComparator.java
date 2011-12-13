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
package org.geotools.data.sort;

import java.util.Comparator;

import org.opengis.feature.simple.SimpleFeature;

/**
 * Compares two feature based on their feature id
 * 
 * @author Andrea Aime - GeoSolutions
 */
class FidComparator implements Comparator<SimpleFeature> {

    boolean ascending;

    /**
     * Builds a new comparator
     * 
     * @param inverse If true the comparator will force an ascending order (descending otherwise)
     */
    public FidComparator(boolean ascending) {
        this.ascending = ascending;
    }

    public int compare(SimpleFeature f1, SimpleFeature f2) {
        int result = compareAscending(f1, f2);
        if (ascending) {
            return result;
        } else {
            return result * -1;
        }
    }

    private int compareAscending(SimpleFeature f1, SimpleFeature f2) {
        String id1 = f1.getID();
        String id2 = f2.getID();

        if (id1 == null) {
            if (id2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (id2 == null) {
            return 1;
        } else {
            return id1.compareTo(id2);
        }
    }

}
