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
package org.geotools.data.shapefile.indexed;

import java.util.Comparator;

import org.geotools.index.Data;

/**
 * A Comparator for sortin search results in ascending record number. So we can
 * read dbf & shape file forward only
 * 
 * @author Tommaso Nolli
 *
 * @source $URL$
 */
public class DataComparator implements Comparator {
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        Integer i1 = (Integer) ((Data) o1).getValue(0);
        Integer i2 = (Integer) ((Data) o2).getValue(0);

        return i1.compareTo(i2);
    }
}
