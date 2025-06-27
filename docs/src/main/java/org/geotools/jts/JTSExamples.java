/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.jts;

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Coordinate;

public class JTSExamples {

    void orthodromicDistance() throws Exception {

        Coordinate start = null;
        Coordinate end = null;
        CoordinateReferenceSystem crs = null;

        // orthodromicDistance start
        double distance = JTS.orthodromicDistance(start, end, crs);
        int totalmeters = (int) distance;
        int km = totalmeters / 1000;
        int meters = totalmeters - km * 1000;
        float remaining_cm = (float) (distance - totalmeters) * 10000;
        remaining_cm = Math.round(remaining_cm);
        float cm = remaining_cm / 100;

        System.out.println("Distance = " + km + "km " + meters + "m " + cm + "cm");
        // orthodromicDistance end
    }
}
