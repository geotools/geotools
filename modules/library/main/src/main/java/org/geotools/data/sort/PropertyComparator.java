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

import org.opengis.feature.simple.SimpleFeature;

/**
 * Compares two feature based on an attribute value
 *
 * @author Andrea Aime - GeoSolutions
 */
class PropertyComparator extends AbstractPropertyComparator {

    String propertyName;

    /**
     * Builds a new comparator
     *
     * @param propertyName The property name to be used
     * @param ascending If true the comparator will force an ascending order (descending otherwise)
     */
    public PropertyComparator(String propertyName, boolean ascending) {
        super(ascending);
        this.propertyName = propertyName;
    }

    @Override
    protected int compareAscending(SimpleFeature f1, SimpleFeature f2) {
        Comparable o1 = (Comparable) f1.getAttribute(propertyName);
        Comparable o2 = (Comparable) f2.getAttribute(propertyName);

        return compareAscending(o1, o2);
    }
}
