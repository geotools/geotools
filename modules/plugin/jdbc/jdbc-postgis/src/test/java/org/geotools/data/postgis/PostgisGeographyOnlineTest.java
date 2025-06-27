/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.RenderingHints;
import java.sql.Connection;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCGeographyOnlineTest;
import org.geotools.jdbc.JDBCGeographyTestSetup;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class PostgisGeographyOnlineTest extends JDBCGeographyOnlineTest {

    public PostgisGeographyOnlineTest() {
        this.forceLongitudeFirst = true;
    }

    @Override
    protected JDBCGeographyTestSetup createTestSetup() {
        return new PostgisGeographyTestSetup(new PostGISTestSetup());
    }

    @Override
    public void testSchema() throws Exception {
        super.testSchema();

        if (!isGeographySupportAvailable()) {
            return;
        }

        // extra check, pg specific: the native typename is actually geography
        SimpleFeatureType ft = dataStore.getFeatureSource(tname("geopoint")).getSchema();
        assertEquals("geography", ft.getGeometryDescriptor().getUserData().get(JDBCDataStore.JDBC_NATIVE_TYPENAME));
    }

    // As reported in GEOS-4384 (http://jira.codehaus.org/browse/GEOS-4384)
    @Test
    public void testDWithinOGCUnits() throws Exception {
        validateOGCUnitUsage(10000, "m");
        validateOGCUnitUsage(10000, "metre");
        validateOGCUnitUsage(10000, "meters");
        validateOGCUnitUsage(10000 * 1000, "mm");
        validateOGCUnitUsage(10, "kilometers");
        validateOGCUnitUsage(10, "km");
        validateOGCUnitUsage(10, "kilometer");
        validateOGCUnitUsage(10000 / 0.0254, "in");
        validateOGCUnitUsage(10000 / 0.3048, "feet");
        validateOGCUnitUsage(10000 / 0.3048, "foot");
        validateOGCUnitUsage(10000 / 0.3048, "ft");
        validateOGCUnitUsage(10000 / 1609.344, "mi");
        validateOGCUnitUsage(10000 / 1609.344, "mile");
        validateOGCUnitUsage(10000 / 1609.344, "miles");
        validateOGCUnitUsage(10000 / 1852, "NM");
    }

    private void validateOGCUnitUsage(double distance, String unit) throws Exception {
        Coordinate coordinate = new Coordinate(-110, 30);
        GeometryFactory factory = new GeometryFactory();
        Point point = factory.createPoint(coordinate);
        Geometry[] geometries = {point};
        GeometryCollection geometry = new GeometryCollection(geometries, factory);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        PropertyName geomName = ff.property(aname("geo"));
        Literal lit = ff.literal(geometry);

        DWithin dwithinGeomFilter = ff.dwithin(geomName, lit, distance, unit);
        Query query = new Query(tname("geopoint"), dwithinGeomFilter);
        SimpleFeatureCollection features =
                dataStore.getFeatureSource(tname("geopoint")).getFeatures(query);
        assertEquals(1, features.size());
        checkSingleResult(features, "Town");
    }

    protected void checkSingleResult(FeatureCollection features, String name) {
        assertEquals(1, features.size());
        try (FeatureIterator fr = features.features()) {
            assertTrue(fr.hasNext());
            SimpleFeature f = (SimpleFeature) fr.next();
            assertNotNull(f);
            assertEquals(name, f.getAttribute(aname("name")));
            assertFalse(fr.hasNext());
        }
    }

    @Test
    public void testSimplifyGeography() throws Exception {
        // try to simplify geometry, but ST_Simplify is not defined for geometry
        Query query = new Query(tname("geoline"));
        query.getHints().add(new RenderingHints(Hints.GEOMETRY_SIMPLIFICATION, 2.0));
        // used to go boom here
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("geoline"));
        SimpleFeatureCollection features = fs.getFeatures(query);
        // check the geometry is what we expect (but make it so that if in the future we can
        // simplify
        // over geography, the test still passes
        SimpleFeature sf = DataUtilities.first(features);
        LineString ls = (LineString) sf.getDefaultGeometry();
        assertEquals(0d, ls.getStartPoint().getX(), 0d);
        assertEquals(0d, ls.getStartPoint().getY(), 0d);
        assertEquals(4d, ls.getEndPoint().getX(), 0d);
        assertEquals(4d, ls.getEndPoint().getY(), 0d);
    }

    @Test
    public void testDimensionFromFirstGeography() throws Exception {
        try (Connection cx = dataStore.getDataSource().getConnection()) {
            PostGISDialect dialect = (PostGISDialect) dataStore.getSQLDialect();
            assertEquals((Integer) 0, dialect.getDimensionFromFirstGeo("public", "geopoint", "geo", cx));
            assertEquals((Integer) 1, dialect.getDimensionFromFirstGeo("public", "geoline", "geo", cx));
        }
    }
}
