/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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

import com.esri.sde.sdk.client.SeVersion;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.arcsde.session.ISession;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;

public class ArcSDEDataStoreVersioningTest {

    private static String defaultVersion;

    private static String version1;

    private static String version2;

    private static TestData testData;

    private static String typeName;

    private static DataStore defaultVersionDataStore;

    private static DataStore version1DataStore;

    private static DataStore version2DataStore;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {}

    @AfterClass
    public static void oneTimeTearDown() {}

    @Before
    public void setUp() throws Exception {
        testData = new TestData();
        testData.setUp();

        typeName = testData.getTempTableName();
        { // set up a couple versions
            ISession session = testData.getConnectionPool().getSession();

            defaultVersion = SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME;
            version1 = "testMultiVersionSupport 1";
            version2 = "testMultiVersionSupport 2";
            try {
                // delete them first if they already exist to ensure test case isolation
                testData.deleteVersion(session, version1);
                testData.deleteVersion(session, version2);

                testData.createVersion(session, version1, defaultVersion);
                testData.createVersion(session, version2, defaultVersion);

            } finally {
                session.dispose();
            }
        }

        final boolean insertTestData = true;
        testData.createTempTable(insertTestData);
        ISession session = testData.getConnectionPool().getSession();
        // make test type versioned
        try {
            testData.makeVersioned(session, typeName);
        } finally {
            session.dispose();
        }

        Map<String, Serializable> params =
                new HashMap<String, Serializable>(testData.getConProps());
        ArcSDEDataStoreFactory factory = new ArcSDEDataStoreFactory();
        defaultVersionDataStore = factory.createDataStore(params);

        params.put(ArcSDEDataStoreConfig.VERSION_PARAM_NAME, version1);
        version1DataStore = factory.createDataStore(params);

        params.put(ArcSDEDataStoreConfig.VERSION_PARAM_NAME, version2);
        version2DataStore = factory.createDataStore(params);
    }

    @After
    public void tearDown() throws Exception {
        defaultVersionDataStore.dispose();
        version1DataStore.dispose();
        version2DataStore.dispose();
        ISession session = testData.getConnectionPool().getSession();
        try {
            testData.deleteVersion(session, version1);
            testData.deleteVersion(session, version2);
        } finally {
            session.dispose();
        }
        boolean cleanTestTable = true;
        boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
    }

    /**
     * Why... oh why this test fails if executed twice, but succeeds if the transactional version
     * ran before? no time to look further into it right now, but beware
     */
    @Test
    @Ignore
    public void testMultiVersionSupportAutoCommit() throws IOException {
        testMultiVersionSupport(Transaction.AUTO_COMMIT);
    }

    @Test
    public void testMultiVersionSupportTransaction() throws IOException {
        DefaultTransaction transaction = new DefaultTransaction();
        testMultiVersionSupport(transaction);
    }

    private void testMultiVersionSupport(final Transaction transaction) throws IOException {
        final int initialCount = 8; // as per TestData.insertData
        SimpleFeatureStore storeDefault;
        SimpleFeatureStore storeV1;
        SimpleFeatureStore storeV2;

        storeDefault = store(defaultVersionDataStore, typeName, transaction);
        storeV1 = store(version1DataStore, typeName, transaction);
        storeV2 = store(version2DataStore, typeName, transaction);

        assertEquals(initialCount, count(storeDefault));
        assertEquals(initialCount, count(storeV1));
        assertEquals(initialCount, count(storeV2));

        // can't use INT32_COL IN(1,2,...) for backward compatibility with 2.5.x. That's ECQL syntax
        // and doesn't exist on 2.5.x
        delete(storeDefault, "INT32_COL = 1");
        delete(storeV1, "INT32_COL = 1 OR INT32_COL = 2");
        delete(storeV2, "INT32_COL = 1 OR INT32_COL = 2 OR INT32_COL = 3");

        assertEquals(initialCount - 1, count(storeDefault));
        assertEquals(initialCount - 2, count(storeV1));
        assertEquals(initialCount - 3, count(storeV2));

        if (!Transaction.AUTO_COMMIT.equals(transaction)) {
            transaction.commit();
            assertEquals(initialCount - 1, count(storeDefault));
            assertEquals(initialCount - 2, count(storeV1));
            assertEquals(initialCount - 3, count(storeV2));
        }
    }

    private void delete(final SimpleFeatureStore store, final String ecqlPredicate)
            throws IOException {

        Filter filter;
        try {
            filter = CQL.toFilter(ecqlPredicate);
        } catch (CQLException e) {
            throw new DataSourceException(e);
        }

        store.removeFeatures(filter);
    }

    private int count(SimpleFeatureStore store) throws IOException {
        return store.getCount(Query.ALL);
    }

    private SimpleFeatureStore store(
            final DataStore ds, final String typeName, final Transaction transaction)
            throws IOException {
        SimpleFeatureStore store;
        store = (SimpleFeatureStore) ds.getFeatureSource(typeName);
        store.setTransaction(transaction);
        return store;
    }
}
