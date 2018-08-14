/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertTrue;

import com.mockrunner.mock.jdbc.MockDataSource;
import com.mockrunner.mock.jdbc.MockDatabaseMetaData;
import com.mockrunner.mock.jdbc.MockResultSet;
import java.awt.RenderingHints;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.jdbc.EnsureAuthorizationTest.TracingMockConnection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

/** @author Ivan Martynovskyi */
@RunWith(Parameterized.class)
public class JDBCDataStoreModSQLTest {
    private static final String SAMPLE_FEATURE_NAME = "FEATURE_NAME";
    private static JDBCDataStore dataStore = null;
    private static SimpleFeatureType featureType = null;
    private static String attribute = "temperature";
    private static PrimaryKey key = null;

    private Query query;
    private StringBuffer expectedSQL;
    private StringBuffer actualSQL;

    public static void setUpDataStore() throws SQLException, IOException {
        TracingMockConnection cx = new TracingMockConnection();
        configureMetadata(cx);

        dataStore = new JDBCDataStore();
        dataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory2());
        dataStore.setSQLDialect(createBasicSQLDialect(dataStore));

        MockDataSource dataSource = new MockDataSource();
        dataSource.setupConnection(cx);
        dataStore.setDataSource(dataSource);
        dataStore.setNamespaceURI("");

