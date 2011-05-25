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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import junit.framework.AssertionFailedError;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.SdeRow;
import org.geotools.data.BatchFeatureEvent;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeDBMSInfo;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeTable;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Unit tests for transaction support
 * 
 * @author Gabriel Roldan, Axios Engineering
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/da/src/test/java/org
 *         /geotools/arcsde/data/ArcSDEFeatureStoreTest.java $
 * @version $Id$
 */
public class ArcSDEFeatureStoreTest {
    /** package logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ArcSDEFeatureStoreTest.class.getPackage().getName());

    private TestData testData;

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

    @Before
    public void setUp() throws Exception {
        testData = new TestData();
        testData.setUp();

        // do not insert test data, will do it at each test case
        final boolean insertTestData = false;
        testData.createTempTable(insertTestData);

        ISession session = testData.getConnectionPool().getSession();
        try {
            SeDBMSInfo dbInfo = session.getDBMSInfo();
            databaseIsMsSqlServer = dbInfo.dbmsId == SeDBMSInfo.SE_DBMS_IS_SQLSERVER;
        } finally {
            session.dispose();
        }
    }

    @After
    public void tearDown() {
        boolean cleanTestTable = false;
        boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
    }

    /**
     * This is an adaptation of a classic MemoryDataStore test to match our sample data.
     * <p>
     * This test is focused on two things:
     * <ul>
     * <li>Are the Transactions actually independent?
     * <li>Do the correct feature event notifications get sent out
     * </ul>
     */
    @Ignore
    @Test
    public void testFeatureEventsAndTransactionsForDelete() throws Exception {
        // We are going to start with ...
        testData.insertTestData();

        final DataStore dataStore = testData.getDataStore();
        final String typeName = testData.getTempTableName();
        final SimpleFeatureSource origional = dataStore.getFeatureSource(typeName);
        TestFeatureListener listener = new TestFeatureListener();
        origional.addFeatureListener(listener);

        // we are going to use this feature source to check that the
        // public Transaction.AUTO_COMMIT view of the world
        // is as expected.
        assertEquals(8, origional.getCount(Query.ALL));
        final SortedSet<String> allFids = new TreeSet<String>();
        SimpleFeatureCollection collection = origional.getFeatures();
        TestProgressListener progress = new TestProgressListener();
        collection.accepts(new FeatureVisitor() {
            public void visit(Feature feature) {
                allFids.add(feature.getIdentifier().getID());
            }
        }, progress);
        assertTrue("visitor completed", progress.completed);
        assertEquals("visitor 100%", 100f, progress.progress);
        assertNull("visitor no problems", progress.exception);

        // we are going to use this transaction to modify and commit
        DefaultTransaction t1 = new DefaultTransaction("Transaction 1");
        SimpleFeatureStore featureStore1 = (SimpleFeatureStore) dataStore
                .getFeatureSource(typeName);
        featureStore1.setTransaction(t1);
        TestFeatureListener listener1 = new TestFeatureListener();
        featureStore1.addFeatureListener(listener1);
        // we are going to use this transaction to modify and rollback
        DefaultTransaction t2 = new DefaultTransaction("Transaction 2");
        SimpleFeatureStore featureStore2 = (SimpleFeatureStore) dataStore
                .getFeatureSource(typeName);
        featureStore2.setTransaction(t2);
        TestFeatureListener listener2 = new TestFeatureListener();
        featureStore2.addFeatureListener(listener2);

        // verify they are all working
        assertEquals(8, origional.getCount(Query.ALL));
        assertEquals(8, featureStore1.getCount(Query.ALL));
        assertEquals(8, featureStore2.getCount(Query.ALL));

        Query queryOneFeature = new Query();
        queryOneFeature.setTypeName(typeName);
        queryOneFeature.setFilter(Filter.INCLUDE);
        queryOneFeature.setMaxFeatures(1);
        queryOneFeature.setPropertyNames(Query.ALL_NAMES);

        collection = featureStore1.getFeatures(queryOneFeature);
        progress.reset();
        final SortedSet<String> fids = new TreeSet<String>();
        collection.accepts(new FeatureVisitor() {
            public void visit(Feature feature) {
                fids.add(feature.getIdentifier().getID());
            }
        }, progress);
        assertTrue("visitor completed", progress.completed);
        assertEquals("visitor 100%", 100f, progress.progress);
        assertNull("visitor no problems", progress.exception);

        assertEquals(1, fids.size());
        String featureId = fids.first();
        assertTrue(allFids.contains(featureId));

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filterOne = ff.id(Collections.singleton(ff.featureId(featureId)));

        assertEquals("no events on AUTO_COMMIT", 0, listener.list.size());
        assertEquals("no events on transaction1", 0, listener1.list.size());
        assertEquals("no events on transaction2", 0, listener2.list.size());
        featureStore1.removeFeatures(filterOne);
        assertEquals("no events on AUTO_COMMIT", 0, listener.list.size());
        assertEquals("single event on transaction2", 1, listener1.list.size());
        assertEquals("no events on AUTO_COMMIT", 0, listener2.list.size());

        FeatureEvent e = listener1.list.get(0);
        assertEquals(featureStore1, e.getFeatureSource());
        Id id = (Id) e.getFilter();
        assertNotNull(id);
        assertNotNull(id.getIDs());
        assertTrue(id.getIDs().contains(featureId));
        ReferencedEnvelope bounds = e.getBounds();
        assertFalse(bounds.isEmpty());
        assertFalse(bounds.isNull());

        t1.commit();
        assertEquals("commit event sent to AUTO_COMMIT", 1, listener.list.size());
        assertEquals("commit event sent to transaction 1", 2, listener1.list.size());
        assertEquals("commit event sent to transaction 2", listener2.list.size());
    }

