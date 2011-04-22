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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.geotools.data.teradata.TeradataDataStoreFactory.PORT;
import static org.geotools.jdbc.JDBCDataStoreFactory.*;

public class TeradataDataStoreFactoryTest extends JDBCTestSupport {


    protected JDBCTestSetup createTestSetup() {
        return new TeradataTestSetup();
    }

    public void testCreateConnection() throws Exception {
        TeradataDataStoreFactory factory = new TeradataDataStoreFactory();
        checkCreateConnection(factory, factory.getDatabaseID());
    }

    private void checkCreateConnection(TeradataDataStoreFactory factory, String dbtype) throws IOException {
        Properties db = fixture;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put(HOST.key, db.getProperty(HOST.key));
        params.put(DATABASE.key, db.getProperty(DATABASE.key));
        params.put(PORT.key, db.getProperty(PORT.key));
        params.put(USER.key, db.getProperty(USER.key));
        params.put(PASSWD.key, db.getProperty("password"));
        params.put(SCHEMA.key, db.getProperty(SCHEMA.key));

        params.put(DBTYPE.key, dbtype);

        assertTrue(factory.canProcess(params));
        JDBCDataStore store = factory.createDataStore(params);
        assertNotNull(store);
        try {
            // check dialect
            assertTrue(store.getSQLDialect() instanceof TeradataDialect);

            // force connection usage
            assertNotNull(store.getSchema(tname("ft1")));
        } finally {
            store.dispose();
        }
    }

}
