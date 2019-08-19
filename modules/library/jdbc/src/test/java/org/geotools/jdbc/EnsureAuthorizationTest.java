/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockDataSource;
import com.mockrunner.mock.jdbc.MockDatabaseMetaData;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockStatement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;

/**
 * Tests for GEOT-4792: optimizations for JDBCDataStore ensureAuthorization.
 *
 * @author Mauro Bartolomeoli (mauro.bartolomeoli @ geo-solutions.it)
 */
public class EnsureAuthorizationTest {

    public static class TracingMockConnection extends MockConnection {
        // number of calls to executeQuery
        int calls = 0;
        // flag set to true if a filter on FeatureId is done
        boolean filteredOnIds = false;
        MockStatement stmt;
        MockPreparedStatement pstmt;

        @Override
        public Statement createStatement() throws SQLException {
            // statement used for all queries
            stmt =
                    new MockStatement(this) {

                        @Override
                        public ResultSet executeQuery(String sql) throws SQLException {
                            // check if the condition ID = 'some text' is present in the query
                            if (sql.matches("^.*\"ID\"\\s+=\\s+'.*'.*$")) {
                                filteredOnIds = true;
                            }
                            return new MockResultSet(SAMPLE_FEATURE_NAME);
                        }
                    };
            calls++;
            return stmt;
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return prepareStatement(sql, 0, 0);
        }

        @Override
        public PreparedStatement prepareStatement(final String sql, int arg1, int arg2)
                throws SQLException {
            pstmt =
                    new MockPreparedStatement(this, sql, arg1, arg2) {

                        @Override
                        public ResultSet executeQuery() throws SQLException {
                            // check if the condition ID = ? is present in the query
                            if (sql.matches("^.*\"ID\"\\s+=\\s+\\?.*$")) {
                                filteredOnIds = true;
                            }
                            return new MockResultSet(SAMPLE_FEATURE_NAME);
                        }
                    };
            calls++;
            return pstmt;
        }
    }

    private JDBCDataStore dataStore;
    private SimpleFeatureType featureType;
    private Transaction tx;
    private TracingMockConnection cx;

    private static final String SAMPLE_FEATURE_NAME = "SAMPLE_FEATURE";
    private static final String SAMPLE_FEATURE_ID = "SAMPLE_ID";

    @Before
    public void setUp() throws SQLException {

        cx = new TracingMockConnection();
        configureMetadata();

        dataStore = new JDBCDataStore();
        dataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory2());
        dataStore.setSQLDialect(createBasicSQLDialect());

        MockDataSource dataSource = new MockDataSource();
        dataSource.setupConnection(cx);
        dataStore.setDataSource(dataSource);

