/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertArrayEquals;

import org.geotools.geometry.jts.CircularRing;
import org.geotools.jdbc.JDBCCurvesTest;
import org.geotools.jdbc.JDBCCurvesTestSetup;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;

public class PostGISCurvesOnlineTest extends JDBCCurvesTest {

    @Override
    protected JDBCCurvesTestSetup createTestSetup() {
        return new PostGISCurvesTestSetup();
    }

    @Test
    public void testSquareHole2Points() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("SquareHole2Points");
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof Polygon);

        Polygon p = (Polygon) g;

        LineString ls = p.getExteriorRing();
        assertEquals(5, ls.getNumPoints());
        assertEquals(new Coordinate(-10, -10), ls.getCoordinateN(0));
        assertEquals(new Coordinate(-10, -8), ls.getCoordinateN(1));
        assertEquals(new Coordinate(-8, -8), ls.getCoordinateN(2));
        assertEquals(new Coordinate(-8, -10), ls.getCoordinateN(3));
        assertEquals(new Coordinate(-10, -10), ls.getCoordinateN(4));

        // check the interior ring has been normalized
        assertEquals(1, p.getNumInteriorRing());
        CircularRing hole = (CircularRing) p.getInteriorRingN(0);
        assertArrayEquals(
                new double[] {-9.0, -8.5, -9.0, -9.5, -9.0, -8.5}, hole.getControlPoints(), 1e-6);
    }
}
