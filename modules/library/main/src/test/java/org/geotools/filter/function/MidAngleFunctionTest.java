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

import static org.junit.Assert.assertEquals;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTReader;

public class MidAngleFunctionTest {

    @Test
    public void testMidAngleSimple() throws Exception {
        // Create a LineString with two points
        LineString line = (LineString) new WKTReader().read("LINESTRING(0 0, 10 10)");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function midAngleFunction = ff.function("midAngle", ff.literal(line));
        Double midAngle = (Double) midAngleFunction.evaluate(line);
        assertEquals(-45, midAngle, 1e-6);

        // for a simple line start and end angle should be the same as mid
        Function startAngleFunction = ff.function("startAngle", ff.literal(line));
        Double startAngle = (Double) startAngleFunction.evaluate(line);
        Function endAngleFunction = ff.function("endAngle", ff.literal(line));
        Double endAngle = (Double) endAngleFunction.evaluate(line);
        assertEquals(midAngle, startAngle, 1e-6);
        assertEquals(midAngle, endAngle, 1e-6);
    }

    @Test
    public void testMidPointComplex() throws Exception {
        // Linestring with first segment shorter, the midpoint is along the second segment
        LineString line = (LineString) new WKTReader().read("LINESTRING(0 10, 5 0, 15 10)");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function midAngleFunction = ff.function("midAngle", ff.literal(line));
        Double midAngle = (Double) midAngleFunction.evaluate(line);
        assertEquals(-45, midAngle, 1e-6);
    }
}
