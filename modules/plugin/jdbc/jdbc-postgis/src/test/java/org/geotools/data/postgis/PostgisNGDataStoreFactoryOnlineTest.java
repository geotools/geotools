/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.postgis.PostgisNGDataStoreFactory.PORT;
import static org.geotools.jdbc.JDBCDataStoreFactory.DATABASE;
import static org.geotools.jdbc.JDBCDataStoreFactory.DBTYPE;
import static org.geotools.jdbc.JDBCDataStoreFactory.HOST;
import static org.geotools.jdbc.JDBCDataStoreFactory.PASSWD;
import static org.geotools.jdbc.JDBCDataStoreFactory.USER;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;

public class PostgisNGDataStoreFactoryOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISTestSetup();
    }

    public void testCreateConnection() throws Exception {
        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
        checkCreateConnection(factory, factory.getDatabaseID());
    }

    public void testCreateConnectionWithOldId() throws Exception {
        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
        checkCreateConnection(factory, "postgis");
    }

    private void checkCreateConnection(PostgisNGDataStoreFactory factory, String dbtype)
            throws IOException {
        Properties db = fixture;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty(PASSWD.key));
        params.put(PostgisNGDataStoreFactory.SIMPLIFY.key, false);

        params.put(DBTYPE.key, dbtype);

        assertTrue(factory.canProcess(params));
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            // check dialect
            assertTrue(store.getSQLDialect() instanceof PostGISDialect);
            // force connection usage
            assertNotNull(store.getSchema(tname("ft1")));
        } finally {
            store.dispose();
        }
    }

    public void testSimplifyParameterDisabled() throws Exception {
        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
        Properties db = fixture;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty(PASSWD.key));

        // force simplify off
        params.put(PostgisNGDataStoreFactory.SIMPLIFY.key, false);
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            // check dialect
            PostGISDialect dialect = (PostGISDialect) store.getSQLDialect();
            assertFalse(dialect.isSimplifyEnabled());
            Set<Hints.Key> baseHints = new HashSet<Key>();
            dialect.addSupportedHints(baseHints);
            assertTrue(baseHints.isEmpty());
        } finally {
            store.dispose();
        }
    }

    public void testSimplifyParameter() throws Exception {
        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
        Properties db = fixture;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty(PASSWD.key));

        // do not specify simplify, on by default
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            // check dialect
            PostGISDialect dialect = (PostGISDialect) store.getSQLDialect();
            assertTrue(dialect.isSimplifyEnabled());
            Set<Hints.Key> baseHints = new HashSet<Key>();
            dialect.addSupportedHints(baseHints);
            assertFalse(baseHints.isEmpty());
            assertTrue(baseHints.contains(Hints.GEOMETRY_SIMPLIFICATION));
        } finally {
            store.dispose();
        }
    }

    public void testEncodeBBOXParameter() throws Exception {
        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();
        Properties db = fixture;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty(PASSWD.key));

        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            PostGISDialect dialect = (PostGISDialect) store.getSQLDialect();
            assertFalse(dialect.isEncodeBBOXFilterAsEnvelope());
        } finally {
            store.dispose();
        }
        System.setProperty("org.geotools.data.postgis.largeGeometriesOptimize", "true");

        store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            // check dialect
            PostGISDialect dialect = (PostGISDialect) store.getSQLDialect();
            assertTrue(dialect.isEncodeBBOXFilterAsEnvelope());
        } finally {
            store.dispose();
        }
    }

    @Override
    protected void tearDownInternal() throws Exception {
        System.clearProperty("org.geotools.data.postgis.largeGeometriesOptimize");
    }
}
