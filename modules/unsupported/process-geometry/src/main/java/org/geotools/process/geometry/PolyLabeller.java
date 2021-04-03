/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.geometry;

import org.geotools.geometry.jts.GeometryBuilder;
import org.locationtech.jts.algorithm.construct.MaximumInscribedCircle;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * Formerly, based on Vladimir Agafonkin's Algorithm https://www.mapbox.com/blog/polygon-center/
 *
 * @author Ian Turton
 * @author Casper Børgesen
 */
public class PolyLabeller {

    static GeometryBuilder GB = new GeometryBuilder();

    static Geometry getPolylabel(Geometry polygon, double precision) {
        if (polygon == null) {
            return null;
        }
        MultiPolygon multiPolygon;
        if (polygon instanceof Polygon) {
            multiPolygon = GB.multiPolygon((Polygon) polygon);
        } else if (polygon instanceof MultiPolygon) {
            multiPolygon = (MultiPolygon) polygon;
        } else {
            throw new IllegalStateException("Input polygon must be a Polygon or MultiPolygon");
        }

        if (polygon.isEmpty() || polygon.getArea() <= 0.0) {
            return null;
        }

        return MaximumInscribedCircle.getCenter(multiPolygon, precision);
    }
}
