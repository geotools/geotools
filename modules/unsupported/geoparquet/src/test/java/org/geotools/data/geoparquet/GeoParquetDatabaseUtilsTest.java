/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.duckdb.DuckDBConnection;
import org.geotools.api.data.DataAccessFactory;
import org.junit.Before;
import org.junit.Test;

/** Tests for {@link GeoParquetDatabaseUtils}. */
@SuppressWarnings("PMD.CheckResultSet")
public class GeoParquetDatabaseUtilsTest {

    private Map<String, Object> params;
    private DataAccessFactory.Param[] parametersInfo;

    @Before
    public void setUp() {
        // Create test parameters
        params = new HashMap<>();
        params.put(GeoParquetDataStoreFactory.DBTYPE.key, GeoParquetDataStoreFactory.GEOPARQUET);
        params.put(GeoParquetDataStoreFactory.URI_PARAM.key, "file:/test/data.parquet");
        params.put(GeoParquetDataStoreFactory.NAMESPACE.key, "http://test.example.com");

        parametersInfo = new GeoParquetDataStoreFactory().getParametersInfo();
    }

    @Test
    public void testParameterOrder() throws IOException {
        // Create parameters with different order
        Map<String, Object> params1 = new LinkedHashMap<>();
        params1.put("uri", "file:/test/data.parquet");
        params1.put("dbtype", "geoparquet");
        params1.put("namespace", "http://test.example.com");

        Map<String, Object> params2 = new LinkedHashMap<>();
        params2.put("namespace", "http://test.example.com");
        params2.put("dbtype", "geoparquet");
        params2.put("uri", "file:/test/data.parquet");

        String url1 = GeoParquetDatabaseUtils.getJDBCUrl(params1, parametersInfo);
        String url2 = GeoParquetDatabaseUtils.getJDBCUrl(params2, parametersInfo);

        assertEquals("Parameter order shouldn't affect the database url", url1, url2);
    }

    @Test
    @SuppressWarnings("PMD.UnusedLocalVariable")
    public void testConnectionsSeeSameData() throws IOException, SQLException {
        String jdbcUrl = GeoParquetDatabaseUtils.getJDBCUrl(params, parametersInfo);
        try (Connection c1 = DriverManager.getConnection(jdbcUrl);
                Connection c2 = DriverManager.getConnection(jdbcUrl)) {
            try (Statement st = c1.createStatement()) {
                st.execute("create table test (data boolean)");
                st.execute("insert into test values(true)");
            }

            try (Statement st = c2.createStatement();
                    ResultSet rs = st.executeQuery("select data from test")) {
                assertTrue(rs.next());
                assertTrue(rs.getBoolean(1));
            }
            // close c1
            c1.close();
            // c2 still sees it
            try (Statement st = c2.createStatement();
                    ResultSet rs = st.executeQuery("select data from test")) {
                assertTrue(rs.next());
                assertTrue(rs.getBoolean(1));
            }
        }

        // both connections closed, data is gone
        SQLException e = assertThrows(SQLException.class, () -> {
            try (Connection c2 = DriverManager.getConnection(jdbcUrl);
                    Statement st = c2.createStatement();
                    ResultSet rs = st.executeQuery("select data from test")) {
                assertFalse("shouldn't reach here", true);
            }
        });

        assertThat(e.getMessage(), containsString("Table with name test does not exist!"));
    }

    @Test
    public void testDuckdbConnectionDuplicateClose() throws IOException, SQLException {
        String jdbcUrl = GeoParquetDatabaseUtils.getJDBCUrl(params, parametersInfo);

        try (DuckDBConnection c1 = (DuckDBConnection) DriverManager.getConnection(jdbcUrl)) {
            try (Statement st = c1.createStatement()) {
                st.execute("create table test (data boolean)");
                st.execute("insert into test values(true)");
            }

            try (Connection c2 = c1.duplicate()) {

                try (Statement st = c2.createStatement();
                        ResultSet rs = st.executeQuery("select data from test")) {
                    assertTrue(rs.next());
                    assertTrue(rs.getBoolean(1));
                }
                // close c1
                c1.close();
                // c2 still sees it
                try (Statement st = c2.createStatement();
                        ResultSet rs = st.executeQuery("select data from test")) {
                    assertTrue(rs.next());
                    assertTrue(rs.getBoolean(1));
                }
            }
        }
    }
}
