/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.jts;

import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class JTSExamples {

    void orthodromicDistance() throws Exception {

        Coordinate start = null;
        Coordinate end = null;
        CoordinateReferenceSystem crs = null;

        // orthodromicDistance start
        double distance = JTS.orthodromicDistance(start, end, crs);
        int totalmeters = (int) distance;
        int km = totalmeters / 1000;
        int meters = totalmeters - (km * 1000);
        float remaining_cm = (float) (distance - totalmeters) * 10000;
        remaining_cm = Math.round(remaining_cm);
        float cm = remaining_cm / 100;

        System.out.println("Distance = " + km + "km " + meters + "m " + cm + "cm");
        // orthodromicDistance end
    }
}
