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
 */
package org.geotools.arcsde.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.logging.Logger;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.session.ISession;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.esri.sde.sdk.client.SeDBMSInfo;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeTable;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Functional tests for {@link ArcSdeFeatureStore} when working with versioned tables
 * 
 * @author Gabriel Roldan
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/da/src/test/java/org
 *         /geotools/arcsde/data/ArcSDEFeatureStoreTest.java $
 * @version $Id$
 */
public class ArcSDEFeatureStoreVersionedTest {
    /** package logger */
    private static Logger LOGGER = Logging.getLogger(" org.geotools.arcsde.data");

    private TestData testData;

    /**
     * Qualified name of the versioned table used for tests
     */
    private String tableName;

    /**
     * Flag that indicates whether the underlying database is MS SQL Server.
     * <p>
     * This is used to decide what's the expected result count in some transaction tests, and its
     * value is obtained from an {@link SeDBMSInfo} object. Hacky as it seems it is. The problem is
     * that ArcSDE for SQL Server _explicitly_ sets the transaction isolation level to READ
     * UNCOMMITTED for all and every transaction, while for other databases it uses READ COMMITTED.
     * And no, ESRI documentation says there's no way to change nor workaround this behaviour.
     * </p>
     */
    private static boolean databaseIsMsSqlServer;

