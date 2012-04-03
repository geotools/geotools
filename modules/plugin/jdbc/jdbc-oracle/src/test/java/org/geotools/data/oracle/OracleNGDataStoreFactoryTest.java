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

import static org.geotools.data.oracle.OracleNGDataStoreFactory.GEOMETRY_METADATA_TABLE;
import static org.geotools.jdbc.JDBCDataStoreFactory.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.jdbc.SQLDialect;
import org.geotools.test.FixtureUtilities;

/**
 * 
 *
 * @source $URL$
 */
public class OracleNGDataStoreFactoryTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }
    
    public void testCreateConnection() throws Exception {
        OracleNGDataStoreFactory factory = new OracleNGDataStoreFactory();
        checkCreateConnection(factory, factory.getDatabaseID());
    }
    
    public void testCaptureOldDatastoreConfig() throws Exception {
        OracleNGDataStoreFactory factory = new OracleNGDataStoreFactory();
        checkCreateConnection(factory, "oracle");
    }
    
    public void testGeometryMetadata() throws IOException {
        OracleNGDataStoreFactory factory = new OracleNGDataStoreFactory();
        Properties db = FixtureUtilities.loadFixture("oracle");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
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

    private void checkCreateConnection(OracleNGDataStoreFactory factory, String dbtype) throws IOException {
        Properties db = FixtureUtilities.loadFixture("oracle");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
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
