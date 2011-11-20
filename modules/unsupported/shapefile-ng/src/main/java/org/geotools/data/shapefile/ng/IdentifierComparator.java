/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.ng;

import java.util.Comparator;

import org.opengis.filter.identity.Identifier;

/**
 * Compares two filter identifiers
 * 
 * @author Andrea Aime - GeoSolutions
 */
class IdentifierComparator implements Comparator<Identifier> {

    String prefix;

    public IdentifierComparator(String typeName) {
        this.prefix = typeName + ".";
    }

    public int compare(Identifier o1, Identifier o2) {
        String s1 = o1.toString();
        String s2 = o2.toString();
        if (s1.startsWith(prefix) && s2.startsWith(prefix)) {
            try {
                int i1 = Integer.parseInt(s1.substring(prefix.length()));
                int i2 = Integer.parseInt(s2.substring(prefix.length()));
                return i1 - i2;
            } catch (NumberFormatException e) {
                // it's ok, we want to fall back to string comparison
            }
        }
        return s1.compareTo(s2);
    }
}