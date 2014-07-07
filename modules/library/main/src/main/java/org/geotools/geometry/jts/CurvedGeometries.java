/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import java.util.concurrent.atomic.AtomicBoolean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryComponentFilter;

/**
 * Utility methods for curved geometries
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class CurvedGeometries {

    /**
     * Returns true if the geometry is curved, or contains elements that are curved
     * 
     * @param geometry
     * @return
     */
    public static boolean isCurved(Geometry geometry) {
        if (geometry instanceof CurvedGeometry<?>) {
            return true;
        }

        final AtomicBoolean curveFound = new AtomicBoolean(false);
        geometry.apply(new GeometryComponentFilter() {

            @Override
            public void filter(Geometry geom) {
                if (geom instanceof CurvedGeometry<?>) {
                    curveFound.set(true);
                }

            }
        });

        return curveFound.get();
    }

}