    /**
     * loads {@code test-data/testparams.properties} into a Properties object, wich is used to
     * obtain test tables names and is used as parameter to find the DataStore
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Before
    public void setUp() throws Exception {
        testData = new TestData();
        testData.setUp();
        {
            ISession session = testData.getConnectionPool().getSession();
            try {
                SeTable versionedTable = testData.createVersionedTable(session);
                tableName = versionedTable.getQualifiedName();
            } finally {
                session.dispose();
            }
        }
        {
            ISession session = testData.getConnectionPool().getSession();
            try {
                SeDBMSInfo dbInfo = session.getDBMSInfo();
                databaseIsMsSqlServer = dbInfo.dbmsId == SeDBMSInfo.SE_DBMS_IS_SQLSERVER;
            } finally {
                session.dispose();
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        testData.tearDown(false, true);
    }

    @Test
    public void testEditVersionedTableAutoCommit() throws Exception {
        final ArcSDEDataStore dataStore = testData.getDataStore();
        final FeatureSource<SimpleFeatureType, SimpleFeature> source;
        final FeatureStore<SimpleFeatureType, SimpleFeature> store;
        source = dataStore.getFeatureSource(tableName);
        store = (FeatureStore<SimpleFeatureType, SimpleFeature>) dataStore
                .getFeatureSource(tableName);

        ArcSdeResourceInfo info = (ArcSdeResourceInfo) store.getInfo();
        assertTrue(info.isVersioned());

        final SimpleFeatureType schema = store.getSchema();
        assertNull(schema.getDescriptor("ROW_ID"));

        final int initialCount = store.getCount(Query.ALL);
        assertEquals(0, initialCount);

        final WKTReader reader = new WKTReader();
        Object[] content = new Object[2];
        SimpleFeature feature;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
        int count;

        content[0] = "Feature name 1";
        content[1] = reader.read("POINT (0 0)");
        feature = SimpleFeatureBuilder.build(schema, content, (String) null);
        collection = DataUtilities.collection(feature);

        store.addFeatures(collection);

        count = store.getCount(Query.ALL);
        assertEquals(1, count);

        content[0] = "Feature name 2";
        content[1] = reader.read("POINT (1 1)");
        feature = SimpleFeatureBuilder.build(schema, content, (String) null);
        collection = DataUtilities.collection(feature);

        store.addFeatures(collection);

        count = store.getCount(Query.ALL);
        assertEquals(2, count);

        assertEquals(2, source.getCount(Query.ALL));
    }

    @Test
    public void testEditVersionedTableTransaction() throws Exception {
        try {
            final String tableName;
            {
                ISession session = testData.getConnectionPool().getSession();
                try {
                    SeTable versionedTable = testData.createVersionedTable(session);
                    tableName = versionedTable.getQualifiedName();
                } finally {
                    session.dispose();
                }
            }

            final ArcSDEDataStore dataStore = testData.getDataStore();
            final FeatureSource<SimpleFeatureType, SimpleFeature> source;
            final FeatureStore<SimpleFeatureType, SimpleFeature> store;

            source = dataStore.getFeatureSource(tableName);
            store = (FeatureStore<SimpleFeatureType, SimpleFeature>) dataStore
                    .getFeatureSource(tableName);

            Transaction transaction = new DefaultTransaction();
            store.setTransaction(transaction);

            ArcSdeResourceInfo info = (ArcSdeResourceInfo) store.getInfo();
            assertTrue(info.isVersioned());

            final SimpleFeatureType schema = store.getSchema();

            final int initialCount = store.getCount(Query.ALL);
            assertEquals(0, initialCount);

            final WKTReader reader = new WKTReader();
            Object[] content = new Object[2];
            SimpleFeature feature;
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
            int count;

            content[0] = "Feature name 1";
            content[1] = reader.read("POINT (10 10)");
            feature = SimpleFeatureBuilder.build(schema, content, (String) null);
            collection = DataUtilities.collection(feature);

            store.addFeatures(collection);

            count = store.getCount(Query.ALL);
            assertEquals(1, count);

            assertEquals(0, source.getCount(Query.ALL));

            {
                FeatureIterator<SimpleFeature> features = store.getFeatures().features();
                SimpleFeature f = features.next();
                features.close();
                Object obj = f.getDefaultGeometry();
                assertTrue(obj instanceof Point);
                Point p = (Point) obj;
                double x = p.getX();
                double y = p.getY();
                assertEquals(10D, x, 1E-5);
                assertEquals(10D, y, 1E-5);
            }

            transaction.commit();
            assertEquals(1, source.getCount(Query.ALL));

            content[0] = "Feature name 2";
            content[1] = reader.read("POINT (2 2)");
            feature = SimpleFeatureBuilder.build(schema, content, (String) null);
            collection = DataUtilities.collection(feature);

            store.addFeatures(collection);

            count = store.getCount(Query.ALL);
            assertEquals(2, count);

            assertEquals(1, source.getCount(Query.ALL));
            transaction.rollback();
            assertEquals(1, store.getCount(Query.ALL));

            transaction.close();

            {
                FeatureIterator<SimpleFeature> features = source.getFeatures().features();
                SimpleFeature f = features.next();
                features.close();
                Object obj = f.getDefaultGeometry();
                assertTrue(obj instanceof Point);
                Point p = (Point) obj;
                double x = p.getX();
                double y = p.getY();
                assertEquals(10D, x, 1E-5);
                assertEquals(10D, y, 1E-5);
            }

        } catch (SeException e) {
            throw new ArcSdeException(e);
        }
    }

    @Test
    public void testEditVersionedTableTransactionConcurrently() throws Exception {
        Map<String, String> conProps = testData.getConProps();

        final ArcSDEDataStore dataStore1 = (ArcSDEDataStore) DataStoreFinder.getDataStore(conProps);
        final ArcSDEDataStore dataStore2 = (ArcSDEDataStore) DataStoreFinder.getDataStore(conProps);
        assertNotSame(dataStore1, dataStore2);
        assertNotSame(dataStore1.connectionPool, dataStore2.connectionPool);

        final FeatureStore<SimpleFeatureType, SimpleFeature> store1, store2;

        store1 = (FeatureStore<SimpleFeatureType, SimpleFeature>) dataStore1
                .getFeatureSource(tableName);

        store2 = (FeatureStore<SimpleFeatureType, SimpleFeature>) dataStore2
                .getFeatureSource(tableName);

        Transaction transaction1 = new DefaultTransaction();
        store1.setTransaction(transaction1);

        Transaction transaction2 = new DefaultTransaction();
        store2.setTransaction(transaction2);

        final SimpleFeatureType schema = store1.getSchema();

        final int initialCount = store1.getCount(Query.ALL);
        assertEquals(0, initialCount);

        final WKTReader reader = new WKTReader();
        Object[] content = new Object[2];
        SimpleFeature feature;
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection;

        // add a feature to store1
        content[0] = "Feature name 1";
        content[1] = reader.read("POINT (10 10)");
        feature = SimpleFeatureBuilder.build(schema, content, (String) null);
        collection = DataUtilities.collection(feature);
        store1.addFeatures(collection);

        // transaction not committed, store1 expects a count of 1, store2 still 0
        assertEquals(1, store1.getCount(Query.ALL));

        assertEquals(0, store2.getCount(Query.ALL));

        // add a feature to store2
        content[0] = "Feature name 2";
        content[1] = reader.read("POINT (20 20)");
        feature = SimpleFeatureBuilder.build(schema, content, (String) null);
        collection = DataUtilities.collection(feature);
        store2.addFeatures(collection);

        // neither transaction committed, both stores expect a count of 1
        assertEquals(1, store1.getCount(Query.ALL));

        assertEquals(1, store2.getCount(Query.ALL));

        // commit t1, store1 still counts 1, store2 counts 2
        transaction1.commit();
        assertEquals(1, store1.getCount(Query.ALL));

        // /assertEquals(2, store2.getCount(Query.ALL));
        assertEquals(1, store2.getCount(Query.ALL));

        // commit t2, overrides the state commited by t1
        transaction2.commit();
        assertEquals(1, store1.getCount(Query.ALL));
        assertEquals(1, store2.getCount(Query.ALL));
    }

}
