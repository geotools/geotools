/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.function;

import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class MidPointFunctionTest {

    @Test
    public void testMidPointSimple() throws Exception {
        // Create a LineString with two points
        LineString line = (LineString) new WKTReader().read("LINESTRING(0 0, 10 10)");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function midPointFunction = ff.function("midPoint", ff.literal(line));
        // Evaluate the function with the LineString
        Point midPoint = (Point) midPointFunction.evaluate(line);

        assertMidPoint(midPoint, new Coordinate(5, 5));
    }

    private static void assertMidPoint(Point midPoint, Coordinate expected) {
        assertTrue("Unexpected value: " + midPoint, expected.equals2D(midPoint.getCoordinate(), 1e-6));
    }

    @Test
    public void testMidPointComplex() throws Exception {
        // Create a LineString with two points
        LineString line = (LineString) new WKTReader().read("LINESTRING(0 0, 0 10, 10 10, 20 10, 20 0)");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function midPointFunction = ff.function("midPoint", ff.literal(line));
        // Evaluate the function with the LineString
        Point midPoint = (Point) midPointFunction.evaluate(line);

        assertMidPoint(midPoint, new Coordinate(10, 10));
    }

    @Test
    public void testMidPointOnPoint() throws Exception {
        // odd enough, there is a converter from Point to LineString
        Point point = (Point) new WKTReader().read("POINT(5 3)");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function midPointFunction = ff.function("midPoint", ff.literal(point));
        Point midPoint = (Point) midPointFunction.evaluate(point);
        assertMidPoint(midPoint, new Coordinate(5, 3));
    }

    @Test
    public void testMidPointOnPolygon() throws Exception {
        // odd enough, there is a converter from Polygon to LineString
        Polygon polygon = (Polygon) new WKTReader().read("POLYGON((0 0, 5 0, 5 5, 0 5, 0 0))");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function midPointFunction = ff.function("midPoint", ff.literal(polygon));
        Point midPoint = (Point) midPointFunction.evaluate(polygon);
        assertMidPoint(midPoint, new Coordinate(5, 5));
    }
}
