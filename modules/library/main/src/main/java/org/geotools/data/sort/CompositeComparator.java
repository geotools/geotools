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
import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

/**
 * A composite comparator that applies the provided comparators as a hierarchical list, the first
 * comparator that returns a non zero value "wins"
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
class CompositeComparator implements Comparator<SimpleFeature> {

    List<Comparator<SimpleFeature>> comparators;

    public CompositeComparator(List<Comparator<SimpleFeature>> comparators) {
        this.comparators = comparators;
    }

    public int compare(SimpleFeature f1, SimpleFeature f2) {
        for (Comparator<SimpleFeature> comp : comparators) {
            int result = comp.compare(f1, f2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

}