        // configure SimpleFeatureType
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(SAMPLE_FEATURE_NAME);
        tb.setNamespaceURI("");
        tb.add(attribute, String.class);
        featureType = tb.buildFeatureType();
        key = dataStore.getPrimaryKey(featureType);
    }

    public JDBCDataStoreModSQLTest(Query query, StringBuffer expectedSQL) {
        this.query = query;
        this.expectedSQL = expectedSQL;
    }

    @Parameterized.Parameters
    public static Collection getListOfCases() throws SQLException, IOException {
        Object[][] listOfCases = new Object[5][2];
        Query query;
        StringBuffer sqlExpected;
        setUpDataStore();
        // configuring cases
        // case 0 - query without FEATURE_CALC hint
        query = new Query("Case 0");
        query.setPropertyNames(Arrays.asList(attribute));
        sqlExpected = new StringBuffer("SELECT ");
        for (PrimaryKeyColumn col : key.getColumns()) {
            dataStore.getSQLDialect().encodeColumnName(null, col.getName(), sqlExpected);
            sqlExpected.append(",");
        }
        dataStore.getSQLDialect().encodeColumnName(null, attribute, sqlExpected);
        sqlExpected.append(" FROM ");
        dataStore.encodeTableName(featureType.getTypeName(), sqlExpected, query.getHints());
        listOfCases[0][0] = query;
        listOfCases[0][1] = sqlExpected;

        // case 1 - query with UniqueVisitor hint
        query = new Query("Case 1");
        query.setPropertyNames(Arrays.asList(attribute));
        query.getHints().add(new RenderingHints(Hints.FEATURE_CALC, new UniqueVisitor(attribute)));
        sqlExpected = new StringBuffer("SELECT ");
        for (PrimaryKeyColumn col : key.getColumns()) {
            sqlExpected.append("MIN(");
            dataStore.getSQLDialect().encodeColumnName(null, col.getName(), sqlExpected);
            sqlExpected.append("),");
        }
        dataStore.getSQLDialect().encodeColumnName(null, attribute, sqlExpected);
        sqlExpected.append(" FROM ");
        dataStore.encodeTableName(featureType.getTypeName(), sqlExpected, query.getHints());

        sqlExpected.append(" GROUP BY ");
        dataStore.getSQLDialect().encodeColumnName(null, attribute, sqlExpected);
        listOfCases[1][0] = query;
        listOfCases[1][1] = sqlExpected;

        // case 2 - query with MinVisitor hint
        query = new Query("Case 2");
        query.setPropertyNames(Arrays.asList(attribute));
        query.getHints().add(new RenderingHints(Hints.FEATURE_CALC, new MinVisitor(attribute)));
        sqlExpected = new StringBuffer("SELECT ");

        for (PrimaryKeyColumn col : key.getColumns()) {
            sqlExpected.append("1,");
        }
        sqlExpected.append("MIN(");
        dataStore.getSQLDialect().encodeColumnName(null, attribute, sqlExpected);
        sqlExpected.append(") FROM ");
        dataStore.encodeTableName(featureType.getTypeName(), sqlExpected, query.getHints());
        listOfCases[2][0] = query;
        listOfCases[2][1] = sqlExpected;

        // case 3 - query with MaxVisitor hint
        query = new Query("Case 3");
        query.setPropertyNames(Arrays.asList(attribute));
        query.getHints().add(new RenderingHints(Hints.FEATURE_CALC, new MaxVisitor(attribute)));
        sqlExpected = new StringBuffer("SELECT ");
        for (PrimaryKeyColumn col : key.getColumns()) {
            sqlExpected.append("1,");
        }
        sqlExpected.append("MAX(");
        dataStore.getSQLDialect().encodeColumnName(null, attribute, sqlExpected);
        sqlExpected.append(") FROM ");
        dataStore.encodeTableName(featureType.getTypeName(), sqlExpected, query.getHints());
        listOfCases[3][0] = query;
        listOfCases[3][1] = sqlExpected;

        // case 4 - query with CountVisitor hint
        query = new Query("Case 4");
        query.setPropertyNames(Arrays.asList(attribute));
        query.getHints().add(new RenderingHints(Hints.FEATURE_CALC, new CountVisitor()));
        sqlExpected = new StringBuffer("SELECT ");
        for (PrimaryKeyColumn col : key.getColumns()) {
            dataStore.getSQLDialect().encodeColumnName(null, col.getName(), sqlExpected);
            sqlExpected.append(",");
        }
        dataStore.getSQLDialect().encodeColumnName(null, attribute, sqlExpected);
        sqlExpected.append(" FROM ");
        dataStore.encodeTableName(featureType.getTypeName(), sqlExpected, query.getHints());
        listOfCases[4][0] = query;
        listOfCases[4][1] = sqlExpected;

        return Arrays.asList(listOfCases);
    }

    @Before
    public void initialize() throws SQLException {
        actualSQL = new StringBuffer("SELECT ");
        for (PrimaryKeyColumn col : key.getColumns()) {
            dataStore.getSQLDialect().encodeColumnName(null, col.getName(), actualSQL);
            actualSQL.append(",");
        }
        dataStore.getSQLDialect().encodeColumnName(null, attribute, actualSQL);
        actualSQL.append(" FROM ");
        dataStore.encodeTableName(featureType.getTypeName(), actualSQL, query.getHints());
    }

    @Test
    public void testSelectSQLOptimization() throws IOException, SQLException {
        // configure SimpleFeatureType
        dataStore.optimizeSQL(featureType, query, actualSQL);
        assertTrue(expectedSQL.toString().equalsIgnoreCase(actualSQL.toString()));
    }

    private static void configureMetadata(TracingMockConnection cx) throws SQLException {
        ((MockDatabaseMetaData) cx.getMetaData()).setSearchStringEscape("");

        MockResultSet tableTypes = new MockResultSet("TABLE_TYPES");
        tableTypes.addColumn("TABLE_TYPE");
        tableTypes.addRow(new Object[] {"TABLE"});
        ((MockDatabaseMetaData) cx.getMetaData()).setTableTypes(tableTypes);

        MockResultSet tables = new MockResultSet("TABLES");
        tables.addColumn("TABLE_SCHEM");
        tables.addColumn("TABLE_NAME");
        tables.addRow(new Object[] {"FEATURE_SCHEM", SAMPLE_FEATURE_NAME});
        ((MockDatabaseMetaData) cx.getMetaData()).setTables(tables);

        MockResultSet key = new MockResultSet("KEY");
        key.addColumn("COLUMN_NAME");
        key.addColumn("DATA_TYPE");
        key.addRow(new Object[] {"FID", 1});
        ((MockDatabaseMetaData) cx.getMetaData())
                .setPrimaryKeys(null, null, SAMPLE_FEATURE_NAME, key);
        MockResultSet columns = new MockResultSet("COLUMNS");
        columns.addColumn("COLUMN_NAME");
        columns.addColumn("DATA_TYPE");
        columns.addRow(new Object[] {"FID", 1});
        ((MockDatabaseMetaData) cx.getMetaData())
                .setColumns(null, null, SAMPLE_FEATURE_NAME, "FID", columns);
    }

    private static BasicSQLDialect createBasicSQLDialect(JDBCDataStore dataStore) {
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
}
