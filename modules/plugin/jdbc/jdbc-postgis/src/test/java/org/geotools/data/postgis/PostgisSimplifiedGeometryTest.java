/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.postgis;

import java.io.IOException;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

public class PostgisSimplifiedGeometryTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostgisSimplifiedGeometryTestSetup(new PostGISTestSetup());
    }

    public void testPoint() throws IOException, ParseException {
        Geometry geom = getFirstGeometry("simplify_point", 10);
        // same point, but before the fix with TWKB enabled it would have been 0,0
        assertGeometryEquals(geom, "POINT(-120 40)");
    }

    public void testLine() throws IOException, ParseException {
        Geometry geom = getFirstGeometry("simplify_line", 20);
        // mid point gone
        assertGeometryEquals(geom, "LINESTRING(-120.0 40.0, -140 60)");
    }

    public void testPolygon() throws IOException, ParseException {
        Geometry geom = getFirstGeometry("simplify_polygon", 20);
        // simplified down to a triangle
        assertGeometryEquals(geom, "POLYGON ((-120 40, -130 40, -130 50, -120 40))");
    }

    public void testCollection() throws IOException, ParseException {
        Geometry geom = getFirstGeometry("simplify_collection", 20);
        // line part simplified, but won't use TWKB
        assertGeometryEquals(
                geom, "GEOMETRYCOLLECTION (POINT (-120 40), LINESTRING (-120 40, -140 50))");
    }

    public void testCurve() throws IOException, ParseException {
        Geometry geom = getFirstGeometry("simplify_curve", 20);
        // actual curve, does not get simplified
        assertGeometryEquals(geom, "CIRCULARSTRING (10.0 15.0, 15.0 20.0, 20.0 15.0)");
    }

    public void assertGeometryEquals(Geometry actual, String expectedWKT) throws ParseException {
        Geometry expected = new WKTReader2().read(expectedWKT);
        assertTrue("Expected " + expectedWKT + " but got " + actual, actual.equalsExact(expected));
    }

    private Geometry getFirstGeometry(String tableName, double simplificationDistance)
            throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(tableName));
        Query q = new Query(tname(tableName));
        q.setHints(new Hints(Hints.GEOMETRY_SIMPLIFICATION, simplificationDistance));
        ContentFeatureCollection fc = fs.getFeatures(q);
        try (SimpleFeatureIterator fi = fc.features()) {
            assertTrue("Was expecting to find at least one feature", fi.hasNext());
            return (Geometry) fi.next().getDefaultGeometry();
        }
    }
}
