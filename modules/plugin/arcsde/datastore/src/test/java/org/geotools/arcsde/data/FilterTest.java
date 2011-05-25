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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This test case does not still use the ArcSDEDataStore testing data supplied with in
 * {@code test-data/import}, so it is excluded in project.xml.
 * 
 * @author cdillard
 * @author Gabriel Roldan
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *         /org/geotools/arcsde/data/FilterTest.java.fixme $
 * @version $Id$
 */
public class FilterTest {
    private static final Comparator<SimpleFeature> FEATURE_COMPARATOR = new Comparator<SimpleFeature>() {
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
    public void tearDown() throws Exception {

    }

    private static void collectResults(FeatureReader<SimpleFeatureType, SimpleFeature> fr,
            Collection<SimpleFeature> c) throws NoSuchElementException, IOException,
            IllegalAttributeException {
        while (fr.hasNext()) {
            c.add(fr.next());
        }
    }

    /**
     * Are the two collections similar?
     * 
     * @param c1
     *            Collection first
     * @param c2
     *            Collection second
     * @return true if they have the same content
     */
    private void assertFeatureListsSimilar(Collection<SimpleFeature> c1,
            Collection<SimpleFeature> c2) {
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
            assertEquals("Feature[" + i + "] identifiers Equal", f1.getIdentifier().getID(), f2
                    .getIdentifier().getID());
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
                        assertTrue("Feature[" + i + "]." + name + " geometry",
                                ((Geometry) value1).equals((Geometry) value2));
                    } else {
                        assertEquals("Feature[" + i + "]." + name, value1, value2);
                    }
                }
            }
        }
    }

    private static LineString buildSegment(double x1, double y1, double x2, double y2) {
        Coordinate[] coordArray = new Coordinate[] { new Coordinate(x1, y1), new Coordinate(x2, y2) };
        LineString result = gf.createLineString(coordArray);

        return result;
    }

    private static Polygon buildPolygon(double minx, double miny, double maxx, double maxy) {
        Coordinate[] coordArray = new Coordinate[] { new Coordinate(minx, miny),
                new Coordinate(minx, maxy), new Coordinate(maxx, maxy), new Coordinate(maxx, miny),
                new Coordinate(minx, miny) };
        Polygon p = gf.createPolygon(gf.createLinearRing(coordArray), new LinearRing[0]);

        return p;
    }

    /**
     * TODO: resurrect testDisjointFilter
     */
    @Test
    @Ignore
    public void testDisjointFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        double minx = -179;
        double maxx = -170;
        double miny = -90;
        double maxy = -80;
        Polygon p = buildPolygon(minx, miny, maxx, maxy);

        Filter filter = ff.not(ff.isNull(ff.property("SHAPE")));
        filter = ff.and(filter, ff.disjoint(ff.property("SHAPE"), ff.literal(p)));

        runTestWithFilter(ft, filter);
    }

    @Test
    public void testContainsFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        double minx = 106.6666;
        double maxx = 106.6677;
        double miny = -6.1676;
        double maxy = -6.1672;
        Polygon p = buildPolygon(minx, miny, maxx, maxy);

        Filter filter = ff.contains(ff.property("SHAPE"), ff.literal(p));

        runTestWithFilter(ft, filter);
    }

    @Test
    public void testBBoxFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        double minx = 106.6337;
        double maxx = 106.6381;
        double miny = -6.1794;
        double maxy = -6.1727;
        Filter filter = ff.bbox("SHAPE", minx, miny, maxx, maxy, "EPSG:4326");

        runTestWithFilter(ft, filter);
    }

    @Test
    public void testIntersectsFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        double minx = 106.6337;
        double maxx = 106.6381;
        double miny = -6.1794;
        double maxy = -6.1727;
        Polygon p = buildPolygon(minx, miny, maxx, maxy);
        Filter filter = ff.intersects(ff.property("SHAPE"), ff.literal(p));

        runTestWithFilter(ft, filter);
    }

    @Test
    public void testOverlapsFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        double minx = 106.6337;
        double maxx = 106.6381;
        double miny = -6.1794;
        double maxy = -6.1727;
        Polygon p = buildPolygon(minx, miny, maxx, maxy);
        Filter filter = ff.overlaps(ff.property("SHAPE"), ff.literal(p));

        runTestWithFilter(ft, filter);
    }

    @Test
    public void testWithinFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        double minx = 106.6337;
        double maxx = 106.6381;
        double miny = -6.1794;
        double maxy = -6.1727;
        Polygon p = buildPolygon(minx, miny, maxx, maxy);
        Filter filter = ff.within(ff.property("SHAPE"), ff.literal(p));

        runTestWithFilter(ft, filter);
    }

    @Test
    public void testCrossesFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Build the filter
        double minx = 106.6337;
        double maxx = 106.6381;
        double miny = -6.1794;
        double maxy = -6.1727;
        LineString ls = buildSegment(minx, miny, maxx, maxy);
        Filter filter = ff.crosses(ff.property("SHAPE"), ff.literal(ls));

        runTestWithFilter(ft, filter);
    }

    /**
     * TODO: resurrect testEqualFilter
     */
    @Test
    @Ignore
    public void testEqualFilter() throws Exception {
        FeatureType ft = this.dataStore.getSchema(testData.getTempTableName());

        // Get a geometry for equality comparison
        Query Query = new Query(testData.getTempTableName());
        Query.setPropertyNames(safePropertyNames(ft));
        Query.setMaxFeatures(1);
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = this.dataStore.getFeatureReader(Query,
                Transaction.AUTO_COMMIT);
        SimpleFeature feature = fr.next();
        fr.close();

        Geometry g = (Geometry) feature.getAttribute("SHAPE");

        // Build the filter
        Filter filter = ff.equal(ff.property("SHAPE"), ff.literal(g));

        runTestWithFilter(ft, filter);
    }

    private void runTestWithFilter(FeatureType ft, Filter filter) throws Exception {
        System.err.println("****************");
        System.err.println("**");
        System.err.println("** TESTING FILTER: " + filter);
        System.err.println("**");
        System.err.println("****************");

        // First, read using the slow, built-in mechanisms
        String[] propertyNames = safePropertyNames(ft);
        Query allQuery = new Query(testData.getTempTableName(), Filter.INCLUDE, propertyNames);
        System.err.println("Performing slow read...");

        long startTime = System.currentTimeMillis();
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = this.dataStore.getFeatureReader(
                allQuery, Transaction.AUTO_COMMIT);
        FilteringFeatureReader<SimpleFeatureType, SimpleFeature> ffr = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(
                fr, filter);
        ArrayList<SimpleFeature> slowResults = new ArrayList<SimpleFeature>();
        collectResults(ffr, slowResults);
        ffr.close();

        long endTime = System.currentTimeMillis();
        System.err.println("Slow read took " + (endTime - startTime) + " milliseconds.");

        // Now read using DataStore's mechanisms.
        System.err.println("Performing fast read...");
        startTime = System.currentTimeMillis();

        Query filteringQuery = new Query(testData.getTempTableName(), filter, safePropertyNames(ft));
        fr = this.dataStore.getFeatureReader(filteringQuery, Transaction.AUTO_COMMIT);

        ArrayList<SimpleFeature> fastResults = new ArrayList<SimpleFeature>();
        collectResults(fr, fastResults);
        fr.close();
        endTime = System.currentTimeMillis();
        System.err.println("Fast read took " + (endTime - startTime) + " milliseconds.");

        assertFeatureListsSimilar(slowResults, fastResults);
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
