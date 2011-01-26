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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
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

import com.esri.sde.sdk.client.SeException;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Stopwatch;

/**
 * {@link ArcSdeFeatureSource} test cases
 * 
 * @author Gabriel Roldan
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *         /org/geotools/arcsde/data/ArcSDEDataStoreTest.java $
 * @version $Id$
 */
public class ArcSDEFeatureSourceTest {
    /** package logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ArcSDEFeatureSourceTest.class.getPackage().getName());

    /** DOCUMENT ME! */
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
     * <p>
     * I found experimentally that until 24 simultaneous streams can be opened by a single
     * connection. Each featurereader has an ArcSDE stream opened until its <code>close()</code>
     * method is called or hasNext() returns flase, wich automatically closes the stream. If more
     * than 24 simultaneous streams are tryied to be opened upon a single SeConnection, an exception
     * is thrown by de Java ArcSDE API saying that a "NETWORK I/O OPERATION FAILED"
     * </p>
     * 
     * @throws IOException
     *             DOCUMENT ME!
     * @throws IllegalAttributeException
     *             DOCUMENT ME!
     * @throws SeException
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

    /**
     * Checks that a query returns only the specified attributes.
     * 
     * @throws IOException
     * @throws IllegalAttributeException
     * @throws SeException
     */
    @Test
    public void testRestrictsAttributes() throws IOException, IllegalAttributeException,
            SeException {
        final String typeName = testData.getTempTableName();
        final DataStore ds = testData.getDataStore();
        final SimpleFeatureType schema = ds.getSchema(typeName);
        final int queriedAttributeCount = schema.getAttributeCount() - 3;
        final String[] queryAtts = new String[queriedAttributeCount];

        for (int i = 0; i < queryAtts.length; i++) {
            queryAtts[i] = schema.getDescriptor(i).getLocalName();
        }

        // build the query asking for a subset of attributes
        final Query query = new DefaultQuery(typeName, Filter.INCLUDE, queryAtts);

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
     * 
     * @throws IOException
     *             DOCUMENT ME!
     * @throws IllegalAttributeException
     *             DOCUMENT ME!
     * @throws SeException
     */
    @Test
    public void testRespectsAttributeOrder() throws IOException, IllegalAttributeException,
            SeException {
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
        final Query query = new DefaultQuery(typeName, Filter.INCLUDE, queryAtts);

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
     * 
     * @throws IOException
     * @throws IllegalAttributeException
     * @throws SeException
     * @throws CQLException
     */
    @Test
    public void testRespectsQueryAttributes() throws IOException, IllegalAttributeException,
            SeException, CQLException {
        final String typeName = testData.getTempTableName();
        final DataStore ds = testData.getDataStore();
        final FeatureSource<SimpleFeatureType, SimpleFeature> fs = ds.getFeatureSource(typeName);

        final String[] queryAtts = { "SHAPE" };
        final Filter filter = CQL.toFilter("INT32_COL = 1");

        // build the query asking for a subset of attributes
        final Query query = new DefaultQuery(typeName, filter, queryAtts);

        FeatureCollection<SimpleFeatureType, SimpleFeature> features = fs.getFeatures(query);
        SimpleFeatureType resultSchema = features.getSchema();

        assertEquals(1, resultSchema.getAttributeCount());
        assertEquals("SHAPE", resultSchema.getDescriptor(0).getLocalName());

        Feature feature = null;
        FeatureIterator<SimpleFeature> iterator = null;
        try {
            iterator = features.features();
            feature = iterator.next();
        } finally {
            if (iterator != null) {
                features.close(iterator);
            }
        }

        assertEquals(resultSchema, feature.getType());
    }

    private boolean testNext(FeatureReader<SimpleFeatureType, SimpleFeature> r) throws IOException,
            IllegalAttributeException {
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
        Query q = new DefaultQuery(typeName, Filter.INCLUDE);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(q,
                Transaction.AUTO_COMMIT);
        SimpleFeatureType retType = reader.getFeatureType();
        assertNotNull(retType.getGeometryDescriptor());
        assertTrue(reader.hasNext());

        return reader;
    }

    /**
     * tests the datastore behavior when fetching data based on mixed queries.
     * <p>
     * "Mixed queries" refers to mixing alphanumeric and geometry based filters, since that is the
     * natural separation of things in the Esri Java API for ArcSDE. This is necessary since mixed
     * queries sometimes are problematic. So this test ensures that:
     * <ul>
     * <li>A mixed query respects all filters</li>
     * <li>A mixed query does not fails when getBounds() is performed</li>
     * <li>A mixed query does not fails when size() is performed</li>
     * </ul>
     * </p>
     * 
     * @throws Exception
     */
    @Test
    public void testMixedQueries() throws Exception {
        final int EXPECTED_RESULT_COUNT = 1;
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = store.getFeatureSource(testData
                .getTempTableName());
        SimpleFeatureType schema = fs.getSchema();
        Filter bboxFilter = ff.bbox(schema.getGeometryDescriptor().getLocalName(), -60, -55, -40,
                -20, schema.getCoordinateReferenceSystem().getName().getCode());
        Filter sqlFilter = CQL.toFilter("INT32_COL < 5");
        LOGGER.fine("Geometry filter: " + bboxFilter);
        LOGGER.fine("SQL filter: " + sqlFilter);

        And mixedFilter = ff.and(sqlFilter, bboxFilter);

        Not not = ff.not(ff.id(Collections.singleton(ff.featureId(testData.getTempTableName()
                + ".90000"))));

        mixedFilter = ff.and(mixedFilter, not);

        LOGGER.fine("Mixed filter: " + mixedFilter);

        // verify both filter constraints are met
        try {
            testFilter(mixedFilter, fs, EXPECTED_RESULT_COUNT);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        // check that getBounds and size do function
        FeatureIterator<SimpleFeature> reader = null;
        FeatureCollection<SimpleFeatureType, SimpleFeature> results = fs.getFeatures(mixedFilter);
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
     * 
     * @throws Exception
     */
    @Test
    public void testAttributeOnlyQuery() throws Exception {
        DataStore ds = testData.getDataStore();
        FeatureSource<SimpleFeatureType, SimpleFeature> fSource = ds.getFeatureSource(testData
                .getTempTableName());
        SimpleFeatureType type = fSource.getSchema();
        DefaultQuery attOnlyQuery = new DefaultQuery(type.getTypeName());
        List propNames = new ArrayList(type.getAttributeCount() - 1);

        for (int i = 0; i < type.getAttributeCount(); i++) {
            if (type.getDescriptor(i) instanceof GeometryDescriptor) {
                continue;
            }

            propNames.add(type.getDescriptor(i).getLocalName());
        }

        attOnlyQuery.setPropertyNames(propNames);

        FeatureCollection<SimpleFeatureType, SimpleFeature> results = fSource
                .getFeatures(attOnlyQuery);
        SimpleFeatureType resultSchema = results.getSchema();
        assertEquals(propNames.size(), resultSchema.getAttributeCount());

        for (int i = 0; i < propNames.size(); i++) {
            assertEquals(propNames.get(i), resultSchema.getDescriptor(i).getLocalName());
        }

        // the problem described in GEOT-408 arises in attribute reader, so
        // we must to try fetching features
        FeatureIterator<SimpleFeature> iterator = results.features();
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

    /**
     * Test that FID filters are correctly handled
     * 
     * @throws Exception
     */
    @Test
    public void testFidFilters() throws Exception {
        final DataStore ds = testData.getDataStore();
        final String typeName = testData.getTempTableName();

        // grab some fids
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = ds.getFeatureReader(
                new DefaultQuery(typeName), Transaction.AUTO_COMMIT);
        List fids = new ArrayList();

        while (reader.hasNext()) {
            fids.add(ff.featureId(reader.next().getID()));

            // skip one
            if (reader.hasNext()) {
                reader.next();
            }
        }

        reader.close();

        Id filter = ff.id(new HashSet(fids));

        FeatureSource<SimpleFeatureType, SimpleFeature> source = ds.getFeatureSource(typeName);
        Query query = new DefaultQuery(typeName, filter);
        FeatureCollection<SimpleFeatureType, SimpleFeature> results = source.getFeatures(query);

        assertEquals(fids.size(), results.size());
        FeatureIterator<SimpleFeature> iterator = results.features();

        while (iterator.hasNext()) {
            String fid = iterator.next().getID();
            assertTrue("a fid not included in query was returned: " + fid, fids.contains(ff
                    .featureId(fid)));
        }
        results.close(iterator);
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
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = ds.getFeatureReader(
                new DefaultQuery(typeName), Transaction.AUTO_COMMIT);

        final String idTemplate;
        Set<FeatureId> fids = new TreeSet<FeatureId>(new Comparator<FeatureId>() {
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

        FeatureSource<SimpleFeatureType, SimpleFeature> source = ds.getFeatureSource(typeName);
        Query query = new DefaultQuery(typeName, filter);
        FeatureCollection<SimpleFeatureType, SimpleFeature> results = source.getFeatures(query);

        assertEquals(1, results.size());
        FeatureIterator<SimpleFeature> iterator = results.features();

        while (iterator.hasNext()) {
            String fid = iterator.next().getID();
            assertTrue("a fid not included in query was returned: " + fid, fids.contains(ff
                    .featureId(fid)));
        }
        results.close(iterator);
    }

    /**
     * test that getFeatureSource over an sde layer works
     * 
     * @throws IOException
     * @throws SeException
     */
    @Test
    public void testGetFeatureSourcePoint() throws IOException, SeException {
        testGetFeatureSource(store.getFeatureSource(testData.getTempTableName()));
    }

    @Test
    public void testGetFeatures() throws Exception {
        final String table = testData.getTempTableName();
        LOGGER.fine("getting all features from " + table);

        FeatureSource<SimpleFeatureType, SimpleFeature> source = store.getFeatureSource(table);
        int expectedCount = 8;
        int fCount = source.getCount(Query.ALL);
        String failMsg = "Expected and returned result count does not match";
        assertEquals(failMsg, expectedCount, fCount);

        FeatureCollection<SimpleFeatureType, SimpleFeature> fresults = source.getFeatures();
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = fresults;
        failMsg = "FeatureResults.size and .collection().size thoes not match";
        assertEquals(failMsg, fCount, features.size());
        LOGGER.fine("fetched " + fCount + " features for " + table + " layer, OK");
    }

    @Test
    public void testSQLFilter() throws Exception {
        int expected = 4;
        Filter filter = CQL.toFilter("INT32_COL < 5");
        FeatureSource<SimpleFeatureType, SimpleFeature> fsource = store.getFeatureSource(testData
                .getTempTableName());
        testFilter(filter, fsource, expected);
    }

    @Test
    public void testBBoxFilter() throws Exception {
        String typeName = testData.getTempTableName();
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = store.getFeatureSource(typeName);
        SimpleFeatureType schema = fs.getSchema();
        BBOX bboxFilter = ff.bbox(schema.getGeometryDescriptor().getLocalName(), -180, -90, 180,
                90, schema.getCoordinateReferenceSystem().getName().getCode());

        int expected = 0;
        FeatureIterator<SimpleFeature> features = fs.getFeatures().features();
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
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = store.getFeatureSource(typeName);

        QueryCapabilities queryCapabilities = fs.getQueryCapabilities();
        assertFalse(queryCapabilities.isOffsetSupported());
        assertTrue(queryCapabilities.isReliableFIDSupported());

        assertFalse(queryCapabilities.supportsSorting(new SortBy[] { NATURAL_ORDER }));
        assertFalse(queryCapabilities.supportsSorting(new SortBy[] { REVERSE_ORDER }));
        assertFalse(queryCapabilities.supportsSorting(new SortBy[] { ff.sort("nonExistent",
                ASCENDING) }));

        assertFalse(queryCapabilities.supportsSorting(new SortBy[] { ff.sort("nonExistent",
                ASCENDING) }));

        // no sorting on geometry columns!
        String geometryAttribute = fs.getSchema().getGeometryDescriptor().getLocalName();
        assertFalse(queryCapabilities.supportsSorting(new SortBy[] { ff.sort(geometryAttribute,
                ASCENDING) }));

        SortBy[] supported = { ff.sort("INT32_COL", ASCENDING),//
                ff.sort("INT32_COL", DESCENDING),//
                ff.sort("INT16_COL", ASCENDING),//
                ff.sort("INT16_COL", DESCENDING),//
                ff.sort("FLOAT32_COL", ASCENDING),//
                ff.sort("FLOAT32_COL", DESCENDING),//
                ff.sort("FLOAT64_COL", ASCENDING),//
                ff.sort("FLOAT64_COL", DESCENDING),//
                ff.sort("STRING_COL", ASCENDING),//
                ff.sort("STRING_COL", DESCENDING),//
                ff.sort("NSTRING_COL", ASCENDING),//
                ff.sort("NSTRING_COL", DESCENDING),//
                ff.sort("DATE_COL", ASCENDING),//
                ff.sort("DATE_COL", ASCENDING) };

        assertTrue(queryCapabilities.supportsSorting(supported));

    }

    @Test
    public void testSorting() throws Exception {
        final String typeName = testData.getTempTableName();
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = store.getFeatureSource(typeName);

        DefaultQuery query = new DefaultQuery(typeName);

        final String sortAtt = "INT32_COL";
        SortBy[] sortBy;
        FeatureIterator<SimpleFeature> features;

        sortBy = new SortBy[] { ff.sort(sortAtt, ASCENDING) };
        query.setSortBy(sortBy);
        features = fs.getFeatures(query).features();
        try {
            Integer previous = Integer.valueOf(Integer.MIN_VALUE);
            while (features.hasNext()) {
                Integer intVal = (Integer) features.next().getAttribute(sortAtt);
                assertTrue(previous + " < " + intVal + "?", previous.intValue() < intVal.intValue());
                previous = intVal;
            }
        } finally {
            features.close();
        }

        sortBy = new SortBy[] { ff.sort(sortAtt, DESCENDING) };
        query.setSortBy(sortBy);
        features = fs.getFeatures(query).features();
        try {
            Integer previous = Integer.valueOf(Integer.MAX_VALUE);
            while (features.hasNext()) {
                Integer intVal = (Integer) features.next().getAttribute(sortAtt);
                assertTrue(previous + " > " + intVal + "?", previous.intValue() > intVal.intValue());
                previous = intVal;
            }
        } finally {
            features.close();
        }

        sortBy = new SortBy[] { ff.sort(sortAtt, DESCENDING), ff.sort("FLOAT32_COL", ASCENDING) };
        query.setSortBy(sortBy);
        features = fs.getFeatures(query).features();
        try {
            Integer previous = Integer.valueOf(Integer.MAX_VALUE);
            while (features.hasNext()) {
                Integer intVal = (Integer) features.next().getAttribute(sortAtt);
                assertTrue(previous + " > " + intVal + "?", previous.intValue() > intVal.intValue());
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
        SimpleFeatureType schema = store.getSchema(typeName);

        FeatureSource<SimpleFeatureType, SimpleFeature> source;
        source = store.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> features;
        features = source.getFeatures(emptyAttNameFilter);

        FeatureIterator<SimpleFeature> iterator = features.features();
        try {
            assertTrue(iterator.hasNext());
        } finally {
            iterator.close();
        }
    }

    // ///////////////// HELPER FUNCTIONS ////////////////////////

    /**
     * for a given FeatureSource, makes the following assertions:
     * <ul>
     * <li>it's not null</li>
     * <li>.getDataStore() != null</li>
     * <li>.getDataStore() == the datastore obtained in setUp()</li>
     * <li>.getSchema() != null</li>
     * <li>.getBounds() != null</li>
     * <li>.getBounds().isNull() == false</li>
     * <li>.getFeatures().getCounr() > 0</li>
     * <li>.getFeatures().reader().hasNex() == true</li>
     * <li>.getFeatures().reader().next() != null</li>
     * </ul>
     * 
     * @param fsource
     *            DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    private void testGetFeatureSource(FeatureSource<SimpleFeatureType, SimpleFeature> fsource)
            throws IOException {
        assertNotNull(fsource);
        assertNotNull(fsource.getDataStore());
        assertEquals(fsource.getDataStore(), store);
        assertNotNull(fsource.getSchema());

        FeatureCollection<SimpleFeatureType, SimpleFeature> results = fsource.getFeatures();
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

        FeatureIterator<SimpleFeature> reader = results.features();
        assertTrue(reader.hasNext());

        try {
            assertNotNull(reader.next());
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }

        reader.close();
    }

    private void testFilter(Filter filter, FeatureSource<SimpleFeatureType, SimpleFeature> fsource,
            int expected) throws IOException {
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = fsource.getFeatures(filter);

        FeatureIterator<SimpleFeature> fi = fc.features();
        try {
            int numFeat = 0;
            while (fi.hasNext()) {
                fi.next();
                numFeat++;
            }

            String failMsg = "Fully fetched features size and estimated num features count does not match";
            assertEquals(failMsg, expected, numFeat);
        } finally {
            fc.close(fi);
        }
    }

    class Time {
        long getTypeNames, getCount, getBounds, getFeatures, iterate;

        public void add(Time o) {
            getTypeNames += o.getTypeNames;
            getCount += o.getCount;
            getBounds += o.getBounds;
            getFeatures += o.getFeatures;
            iterate += o.iterate;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Time[");
            sb.append("\n getTypeNames=").append(getTypeNames).append("\n getCount=\t").append(
                    getCount).append("\n getBounds\t").append(getBounds)
                    .append("\n getFeatures=\t").append(getFeatures).append("\n iterate=\t")
                    .append(iterate).append("\n]");
            return sb.toString();
        }

        public long getTotal() {
            return getTypeNames + getBounds + getCount + getFeatures + iterate;
        }
    }

    int count;

    /**
     * NOTE: manually run it with pool.minConnections == pool.maxConnections to avoid the
     * penalization of creating the connections to reach maxConnections while the test runs
     * 
     * @throws Exception
     */
    Time testConcurrencyPerformance(final int nExecutions, final int nThreads) throws Exception {
        final String table = testData.getTempTableName();

        class TestCommand implements Callable<Time> {

            public Time call() throws Exception {
                int mine = ++count;
                // System.out.println("Starting execution " + count);
                Stopwatch sw = new Stopwatch();
                Time result = new Time();

                sw.start();
                store.getTypeNames();
                sw.stop();
                result.getTypeNames = sw.getTime();
                sw.reset();

                FeatureSource<SimpleFeatureType, SimpleFeature> source;
                source = store.getFeatureSource(table);

                sw.start();
                int fCount = source.getCount(Query.ALL);
                sw.stop();
                result.getCount = sw.getTime();
                sw.reset();

                sw.start();
                ReferencedEnvelope bounds = source.getBounds();
                sw.stop();
                result.getBounds = sw.getTime();
                sw.reset();

                FeatureCollection<SimpleFeatureType, SimpleFeature> fcol;

                sw.start();
                fcol = source.getFeatures();
                FeatureIterator<SimpleFeature> features = fcol.features();
                sw.stop();
                result.getFeatures = sw.getTime();
                sw.reset();

                sw.start();
                while (features.hasNext()) {
                    features.next();
                }
                sw.stop();
                result.iterate = sw.getTime();
                sw.reset();
                features.close();

                // System.out.println("Finishing execution " + mine);
                return result;
            }
        }

        // ignore initialization time
        new TestCommand().call();

        ExecutorService executor = Executors.newFixedThreadPool(nThreads);

        List<FutureTask<Time>> tasks = new ArrayList<FutureTask<Time>>(nExecutions);

        for (int i = 0; i < nExecutions; i++) {
            TestCommand command = new TestCommand();
            FutureTask<Time> task = new FutureTask<Time>(command);
            executor.execute(task);
            tasks.add(task);
        }

        Thread.currentThread().sleep(10000);

        Time totalTime = new Time();
        List<Time> results;
        while (true) {
            for (ListIterator<FutureTask<Time>> it = tasks.listIterator(); it.hasNext();) {
                FutureTask<Time> next = it.next();
                if (next.isDone()) {
                    it.remove();
                    Time taskTime = next.get();
                    totalTime.add(taskTime);
                }
            }
            if (tasks.size() == 0) {
                break;
            }
            Thread.currentThread().sleep(100);
        }

        return totalTime;
    }

    public static void main(String[] argv) {

        final int nExecutions = 30;

        // how many connections to use for the tests. Make sure pool.maxConnections is set to a
        // large enough value on testparams.properties
        final int[] connections = { 10 };
        final int[] testThreads = { 1, 5, 10, 15, 20 };

        ArcSDEFeatureSourceTest test = new ArcSDEFeatureSourceTest();
        try {
            testData = new TestData();
            testData.setUp();
            final boolean insertTestData = true;
            testData.createTempTable(insertTestData);
            testData.getConnectionPool().close();

            for (int nConnections : connections) {
                for (int threads : testThreads) {

                    testData.getConProps().put("pool.minConnections", String.valueOf(nConnections));
                    testData.getConProps().put("pool.maxConnections", String.valueOf(nConnections));

                    testData.tearDown(false, true);
                    if (store != null) {
                        store.dispose();
                    }
                    store = testData.getDataStore();

                    Time t = test.testConcurrencyPerformance(nExecutions, threads);

                    final int usedConnections = store.connectionPool.getPoolSize();

                    testData.tearDown(false, false);
                    store.dispose();

                    System.out.println("\n\n" + nExecutions + " executions on " + threads
                            + " threads " + " with " + usedConnections
                            + " connections \nexecuted in " + t.getTotal() + "ms. " + t
                            + "\n avg: " + (t.getTotal() / nExecutions));

                }
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
