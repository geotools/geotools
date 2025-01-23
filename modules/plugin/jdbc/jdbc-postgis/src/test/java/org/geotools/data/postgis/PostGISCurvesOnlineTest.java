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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.jdbc.JDBCCurvesTest;
import org.geotools.jdbc.JDBCCurvesTestSetup;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

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
        assertArrayEquals(new double[] {-9.0, -8.5, -9.0, -9.5, -9.0, -8.5}, hole.getControlPoints(), 1e-6);
    }

    @Test
    public void testIntersectionAccuracy() throws Exception {
        String tableName = tname("curveIntersection");
        SimpleFeatureSource fs = dataStore.getFeatureSource(tableName);
        Query q = new Query(tableName);
        FilterFactory ff = dataStore.getFilterFactory();
        Point testPoint = new GeometryFactory().createPoint(new Coordinate(25492818, 6677399.98));
        q.setFilter(ff.intersects(ff.property(aname("geometry")), ff.literal(testPoint)));
        Set<String> names = new HashSet<>();
        try (SimpleFeatureIterator fi = fs.getFeatures(q).features()) {
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                names.add((String) f.getAttribute(aname("name")));
            }
        }
        // the point actually intersects only the inner polygon, before the fix, it returned both p1 and p2
        assertEquals(Set.of("p2"), names);
    }
}