    @Test
    @Ignore
    public void testFeatureEventsAndTransactionsForAdd() throws Exception {
        // We are going to start with ...
        testData.insertTestData();

        final DataStore dataStore = testData.getDataStore();
        final String typeName = testData.getTempTableName();
        final SimpleFeatureSource origional = dataStore.getFeatureSource(typeName);
        TestFeatureListener listener = new TestFeatureListener();
        origional.addFeatureListener(listener);

        // we are going to use this feature source to check that the
        // public Transaction.AUTO_COMMIT view of the world
        // is as expected.
        assertEquals(8, origional.getCount(Query.ALL));

        // we are going to use this transaction to modify and commit
        DefaultTransaction t1 = new DefaultTransaction("Transaction 1");
        SimpleFeatureStore featureStore1 = (SimpleFeatureStore) dataStore
                .getFeatureSource(typeName);
        featureStore1.setTransaction(t1);
        TestFeatureListener listener1 = new TestFeatureListener();
        featureStore1.addFeatureListener(listener1);

        // verify they are all working
        assertEquals(8, origional.getCount(Query.ALL));
        assertEquals(8, featureStore1.getCount(Query.ALL));

        SimpleFeatureType schema = origional.getSchema();
        SimpleFeatureBuilder build = new SimpleFeatureBuilder(schema);

        int value = 24;
        build.add(Integer.valueOf(value));
        build.add(Short.valueOf((short) value));
        build.add(new Float(value / 10.0F));
        build.add(new Double(value / 10D));
        build.add("FEATURE_" + value);

        Calendar cal = Calendar.getInstance();
        cal.set(2004, 06, value, 0, 0, 0);
        build.add(cal);

        WKTReader reader = new WKTReader();
        build.add(reader.read("POINT(1 1)"));

        SimpleFeature newFeature = build.buildFeature(null);
        SimpleFeatureCollection newFeatures = DataUtilities.collection(newFeature);

        List<FeatureId> newFids = featureStore1.addFeatures(newFeatures);
        assertEquals(0, listener.list.size());
        assertEquals(1, listener1.list.size());

        FeatureEvent e = listener1.list.get(0);
        Id id = (Id) e.getFilter();
        assertTrue(id.getIdentifiers().containsAll(newFids));
        // remember the FeatureId with a strong reference
        FeatureId tempFeatureId = (FeatureId) id.getIdentifiers().iterator().next();
        assertTrue(newFids.contains(tempFeatureId));

        t1.commit();
        assertEquals(1, listener.list.size());
        assertEquals(2, listener1.list.size());

        BatchFeatureEvent batch = (BatchFeatureEvent) listener1.list.get(2);
        assertFalse("confirm tempFid is not in the commit",
                id.getIdentifiers().contains(tempFeatureId));
        assertNotNull(batch.getFilter());

        FeatureId featureId = (FeatureId) batch.getCreatedFeatureIds().iterator().next();
        assertSame("confirm temp feature Id was updated", tempFeatureId, featureId);
    }

    @Test
    public void testDeleteByFIDAutoCommit() throws Exception {
        testData.insertTestData();

        final DataStore ds = testData.getDataStore();
        final String typeName = testData.getTempTableName();

        final String fid;
        final Filter fidFilter;
        {
            // get a fid
            Query query = new Query(typeName);
            FeatureReader<SimpleFeatureType, SimpleFeature> reader = ds.getFeatureReader(query,
                    Transaction.AUTO_COMMIT);
            try {
                fid = reader.next().getID();
            } finally {
                reader.close();
            }

            final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            Set<FeatureId> ids = new HashSet<FeatureId>();
            ids.add(ff.featureId(fid));
            fidFilter = ff.id(ids);
        }

        {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
            writer = ds.getFeatureWriter(typeName, fidFilter, Transaction.AUTO_COMMIT);

            try {
                assertTrue(writer.hasNext());
                SimpleFeature feature = writer.next();
                assertEquals(fid, feature.getID());
                writer.remove();
                assertFalse(writer.hasNext());
            } finally {
                writer.close();
            }
        }

        ISessionPool connectionPool = testData.getConnectionPool();
        ISession session = connectionPool.getSession();
        SeQuery seQuery;
        try {
            int objectId = (int) ArcSDEAdapter.getNumericFid(fid);
            final String whereClause = "ROW_ID=" + objectId;
            seQuery = session.issue(new Command<SeQuery>() {
                @Override
                public SeQuery execute(ISession session, SeConnection connection)
                        throws SeException, IOException {
                    SeQuery seQuery = new SeQuery(connection, new String[] { "ROW_ID", "INT32_COL",
                            "STRING_COL" }, new SeSqlConstruct(typeName, whereClause));
                    seQuery.prepareQuery();
                    seQuery.execute();
                    return seQuery;
                }
            });

            SdeRow row = session.fetch(seQuery);
            assertNull(row);
        } finally {
            session.dispose();
        }

        // was it really removed?
        {
            Query query = new Query(typeName, fidFilter);
            FeatureReader<SimpleFeatureType, SimpleFeature> reader = ds.getFeatureReader(query,
                    Transaction.AUTO_COMMIT);
            try {
                assertFalse(reader.hasNext());
            } finally {
                reader.close();
            }
        }
    }

