/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2021, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Utility class for JTS geometries. Allows retrieval and manipulation of CRS. */
public class GeometryUtil {

    /**
     * Get the CRS of a geometry.
     *
     * @param geometry the geometry
     * @return the CRS
     */
    public static CoordinateReferenceSystem getCRS(Geometry geometry) {
        Object userData = geometry.getUserData();
        if (userData != null && userData instanceof CoordinateReferenceSystem) {
            return (CoordinateReferenceSystem) userData;
        } else if (userData instanceof Map) {
            Map<?, ?> userDataMap = (Map<?, ?>) userData;
            CoordinateReferenceSystem crs =
                    (CoordinateReferenceSystem) userDataMap.get(CoordinateReferenceSystem.class);
            if (crs != null) {
                return crs;
            }
        }
        return null;
    }

    /**
     * Set the CRS of a geometry.
     *
     * @param geometry the geometry
     * @param crs the CRS
     */
    public static void setCRS(Geometry geometry, CoordinateReferenceSystem crs) {
        Object obj = geometry.getUserData();
        Map<Object, Object> userData = new HashMap<Object, Object>();
        if (obj != null && obj instanceof Map) {
            userData.putAll((Map<?, ?>) obj);
        }
        userData.put(CoordinateReferenceSystem.class, crs);
        geometry.setUserData(userData);
    }
}
