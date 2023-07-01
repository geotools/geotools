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
package org.geotools.geometry.iso.text;

import org.geotools.api.geometry.PositionFactory;
import org.geotools.api.geometry.primitive.Curve;
import org.geotools.api.geometry.primitive.Point;
import org.geotools.geometry.iso.MockGeometryFactory;
import org.geotools.geometry.iso.MockPositionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WKTParserTest {
    // TODO: POLYGON ((280 380, 280 200, 60 200, 60 380, 180 220, 280 380),(40 160, 260 160, 240 60,
    // 20 80, 40 160))
    WKTParser parser;

    @Before
    public void setUp() throws Exception {

        MockGeometryFactory mockFactory = new MockGeometryFactory();
        PositionFactory pf = new MockPositionFactory();
        parser = new WKTParser(mockFactory, mockFactory, pf, null);
    }

    @Test
    public void testPoint1() throws Exception {
        String WKT = "POINT (80 340)";
        Object geometry = parser.parse(WKT);
        Assert.assertNotNull(geometry);
        Assert.assertTrue(geometry instanceof Point);
        Point point = (Point) geometry;
        Assert.assertNotNull(point.getDirectPosition());
        Assert.assertEquals(80, point.getDirectPosition().getOrdinate(0), 0.0);
        Assert.assertEquals(340, point.getDirectPosition().getOrdinate(1), 0.0);
    }

    @Test
    public void testPoint2() throws Exception {
        String WKT = "POINT (320.324 180.234)";

        Object geometry = parser.parse(WKT);
        Assert.assertNotNull(geometry);
        Assert.assertTrue(geometry instanceof Point);
        Point point = (Point) geometry;
        Assert.assertEquals(320.324, point.getDirectPosition().getOrdinate(0), 0.0);
        Assert.assertEquals(180.234, point.getDirectPosition().getOrdinate(1), 0.0);
    }

    @Test
    public void testPoint3() throws Exception {
        String WKT = "POINT (260.01 -360.55)";

        Object geometry = parser.parse(WKT);
        Assert.assertNotNull(geometry);
        Assert.assertTrue(geometry instanceof Point);
        Point point = (Point) geometry;
        Assert.assertEquals(260.01, point.getDirectPosition().getOrdinate(0), 0.0);
        Assert.assertEquals(-360.55, point.getDirectPosition().getOrdinate(1), 0.0);
    }

    @Test
    public void testLine1() throws Exception {
        String WKT =
                "LINESTRING (60 380, 60 20, 200 400, 280 20, 360 400, 420 20, 500 400, 580 20, 620 400)";

        Object geometry = parser.parse(WKT);
        Assert.assertNotNull(geometry);
        Assert.assertTrue(geometry instanceof Curve);
    }

    @Test
    public void testLine2() throws Exception {
        String WKT =
                "LINESTRING (80 360, 520 360, 520 40, 120 40, 120 300, 460 300, 460 100, 200 100, 200 240, 400 240, 400 140, 560 0)";

        Object geometry = parser.parse(WKT);
        Assert.assertNotNull(geometry);
        Assert.assertTrue(geometry instanceof Curve);
    }
}
