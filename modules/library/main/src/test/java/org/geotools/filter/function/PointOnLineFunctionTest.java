/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionFinder;
import org.junit.Test;
import org.locationtech.jts.geom.Point;

public class PointOnLineFunctionTest {

    public static final double EPS = 1e-6;
    public static final String NAME = "pointOnLine";
    public static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testLookup() {
        Function f = new FunctionFinder(null).findFunction(NAME);
        assertNotNull(f);
    }

    @Test
    public void testNotLine() {
        FilterFactory FF = CommonFactoryFinder.getFilterFactory();
        Function pointOnLine = FF.function(NAME, FF.literal("POINT(0 0)"));
        assertThrows(IllegalArgumentException.class, () -> pointOnLine.evaluate(null));
    }

    @Test
    public void testInvalidPercentageAbove() {
        Function pointOnLine =
                FF.function(NAME, FF.literal("LINESTRING(0 0, 0 1, 1 1)"), FF.literal(1.5));
        assertThrows(IllegalArgumentException.class, () -> pointOnLine.evaluate(null));
    }

    @Test
    public void testInvalidPercentageBelow() {
        Function pointOnLine =
                FF.function(NAME, FF.literal("LINESTRING(0 0, 0 1, 1 1)"), FF.literal(-0.1));
        assertThrows(IllegalArgumentException.class, () -> pointOnLine.evaluate(null));
    }

    @Test
    public void testInvalidMultiLineString() {
        Function pointOnLine =
                FF.function(NAME, FF.literal("MULTILINESTRING((0 0, 1 1), (2 2, 3 3))"));
        assertThrows(IllegalArgumentException.class, () -> pointOnLine.evaluate(null));
    }

    @Test
    public void testMidPoint() {
        Function pointOnLine = FF.function(NAME, FF.literal("LINESTRING(0 0, 0 1, 1 1)"));
        Point mid = (Point) pointOnLine.evaluate(null);
        assertNotNull(mid);
        assertEquals(0, mid.getX(), EPS);
        assertEquals(1, mid.getY(), EPS);
    }

    @Test
    public void testValidMultiLineString() {
        Function pointOnLine = FF.function(NAME, FF.literal("MULTILINESTRING((0 0, 1 1))"));
        Point mid = (Point) pointOnLine.evaluate(null);
        assertNotNull(mid);
        assertEquals(0.5, mid.getX(), EPS);
        assertEquals(0.5, mid.getY(), EPS);
    }

    @Test
    public void testOtherPositions() {
        // Test with various percentages
        double[] percentages = {0.0, 0.25, 0.5, 0.75, 1.0};
        for (double percentage : percentages) {
            Function pointOnLine =
                    FF.function(
                            "pointOnLine",
                            FF.literal("LINESTRING(0 0, 3 3)"),
                            FF.literal(percentage));
            Point point = (Point) pointOnLine.evaluate(null);
            assertNotNull(point);

            // Calculate the expected position
            double expectedX = 3 * percentage;
            double expectedY = 3 * percentage;

            // Check that the point's position is correct
            assertEquals(expectedX, point.getX(), EPS);
            assertEquals(expectedY, point.getY(), EPS);
        }
    }
}