    /**
     * Tests that all the features that match a filter based on attribute only filters (aka non
     * spatial filters), are deleted correctly. This test assumes that there are no duplicate values
     * in the test data.
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteByAttOnlyFilter() throws Exception {
        testData.insertTestData();

        final DataStore ds = testData.getDataStore();
        final String typeName = testData.getTempTableName();

        // get 2 features and build an OR'ed PropertyIsEqualTo filter
        Filter or = CQL.toFilter("INT32_COL = 1 OR INT32_COL = 2");
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(typeName, or,
                Transaction.AUTO_COMMIT);

        try {
            assertTrue(writer.hasNext());

            SimpleFeature feature = writer.next();
            assertEquals(Integer.valueOf(1), feature.getAttribute("INT32_COL"));
            writer.remove();

            feature = writer.next();
            assertEquals(Integer.valueOf(2), feature.getAttribute("INT32_COL"));
            writer.remove();

            assertFalse(writer.hasNext());
        } finally {
            writer.close();
        }

        // was it really removed?
        FeatureReader<SimpleFeatureType, SimpleFeature> read = ds.getFeatureReader(new Query(
                typeName, or), Transaction.AUTO_COMMIT);
        try {
            assertFalse(read.hasNext());
        } finally {
            read.close();
        }
    }

    @Test
    public void testInsertAutoCommit() throws Exception {
        // the table populated here is test friendly since it can hold
        // any kind of geometries.
        testData.truncateTempTable();

        // there are some commented out just because the server I'm hitting
        // is slow, not because they don't work. Feel free to uncomment.
        testInsertAutoCommit(Geometry.class);
        testInsertAutoCommit(Point.class);
        testInsertAutoCommit(MultiPoint.class);
        testInsertAutoCommit(LineString.class);
        testInsertAutoCommit(MultiLineString.class);
        testInsertAutoCommit(Polygon.class);
        testInsertAutoCommit(MultiPolygon.class);
    }

    /**
     * Add features to a FeatureWriter with a {@link Transaction} and ensure if the transaction was
     * not committed, a request gets no features, and when the transaction is committed the query
     * returns it.
     * 
     * @throws Exception
     */
    @Test
    public void testInsertTransaction() throws Exception {
        // start with an empty table
        testData.truncateTempTable();
        final String typeName = testData.getTempTableName();
        final int featureCount = 2;
        final SimpleFeatureCollection testFeatures;
        testFeatures = testData.createTestFeatures(LineString.class, featureCount);

        final DataStore ds = testData.getDataStore();

        final SimpleFeatureType ftype = testFeatures.getSchema();
        final SimpleFeatureIterator iterator = testFeatures.features();

        final Transaction transaction = new DefaultTransaction();
        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = ds.getFeatureWriter(typeName, transaction);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        final Query query = new Query(typeName);
        try {
            try {
                while (iterator.hasNext()) {
                    SimpleFeature addFeature = iterator.next();
                    SimpleFeature newFeature = writer.next();
                    for (int i = 0; i < ftype.getAttributeCount(); i++) {
                        String localName = ftype.getDescriptor(i).getLocalName();
                        newFeature.setAttribute(localName, addFeature.getAttribute(localName));
                    }
                    writer.write();
                }
            } catch (Exception e) {
                transaction.rollback();
                transaction.close();
            } finally {
                writer.close();
            }

            reader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
            boolean hasNext;
            try {
                hasNext = reader.hasNext();
            } finally {
                reader.close();
            }
            if (databaseIsMsSqlServer) {
                // SQL Server always is at READ UNCOMMITTED isolation level...
                assertTrue(hasNext);
            } else {
                assertFalse("Features added, transaction not commited", hasNext);
            }

            try {
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } finally {
            transaction.close();
        }

        try {
            reader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
            for (int i = 0; i < featureCount; i++) {
                assertTrue(reader.hasNext());
                reader.next();
            }
            assertFalse(reader.hasNext());
        } finally {
            reader.close();
        }
    }

    @Test
    public void testInsertTransactionAndQueryByFid() throws Exception {
        // start with an empty table
        final String typeName = testData.getTempTableName();
        final int featureCount = 2;
        final SimpleFeatureCollection testFeatures;
        testFeatures = testData.createTestFeatures(LineString.class, featureCount);

        final DataStore ds = testData.getDataStore();
        final SimpleFeatureStore fStore;
        fStore = (SimpleFeatureStore) ds.getFeatureSource(typeName);
        final Transaction transaction = new DefaultTransaction("testInsertTransactionAndQueryByFid");
        fStore.setTransaction(transaction);
        try {
            final List<FeatureId> addedFids = fStore.addFeatures(testFeatures);
            final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

            final Set<FeatureId> fids = new HashSet<FeatureId>();
            for (FeatureId fid : addedFids) {
                fids.add(fid);
            }
            final Id newFidsFilter = ff.id(fids);

            SimpleFeatureCollection features;
            features = fStore.getFeatures(newFidsFilter);
            assertEquals(2, features.size());
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    /**
     * Make sure features are validated against it's schema before being added
     */
    @Test
    public void testInsertNonNillableAttributeCheck() throws Exception {
        // start with an empty table
        testData.truncateTempTable();
        final String typeName = testData.getTempTableName();
        final int featureCount = 1;

        SimpleFeature feature;
        feature = testData.createTestFeatures(LineString.class, featureCount).features().next();

        final DataStore ds = testData.getDataStore();

        SimpleFeatureStore store = (SimpleFeatureStore) ds.getFeatureSource(typeName);

        SimpleFeatureType ftype = store.getSchema();

        assertFalse(ftype.getDescriptor("INT32_COL").isNillable());

        feature.setAttribute("INT32_COL", null);

        SimpleFeatureCollection collection = DataUtilities.collection(feature);
        try {
            store.addFeatures(collection);
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            // note this should really be org.opengis.feature.IllegalAttributeException but
            // Types.validate throws IllegalArgumentException instead
            assertTrue(true);
        }
    }

    @Test
    public void testUpdateAutoCommit() throws Exception {
        testData.insertTestData();

        final String typeName = testData.getTempTableName();
        final DataStore ds = testData.getDataStore();
        final Filter filter = CQL.toFilter("INT32_COL = 3");

        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = ds.getFeatureWriter(typeName, filter, Transaction.AUTO_COMMIT);

        try {
            assertTrue(writer.hasNext());
            SimpleFeature feature = writer.next();
            feature.setAttribute("INT32_COL", Integer.valueOf(-1000));
            writer.write();
            assertFalse(writer.hasNext());
        } finally {
            writer.close();
        }

        Query query = new Query(typeName, filter);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertFalse(reader.hasNext());
        } finally {
            reader.close();
        }

        query = new Query(typeName, CQL.toFilter("INT32_COL = -1000"));
        reader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertTrue(reader.hasNext());
            reader.next();
            assertFalse(reader.hasNext());
        } finally {
            reader.close();
        }
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        testData.insertTestData();

        final String typeName = testData.getTempTableName();
        final DataStore ds = testData.getDataStore();
        final Filter oldValueFilter = CQL.toFilter("INT32_COL = 3");
        final Query oldValueQuery = new Query(typeName, oldValueFilter);
        final Filter newValueFilter = CQL.toFilter("INT32_COL = -1000");
        final Query newValueQuery = new Query(typeName, newValueFilter);

        final Transaction transaction = new DefaultTransaction("testUpdateTransaction");
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = ds.getFeatureWriter(typeName, oldValueFilter, transaction);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        try {
            try {
                assertTrue(writer.hasNext());
                SimpleFeature feature = writer.next();
                feature.setAttribute("INT32_COL", Integer.valueOf(-1000));
                writer.write();
                assertFalse(writer.hasNext());
            } finally {
                writer.close();
            }

            reader = ds.getFeatureReader(oldValueQuery, Transaction.AUTO_COMMIT);
            try {
                if (databaseIsMsSqlServer) {
                    // SQL Server always is at READ UNCOMMITTED isolation level...
                    assertFalse(reader.hasNext());
                } else {
                    assertTrue(reader.hasNext());
                }
            } finally {
                reader.close();
            }

            reader = ds.getFeatureReader(newValueQuery, Transaction.AUTO_COMMIT);
            try {
                if (databaseIsMsSqlServer) {
                    // SQL Server always is at READ UNCOMMITTED isolation level...
                    assertTrue(reader.hasNext());
                } else {
                    assertFalse(reader.hasNext());
                }
            } finally {
                reader.close();
            }

            reader = ds.getFeatureReader(oldValueQuery, transaction);
            try {
                assertFalse(reader.hasNext());
            } finally {
                reader.close();
            }

            reader = ds.getFeatureReader(newValueQuery, transaction);
            try {
                assertTrue(reader.hasNext());
            } finally {
                reader.close();
            }

            try {
                transaction.commit();
            } catch (IOException e) {
                transaction.rollback();
                throw e;
            }
        } finally {
            transaction.close();
        }

        reader = ds.getFeatureReader(newValueQuery, Transaction.AUTO_COMMIT);
        try {
            assertTrue(reader.hasNext());
        } finally {
            reader.close();
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testModifyFeaturesTransaction() throws Exception {
        testData.insertTestData();

        final String typeName = testData.getTempTableName();
        final DataStore ds = testData.getDataStore();
        final SimpleFeatureStore store;
        store = (SimpleFeatureStore) ds.getFeatureSource(typeName);
        final SimpleFeatureType schema = store.getSchema();
        final Filter oldValueFilter = CQL.toFilter("INT32_COL = 3");
        final Filter newValueFilter = CQL.toFilter("INT32_COL = -1000");

        SimpleFeatureCollection features = store.getFeatures(oldValueFilter);
        final int initialSize = features.size();
        assertEquals(1, initialSize);// just to not go forward with bad data
        final SimpleFeature originalFeature;
        SimpleFeatureIterator iterator = features.features();
        try {
            originalFeature = iterator.next();
        } finally {
            iterator.close();
        }

        {
            final Transaction transaction = new DefaultTransaction("testModifyFeaturesTransaction");
            store.setTransaction(transaction);

            try {
                final AttributeDescriptor propDescriptor = schema.getDescriptor("INT32_COL");
                store.modifyFeatures(propDescriptor.getName(), Integer.valueOf(-1000),
                        oldValueFilter);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            } finally {
                transaction.close();
            }
        }
        store.setTransaction(Transaction.AUTO_COMMIT);

        final Query oldValueQuery = new Query(typeName, oldValueFilter);
        final Query newValueQuery = new Query(typeName, newValueFilter);

        assertEquals(0, store.getCount(oldValueQuery));
        assertEquals(1, store.getCount(newValueQuery));

        final SimpleFeatureIterator newFeatures = store.getFeatures(newValueQuery).features();
        final SimpleFeature modifiedFeature;
        try {
            modifiedFeature = newFeatures.next();
        } finally {
            newFeatures.close();
        }

        // verify the non modified properties stay the same
        final List<Attribute> originalValues = (List<Attribute>) originalFeature.getValue();
        final List<Attribute> actualValues = (List<Attribute>) modifiedFeature.getValue();
        for (int i = 0; i < originalValues.size(); i++) {
            Attribute originalAtt = originalValues.get(i);
            Attribute actualAtt = actualValues.get(i);
            Name name = originalAtt.getName();
            // bah, date equals does not work, I don't care for this test
            String localName = name.getLocalPart();
            if (!"INT32_COL".equals(localName) && !"SHAPE".equals(localName)
                    && !"DATE_COL".equals(localName)) {
                assertEquals(name + " does not match", originalAtt.getValue(), actualAtt.getValue());
            }
        }
    }

    @Test
    public void testUpdateAdjacentPolygonsTransaction() throws Exception {
        final WKTReader reader = new WKTReader();
        final Polygon p1 = (Polygon) reader
                .read("POLYGON((-10 -10, -10 10, 0 10, 0 -10, -10 -10))");
        final Polygon p2 = (Polygon) reader.read("POLYGON((0 -10, 0 10, 10 10, 10 -10, 0 -10))");

        final Polygon modif1 = (Polygon) reader
                .read("POLYGON ((-10 -10, -10 10, 5 10, -5 -10, -10 -10))");
        final Polygon modif2 = (Polygon) reader
                .read("POLYGON ((-5 -10, 5 10, 10 10, 10 -10, -5 -10))");

        final String typeName = testData.getTempTableName(); // "SDE.CJ_TST_1";
        final ArcSDEDataStore dataStore = testData.getDataStore();
        // String[] typeNames = dataStore.getTypeNames();
        // System.err.println(typeNames);
        final SimpleFeatureStore store;
        store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
        final SimpleFeatureType schema = store.getSchema();
        GeometryDescriptor defaultGeometry = schema.getGeometryDescriptor();
        String fid1;
        String fid2;
        // insert polygons p1, p2 and grab the fids for later retrieval
        {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore
                    .getFeatureWriterAppend(typeName, Transaction.AUTO_COMMIT);
            SimpleFeature feature;
            try {
                feature = writer.next();
                // set this attribute as its the only non nillable one
                feature.setAttribute("INT32_COL", Integer.valueOf(0));
                // now set the geometry
                feature.setAttribute(defaultGeometry.getName(), p1);
                writer.write();
                fid1 = feature.getID();

                feature = writer.next();
                // set this attribute as its the only non nillable one
                feature.setAttribute("INT32_COL", Integer.valueOf(0));
                // now set the geometry
                feature.setAttribute(defaultGeometry.getName(), p2);
                writer.write();
                fid2 = feature.getID();
            } finally {
                writer.close();
            }
        }

        final Transaction transaction = new DefaultTransaction("testUpdateAdjacentPolygons");
        store.setTransaction(transaction);
        final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter fid1Filter = ff.id(Collections.singleton(ff.featureId(fid1)));
        Filter fid2Filter = ff.id(Collections.singleton(ff.featureId(fid2)));
        try {
            store.modifyFeatures(defaultGeometry.getName(), modif2, fid2Filter);
            store.modifyFeatures(defaultGeometry.getName(), modif1, fid1Filter);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
        store.setTransaction(Transaction.AUTO_COMMIT);

        try {
            SimpleFeatureCollection features;
            SimpleFeatureIterator iterator;

            features = store.getFeatures(fid1Filter);
            iterator = features.features();
            final SimpleFeature feature1 = iterator.next();
            iterator.close();

            features = store.getFeatures(fid2Filter);
            iterator = features.features();
            final SimpleFeature feature2 = iterator.next();
            iterator.close();

            // Note that for tables that are ambiguous about what types of geometries
            // they store (as this table is), ArcSDE will "compress" a stored geometry
            // to it's simplest representation. So in case the defaultGeometry.getBinding()
            // returns "Geometry", do instanceof checks to verify what kind of geometry
            // you're getting back
            Geometry actual1 = (Geometry) feature1.getAttribute(defaultGeometry.getLocalName());
            Geometry actual2 = (Geometry) feature2.getAttribute(defaultGeometry.getLocalName());
            System.out.println(actual1);
            System.out.println(modif1);

            // there's some rounding that goes on inside SDE. Need to do some simple buffering to
            // make sure
            // we're not getting rounding errors
            assertTrue(modif1.buffer(.01).contains(actual1));
            assertTrue(modif2.buffer(.01).contains(actual2));
        } finally {
            try {
                store.removeFeatures(fid1Filter);
                store.removeFeatures(fid2Filter);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /**
     * Tests the writing of features with autocommit transaction.
     * 
     */
    private void testInsertAutoCommit(Class<? extends Geometry> geometryClass) throws Exception {
        final String typeName = testData.getTempTableName();
        final int insertCount = 2;
        final SimpleFeatureCollection testFeatures;
        testFeatures = testData.createTestFeatures(geometryClass, insertCount);

        final DataStore ds = testData.getDataStore();
        final SimpleFeatureSource fsource = ds.getFeatureSource(typeName);

        // incremented on each feature added event to
        // ensure events are being raised as expected
        // (the count is wraped inside an array to be able of declaring
        // the variable as final and accessing it from inside the anonymous
        // inner class)
        /*
         * final int[] featureAddedEventCount = { 0 };
         * 
         * fsource.addFeatureListener(new FeatureListener() { public void changed(FeatureEvent evt)
         * { if (evt.getEventType() != FeatureEvent.FEATURES_ADDED) { throw new
         * IllegalArgumentException( "Expected FEATURES_ADDED event, got " + evt.getEventType()); }
         * 
         * ++featureAddedEventCount[0]; } });
         */

        final int initialCount = fsource.getCount(Query.ALL);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriterAppend(
                typeName, Transaction.AUTO_COMMIT);

        SimpleFeature source;
        SimpleFeature dest;

        try {
            for (SimpleFeatureIterator fi = testFeatures.features(); fi.hasNext();) {
                source = fi.next();
                dest = writer.next();
                dest.setAttributes(source.getAttributes());
                writer.write();
            }
        } finally {
            writer.close();
        }

        // was the features really inserted?
        int fcount = fsource.getCount(Query.ALL);
        assertEquals(testFeatures.size() + initialCount, fcount);

        /*
         * String msg = "a FEATURES_ADDED event should have been called " + features.size() + "
         * times"; assertEquals(msg, features.size(), featureAddedEventCount[0]);
         */
    }

    @Test
    public void testWriteAndUpdateNullShapes() throws Exception {
        final String typeName = testData.getTempTableName();
        testData.truncateTempTable();

        DataStore ds = testData.getDataStore();

        SimpleFeature feature;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = ds.getFeatureWriter(typeName, Transaction.AUTO_COMMIT);
        try {
            feature = writer.next();
            feature.setAttribute("INT32_COL", Integer.valueOf(1000));

            writer.write();
        } finally {
            writer.close();
        }
        LOGGER.info("Wrote null-geom feature to sde");

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        reader = ds.getFeatureReader(new Query(typeName, Filter.INCLUDE), Transaction.AUTO_COMMIT);

        // save the ID to update the feature later
        String newId;
        try {
            assertTrue(reader.hasNext());
            feature = reader.next();
            LOGGER.info("recovered geometry " + feature.getDefaultGeometry()
                    + " from single inserted feature.");
            assertNull(feature.getDefaultGeometry());
            newId = feature.getID();
            assertFalse(reader.hasNext());
        } finally {
            reader.close();
        }
        LOGGER.info("Confirmed exactly one feature in new sde layer");

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        HashSet<FeatureId> ids = new HashSet<FeatureId>();
        ids.add(ff.featureId(newId));
        Filter idFilter = ff.id(ids);

        writer = ds.getFeatureWriter(typeName, idFilter, Transaction.AUTO_COMMIT);

        try {
            assertTrue(writer.hasNext());

            LOGGER.info("Confirmed feature is fetchable via it's api-determined FID");

            GeometryFactory gf = new GeometryFactory();
            int index = 10;
            Coordinate[] coords1 = { new Coordinate(0, 0), new Coordinate(++index, ++index) };
            Coordinate[] coords2 = { new Coordinate(0, index), new Coordinate(index, 0) };
            LineString[] lines = { gf.createLineString(coords1), gf.createLineString(coords2) };
            MultiLineString sampleMultiLine = gf.createMultiLineString(lines);

            SimpleFeature toBeUpdated = writer.next();
            toBeUpdated.setAttribute("SHAPE", sampleMultiLine);
            writer.write();
        } finally {
            writer.close();
        }
        LOGGER.info("Null-geom feature updated with a sample geometry.");

        Query query = new Query(testData.getTempTableName(), idFilter);
        reader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertTrue(reader.hasNext());
            feature = reader.next();
            MultiLineString recoveredMLS = (MultiLineString) feature.getDefaultGeometry();
            assertTrue(!recoveredMLS.isEmpty());
            // I tried to compare the recovered MLS to the
            // sampleMultiLineString, but they're
            // slightly different. SDE does some rounding, and winds up giving
            // me 0.0000002 for zero,
            // and 11.9992 for 12. Meh.
        } finally {
            reader.close();
        }
    }

    /**
     * Tests the writing of features with real transactions
     * 
     */
    @Test
    public void testFeatureWriterTransaction() throws Exception {
        // the table populated here is test friendly since it can hold
        // any kind of geometries.
        testData.insertTestData();

        final String typeName = testData.getTempTableName();

        final DataStore ds = testData.getDataStore();
        final SimpleFeatureSource fsource = ds.getFeatureSource(typeName);

        final int initialCount = fsource.getCount(Query.ALL);
        final int writeCount = initialCount + 2;
        final SimpleFeatureCollection testFeatures = testData.createTestFeatures(LineString.class,
                writeCount);

        // incremented on each feature added event to
        // ensure events are being raised as expected
        // (the count is wraped inside an array to be able of declaring
        // the variable as final and accessing it from inside the anonymous
        // inner class)
        // final int[] featureAddedEventCount = { 0 };

        final Transaction transaction = new DefaultTransaction();
        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(
                typeName, Filter.INCLUDE, transaction);

        SimpleFeature source;
        SimpleFeature dest;

        int count = 0;
        try {
            for (SimpleFeatureIterator fi = testFeatures.features(); fi.hasNext(); count++) {
                if (count < initialCount) {
                    assertTrue("at index " + count, writer.hasNext());
                } else {
                    assertFalse("at index " + count, writer.hasNext());
                }

                source = fi.next();
                dest = writer.next();
                dest.setAttributes(source.getAttributes());
                writer.write();
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            writer.close();
            transaction.close();
        }

        // was the features really inserted?
        int fcount = fsource.getCount(Query.ALL);
        assertEquals(writeCount, fcount);

        /*
         * String msg = "a FEATURES_ADDED event should have been called " + features.size() + "
         * times"; assertEquals(msg, features.size(), featureAddedEventCount[0]);
         */
    }

    @Test
    public void testFeatureWriterAppend() throws Exception {
        // the table populated here is test friendly since it can hold
        // any kind of geometries.
        testData.insertTestData();

        final String typeName = testData.getTempTableName();
        final SimpleFeatureCollection testFeatures = testData.createTestFeatures(LineString.class,
                2);

        final DataStore ds = testData.getDataStore();
        final SimpleFeatureSource fsource = ds.getFeatureSource(typeName);
        final int initialCount = fsource.getCount(Query.ALL);

        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriterAppend(
                typeName, Transaction.AUTO_COMMIT);

        SimpleFeature source;
        SimpleFeature dest;

        for (SimpleFeatureIterator fi = testFeatures.features(); fi.hasNext();) {
            assertFalse(writer.hasNext());
            source = fi.next();
            dest = writer.next();
            dest.setAttributes(source.getAttributes());
            writer.write();
        }

        writer.close();

        // were the features really inserted?
        int fcount = fsource.getCount(Query.ALL);
        assertEquals(testFeatures.size() + initialCount, fcount);
    }

    /**
     * Ensure modified features for a given FeatureStore are returned by subsequent queries even if
     * the transaction has not been committed.
     * 
     * @throws Exception
     */
    @Test
    public void testTransactionStateDiff() throws Exception {
        testData.createTempTable(true);
        // testData.insertTestData();

        final DataStore ds = testData.getDataStore();
        final String typeName = testData.getTempTableName();
        final SimpleFeatureStore transFs;
        transFs = (SimpleFeatureStore) ds.getFeatureSource(typeName);
        final SimpleFeatureType schema = transFs.getSchema();

        // once the transaction is set to the FeatureStore, it lasts until
        // another transaction
        // is set. Calling transaction.close() closes Transaction.State
        // held on it, allowing State objects to release resources. After
        // close() the transaction
        // is no longer valid.
        final Transaction transaction = new DefaultTransaction("test_handle");
        transFs.setTransaction(transaction);

        try {
            // create a feature to add
            SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
            builder.set("INT32_COL", Integer.valueOf(1000));
            builder.set("STRING_COL", "inside transaction");
            SimpleFeature feature = builder.buildFeature(null);

            // add the feature
            transFs.addFeatures(DataUtilities.collection(feature));

            // now confirm for that transaction the feature is fetched, and outside
            // it it's not.
            final Filter filterNewFeature = CQL.toFilter("INT32_COL = 1000");
            final Query newFeatureQuery = new Query(typeName, filterNewFeature);

            SimpleFeatureCollection features;
            features = transFs.getFeatures(filterNewFeature);
            int size = features.size();
            assertEquals(1, size);

            // ok transaction respected, assert the feature does not exist outside
            // it (except is the db is MS SQL Server)
            {
                FeatureReader<SimpleFeatureType, SimpleFeature> autoCommitReader;
                autoCommitReader = ds.getFeatureReader(newFeatureQuery, Transaction.AUTO_COMMIT);
                try {
                    if (databaseIsMsSqlServer) {
                        // SQL Server always is at READ UNCOMMITTED isolation level...
                        assertTrue(autoCommitReader.hasNext());
                    } else {
                        assertFalse(autoCommitReader.hasNext());
                    }
                } finally {
                    autoCommitReader.close();
                }
            }

            // ok, but what if we ask for a feature reader with the same transaction
            {
                FeatureReader<SimpleFeatureType, SimpleFeature> transactionReader;
                transactionReader = ds.getFeatureReader(newFeatureQuery, transaction);
                try {
                    assertTrue(transactionReader.hasNext());
                    transactionReader.next();
                    assertFalse(transactionReader.hasNext());
                } finally {
                    transactionReader.close();
                }
            }

            // now commit, and Transaction.AUTO_COMMIT should carry it over
            // do not close the transaction, we'll keep using it
            try {
                transaction.commit();
            } catch (IOException e) {
                transaction.rollback();
                throw e;
            }

            {
                FeatureReader<SimpleFeatureType, SimpleFeature> autoCommitReader;
                autoCommitReader = ds.getFeatureReader(newFeatureQuery, Transaction.AUTO_COMMIT);
                try {
                    assertTrue(autoCommitReader.hasNext());
                } finally {
                    autoCommitReader.close();
                }
            }

            // now keep using the transaction, it should still work
            transFs.removeFeatures(filterNewFeature);

            // no features removed yet outside the transaction
            {
                FeatureReader<SimpleFeatureType, SimpleFeature> autoCommitReader;
                autoCommitReader = ds.getFeatureReader(newFeatureQuery, Transaction.AUTO_COMMIT);
                try {
                    if (databaseIsMsSqlServer) {
                        // SQL Server always is at READ UNCOMMITTED isolation level...
                        assertFalse(autoCommitReader.hasNext());
                    } else {
                        assertTrue(autoCommitReader.hasNext());
                    }
                } finally {
                    autoCommitReader.close();
                }
            }

            // but yes inside it
            {
                FeatureReader<SimpleFeatureType, SimpleFeature> transactionReader;
                transactionReader = ds.getFeatureReader(newFeatureQuery, transaction);
                try {
                    assertFalse(transactionReader.hasNext());
                } finally {
                    transactionReader.close();
                }
            }

            {
                FeatureReader<SimpleFeatureType, SimpleFeature> autoCommitReader;
                try {
                    transaction.commit();
                    autoCommitReader = ds
                            .getFeatureReader(newFeatureQuery, Transaction.AUTO_COMMIT);
                    assertFalse(autoCommitReader.hasNext());
                } catch (Exception e) {
                    transaction.rollback();
                    throw e;
                }
            }
        } finally {
            transaction.close();
        }

    }

    @Test
    public void testSetFeaturesAutoCommit() throws Exception {
        testData.insertTestData();
        final SimpleFeatureCollection featuresToSet = testData.createTestFeatures(Point.class, 5);
        final DataStore ds = testData.getDataStore();
        final String typeName = testData.getTempTableName();

        final SimpleFeatureStore store;
        store = (SimpleFeatureStore) ds.getFeatureSource(typeName);

        final int initialCount = store.getCount(Query.ALL);
        assertTrue(initialCount > 0);
        assertTrue(initialCount != 5);

        store.setFeatures(DataUtilities.reader(featuresToSet));

        final int newCount = store.getCount(Query.ALL);
        assertEquals(5, newCount);
    }

    @Test
    public void testSetFeaturesTransaction() throws Exception {
        testData.insertTestData();
        final SimpleFeatureCollection featuresToSet = testData.createTestFeatures(Point.class, 5);
        final DataStore ds = testData.getDataStore();
        final String typeName = testData.getTempTableName();

        final Transaction transaction = new DefaultTransaction("testSetFeaturesTransaction handle");
        final SimpleFeatureStore store;
        store = (SimpleFeatureStore) ds.getFeatureSource(typeName);
        store.setTransaction(transaction);

        final int initialCount = store.getCount(Query.ALL);
        assertTrue(initialCount > 0);
        assertTrue(initialCount != 5);

        try {
            store.setFeatures(DataUtilities.reader(featuresToSet));
            final int countInsideTransaction = store.getCount(Query.ALL);
            assertEquals(5, countInsideTransaction);

            final SimpleFeatureSource sourceNoTransaction;
            sourceNoTransaction = ds.getFeatureSource(typeName);
            int countNoTransaction = sourceNoTransaction.getCount(Query.ALL);

            if (databaseIsMsSqlServer) {
                // SQL Server always is at READ UNCOMMITTED isolation level...
                assertEquals(countInsideTransaction, countNoTransaction);
            } else {
                assertEquals(initialCount, countNoTransaction);
            }
            // now commit
            transaction.commit();
            countNoTransaction = sourceNoTransaction.getCount(Query.ALL);
            assertEquals(5, countNoTransaction);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } catch (AssertionFailedError e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    /**
     * Simultate an application where one thread works over a transaction adding features while
     * another thread accesses the same FeatureStore with a query. Archetypical use case being a
     * udig addFeatures command sends calls addFeatures and the rendering thread does getFeatures.
     */
    @Test
    public void testTransactionMultithreadAccess() throws Exception {
        testData.insertTestData();
        // start with an empty table
        final String typeName = testData.getTempTableName();
        final int featureCount = 2;
        final SimpleFeatureCollection testFeatures = testData.createTestFeatures(LineString.class,
                featureCount);

        final DataStore ds = testData.getDataStore();
        final SimpleFeatureStore fStore = (SimpleFeatureStore) ds.getFeatureSource(typeName);
        final Transaction transaction = new DefaultTransaction("testTransactionMultithreadAccess");
        fStore.setTransaction(transaction);

        final boolean[] done = { false, false };
        final Throwable[] errors = new Throwable[2];

        Runnable worker1 = new Runnable() {
            public void run() {
                try {
                    System.err.println("adding..");
                    List<FeatureId> addedFids = fStore.addFeatures(testFeatures);
                    System.err.println("got " + addedFids);
                    final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

                    final Set<FeatureId> fids = new HashSet<FeatureId>();
                    for (FeatureId fid : addedFids) {
                        fids.add(fid);
                    }
                    final Id newFidsFilter = ff.id(fids);

                    System.err.println("querying..");
                    SimpleFeatureCollection features = fStore.getFeatures(newFidsFilter);
                    System.err.println("querying returned...");

                    int size = features.size();
                    System.err.println("Collection Size: " + size);
                    assertEquals(2, size);

                    System.err.println("commiting...");
                    transaction.commit();
                    System.err.println("commited.");

                    size = fStore.getCount(new Query(typeName, newFidsFilter));
                    System.err.println("Size: " + size);
                    assertEquals(2, size);
                } catch (Throwable e) {
                    errors[0] = e;
                    try {
                        System.err.println("rolling back!.");
                        transaction.rollback();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } finally {
                    done[0] = true;
                }
            }
        };

        Runnable worker2 = new Runnable() {
            public void run() {
                try {
                    System.err.println("worker2 calling getFeartures()");
                    SimpleFeatureCollection collection = fStore.getFeatures();
                    System.err.println("worker2 opening iterator...");
                    SimpleFeatureIterator features = collection.features();
                    try {
                        System.err.println("worker2 iterating...");
                        while (features.hasNext()) {
                            SimpleFeature next = features.next();
                            System.out.println("**Got feature " + next.getID());
                        }
                        System.err.println("worker2 closing FeatureCollection");
                    } finally {
                        features.close();
                    }
                    System.err.println("worker2 done.");
                } catch (Throwable e) {
                    errors[1] = e;
                } finally {
                    done[1] = true;
                }
            }
        };

        Thread thread1 = new Thread(worker1, "worker1");
        Thread thread2 = new Thread(worker2, "worker2");
        thread1.start();
        thread2.start();
        while (!(done[0] && done[1])) {
            Thread.sleep(100);
        }
        try {
            System.err.println("closing transaction.");
            transaction.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Throwable worker1Error = errors[0];
        Throwable worker2Error = errors[1];
        if (worker1Error != null || worker2Error != null) {
            String errMessg = "worker1: "
                    + (worker1Error == null ? "ok." : worker1Error.getMessage());
            errMessg += " -- worker2: "
                    + (worker2Error == null ? "ok." : worker2Error.getMessage());

            if (worker1Error != null) {
                worker1Error.printStackTrace();
            }
            if (worker2Error != null) {
                worker2Error.printStackTrace();
            }
            fail(errMessg);
        }
    }

    public void testEditVersionedTableTransactionConcurrently() throws Exception {
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
            final SimpleFeatureSource source;
            final SimpleFeatureStore store;

            source = dataStore.getFeatureSource(tableName);
            store = (SimpleFeatureStore) dataStore.getFeatureSource(tableName);

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
            SimpleFeatureCollection collection;
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
                SimpleFeatureIterator features = store.getFeatures().features();
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
                SimpleFeatureIterator features = source.getFeatures().features();
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

}