        featureType = Mockito.mock(SimpleFeatureType.class);
        Mockito.when(featureType.getTypeName()).thenReturn(SAMPLE_FEATURE_NAME);
        Mockito.when(featureType.getName()).thenReturn(new NameImpl(SAMPLE_FEATURE_NAME));
        tx = new DefaultTransaction();
    }

    @Test
    public void testFetchSizeUsedWhenNoPreparedStatement() throws IOException, SQLException {
        dataStore.setFetchSize(1000);
        createLock();
        dataStore.ensureAuthorization(featureType, Filter.INCLUDE, tx, cx);
        assertEquals(1000, cx.stmt.getFetchSize());
    }

    @Test
    public void testQueryIsNotExecutedIfThereAreNoLocks() throws IOException, SQLException {
        dataStore.ensureAuthorization(featureType, Filter.INCLUDE, tx, cx);
        assertEquals(0, cx.calls);
    }

    @Test
    public void testQueryIsExecutedIfThereAreLocks() throws IOException, SQLException {
        createLock();
        dataStore.ensureAuthorization(featureType, Filter.INCLUDE, tx, cx);
        assertNotEquals(0, cx.calls);
    }

    @Test
    public void testQueryIsFilteredOnLockedFeatureIds() throws IOException, SQLException {
        createLock();
        dataStore.ensureAuthorization(featureType, Filter.INCLUDE, tx, cx);
        assertTrue(cx.filteredOnIds);
    }

    @Test
    public void testQueryIsFilteredOnLockedFeatureIdsWithPreparedStatements()
            throws IOException, SQLException {
        dataStore.setSQLDialect(createPreparedSQLDialect());
        createLock();
        dataStore.ensureAuthorization(featureType, Filter.INCLUDE, tx, cx);
        assertTrue(cx.filteredOnIds);
    }

    @Test
    public void testQueryIsNotFilteredOnLockedFeatureIdsIfThereAreTooManyLocks()
            throws IOException, SQLException {
        createManyLocks();
        dataStore.ensureAuthorization(featureType, Filter.INCLUDE, tx, cx);
        assertFalse(cx.filteredOnIds);
    }

    private void createManyLocks() throws IOException {
        for (int count = 0; count < JDBCDataStore.MAX_IDS_IN_FILTER + 1; count++) {
            dataStore
                    .getLockingManager()
                    .lockFeatureID(
                            SAMPLE_FEATURE_NAME,
                            count + "",
                            tx,
                            new FeatureLock(count + "", 10000000L));
        }
    }

    private void createLock() throws IOException {
        dataStore
                .getLockingManager()
                .lockFeatureID(
                        SAMPLE_FEATURE_NAME,
                        SAMPLE_FEATURE_ID,
                        tx,
                        new FeatureLock(SAMPLE_FEATURE_ID, 10000000L));
    }

    private void configureMetadata() throws SQLException {
        ((MockDatabaseMetaData) cx.getMetaData()).setSearchStringEscape("");

        MockResultSet tableTypes = new MockResultSet("TABLE_TYPES");
        tableTypes.addColumn("TABLE_TYPE");
        tableTypes.addRow(new Object[] {"TABLE"});
        ((MockDatabaseMetaData) cx.getMetaData()).setTableTypes(tableTypes);

        MockResultSet tables = new MockResultSet("TABLES");
        tables.addColumn("TABLE_SCHEM");
        tables.addColumn("TABLE_NAME");
        tables.addRow(new Object[] {"", SAMPLE_FEATURE_NAME});
        ((MockDatabaseMetaData) cx.getMetaData()).setTables(tables);

        MockResultSet key = new MockResultSet("KEY");
        key.addColumn("COLUMN_NAME");
        key.addColumn("DATA_TYPE");
        key.addRow(new Object[] {"ID", 1});
        ((MockDatabaseMetaData) cx.getMetaData())
                .setPrimaryKeys(null, null, SAMPLE_FEATURE_NAME, key);
        MockResultSet columns = new MockResultSet("COLUMNS");
        columns.addColumn("COLUMN_NAME");
        columns.addColumn("DATA_TYPE");
        columns.addRow(new Object[] {"ID", 1});
        ((MockDatabaseMetaData) cx.getMetaData())
                .setColumns(null, null, SAMPLE_FEATURE_NAME, "ID", columns);
    }

    private BasicSQLDialect createBasicSQLDialect() {
        return new BasicSQLDialect(dataStore) {

            @Override
            public void encodeGeometryValue(
                    Geometry value, int dimension, int srid, StringBuffer sql) throws IOException {}

            @Override
            public void encodeGeometryEnvelope(
                    String tableName, String geometryColumn, StringBuffer sql) {}

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
    }

    private PreparedStatementSQLDialect createPreparedSQLDialect() {
        return new PreparedStatementSQLDialect(dataStore) {
            @Override
            public void encodeGeometryEnvelope(
                    String tableName, String geometryColumn, StringBuffer sql) {}

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

            @Override
            public void setGeometryValue(
                    Geometry g,
                    int dimension,
                    int srid,
                    Class binding,
                    PreparedStatement ps,
                    int column)
                    throws SQLException {}
        };
    }
}
