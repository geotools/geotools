/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockDatabaseMetaData;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockStatement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.type.GeometryDescriptor;

/** @author Dean Povey */
public class JDBCDataStoreTest {

    // Subtype of the requested mapping type.  This is used in the mapping initialisation test
    // as it triggers the code path that previously led to a ConcurrentModificationException.
    public static class DateSubClass extends Timestamp {

        public DateSubClass(long time) {
            super(time);
        }
    }

    @Test
    public void testMappingInitialisationIsThreadSafe()
            throws InterruptedException, ExecutionException {
        // This test attempts to exercise a race condition on initialisation of mappings.  It is
        // caused when
        // two threads simultaneously try to access mapping of SQL types for a datastore.  This
        // previously led to a ConcurrentModificationException. (c.f. GEOT-4506)
        int nThreads = Math.min(Runtime.getRuntime().availableProcessors(), 2);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for (int i = 0; i < 100; i++) {
            final CountDownLatch latch = new CountDownLatch(nThreads);
            final JDBCDataStore jdbcDataStore = new JDBCDataStore();
            SQLDialect sqlDialect =
                    new BasicSQLDialect(jdbcDataStore) {
                        @Override
                        public void encodeGeometryValue(
                                Geometry value, int dimension, int srid, StringBuffer sql)
                                throws IOException {}

                        @Override
                        public void encodeGeometryEnvelope(
                                String tableName, String geometryColumn, StringBuffer sql) {}

                        @Override
                        public Envelope decodeGeometryEnvelope(
                                ResultSet rs, int column, Connection cx)
                                throws SQLException, IOException {
                            return null;
                        }

                        @Override
                        public Geometry decodeGeometryValue(
                                GeometryDescriptor descriptor,
                                ResultSet rs,
                                String column,
                                GeometryFactory factory,
                                Connection cx,
                                Hints hints)
                                throws IOException, SQLException {
                            return null;
                        }
                    };
            jdbcDataStore.setSQLDialect(sqlDialect);
            List<Future> futures = new ArrayList<>();
            for (int j = 0; j < nThreads; j++) {
                Future f =
                        executorService.submit(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            // Get all the threads to the same point to increase the
                                            // likelihood
                                            // of finding the issue
                                            latch.countDown();
                                            latch.await();
                                        } catch (InterruptedException e) {
                                            Thread.interrupted();
                                            return;
                                        }
                                        jdbcDataStore.getMapping(DateSubClass.class);
                                    }
                                });
                futures.add(f);
            }
            for (Future future : futures) {
                future.get();
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }

    @Test(expected = IOException.class)
    public void testCheckAllInsertedPositive() throws IOException {
        JDBCDataStore.checkAllInserted(new int[0], 0);
        JDBCDataStore.checkAllInserted(new int[] {1, 1, 1}, 3);
        JDBCDataStore.checkAllInserted(new int[] {3}, 3);
        JDBCDataStore.checkAllInserted(new int[] {1, 1, 0}, 3);
    }

    @Test
    public void testReaderCallback() throws Exception {
        JDBCReaderCallback callback = mock(JDBCReaderCallback.class);

        JDBCDataStore store = new JDBCDataStore();
        store.setNamespaceURI("http://geotools.org");
        store.setPrimaryKeyFinder(
                new PrimaryKeyFinder() {
                    @Override
                    public PrimaryKey getPrimaryKey(
                            JDBCDataStore store, String schema, String table, Connection cx)
                            throws SQLException {
                        return new NullPrimaryKey(table);
                    }
                });
        store.setCallbackFactory(
                new JDBCCallbackFactory() {
                    @Override
                    public String getName() {
                        return "mock";
                    }

                    @Override
                    public JDBCReaderCallback createReaderCallback() {
                        return callback;
                    }
                });
        store.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));

        JDBCMockObjectFactory jdbcMock = new JDBCMockObjectFactory();
        store.setDataSource(jdbcMock.getMockDataSource());

        MockResultSet tableTypes = new MockResultSet("tableTypes");
        tableTypes.addColumn("TABLE_TYPE", Arrays.asList("TABLE"));

        MockResultSet tables = new MockResultSet("tables");
        tables.addColumn("TABLE_NAME", Arrays.asList("foo"));
        tables.addColumn("TABLE_SCHEM", Arrays.asList(""));

        MockDatabaseMetaData meta = new MockDatabaseMetaData();
        meta.setTableTypes(tableTypes);
        meta.setTables(tables);

        MockConnection cx = jdbcMock.getMockConnection();
        cx.setMetaData(meta);

        BasicSQLDialect dialect = mock(BasicSQLDialect.class);
        when(dialect.getDesiredTablesType()).thenReturn(new String[] {"TABLE"});
        when(dialect.includeTable(anyString(), anyString(), any(Connection.class)))
                .thenReturn(true);

        store.setSQLDialect(dialect);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("foo");
        tb.setNamespaceURI("http://geotools.org");
        tb.add("name", String.class);

        JDBCFeatureSource source = mock(JDBCFeatureSource.class);
        when(source.getDataStore()).thenReturn(store);

        MockResultSet rowData = new MockResultSet("foo");
        rowData.addColumn("name", Arrays.asList("foo", "bar", "baz"));
        rowData.setStatement(new MockStatement(cx));

        JDBCFeatureReader reader =
                new JDBCFeatureReader(rowData, cx, 0, source, tb.buildFeatureType(), new Query());
        while (reader.hasNext()) {
            reader.next();
        }

        verify(callback, times(1)).init(reader);
        verify(callback, times(4)).beforeNext(rowData);
        verify(callback, times(3)).afterNext(rowData, true);
        verify(callback, times(1)).afterNext(rowData, false);
        verify(callback, times(1)).finish(reader);
    }
}
