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
package org.geotools.gml2.bindings;

import com.vividsolutions.jts.geom.CoordinateSequence;


public class GMLUtil {
    /**
     * Determines the dimension of a coordinate sequence. This is based off of
     * the first coordinate in the sequence.
     *
     * @param seq The coordinate sequence in question.
     *
     * @return The best guess at a dimension, -1 if it can not be determined.
     */
    public static int getDimension(CoordinateSequence seq) {
        int dimension = 0;

        if (seq.size() == 0) {
            return -1;
        }

        for (; dimension < 100; dimension++) {
            try {
                double d = seq.getOrdinate(0, dimension);

                if (Double.isNaN(d)) {
                    return dimension;
                }
            } catch (Throwable t) {
                return dimension;
            }
        }

        //whoever has a coordinate with more then 100 dimensions is not sane
        return -1;
    }
}
