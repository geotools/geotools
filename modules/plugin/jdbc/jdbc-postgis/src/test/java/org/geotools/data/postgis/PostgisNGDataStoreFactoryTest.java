/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.geotools.data.postgis.PostgisNGDataStoreFactory.PREPARED_STATEMENTS;
import static org.geotools.jdbc.JDBCDataStoreFactory.DATASOURCE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;
import org.geotools.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.jdbc.SslMode;

public class PostgisNGDataStoreFactoryTest {

    @Mock
    private DataSource ds = null;

    @Mock
    private Connection conn = null;

    @Mock
    private PgConnection pgConn = null;

    @Mock
    private DatabaseMetaData md = null;

    @Mock
    private Statement st1 = null;

    @Mock
    private Statement st2 = null;

    @Mock
    private ResultSet rs1 = null;

    @Mock
    private ResultSet rs2 = null;

    private JDBCDataStore store = null;

    private AutoCloseable mocks = null;

    @Before
    @SuppressWarnings("PMD.CheckResultSet")
    public void setUp() throws Exception {
        this.mocks = MockitoAnnotations.openMocks(this);
        when(this.ds.getConnection()).thenReturn(this.conn);
        when(this.conn.getMetaData()).thenReturn(this.md);
        when(this.md.getDatabaseMajorVersion()).thenReturn(15);
        when(this.md.getDatabaseMinorVersion()).thenReturn(1);
        when(this.conn.createStatement()).thenReturn(this.st1, this.st2);
        when(this.st1.executeQuery("select PostGIS_Lib_Version()")).thenReturn(this.rs1);
        when(this.rs1.next()).thenReturn(true);
        when(this.rs1.getString(1)).thenReturn("3.3.2");
    }

    @After
    public void tearDown() throws Exception {
        if (this.mocks != null) {
            this.mocks.close();
            this.mocks = null;
        }
        if (this.store != null) {
            this.store.dispose();
        }
    }

    @Test
    public void testStandardConformingStringsOnFromConnection() throws Exception {
        verifyFilterToSqlSettings(false, false, false);
    }

    @Test
    public void testStandardConformingStringsOffFromConnection() throws Exception {
        verifyFilterToSqlSettings(true, false, false);
    }

    @Test
    public void testStandardConformingStringsOnFromQuery() throws Exception {
        verifyFilterToSqlSettings(false, false, true);
    }

    @Test
    public void testStandardConformingStringsOffFromQuery() throws Exception {
        verifyFilterToSqlSettings(true, false, true);
    }

    @Test
    public void testStandardConformingStringsOnWithPSFromConnection() throws Exception {
        verifyFilterToSqlSettings(false, true, false);
    }

    @Test
    public void testStandardConformingStringsOffWithPSFromConnection() throws Exception {
        verifyFilterToSqlSettings(true, true, false);
    }

    @Test
    public void testStandardConformingStringsOnWithPSFromQuery() throws Exception {
        verifyFilterToSqlSettings(false, true, true);
    }

    @Test
    public void testStandardConformingStringsOffWithPSFromQuery() throws Exception {
        verifyFilterToSqlSettings(true, true, true);
    }

    private void verifyFilterToSqlSettings(boolean escapeBackslash, boolean withPS, boolean withQuery)
            throws Exception {
        if (withQuery) {
            when(this.st2.executeQuery("SHOW standard_conforming_strings")).thenReturn(this.rs2);
            when(this.rs2.next()).thenReturn(true);
            when(this.rs2.getString(1)).thenReturn(escapeBackslash ? "off" : "on");
        } else {
            when(this.conn.isWrapperFor(PgConnection.class)).thenReturn(true);
            when(this.conn.unwrap(PgConnection.class)).thenReturn(this.pgConn);
            when(this.pgConn.getStandardConformingStrings()).thenReturn(!escapeBackslash);
        }
        Map<String, Object> params = new HashMap<>();
        params.put(DATASOURCE.key, this.ds);
        params.put(PREPARED_STATEMENTS.key, withPS);
        this.store = new PostgisNGDataStoreFactory().createDataStore(params);
        assertNotNull(this.store);
        SQLDialect dialect = this.store.getSQLDialect();
        assertThat(dialect, instanceOf(withPS ? PostGISPSDialect.class : PostGISDialect.class));
        PostGISDialect pgDialect = withPS ? ((PostGISPSDialect) dialect).getDelegate() : (PostGISDialect) dialect;
        assertEquals(new Version("15.1"), pgDialect.getPostgreSQLVersion(this.conn));
        assertEquals(new Version("3.3.2"), pgDialect.getVersion(this.conn));
        assertEquals(escapeBackslash, pgDialect.isEscapeBackslash());
        verify(this.conn, withQuery ? times(2) : times(1)).createStatement();
        verify(this.conn).close();
        verify(this.st1).close();
        verify(this.rs1).close();
        verify(this.st2, withQuery ? times(1) : never()).close();
        verify(this.rs2, withQuery ? times(1) : never()).close();
    }

    @Test
    public void testGetJDBCUrl() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put(PostgisNGDataStoreFactory.HOST.key, "localhost");
        params.put(PostgisNGDataStoreFactory.PORT.key, 5432);
        params.put(PostgisNGDataStoreFactory.DATABASE.key, "template1");
        params.put(PostgisNGDataStoreFactory.REWRITE_BATCHED_INSERTS.key, Boolean.TRUE);
        assertEquals(
                "jdbc:postgresql://localhost:5432/template1?reWriteBatchedInserts=true",
                new PostgisNGDataStoreFactory().getJDBCUrl(params));
    }

    @Test
    public void testGetJDBCUrlSslModePreferEnumValue() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put(PostgisNGDataStoreFactory.HOST.key, "localhost");
        params.put(PostgisNGDataStoreFactory.PORT.key, 5432);
        params.put(PostgisNGDataStoreFactory.DATABASE.key, "template1");
        params.put(PostgisNGDataStoreFactory.SSL_MODE.key, SslMode.PREFER);
        assertEquals(
                "jdbc:postgresql://localhost:5432/template1?reWriteBatchedInserts=false&sslmode=prefer&binaryTransferEnable=bytea",
                new PostgisNGDataStoreFactory().getJDBCUrl(params));
    }

    @Test
    public void testGetJDBCUrlSslModePreferString() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put(PostgisNGDataStoreFactory.HOST.key, "localhost");
        params.put(PostgisNGDataStoreFactory.PORT.key, 5432);
        params.put(PostgisNGDataStoreFactory.DATABASE.key, "template1");
        params.put(PostgisNGDataStoreFactory.SSL_MODE.key, "prefer");
        assertEquals(
                "jdbc:postgresql://localhost:5432/template1?reWriteBatchedInserts=false&sslmode=prefer&binaryTransferEnable=bytea",
                new PostgisNGDataStoreFactory().getJDBCUrl(params));
    }
}
