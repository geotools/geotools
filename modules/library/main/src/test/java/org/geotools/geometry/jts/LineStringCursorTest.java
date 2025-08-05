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
package org.geotools.geometry.jts;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTReader;

public class LineStringCursorTest {

    @Test
    public void testMaxAngle() throws Exception {
        LineString ls = (LineString) new WKTReader().read("LINESTRING(20 0, 10 1, 0 0)");
        LineStringCursor cursor = new LineStringCursor(ls);
        double maxAngle = cursor.getMaxAngleChange(0, ls.getLength());
        assertTrue(maxAngle < 11.5);
    }
}
