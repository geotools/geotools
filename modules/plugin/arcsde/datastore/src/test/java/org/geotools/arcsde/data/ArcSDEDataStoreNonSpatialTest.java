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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeDBMSInfo;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeRegistration;

/**
 * {@link ArcSDEDataStore} integration test for the case of registered, non spatial tables as
 * required by the new feature <a href="http://jira.codehaus.org/browse/GEOT-2548">GEOT-2548</a>
 * 
 * @author Gabriel Roldan, OpenGeo
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/test/java/org
 *         /geotools/arcsde/data/ArcSDEDataStoreNonSpatialTest.java $
 * @version $Id$
 * @since 2.5.6
 */
public class ArcSDEDataStoreNonSpatialTest {

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

    private static TestData testData;

    private static String seRowidNoneTable;

    private static String seRowidUserTable;

    private static String seRowidSdeTable;

    private DataStore ds;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        testData = new TestData();
        testData.setUp();

        final String baseTypeName = testData.getTempTableName();
        seRowidNoneTable = baseTypeName + "_ROWID_NONE";
        seRowidSdeTable = baseTypeName + "_ROWID_SDE";
        seRowidUserTable = baseTypeName + "_ROWID_USER";

        ISessionPool sessionPool = testData.getConnectionPool();
        ISession session = sessionPool.getSession();
        try {
            SeDBMSInfo dbInfo = session.getDBMSInfo();
            databaseIsMsSqlServer = dbInfo.dbmsId == SeDBMSInfo.SE_DBMS_IS_SQLSERVER;

            session.issue(new Command<Void>() {
                @Override
                public Void execute(ISession session, SeConnection connection) throws SeException,
                        IOException {

                    final String rowIdColName = "ROW_ID";
                    // just register the non spatial table
                    final boolean createLayer = false;
                    final int shapeTypeMask = -1;// not relevant

                    int rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE;
                    testData.createTestTable(session, seRowidNoneTable, rowIdColName,
                            rowIdColumnType, createLayer, shapeTypeMask);

                    rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_USER;
                    testData.createTestTable(session, seRowidUserTable, rowIdColName,
                            rowIdColumnType, createLayer, shapeTypeMask);

                    rowIdColumnType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE;
                    testData.createTestTable(session, seRowidSdeTable, rowIdColName,
                            rowIdColumnType, createLayer, shapeTypeMask);
                    return null;
                }
            });
        } finally {
            session.dispose();
        }
    }

    @AfterClass
    public static void oneTimeTearDown() {
    }

    @Before
    public void setUp() throws Exception {
        ISessionPool sessionPool = testData.newSessionPool();
        final boolean allowNonSpatialTables = true;
        ds = new ArcSDEDataStore(sessionPool, null, null, allowNonSpatialTables);

        // List<String> typeNames = new ArrayList<String>(Arrays.asList(ds.getTypeNames()));
        // Collections.sort(typeNames);
        // for (String s : typeNames) {
        // System.out.println(s);
        // }
    }

    @After
    public void tearDown() {
        if (ds != null) {
            ds.dispose();
        }
    }

    @Test
    public void testGetTypeNamesContainNonSpatialTables() throws IOException {
        List<String> typeNames = Arrays.asList(ds.getTypeNames());

        assertFalse(seRowidNoneTable + " has no row id", typeNames.contains(seRowidNoneTable));

        assertTrue(seRowidUserTable, typeNames.contains(seRowidUserTable));

        assertTrue(seRowidSdeTable, typeNames.contains(seRowidSdeTable));
    }

    @Test
    public void testGetFeatureType() throws IOException {
        SimpleFeatureType schema;
        schema = ds.getSchema(seRowidSdeTable);
        assertNotNull(schema);
        assertNull(schema.getGeometryDescriptor());
        assertEquals(3, schema.getAttributeCount());

        schema = ds.getSchema(seRowidUserTable);
        assertNotNull(schema);
        assertNull(schema.getGeometryDescriptor());
        assertEquals(4, schema.getAttributeCount());
    }

    @Test
    public void testFeatureSource_RowIDUser_Table() throws Exception {
        testFeatureSource(seRowidUserTable);
    }

    @Test
    public void testFeatureSource_RowIDSDE_Table() throws Exception {
        testFeatureSource(seRowidSdeTable);
    }

    private void testFeatureSource(final String tableName) throws IOException, DataSourceException,
            UnavailableConnectionException, CQLException {

        testData.truncateTestTable(tableName);

        SimpleFeatureSource fs;

        fs = ds.getFeatureSource(tableName);
        assertNotNull(fs);

        assertNull(fs.getBounds());
        assertTrue(fs instanceof FeatureStore);
        assertEquals(0, fs.getCount(Query.ALL));

        addFeatures(tableName, Transaction.AUTO_COMMIT).close();

        assertNotNull(fs.getFeatures());
        assertEquals(2, fs.getFeatures().size());
        assertNull(fs.getBounds());

        Filter filter = CQL.toFilter("INT_COL = 2000");

        SimpleFeatureCollection features = fs.getFeatures(filter);
        assertNotNull(features);
        assertEquals(1, features.size());
        assertNull(fs.getBounds());

        SimpleFeatureIterator iterator = features.features();
        assertTrue(iterator.hasNext());
        SimpleFeature next = iterator.next();
        assertNotNull(next);
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testFeatureWriter_RowIDSDE_AutoCommit() throws Exception {
        final String tableName = seRowidSdeTable;
        testFeatureWriterAutoCommit(tableName);
    }

    @Test
    public void testFeatureWriter_RowIDSDE_Transaction() throws IOException,
            UnavailableConnectionException {
        final String tableName = seRowidSdeTable;
        final Transaction transaction = new DefaultTransaction();
        try {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

            writer = addFeatures(tableName, transaction);
            /*
             * This one works regardless of the database being MSSQL or not because the table IS
             * being created as versioned by testData.createTestTable.
             * <http://support.esri.com/index.cfm?fa=knowledgebase.techarticles.articleShow&d=32190>
             */
            assertEquals(0, ds.getFeatureSource(tableName).getCount(Query.ALL));
            transaction.commit();
            writer.close();
            assertEquals(2, ds.getFeatureSource(tableName).getCount(Query.ALL));
        } finally {
            transaction.close();
        }
    }

    @Test
    public void testFeatureWriter_RowID_USER_AutoCommit() throws Exception {
        final String tableName = seRowidUserTable;
        testFeatureWriterAutoCommit(tableName);
    }

    private void testFeatureWriterAutoCommit(final String tableName) throws IOException,
            CQLException, UnavailableConnectionException {
        final Transaction transaction = Transaction.AUTO_COMMIT;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = addFeatures(tableName, transaction);
        writer.close();
        assertEquals(2, ds.getFeatureSource(tableName).getCount(Query.ALL));

        Query query = new Query(tableName, CQL.toFilter("STRING_COL = 'modified'"));
        assertEquals(0, ds.getFeatureSource(tableName).getCount(query));

        Filter f = CQL.toFilter("INT_COL = 1000");
        writer = ds.getFeatureWriter(tableName, f, transaction);
        assertTrue(writer.hasNext());
        writer.next().setAttribute("STRING_COL", "modified");
        writer.write();
        writer.close();

        assertEquals(1, ds.getFeatureSource(tableName).getCount(query));

        writer = ds.getFeatureWriter(tableName, f, transaction);
        assertTrue(writer.hasNext());
        assertNotNull(writer.next());
        writer.remove();
        assertFalse(writer.hasNext());
        writer.close();
    }

    @Test
    public void testFeatureWriter_RowID_USER_Transaction() throws IOException,
            UnavailableConnectionException {
        final String tableName = seRowidUserTable;
        final Transaction transaction = new DefaultTransaction();
        try {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

            writer = addFeatures(tableName, transaction);
            if (databaseIsMsSqlServer) {
                /*
                 * SQL Server always is at READ UNCOMMITTED isolation level iff the table is not
                 * versioned. And this one can't be versioned cause it has no sde maintained row id
                 * <
                 * http://support.esri.com/index.cfm?fa=knowledgebase.techarticles.articleShow&d=32190
                 * >
                 */
                assertEquals(2, ds.getFeatureSource(tableName).getCount(Query.ALL));
            } else {
                assertEquals(0, ds.getFeatureSource(tableName).getCount(Query.ALL));
            }
            transaction.commit();
            writer.close();
            assertEquals(2, ds.getFeatureSource(tableName).getCount(Query.ALL));
        } finally {
            transaction.close();
        }
    }

    private FeatureWriter<SimpleFeatureType, SimpleFeature> addFeatures(final String tableName,
            final Transaction transaction) throws IOException, UnavailableConnectionException {

        testData.truncateTestTable(tableName);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = ds.getFeatureWriter(tableName, transaction);

        assertNotNull(writer);
        assertFalse(writer.hasNext());

        SimpleFeature feature;

        feature = writer.next();
        if (writer.getFeatureType().getDescriptor("ROW_ID") != null) {
            feature.setAttribute("ROW_ID", Integer.valueOf(1000));
        }
        feature.setAttribute("INT_COL", Integer.valueOf(1000));
        feature.setAttribute("STRING_COL", "s1");
        writer.write();

        feature = writer.next();
        if (writer.getFeatureType().getDescriptor("ROW_ID") != null) {
            feature.setAttribute("ROW_ID", Integer.valueOf(2000));
        }
        feature.setAttribute("INT_COL", Integer.valueOf(2000));
        feature.setAttribute("STRING_COL", "s2");
        writer.write();

        return writer;
    }

    @Test
    public void testFetureStore_ROWID_SDE_AutoCommit() throws IOException,
            UnavailableConnectionException {
        String tableName = seRowidSdeTable;

        List<FeatureId> fids = testFeatureStore(tableName, Transaction.AUTO_COMMIT);
        assertEquals(1, fids.size());
        assertEquals(1, ds.getFeatureSource(tableName).getCount(Query.ALL));
    }

    @Test
    public void testFetureStore_ROWID_SDE_Transaction() throws IOException,
            UnavailableConnectionException {
        String tableName = seRowidSdeTable;

        Transaction transaction = new DefaultTransaction();
        try {
            List<FeatureId> fids = testFeatureStore(tableName, transaction);
            assertEquals(1, fids.size());

            assertEquals(0, ds.getFeatureSource(tableName).getCount(Query.ALL));
            transaction.commit();
            assertEquals(1, ds.getFeatureSource(tableName).getCount(Query.ALL));
        } finally {
            transaction.close();
        }
    }

    @Test
    public void testFetureStore_ROWID_USER_AutoCommit() throws IOException,
            UnavailableConnectionException {
        String tableName = seRowidUserTable;

        List<FeatureId> fids = testFeatureStore(tableName, Transaction.AUTO_COMMIT);
        assertEquals(1, fids.size());

        assertEquals(1, ds.getFeatureSource(tableName).getCount(Query.ALL));
    }

    @Test
    public void testFetureStore_ROWID_USER_Transaction() throws IOException,
            UnavailableConnectionException {
        String tableName = seRowidUserTable;

        Transaction transaction = new DefaultTransaction();
        try {
            List<FeatureId> fids = testFeatureStore(tableName, transaction);
            assertEquals(1, fids.size());

            if (databaseIsMsSqlServer) {
                /*
                 * SQL Server always is at READ UNCOMMITTED isolation level iff the table is not
                 * versioned. And this one can't be versioned cause it has no sde maintained row id
                 * <
                 * http://support.esri.com/index.cfm?fa=knowledgebase.techarticles.articleShow&d=32190
                 * >
                 */
                assertEquals(1, ds.getFeatureSource(tableName).getCount(Query.ALL));
            } else {
                assertEquals(0, ds.getFeatureSource(tableName).getCount(Query.ALL));
            }
            transaction.commit();
            assertEquals(1, ds.getFeatureSource(tableName).getCount(Query.ALL));
        } finally {
            transaction.close();
        }
    }

    private List<FeatureId> testFeatureStore(final String tableName, final Transaction transaction)
            throws IOException, UnavailableConnectionException {
        testData.truncateTestTable(tableName);

        SimpleFeatureStore store;
        store = (SimpleFeatureStore) ds.getFeatureSource(tableName);
        assertNotNull(store);

        store.setTransaction(transaction);

        SimpleFeatureType schema = store.getSchema();
        assertNotNull(schema);

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
        if (schema.getDescriptor("ROW_ID") != null) {
            // it's user managed...
            builder.set("ROW_ID", Integer.valueOf(1000));
        }
        builder.set("INT_COL", Integer.valueOf(1000));
        builder.set("STRING_COL", Integer.valueOf(1000));

        SimpleFeatureCollection fc;
        fc = DataUtilities.collection(builder.buildFeature(null));

        List<FeatureId> fids = store.addFeatures(fc);
        assertNotNull(fids);
        assertEquals(1, fids.size());
        return fids;
    }

    @Test
    public void testCreateGeometrylessSchema() throws Exception {
        final String typeName = testData.getTempTableName() + "_CREATE_GEOMLESS";
        testData.deleteTable(typeName);
        try {
            SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
            b.setName(typeName);

            b.add("STRING_COL", String.class);
            b.add("INT_COL", Integer.class);
            b.add("LONG_COL", Long.class);

            final SimpleFeatureType type = b.buildFeatureType();

            ds.createSchema(type);

            SimpleFeatureType schema = ds.getSchema(typeName);
            assertNotNull(schema);
            assertEquals(3, schema.getAttributeCount());
        } finally {
            // testData.deleteTable(typeName);
        }
    }
}
