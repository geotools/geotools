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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.opengis.filter.sort.SortBy.NATURAL_ORDER;
import static org.opengis.filter.sort.SortBy.REVERSE_ORDER;
import static org.opengis.filter.sort.SortOrder.ASCENDING;
import static org.opengis.filter.sort.SortOrder.DESCENDING;

import com.esri.sde.sdk.client.SeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.spatial.BBOX;

/**
 * {@link ArcSdeFeatureSource} test cases
 *
 * @author Gabriel Roldan
 */
public class ArcSDEFeatureSourceTest {
    /** package logger */
    private static Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ArcSDEFeatureSourceTest.class);

    private static TestData testData;

    /** an ArcSDEDataStore created on setUp() to run tests against */
    private static ArcSDEDataStore store;

    /** a filter factory for testing */
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        testData = new TestData();
        testData.setUp();
        final boolean insertTestData = true;
        testData.createTempTable(insertTestData);
        store = testData.getDataStore();
    }

    @AfterClass
    public static void oneTimeTearDown() {
        boolean cleanTestTable = false;
        boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
        store.dispose();
    }

    /**
     * This method tests the feature reader by opening various simultaneous FeatureReaders using the
     * 3 test tables.
     *
     * <p>I found experimentally that until 24 simultaneous streams can be opened by a single
     * connection. Each featurereader has an ArcSDE stream opened until its <code>close()</code>
     * method is called or hasNext() returns flase, wich automatically closes the stream. If more
     * than 24 simultaneous streams are tryied to be opened upon a single SeConnection, an exception
     * is thrown by de Java ArcSDE API saying that a "NETWORK I/O OPERATION FAILED"
     */
    @Test
    public void testGetFeatureReader() throws IOException {
        final String typeName = testData.getTempTableName();

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(typeName);

        assertNotNull(reader);
        int count = 0;
        try {
            while (testNext(reader)) {
                ++count;
            }
        } finally {
            reader.close();
        }

        assertEquals(8, count);
    }

    /** Checks that a query returns only the specified attributes. */
    @Test
    public void testRestrictsAttributes()
            throws IOException, IllegalAttributeException, SeException {
        final String typeName = testData.getTempTableName();
        final DataStore ds = testData.getDataStore();
        final SimpleFeatureType schema = ds.getSchema(typeName);
        final int queriedAttributeCount = schema.getAttributeCount() - 3;
        final String[] queryAtts = new String[queriedAttributeCount];

        for (int i = 0; i < queryAtts.length; i++) {
            queryAtts[i] = schema.getDescriptor(i).getLocalName();
        }

        // build the query asking for a subset of attributes
        final Query query = new Query(typeName, Filter.INCLUDE, queryAtts);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        SimpleFeatureType resultSchema;
        try {
            resultSchema = reader.getFeatureType();
        } finally {
            reader.close();
        }

        assertTrue(queriedAttributeCount == resultSchema.getAttributeCount());

        for (int i = 0; i < queriedAttributeCount; i++) {
            assertEquals(queryAtts[i], resultSchema.getDescriptor(i).getLocalName());
        }
    }

    /**
     * Checks that arcsde datastore returns featuretypes whose attributes are exactly in the
     * requested order.
     */
    @Test
    public void testRespectsAttributeOrder()
            throws IOException, IllegalAttributeException, SeException {
        final String typeName = testData.getTempTableName();
        final DataStore ds = testData.getDataStore();
        final SimpleFeatureType schema = ds.getSchema(typeName);
        final int queriedAttributeCount = schema.getAttributeCount();
        final String[] queryAtts = new String[queriedAttributeCount];

        // build the attnames in inverse order
        for (int i = queryAtts.length, j = 0; i > 0; j++) {
            --i;
            queryAtts[j] = schema.getDescriptor(i).getLocalName();
        }

        // build the query asking for a subset of attributes
        final Query query = new Query(typeName, Filter.INCLUDE, queryAtts);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {

            SimpleFeatureType resultSchema = reader.getFeatureType();
            assertEquals(queriedAttributeCount, resultSchema.getAttributeCount());

            for (int i = 0; i < queriedAttributeCount; i++) {
                assertEquals(queryAtts[i], resultSchema.getDescriptor(i).getLocalName());
            }
        } finally {
            reader.close();
        }
    }

    /**
     * Say the query contains a set of propertynames to retrieve and the query filter others, the
     * returned feature type should still match the ones in Query.propertyNames
     */
    @Test
    public void testRespectsQueryAttributes()
            throws IOException, IllegalAttributeException, SeException, CQLException {
        final String typeName = testData.getTempTableName();
        final DataStore ds = testData.getDataStore();
        final SimpleFeatureSource fs = ds.getFeatureSource(typeName);

        final String[] queryAtts = {"SHAPE"};
        final Filter filter = CQL.toFilter("INT32_COL = 1");

        // build the query asking for a subset of attributes
        final Query query = new Query(typeName, filter, queryAtts);

        SimpleFeatureCollection features = fs.getFeatures(query);
        SimpleFeatureType resultSchema = features.getSchema();

        assertEquals(1, resultSchema.getAttributeCount());
        assertEquals("SHAPE", resultSchema.getDescriptor(0).getLocalName());

        Feature feature = null;
        SimpleFeatureIterator iterator = features.features();
        try {
            feature = iterator.next();
        } finally {
            iterator.close();
        }

        assertEquals(resultSchema, feature.getType());
    }

    private boolean testNext(FeatureReader<SimpleFeatureType, SimpleFeature> r)
            throws IOException, IllegalAttributeException {
        if (r.hasNext()) {
            SimpleFeature f = r.next();
            assertNotNull(f);
            assertNotNull(f.getFeatureType());
            assertNotNull(f.getBounds());

            GeometryAttribute defaultGeom = f.getDefaultGeometryProperty();
            assertNotNull("expected non null GeometryAttribute", defaultGeom);

            return true;
        }

        return false;
    }

    private FeatureReader<SimpleFeatureType, SimpleFeature> getReader(String typeName)
            throws IOException {
        Query q = new Query(typeName, Filter.INCLUDE);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(q, Transaction.AUTO_COMMIT);
        SimpleFeatureType retType = reader.getFeatureType();
        assertNotNull(retType.getGeometryDescriptor());
        assertTrue(reader.hasNext());

        return reader;
    }

    /**
     * tests the datastore behavior when fetching data based on mixed queries.
     *
     * <p>"Mixed queries" refers to mixing alphanumeric and geometry based filters, since that is
     * the natural separation of things in the Esri Java API for ArcSDE. This is necessary since
     * mixed queries sometimes are problematic. So this test ensures that:
     *
     * <ul>
     *   <li>A mixed query respects all filters
     *   <li>A mixed query does not fails when getBounds() is performed
     *   <li>A mixed query does not fails when size() is performed
     * </ul>
     */
    @Test
    public void testMixedQueries() throws Exception {
        final int EXPECTED_RESULT_COUNT = 1;
        SimpleFeatureSource fs = store.getFeatureSource(testData.getTempTableName());
        SimpleFeatureType schema = fs.getSchema();
        Filter bboxFilter =
                ff.bbox(
                        schema.getGeometryDescriptor().getLocalName(),
                        -60,
                        -55,
                        -40,
                        -20,
                        schema.getCoordinateReferenceSystem().getName().getCode());
        Filter sqlFilter = CQL.toFilter("INT32_COL < 5");
        LOGGER.fine("Geometry filter: " + bboxFilter);
        LOGGER.fine("SQL filter: " + sqlFilter);

        And mixedFilter = ff.and(sqlFilter, bboxFilter);

        Not not =
                ff.not(
                        ff.id(
                                Collections.singleton(
                                        ff.featureId(testData.getTempTableName() + ".90000"))));

        mixedFilter = ff.and(mixedFilter, not);

        LOGGER.fine("Mixed filter: " + mixedFilter);

        // verify both filter constraints are met
        try {
            testFilter(mixedFilter, fs, EXPECTED_RESULT_COUNT);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            throw e;
        }
        // check that getBounds and size do function
        SimpleFeatureIterator reader = null;
        SimpleFeatureCollection results = fs.getFeatures(mixedFilter);
        Envelope bounds = results.getBounds();
        assertNotNull(bounds);
        LOGGER.fine("results bounds: " + bounds);

        reader = results.features();
        try {
            /*
             * verify that when features are already being fetched, getBounds and size still work
             */
            reader.next();
            bounds = results.getBounds();
            assertNotNull(bounds);
            LOGGER.fine("results bounds when reading: " + bounds);

            int count = results.size();
            assertEquals(EXPECTED_RESULT_COUNT, count);
            LOGGER.fine("wooohoooo...");

        } finally {
            reader.close();
        }
    }

    /**
     * to expose GEOT-408, tests that queries in which only non spatial attributes are requested
     * does not fails due to the datastore trying to parse the geometry attribute.
     */
    @Test
    public void testAttributeOnlyQuery() throws Exception {
        DataStore ds = testData.getDataStore();
        SimpleFeatureSource fSource = ds.getFeatureSource(testData.getTempTableName());
        SimpleFeatureType type = fSource.getSchema();
        Query attOnlyQuery = new Query(type.getTypeName());
        List<String> propNames = new ArrayList<String>(type.getAttributeCount() - 1);

        for (int i = 0; i < type.getAttributeCount(); i++) {
            if (type.getDescriptor(i) instanceof GeometryDescriptor) {
                continue;
            }

            propNames.add(type.getDescriptor(i).getLocalName());
        }

        attOnlyQuery.setPropertyNames(propNames);

        SimpleFeatureCollection results = fSource.getFeatures(attOnlyQuery);
        SimpleFeatureType resultSchema = results.getSchema();
        assertEquals(propNames.size(), resultSchema.getAttributeCount());

        for (int i = 0; i < propNames.size(); i++) {
            assertEquals(propNames.get(i), resultSchema.getDescriptor(i).getLocalName());
        }

        // the problem described in GEOT-408 arises in attribute reader, so
        // we must to try fetching features
        SimpleFeatureIterator iterator = results.features();
        SimpleFeature feature = iterator.next();
        iterator.close();
        assertNotNull(feature);

        // the id must be grabed correctly.
        // this exercises the fact that although the geometry is not included
        // in the request, it must be fecthed anyway to obtain the
        // SeShape.getFeatureId()
        // getID() should throw an exception if the feature is was not grabed
        // (see
        // ArcSDEAttributeReader.readFID().
        String id = feature.getID();
        assertNotNull(id);
        assertFalse(id.endsWith(".-1"));
        assertFalse(id.endsWith(".0"));
    }

    /** Test that FID filters are correctly handled */
    @Test
    public void testFidFilters() throws Exception {
        final DataStore ds = testData.getDataStore();
        final String typeName = testData.getTempTableName();

        // grab some fids
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                ds.getFeatureReader(new Query(typeName), Transaction.AUTO_COMMIT);
        List<FeatureId> fids = new ArrayList<FeatureId>();

        while (reader.hasNext()) {
            fids.add(ff.featureId(reader.next().getID()));

            // skip one
            if (reader.hasNext()) {
                reader.next();
            }
        }

        reader.close();

        Id filter = ff.id(new HashSet<FeatureId>(fids));

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        Query query = new Query(typeName, filter);
        SimpleFeatureCollection results = source.getFeatures(query);

        assertEquals(fids.size(), results.size());
        SimpleFeatureIterator iterator = results.features();
        try {
            while (iterator.hasNext()) {
                String fid = iterator.next().getID();
                assertTrue(
                        "a fid not included in query was returned: " + fid,
                        fids.contains(ff.featureId(fid)));
            }
        } finally {
            iterator.close();
        }
    }

    @Test
    public void testMoreThan1000FidFilters() throws Exception {
        testFidFilters(1000);
        testFidFilters(1001);
        testFidFilters(2000);
        testFidFilters(2001);
    }

    private void testFidFilters(final int numFids) throws Exception {
        final DataStore ds = testData.getDataStore();
        final String typeName = testData.getTempTableName();

        // grab some fids
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                ds.getFeatureReader(new Query(typeName), Transaction.AUTO_COMMIT);

        final String idTemplate;
        Set<FeatureId> fids =
                new TreeSet<FeatureId>(
                        new Comparator<FeatureId>() {
                            public int compare(FeatureId o1, FeatureId o2) {
                                return o1.getID().compareTo(o2.getID());
                            }
                        });

        try {
            String id = reader.next().getID();
            fids.add(ff.featureId(id));
            idTemplate = id.substring(0, id.length() - 1);
        } finally {
            reader.close();
        }

        int x = 1000;
        while (fids.size() < numFids) {
            fids.add(ff.featureId(idTemplate + x++));
        }

        Id filter = ff.id(fids);

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        Query query = new Query(typeName, filter);
        SimpleFeatureCollection results = source.getFeatures(query);

        assertEquals(1, results.size());
        SimpleFeatureIterator iterator = results.features();
        try {
            while (iterator.hasNext()) {
                String fid = iterator.next().getID();
                assertTrue(
                        "a fid not included in query was returned: " + fid,
                        fids.contains(ff.featureId(fid)));
            }
        } finally {
            iterator.close();
        }
    }

    /** test that getFeatureSource over an sde layer works */
    @Test
    public void testGetFeatureSourcePoint() throws IOException, SeException {
        testGetFeatureSource(store.getFeatureSource(testData.getTempTableName()));
    }

    @Test
    public void testGetFeatures() throws Exception {
        final String table = testData.getTempTableName();
        LOGGER.fine("getting all features from " + table);

        SimpleFeatureSource source = store.getFeatureSource(table);
        int expectedCount = 8;
        int fCount = source.getCount(Query.ALL);
        String failMsg = "Expected and returned result count does not match";
        assertEquals(failMsg, expectedCount, fCount);

        SimpleFeatureCollection fresults = source.getFeatures();
        SimpleFeatureCollection features = fresults;
        failMsg = "FeatureResults.size and .collection().size thoes not match";
        assertEquals(failMsg, fCount, features.size());
        LOGGER.fine("fetched " + fCount + " features for " + table + " layer, OK");
    }

    @Test
    public void testSQLFilter() throws Exception {
        int expected = 4;
        Filter filter = CQL.toFilter("INT32_COL < 5");
        SimpleFeatureSource fsource = store.getFeatureSource(testData.getTempTableName());
        testFilter(filter, fsource, expected);
    }

    @Test
    public void testBBoxFilter() throws Exception {
        String typeName = testData.getTempTableName();
        SimpleFeatureSource fs = store.getFeatureSource(typeName);
        SimpleFeatureType schema = fs.getSchema();
        BBOX bboxFilter =
                ff.bbox(
                        schema.getGeometryDescriptor().getLocalName(),
                        -180,
                        -90,
                        180,
                        90,
                        schema.getCoordinateReferenceSystem().getName().getCode());

        int expected = 0;
        SimpleFeatureIterator features = fs.getFeatures().features();
        try {
            while (features.hasNext()) {
                SimpleFeature next = features.next();
                if (bboxFilter.evaluate(next)) {
                    expected++;
                }
            }
        } finally {
            features.close();
        }
        assertTrue(expected > 0);
        testFilter(bboxFilter, fs, expected);
    }

    @Test
    public void testQueryCapabilities() throws Exception {
        final String typeName = testData.getTempTableName();
        SimpleFeatureSource fs = store.getFeatureSource(typeName);

        QueryCapabilities queryCapabilities = fs.getQueryCapabilities();
        assertFalse(queryCapabilities.isOffsetSupported());
        assertTrue(queryCapabilities.isReliableFIDSupported());

        assertFalse(queryCapabilities.supportsSorting(new SortBy[] {NATURAL_ORDER}));
        assertFalse(queryCapabilities.supportsSorting(new SortBy[] {REVERSE_ORDER}));
        assertFalse(
                queryCapabilities.supportsSorting(
                        new SortBy[] {ff.sort("nonExistent", ASCENDING)}));

        assertFalse(
                queryCapabilities.supportsSorting(
                        new SortBy[] {ff.sort("nonExistent", ASCENDING)}));

        // no sorting on geometry columns!
        String geometryAttribute = fs.getSchema().getGeometryDescriptor().getLocalName();
        assertFalse(
                queryCapabilities.supportsSorting(
                        new SortBy[] {ff.sort(geometryAttribute, ASCENDING)}));

        SortBy[] supported = {
            ff.sort("INT32_COL", ASCENDING), //
            ff.sort("INT32_COL", DESCENDING), //
            ff.sort("INT16_COL", ASCENDING), //
            ff.sort("INT16_COL", DESCENDING), //
            ff.sort("FLOAT32_COL", ASCENDING), //
            ff.sort("FLOAT32_COL", DESCENDING), //
            ff.sort("FLOAT64_COL", ASCENDING), //
            ff.sort("FLOAT64_COL", DESCENDING), //
            ff.sort("STRING_COL", ASCENDING), //
            ff.sort("STRING_COL", DESCENDING), //
            ff.sort("NSTRING_COL", ASCENDING), //
            ff.sort("NSTRING_COL", DESCENDING), //
            ff.sort("DATE_COL", ASCENDING), //
            ff.sort("DATE_COL", ASCENDING)
        };

        assertTrue(queryCapabilities.supportsSorting(supported));
    }

    @Test
    public void testFilterDateColumn() throws Exception {
        final String typeName = testData.getTempTableName();
        SimpleFeatureSource fs = store.getFeatureSource(typeName);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        // Year, month, date, hour, minute, second.
        cal.set(2004, 06, 1, 0, 0, 0);

        Filter filter = ff.equals(ff.property("DATE_COL"), ff.literal(cal.getTime()));
        testFilter(filter, fs, 1);

        cal.set(Calendar.DAY_OF_MONTH, 2);
        filter = ff.greaterOrEqual(ff.property("DATE_COL"), ff.literal(cal));
        testFilter(filter, fs, 7);

        Date date1 = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 6);
        Date date2 = cal.getTime();

        filter =
                ff.and(
                        Arrays.asList( //
                                (Filter) ff.greater(ff.property("DATE_COL"), ff.literal(date1)), //
                                (Filter) ff.less(ff.property("DATE_COL"), ff.literal(date2)) //
                                ));
        testFilter(filter, fs, 3);
    }

    @Test
    public void testSorting() throws Exception {
        final String typeName = testData.getTempTableName();
        SimpleFeatureSource fs = store.getFeatureSource(typeName);

        Query query = new Query(typeName);

        final String sortAtt = "INT32_COL";
        SortBy[] sortBy;
        SimpleFeatureIterator features;

        sortBy = new SortBy[] {ff.sort(sortAtt, ASCENDING)};
        query.setSortBy(sortBy);
        features = fs.getFeatures(query).features();
        try {
            Integer previous = Integer.valueOf(Integer.MIN_VALUE);
            while (features.hasNext()) {
                Integer intVal = (Integer) features.next().getAttribute(sortAtt);
                assertTrue(
                        previous + " < " + intVal + "?", previous.intValue() < intVal.intValue());
                previous = intVal;
            }
        } finally {
            features.close();
        }

        sortBy = new SortBy[] {ff.sort(sortAtt, DESCENDING)};
        query.setSortBy(sortBy);
        features = fs.getFeatures(query).features();
        try {
            Integer previous = Integer.valueOf(Integer.MAX_VALUE);
            while (features.hasNext()) {
                Integer intVal = (Integer) features.next().getAttribute(sortAtt);
                assertTrue(
                        previous + " > " + intVal + "?", previous.intValue() > intVal.intValue());
                previous = intVal;
            }
        } finally {
            features.close();
        }

        sortBy = new SortBy[] {ff.sort(sortAtt, DESCENDING), ff.sort("FLOAT32_COL", ASCENDING)};
        query.setSortBy(sortBy);
        features = fs.getFeatures(query).features();
        try {
            Integer previous = Integer.valueOf(Integer.MAX_VALUE);
            while (features.hasNext()) {
                Integer intVal = (Integer) features.next().getAttribute(sortAtt);
                assertTrue(
                        previous + " > " + intVal + "?", previous.intValue() > intVal.intValue());
                previous = intVal;
            }
        } finally {
            features.close();
        }
    }

    /**
     * A bbox filter with an empty attribute name should work against the default geometry attribute
     */
    @SuppressWarnings("nls")
    @Test
    public void testBboxFilterWithEmptyAttributeName() throws Exception {
        BBOX emptyAttNameFilter = ff.bbox("", -10, -10, 10, 10, "EPSG:4326");
        String typeName = testData.getTempTableName();

        SimpleFeatureSource source;
        source = store.getFeatureSource(typeName);
        SimpleFeatureCollection features;
        features = source.getFeatures(emptyAttNameFilter);

        SimpleFeatureIterator iterator = features.features();
        try {
            assertTrue(iterator.hasNext());
        } finally {
            iterator.close();
        }
    }

    // ///////////////// HELPER FUNCTIONS ////////////////////////

    /**
     * for a given FeatureSource, makes the following assertions:
     *
     * <ul>
     *   <li>it's not null
     *   <li>.getDataStore() != null
     *   <li>.getDataStore() == the datastore obtained in setUp()
     *   <li>.getSchema() != null
     *   <li>.getBounds() != null
     *   <li>.getBounds().isNull() == false
     *   <li>.getFeatures().getCounr() > 0
     *   <li>.getFeatures().reader().hasNex() == true
     *   <li>.getFeatures().reader().next() != null
     * </ul>
     */
    private void testGetFeatureSource(SimpleFeatureSource fsource) throws IOException {
        assertNotNull(fsource);
        assertNotNull(fsource.getDataStore());
        assertEquals(fsource.getDataStore(), store);
        assertNotNull(fsource.getSchema());

        SimpleFeatureCollection results = fsource.getFeatures();
        int count = results.size();
        assertTrue("size returns " + count, count > 0);
        LOGGER.fine("feature count: " + count);

        Envelope env1;
        Envelope env2;
        env1 = fsource.getBounds();
        assertNotNull(env1);
        assertFalse(env1.isNull());
        env2 = fsource.getBounds(Query.ALL);
        assertNotNull(env2);
        assertFalse(env2.isNull());
        env1 = results.getBounds();
        assertNotNull(env1);
        assertFalse(env1.isNull());

        SimpleFeatureIterator reader = results.features();
        assertTrue(reader.hasNext());

        try {
            assertNotNull(reader.next());
        } catch (NoSuchElementException ex) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ex);
            fail(ex.getMessage());
        }

        reader.close();
    }

    private void testFilter(Filter filter, SimpleFeatureSource fsource, int expected)
            throws IOException {
        SimpleFeatureCollection fc = fsource.getFeatures(filter);

        SimpleFeatureIterator fi = fc.features();
        try {
            int numFeat = 0;
            while (fi.hasNext()) {
                fi.next();
                numFeat++;
            }

            String failMsg =
                    "Fully fetched features size and estimated num features count does not match";
            assertEquals(failMsg, expected, numFeat);
        } finally {
            fi.close();
        }
    }
}
