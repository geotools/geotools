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

import static org.geotools.jdbc.SQLDialect.BASE_DBMS_CAPABILITIES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockDataSource;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.NilExpression;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

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
    public void testMappingInitialisationIsThreadSafe() throws InterruptedException, ExecutionException {
        // This test attempts to exercise a race condition on initialisation of mappings.  It is
        // caused when
        // two threads simultaneously try to access mapping of SQL types for a datastore.  This
        // previously led to a ConcurrentModificationException. (c.f. GEOT-4506)
        int nThreads = Math.min(Runtime.getRuntime().availableProcessors(), 2);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for (int i = 0; i < 100; i++) {
            final CountDownLatch latch = new CountDownLatch(nThreads);
            final JDBCDataStore jdbcDataStore = new JDBCDataStore();
            SQLDialect sqlDialect = new BasicSQLDialect(jdbcDataStore) {
                @Override
                public void encodeGeometryValue(Geometry value, int dimension, int srid, StringBuffer sql)
                        throws IOException {}

                @Override
                public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {}

                @Override
                public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
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
                Future f = executorService.submit(() -> {
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
    @SuppressWarnings("PMD.CloseResource") // mock resources
    public void testReaderCallback() throws Exception {
        JDBCReaderCallback callback = mock(JDBCReaderCallback.class);

        JDBCDataStore store = new JDBCDataStore();
        store.setNamespaceURI("http://geotools.org");
        store.setPrimaryKeyFinder(new PrimaryKeyFinder() {
            @Override
            public PrimaryKey getPrimaryKey(JDBCDataStore store, String schema, String table, Connection cx)
                    throws SQLException {
                return new NullPrimaryKey(table);
            }
        });
        store.setCallbackFactory(new JDBCCallbackFactory() {
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

        JDBCFeatureReader reader = new JDBCFeatureReader(rowData, cx, 0, source, tb.buildFeatureType(), new Query());
        while (reader.hasNext()) {
            reader.next();
        }

        verify(callback, times(1)).init(reader);
        verify(callback, times(4)).beforeNext(rowData);
        verify(callback, times(3)).afterNext(rowData, true);
        verify(callback, times(1)).afterNext(rowData, false);
        verify(callback, times(1)).finish(reader);
    }

    @Test
    @SuppressWarnings({"PMD.EmptyControlStatement", "PMD.UnusedLocalVariable"})
    public void testGetConnectionAutocommit() throws Exception {

        JDBCMockObjectFactory jdbcMock = new JDBCMockObjectFactory();
        MockDataSource dataSource = jdbcMock.getMockDataSource();
        dataSource.setupConnection(jdbcMock.getMockConnection());
        JDBCDataStore store = new JDBCDataStore();
        store.setSQLDialect(mock(BasicSQLDialect.class));
        store.setDataSource(dataSource);

        try (Connection conn = store.getConnection(Transaction.AUTO_COMMIT)) {
            Assert.assertEquals(Boolean.TRUE, conn.getAutoCommit());
        }

        try (Transaction transaction = new DefaultTransaction();
                Connection conn2 = store.getConnection(transaction)) {
            Assert.assertEquals(Boolean.FALSE, conn2.getAutoCommit());
        }

        Assert.assertThrows(Exception.class, () -> {
            try (Connection connection = store.getConnection((Transaction) null)) {}
        });
    }

    @Test
    public void testSplitFilterByGetAggregateValue() throws Exception {
        JDBCDataStore store = new JDBCDataStore();
        store.aggregateFunctions = new HashMap<>();
        store.aggregateFunctions.put(CountVisitor.class, "COUNT");
        BasicSQLDialect sqlDialect = mock(BasicSQLDialect.class);
        when(sqlDialect.isGroupBySupported()).thenReturn(true);
        FilterToSQL f2s = new FilterToSQL();
        f2s.setCapabilities(BASE_DBMS_CAPABILITIES);
        when(sqlDialect.createFilterToSQL()).thenReturn(f2s);
        Filter[] filters = new Filter[2];
        filters[0] = Filter.EXCLUDE;
        filters[1] = Filter.EXCLUDE;
        when(sqlDialect.splitFilter(any(), any())).thenReturn(filters);
        store.setSQLDialect(sqlDialect);
        GroupByVisitor groupVisitor =
                new GroupByVisitor(Aggregate.COUNT, NilExpression.NIL, Collections.emptyList(), null);
        Query query = mock(Query.class);
        when(query.getFilter()).thenReturn(Filter.INCLUDE);
        SimpleFeatureType featureType = mock(SimpleFeatureType.class);
        when(query.getTypeName()).thenReturn("test");
        store.getAggregateValue(groupVisitor, featureType, query, null);
        verify(sqlDialect, times(1)).splitFilter(any(), any());
    }
}
