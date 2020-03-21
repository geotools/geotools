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
 *
 */
package org.geotools.arcsde.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Feature;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Within;

/**
 * @author cdillard
 * @author Gabriel Roldan
 */
public class SpatialFilterTest {
    private static final Comparator<SimpleFeature> FEATURE_COMPARATOR =
            new Comparator<SimpleFeature>() {
                public int compare(SimpleFeature f1, SimpleFeature f2) {
                    return f1.getID().compareTo(f2.getID());
                }
            };

    private static final GeometryFactory gf = new GeometryFactory();

    private static TestData testData;

    private DataStore dataStore;

    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        testData = new TestData();
        testData.setUp();

        final boolean insertTestData = true;
        testData.createTempTable(insertTestData);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        boolean cleanTestTable = false;
        boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
    }

    @Before
    public void setUp() throws Exception {
        // facilitates running a single test at a time (eclipse lets you do this
        // and it's very useful)
        if (testData == null) {
            oneTimeSetUp();
        }
        this.dataStore = testData.getDataStore();
    }

    @After
    public void tearDown() throws Exception {}

    private static void collectResults(
            FeatureReader<SimpleFeatureType, SimpleFeature> fr, Collection<SimpleFeature> c)
            throws NoSuchElementException, IOException, IllegalAttributeException {
        while (fr.hasNext()) {
            c.add(fr.next());
        }
    }

    /**
     * Are the two collections similar?
     *
     * @param c1 Collection first
     * @param c2 Collection second
     * @return true if they have the same content
     */
    private void assertFeatureListsSimilar(
            Collection<SimpleFeature> c1, Collection<SimpleFeature> c2) {
        assertEquals("Actual feature collection was not the expected size", c1.size(), c2.size());

        ArrayList<SimpleFeature> al1 = new ArrayList<SimpleFeature>(c1);
        ArrayList<SimpleFeature> al2 = new ArrayList<SimpleFeature>(c2);
        Collections.sort(al1, FEATURE_COMPARATOR);
        Collections.sort(al2, FEATURE_COMPARATOR);

        int n = c1.size();

        for (int i = 0; i < n; i++) {
            Feature f1 = al1.get(i);
            Feature f2 = al2.get(i);
            if (i == 0) {
                assertEquals("Feature Type", f1.getType(), f2.getType());
            }
            assertEquals(
                    "Feature[" + i + "] identifiers Equal",
                    f1.getIdentifier().getID(),
                    f2.getIdentifier().getID());
            if (!f1.equals(f2)) {
                // go through properties and figure out differneces...
                for (PropertyDescriptor property : f1.getType().getDescriptors()) {
                    String name = property.getName().getLocalPart();
                    Object value1 = f1.getProperty(name).getValue();
                    Object value2 = f2.getProperty(name).getValue();
                    if (value1 instanceof Calendar) {
                        continue;
                    }
                    if (value1 instanceof Date) {
                        continue;
                    }

                    if (value1 instanceof Geometry) {
                        // jts geometry is not my friend
                        assertTrue(
                                "Feature[" + i + "]." + name + " geometry",
                                ((Geometry) value1).equalsTopo((Geometry) value2));
                    } else {
                        assertEquals("Feature[" + i + "]." + name, value1, value2);
                    }
                }
            }
        }
    }

    private static LineString buildSegment(double x1, double y1, double x2, double y2) {
        Coordinate[] coordArray = new Coordinate[] {new Coordinate(x1, y1), new Coordinate(x2, y2)};
        LineString result = gf.createLineString(coordArray);

        return result;
    }

    private static Polygon buildPolygon(double minx, double miny, double maxx, double maxy) {
        Coordinate[] coordArray =
                new Coordinate[] {
                    new Coordinate(minx, miny),
                    new Coordinate(minx, maxy),
                    new Coordinate(maxx, maxy),
                    new Coordinate(maxx, miny),
                    new Coordinate(minx, miny)
                };
        Polygon p = gf.createPolygon(gf.createLinearRing(coordArray), new LinearRing[0]);

        return p;
    }

    /** TODO: resurrect testDisjointFilter */
    @Test
    public void testDisjointFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build a polygon that intercepts some of the geometries, but not all of them
        Polygon p = buildPolygon(-180, 0, -160, 90);

        Filter filter = ff.not(ff.isNull(ff.property("SHAPE")));
        filter = ff.and(filter, ff.disjoint(ff.property("SHAPE"), ff.literal(p)));

        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testContainsFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter with a polygon that is inside POLYGON((-10 -10, -10 10, 10 10, 10 -10,
        // -10 -10))
        Polygon p = buildPolygon(-9, -9, -8, -8);
        Filter filter = ff.contains(ff.property("SHAPE"), ff.literal(p));
        runTestWithFilter(ft, filter, false);

        // now build the opposite filter, the polygon contains the shape
        p = buildPolygon(-1, -1, 1, 1);
        filter = ff.contains(ff.literal(p), ff.property("SHAPE"));
        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testContainsSDESemanticsFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build a filter so that SDE would actually catch more geometries, it would
        // actually include
        // "MULTIPOLYGON( ((-1 -1, -1 1, 1 1, 1 -1, -1 -1)), ((-170 -80, -170 -70, -160 -70, -160
        // -80, -170 -80)) )"
        // in the results as well. It seems the containment semantics is applied in or to the
        // multigeometry
        // components. We do in memory post filtering to get the right semantics
        Polygon p = buildPolygon(-1, -1, 1, 1);
        Filter filter = ff.contains(ff.property("SHAPE"), ff.literal(p));
        runTestWithFilter(ft, filter, false);

        // now build the opposite filter, the polygon contains the shape
        p = buildPolygon(-1, -1, 1, 1);
        filter = ff.contains(ff.literal(p), ff.property("SHAPE"));
        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testBBoxFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        Filter filter = ff.bbox("SHAPE", -1, -1, 1, 1, "EPSG:4326");

        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testOrBBoxFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // System.out.println(
        // this.dataStore.getFeatureSource(ft.getName().getLocalPart()).getBounds());

        // build a or of bbox so that
        // - the intersection of the bboxes is empty
        // - the union of the bboxes actually gets more data than necessary
        BBOX bbox1 = ff.bbox("SHAPE", -171, -90, -169, 90, "EPSG:4326");
        BBOX bbox2 = ff.bbox("SHAPE", 169, -90, 171, 90, "EPSG:4326");
        Filter filter = ff.or(bbox1, bbox2);

        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testIntersectsFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        Polygon p = buildPolygon(-1, -1, 1, 1);
        Filter filter = ff.intersects(ff.property("SHAPE"), ff.literal(p));

        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testOverlapsFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        Polygon p = buildPolygon(-10, -10, -8, -8);
        Filter filter = ff.overlaps(ff.property("SHAPE"), ff.literal(p));

        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testWithinFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        Polygon p = buildPolygon(-9, -9, -8, -8);
        Within filter = ff.within(ff.literal(p), ff.property("SHAPE"));
        runTestWithFilter(ft, filter, false);

        // now build the opposite filter, the polygon contains the shape
        p = buildPolygon(-1, -1, 1, 1);
        filter = ff.within(ff.property("SHAPE"), ff.literal(p));
        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testCrossesFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        LineString ls = buildSegment(-12, -12, 12, 12);
        Filter filter = ff.crosses(ff.property("SHAPE"), ff.literal(ls));

        runTestWithFilter(ft, filter, false);
    }

    /** TODO: resurrect testEqualFilter */
    @Test
    public void testEqualFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Get a geometry for equality comparison
        Query Query = new Query(testData.getTempTableName());
        Query.setPropertyNames(safePropertyNames(ft));
        Query.setMaxFeatures(1);
        FeatureReader<SimpleFeatureType, SimpleFeature> fr =
                this.dataStore.getFeatureReader(Query, Transaction.AUTO_COMMIT);
        SimpleFeature feature = fr.next();
        fr.close();

        Geometry g = (Geometry) feature.getAttribute("SHAPE");

        // Build the filter
        Filter filter = ff.equal(ff.property("SHAPE"), ff.literal(g));

        runTestWithFilter(ft, filter, false);
    }

    @Test
    public void testDwithinFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Available geometries:
        // 1. POINT(0 0)
        // 2. MULTIPOINT(0 0, 170 0)
        // 3. LINESTRING(0 0, 170 80)
        // 4. MULTILINESTRING((-170 -80, 170 80), (-170 80, 170 -80))
        // 5. POLYGON((-10 -10, -10 10, 10 10, 10 -10, -10 -10))
        // 6. MULTIPOLYGON( ((-1 -1, -1 1, 1 1, 1 -1, -1 -1)), ((-170 -80, -170 -70, -160 -70, -160
        // -80, -170 -80)) )
        // 7. POINT EMPTY
        // 8. null

        final PropertyName property = ff.property("SHAPE");
        Geometry geom;
        Filter filter;

        geom = geom("POINT(170 0)");
        filter = ff.dwithin(property, ff.literal(geom), 1d, "");
        runTestWithFilter(ft, filter, false, 1);

        geom = geom("POINT(-1 -1)");
        filter = ff.dwithin(property, ff.literal(geom), 0.1, "");
        runTestWithFilter(ft, filter, false, 2); // geoms 4. and 6.
    }

    @Test
    public void testBeyondFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Available geometries:
        // 1. POINT(0 0)
        // 2. MULTIPOINT(0 0, 170 0)
        // 3. LINESTRING(0 0, 170 80)
        // 4. MULTILINESTRING((-170 -80, 170 80), (-170 80, 170 -80))
        // 5. POLYGON((-10 -10, -10 10, 10 10, 10 -10, -10 -10))
        // 6. MULTIPOLYGON( ((-1 -1, -1 1, 1 1, 1 -1, -1 -1)), ((-170 -80, -170 -70, -160 -70, -160
        // -80, -170 -80)) )
        // 7. POINT EMPTY
        // 8. null

        final PropertyName property = ff.property("SHAPE");
        Geometry geom;
        Filter filter;

        geom = geom("POINT(170 0)");
        filter = ff.beyond(property, ff.literal(geom), 1d, "");
        runTestWithFilter(ft, filter, false, 5); // all non null but geom 2.

        geom = geom("POINT(-1 -1)");
        filter = ff.beyond(property, ff.literal(geom), 0.1, "");
        runTestWithFilter(ft, filter, false, 4); // all non null but geoms 4. and 6.
    }

    @Test
    public void testTouches() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Available geometries:
        // INT32_COL SHAPE
        // --------- --------
        // 1. ------ POINT(0 0)
        // 2. ------ MULTIPOINT(0 0, 170 0)
        // 3. ------ LINESTRING(0 0, 170 80)
        // 4. ------ MULTILINESTRING((-170 -80, 170 80), (-170 80, 170 -80))
        // 5. ------ POLYGON((-10 -10, -10 10, 10 10, 10 -10, -10 -10))
        // 6. ------ MULTIPOLYGON( ((-1 -1, -1 1, 1 1, 1 -1, -1 -1)),
        // ((-170 -80, -170 -70, -160 -70, -160 -80, -170 -80)) )
        // 7. ------ POINT EMPTY
        // 8. ------ null

        final PropertyName property = ff.property("SHAPE");
        Geometry geom;
        Filter filter;

        geom = findGeom("INT32_COL = 3");
        filter = ff.touches(property, ff.literal(geom));
        runTestWithFilter(ft, filter, false, 2); // features 1. and 2.

        // doesn't touch itself
        geom = findGeom("INT32_COL = 3");
        filter =
                ff.and(
                        ff.equals(ff.property("INT32_COL"), ff.literal(3)),
                        ff.touches(property, ff.literal(geom)));
        runTestWithFilter(ft, filter, true); // empty

        geom = findGeom("INT32_COL = 3").getBoundary().getGeometryN(0); // first point of 3.
        filter = ff.touches(property, ff.literal(geom));
        runTestWithFilter(ft, filter, false, 1);

        geom = findGeom("INT32_COL = 6");
        filter = ff.touches(property, ff.literal(geom));
        runTestWithFilter(ft, filter, true); // empty, polygon intersects

        geom = findGeom("INT32_COL = 6").getBoundary();
        filter = ff.touches(property, ff.literal(geom));
        runTestWithFilter(ft, filter, false, 1); // true, polygon boundary touches polygon

        geom = findGeom("INT32_COL = 4").getGeometryN(0);
        filter = ff.touches(property, ff.literal(geom));
        runTestWithFilter(ft, filter, false, 1);
    }

    /** */
    private Geometry findGeom(String cql) throws Exception {

        Query query = new Query(testData.getTempTableName());
        query.setFilter(ECQL.toFilter(cql));
        FeatureReader<SimpleFeatureType, SimpleFeature> fr =
                this.dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);
        SimpleFeature feature = fr.next();
        fr.close();
        return (Geometry) feature.getDefaultGeometry();
    }

    private void runTestWithFilter(FeatureType ft, Filter filter, boolean empty) throws Exception {
        runTestWithFilter(ft, filter, empty, null);
    }

    private Geometry geom(String wkt) {
        try {
            return new WKTReader().read(wkt);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void runTestWithFilter(
            FeatureType ft, Filter filter, boolean empty, /* Nullable */ Integer expectedCount)
            throws Exception {
        // System.err.println("****************");
        // System.err.println("**");
        // System.err.println("** TESTING FILTER: " + filter);
        // System.err.println("**");
        // System.err.println("****************");

        // First, read using the slow, built-in mechanisms
        String[] propertyNames = safePropertyNames(ft);
        final String typeName = testData.getTempTableName();
        Query allQuery = new Query(typeName, Filter.INCLUDE, propertyNames);
        // System.err.println("Performing slow read...");

        long startTime = System.currentTimeMillis();
        FeatureReader<SimpleFeatureType, SimpleFeature> fr =
                this.dataStore.getFeatureReader(allQuery, Transaction.AUTO_COMMIT);
        FilteringFeatureReader<SimpleFeatureType, SimpleFeature> ffr =
                new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(fr, filter);
        ArrayList<SimpleFeature> slowResults = new ArrayList<SimpleFeature>();
        collectResults(ffr, slowResults);
        ffr.close();

        long endTime = System.currentTimeMillis();
        // System.err.println("Slow read took " + (endTime - startTime) + " milliseconds.");

        // Now read using DataStore's mechanisms.
        // System.err.println("Performing fast read...");
        startTime = System.currentTimeMillis();

        Query filteringQuery =
                new Query(testData.getTempTableName(), filter, safePropertyNames(ft));
        fr = this.dataStore.getFeatureReader(filteringQuery, Transaction.AUTO_COMMIT);

        ArrayList<SimpleFeature> fastResults = new ArrayList<SimpleFeature>();
        collectResults(fr, fastResults);
        fr.close();
        endTime = System.currentTimeMillis();
        // System.err.println("Fast read took " + (endTime - startTime) + " milliseconds.");

        assertFeatureListsSimilar(slowResults, fastResults);

        if (expectedCount != null) {
            assertEquals(expectedCount.intValue(), slowResults.size());
            assertEquals(expectedCount.intValue(), fastResults.size());
        }

        if (empty) {
            assertEquals("Result was supposed to be empty", 0, fastResults.size());
        } else {
            assertTrue("Result was supposed to be non empty", fastResults.size() > 0);
        }
    }

    private String[] safePropertyNames(FeatureType ft) {
        ArrayList<String> names = new ArrayList<String>();
        for (PropertyDescriptor descriptor : ft.getDescriptors()) {
            Class<?> binding = descriptor.getType().getBinding();
            if (Calendar.class.equals(binding)) {
                continue;
            }
            if (Date.class.equals(binding)) {
                continue;
            }
            names.add(descriptor.getName().getLocalPart());
        }
        String[] propertyNames = names.toArray(new String[names.size()]);
        return propertyNames;
    }
}
