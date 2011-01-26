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

import static org.geotools.arcsde.data.TestData.TEST_TABLE_COLS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFinder;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;

import com.esri.sde.sdk.client.SeException;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;

/**
 * ArcSDEDAtaStore test cases
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *         /org/geotools/arcsde/data/ArcSDEDataStoreTest.java $
 * @version $Id$
 */
public class ArcSDEDataStoreTest {
    /** package logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ArcSDEDataStoreTest.class.getPackage().getName());

    private static TestData testData;

    /** an ArcSDEDataStore created on setUp() to run tests against */
    private DataStore store;

    /** a filter factory for testing */
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

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

    /**
     * loads {@code testData/testparams.properties} into a Properties object, wich is used to obtain
     * test tables names and is used as parameter to find the DataStore
     */
    @Before
    public void setUp() throws Exception {
        if (testData == null) {
            oneTimeSetUp();
        }
        this.store = testData.getDataStore();
    }

    @After
    public void tearDown() throws Exception {
        this.store = null;
    }

    @Test
    public void testDataStoreFinderFindsIt() throws IOException {
        DataStore sdeDs = null;

        DataStoreFinder.scanForPlugins();
        sdeDs = DataStoreFinder.getDataStore(testData.getConProps());
        assertNotNull(sdeDs);
        String failMsg = sdeDs + " is not an ArcSDEDataStore";
        assertTrue(failMsg, (sdeDs instanceof ArcSDEDataStore));
        LOGGER.fine("testFinder OK :" + sdeDs.getClass().getName());
    }

    @Test
    public void testDataAccessFinderFindsIt() throws IOException {

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.putAll(testData.getConProps());

        DataAccess<? extends FeatureType, ? extends Feature> dataStore;
        dataStore = DataAccessFinder.getDataStore(params);

        assertNotNull(dataStore);
        String failMsg = dataStore + " is not an ArcSDEDataStore";
        assertTrue(failMsg, dataStore instanceof ArcSDEDataStore);
    }

    @Test
    public void testGetInfo() {
        ServiceInfo info = store.getInfo();
        assertNotNull(info);
        assertNotNull(info.getTitle());
        assertNotNull(info.getDescription());
        assertNotNull(info.getSchema());
    }

    @Test
    public void testGet() {
        ServiceInfo info = store.getInfo();
        assertNotNull(info);
        assertNotNull(info.getTitle());
        assertNotNull(info.getDescription());
        assertNotNull(info.getSchema());
    }

    @Test
    public void testDispose() throws IOException {
        store.dispose();
        try {
            ((ArcSDEDataStore) store).getSession(Transaction.AUTO_COMMIT);
            fail("Expected IllegalStateException when the datastore has been disposed");
        } catch (IllegalStateException e) {
            assertTrue(true);
        } finally {
            // dispose test data so next test does not fail due to pool being
            // closed
            testData.tearDown(false, true);
            testData = null;
        }
    }

    /**
     * test that a ArcSDEDataStore that connects to de configured test database contains the tables
     * defined by the parameters "point_table", "line_table" and "polygon_table", wether ot not
     * they're defined as single table names or as full qualified sde table names (i.e.
     * SDE.SDE.TEST_POINT)
     * 
     * @throws IOException
     * @throws SeException
     */
    @Test
    public void testGetTypeNames() throws IOException, SeException {
        String[] featureTypes = store.getTypeNames();
        assertNotNull(featureTypes);

        // if (LOGGER.isLoggable(Level.FINE)) {
        // for (int i = 0; i < featureTypes.length; i++)
        // System.out.println(featureTypes[i]);
        // }
        testTypeExists(featureTypes, testData.getTempTableName());
    }

    /**
     * tests that the schema for the defined tests tables are returned.
     */
    @Test
    public void testGetSchema() throws IOException, SeException {
        SimpleFeatureType schema;

        schema = store.getSchema(testData.getTempTableName());
        assertNotNull(schema);
        // ROW_ID is not included in TEST_TABLE_COLS
        assertEquals(TEST_TABLE_COLS.length, schema.getAttributeCount());

        for (int i = 0; i < TEST_TABLE_COLS.length; i++) {
            assertEquals("at index" + i, TEST_TABLE_COLS[i], schema.getDescriptor(i).getLocalName());
        }
        assertFalse(schema.getDescriptor(0).isNillable());
        assertTrue(schema.getDescriptor(1).isNillable());
    }

    /**
     * Tests the creation of new feature types, with CRS and all.
     * <p>
     * This test also ensures that the arcsde datastore is able of creating schemas where the
     * geometry attribute is not the last one. This is important since to do so, the ArcSDE
     * datastore must break the usual way of creating schemas with the ArcSDE Java API, in which one
     * first creates the (non spatially enabled) "table" with all the non spatial attributes and
     * finally creates the "layer", adding the spatial attribute to the previously created table.
     * So, this test ensures the datastore correctly works arround this limitation.
     * </p>
     * 
     * @throws IOException
     * @throws SchemaException
     * @throws SeException
     * @throws UnavailableConnectionException
     */
    @Test
    public void testCreateSchema() throws IOException, SchemaException, SeException,
            UnavailableConnectionException {
        final String typeName;
        {
            ISessionPool connectionPool = testData.getConnectionPool();
            ISession session = connectionPool.getSession();
            final String user;
            user = session.getUser();
            session.dispose();
            typeName = user + ".GT_TEST_CREATE";
        }

        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName(typeName);

        b.add("FST_COL", String.class);
        b.add("SECOND_COL", String.class);
        b.add("GEOM", Point.class);
        b.add("FOURTH_COL", Integer.class);

        final SimpleFeatureType type = b.buildFeatureType();

        DataStore ds = testData.getDataStore();
        testData.deleteTable(typeName);

        Map<String, String> hints = new HashMap<String, String>();
        hints.put("configuration.keyword", testData.getConfigKeyword());
        ((ArcSDEDataStore) ds).createSchema(type, hints);

        testData.deleteTable(typeName);
    }

    @Test
    public void testCreateNillableShapeSchema() throws IOException, SchemaException, SeException,
            UnavailableConnectionException {
        SimpleFeatureType type;
        final String typeName = "GT_TEST_CREATE";
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName(typeName);

        b.add("OBJECTID", Integer.class);

        b.nillable(true);
        b.add("SHAPE", MultiLineString.class);

        type = b.buildFeatureType();

        ArcSDEDataStore ds = testData.getDataStore();

        testData.deleteTable(typeName);
        Map<String, String> hints = new HashMap<String, String>();
        hints.put("configuration.keyword", testData.getConfigKeyword());
        ds.createSchema(type, hints);
        testData.deleteTable(typeName);
    }

    // ///////////////// HELPER FUNCTIONS ////////////////////////

    /**
     * checks for the existence of <code>table</code> in <code>featureTypes</code>.
     * <code>table</code> must be a full qualified sde feature type name. (i.e "TEST_POINT" ==
     * "SDE.SDE.TEST_POINT")
     */
    private void testTypeExists(String[] featureTypes, String table) {
        for (int i = 0; i < featureTypes.length; i++) {
            if (featureTypes[i].equalsIgnoreCase(table.toUpperCase())) {
                LOGGER.fine("testTypeExists OK: " + table);

                return;
            }
        }

        fail("table " + table + " not found in getFeatureTypes results");
    }

}
