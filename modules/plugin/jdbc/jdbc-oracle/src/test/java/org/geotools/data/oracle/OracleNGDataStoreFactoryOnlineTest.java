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
 */
package org.geotools.data.oracle;

import static org.geotools.data.oracle.OracleNGDataStoreFactory.CONNECTION_TIMEOUT;
import static org.geotools.data.oracle.OracleNGDataStoreFactory.CONN_TIMEOUT_NAME;
import static org.geotools.data.oracle.OracleNGDataStoreFactory.GEOMETRY_METADATA_TABLE;
import static org.geotools.data.oracle.OracleNGDataStoreFactory.LOGIN_TIMEOUT;
import static org.geotools.data.oracle.OracleNGDataStoreFactory.LOGIN_TIMEOUT_NAME;
import static org.geotools.data.oracle.OracleNGDataStoreFactory.OUTBOUND_CONNECTION_TIMEOUT;
import static org.geotools.data.oracle.OracleNGDataStoreFactory.OUTBOUND_TIMEOUT_NAME;
import static org.geotools.jdbc.JDBCDataStoreFactory.DATABASE;
import static org.geotools.jdbc.JDBCDataStoreFactory.DBTYPE;
import static org.geotools.jdbc.JDBCDataStoreFactory.HOST;
import static org.geotools.jdbc.JDBCDataStoreFactory.PASSWD;
import static org.geotools.jdbc.JDBCDataStoreFactory.PORT;
import static org.geotools.jdbc.JDBCDataStoreFactory.SCHEMA;
import static org.geotools.jdbc.JDBCDataStoreFactory.USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.jdbc.datasource.DBCPDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.test.FixtureUtilities;
import org.junit.Test;

public class OracleNGDataStoreFactoryOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

    @Test
    public void testCreateConnection() throws Exception {
        OracleNGDataStoreFactory factory = new OracleNGDataStoreFactory();
        checkCreateConnection(factory, factory.getDatabaseID());
    }

    @Test
    public void testCaptureOldDatastoreConfig() throws Exception {
        OracleNGDataStoreFactory factory = new OracleNGDataStoreFactory();
        checkCreateConnection(factory, "oracle");
    }

    @Test
    public void testGeometryMetadata() throws IOException {
        OracleNGDataStoreFactory factory = new OracleNGDataStoreFactory();
        Properties db = FixtureUtilities.loadFixture("oracle");
        Map<String, Object> params = new HashMap<>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
        params.put(SCHEMA.key, db.getProperty(SCHEMA.key));
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty("password"));
        params.put(DBTYPE.key, "oracle");
        params.put(GEOMETRY_METADATA_TABLE.key, "geometry_columns_test");

        assertTrue(factory.canProcess(params));
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            // check dialect
            OracleDialect dialect = (OracleDialect) store.getSQLDialect();

            // check the metadata table has been set (other tests check it's actually working)
            assertEquals("geometry_columns_test", dialect.getGeometryMetadataTable());
        } finally {
            store.dispose();
        }
    }

    @Test
    public void testTimeoutProps() throws IOException, ReflectiveOperationException {
        OracleNGDataStoreFactory factory = new OracleNGDataStoreFactory();
        Properties db = FixtureUtilities.loadFixture("oracle");
        Map<String, Object> params = new HashMap<>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
        params.put(SCHEMA.key, db.getProperty(SCHEMA.key));
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty("password"));
        params.put(DBTYPE.key, "oracle");
        int login = Integer.MAX_VALUE - 3;
        int connTimeout = Integer.MAX_VALUE - 2;
        int outbound = Integer.MAX_VALUE - 1;
        params.put(LOGIN_TIMEOUT.key, login);
        params.put(CONNECTION_TIMEOUT.key, connTimeout);
        params.put(OUTBOUND_CONNECTION_TIMEOUT.key, outbound);

        assertTrue(factory.canProcess(params));
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        DataSource ds = store.getDataSource();
        try (DBCPDataSource dataSource = (DBCPDataSource) ds) {
            Field field = BasicDataSource.class.getDeclaredField("connectionProperties");
            field.setAccessible(true);
            Properties props = (Properties) field.get(dataSource.getWrapped());
            assertEquals(String.valueOf(login), props.getProperty(LOGIN_TIMEOUT_NAME));
            assertEquals(String.valueOf(connTimeout), props.getProperty(CONN_TIMEOUT_NAME));
            assertEquals(String.valueOf(outbound), props.getProperty(OUTBOUND_TIMEOUT_NAME));
        } catch (SQLException e) {
            // do nothing
        } finally {
            store.dispose();
        }
    }

    private void checkCreateConnection(OracleNGDataStoreFactory factory, String dbtype)
            throws IOException {
        Properties db = FixtureUtilities.loadFixture("oracle");
        Map<String, Object> params = new HashMap<>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
        params.put(SCHEMA.key, db.getProperty(SCHEMA.key));
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty("password"));
        params.put(DBTYPE.key, dbtype);

        assertTrue(factory.canProcess(params));
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            // check dialect
            assertTrue(store.getSQLDialect() instanceof OracleDialect);
            // force connection usage
            assertNotNull(store.getSchema(tname("ft1")));
        } finally {
            store.dispose();
        }
    }
}
