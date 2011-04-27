package org.geotools.jts;

import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;

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
