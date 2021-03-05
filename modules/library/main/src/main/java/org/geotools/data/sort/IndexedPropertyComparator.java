/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.feature.simple.SimpleFeature;

/**
 * Compares two feature based on an attribute value
 *
 * @author Andrea Aime - GeoSolutions
 */
class IndexedPropertyComparator extends AbstractPropertyComparator {

    int idx;

    /**
     * Builds a new comparator
     *
     * @param idx the property index to use
     * @param ascending If true the comparator will force an ascending order (descending otherwise)
     */
    public IndexedPropertyComparator(int idx, boolean ascending) {
        super(ascending);
        this.idx = idx;
    }

    @Override
    protected int compareAscending(SimpleFeature f1, SimpleFeature f2) {
        Comparable o1 = (Comparable) f1.getAttribute(idx);
        Comparable o2 = (Comparable) f2.getAttribute(idx);

        return compareAscending(o1, o2);
    }
}
